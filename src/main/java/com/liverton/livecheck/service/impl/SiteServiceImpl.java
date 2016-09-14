package com.liverton.livecheck.service.impl;

import com.liverton.livecheck.dao.model.*;
import com.liverton.livecheck.dao.model.Site;
import com.liverton.livecheck.dao.repository.ApplicationStatusRepository;
import com.liverton.livecheck.dao.repository.OrganisationRepository;
import com.liverton.livecheck.dao.repository.SiteRepository;
import com.liverton.livecheck.dao.repository.SitePingResultRepository;
import com.liverton.livecheck.model.ApplicationType;
import com.liverton.livecheck.model.NotificationAction;
import com.liverton.livecheck.model.PingState;
import com.liverton.livecheck.model.SiteState;
import com.liverton.livecheck.service.SiteService;
import com.liverton.livecheck.service.domain.*;
import com.liverton.livecheck.web.form.OrganisationForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.lang.*;
import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.liverton.livecheck.model.SiteState.OKAY;

/**
 * Created by sshah on 8/08/2016.
 */
@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    private SiteRepository repository;

    @Autowired
    SitePingResultRepository sitePingResultRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ApplicationStatusRepository applicationStatusRepository;

    @Autowired
    private OrganisationRepository organisationRepository;


    private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);
    private static final String AUTHORIZATION_KEY = "pHeJUYYg8r2JPjX2A.R6QDd7cUMlVTVhaS.UlM7xF.tFua_vJZhYejqrUsE1gJVNIZLrrA6SCYeZ";
    private static final String EHLO_COMMAND = "ehlo localhost";
    private static final Pattern PATTERN_IP_ADDRESS = Pattern.compile(".*\\[.*\\].*");


    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }


    @Scheduled(cron = "0 */5 * * * *")
    public void scanSites() {
//        Boolean keepRunning = true;
//        while (keepRunning) {
        SiteState pollState = null;
        for (Site site : repository.findAll()) {
            for (ApplicationStatus applicationStatus : site.getApplicationStatus()) {
                if (applicationStatus.getEnabled()) {
                    if (site.getEnabled()) {
                        switch (applicationStatus.getApplicationType()) {
                            case PING:
                                pollState = pingSite(site, applicationStatus);
                                break;
                            case HTTP:

                                pollState = pollHttp(site, applicationStatus);
                                break;
                            case SMTP:
                                pollState = pollSmtp(site, applicationStatus);
                                break;
                        }
                        if (pollState == OKAY) {
                            applicationStatus.setFailureCount(0);
                            applicationStatus.setSiteState(OKAY);
                        } else {
                            applicationStatus.setFailureCount((applicationStatus.getFailureCount() != null) ? applicationStatus.getFailureCount() + 1 : 1);
                            if (applicationStatus.getFailureCount() >= 5 && applicationStatus.getFailureCount() < 10) {
                                applicationStatus.setSiteState(SiteState.WARNING);
                            } else if (applicationStatus.getFailureCount() >= 10) {
                                applicationStatus.setSiteState(SiteState.ERROR);
                                if (applicationStatus.getApplicationType() == ApplicationType.PING) {
                                    site.setSendEmail(false);
                                }
                            }
                        }
                        applicationStatusRepository.save(applicationStatus);
                    }
                }
            }
        }
    }

    private SiteState pingSite(Site site, ApplicationStatus applicationStatus) {
        try {
            if (site.getEnabled()) {
                Process p1 = java.lang.Runtime.getRuntime().exec("ping " + site.getIpAddress());
                int returnVal = p1.waitFor();
                SitePingResult sitePingResult = new SitePingResult();
                BufferedReader in = new BufferedReader(new InputStreamReader(p1.getInputStream()));
                StringBuilder builder = new StringBuilder();
                Pattern pattern = Pattern.compile(".*Average.*", Pattern.MULTILINE);
                String line = null;
                while ((line = in.readLine()) != null) {
                    builder.append(line);
                    builder.append("\n");
                }
                String resultping = builder.toString();
                String[] results = resultping.split("Packets: ");
                String pingstats = results[1].trim();
                sitePingResult.setResponseTime(pingstats);
                sitePingResult.setDate(new Date());
                sitePingResult.setSite(site);
                p1.waitFor();
                Matcher matcher = pattern.matcher(resultping);
                if (matcher.find() && returnVal == 0) {

                    String ss = builder.toString();

                    String[] parts = ss.split("Average =");
//                    LOGGER.info(ss);
                    String pingTime = parts[1].trim();
//                    LOGGER.info(pingTime);
                    site.setAverageResponse(pingTime);

                    site.setAcknowledged(false);
                    sitePingResult.setPingState(PingState.YES);
                    site.setState(SiteState.OKAY);
                    if (!site.getEnabled()) {
                        site.setState(SiteState.DISABLED);
                    }
                } else {
                    site.setFailureCount((site.getFailureCount() != null) ? site.getFailureCount() + 1 : 1);
                    site.setAverageResponse("N/A");
                    sitePingResult.setPingState(PingState.NO);
                    if (site.getFailureCount() >= 5 && site.getFailureCount() < 10) {
                        site.setState(SiteState.WARNING);
                    } else if (site.getFailureCount() >= 10) {
                        site.setState(SiteState.ERROR);

                    } else {
                        LOGGER.info("Site with name :{} has failed with count :{}", site.getSiteName(), site.getFailureCount());
                    }
                }
                sitePingResultRepository.save(sitePingResult);
                repository.save(site);
                return returnVal == 0 ? SiteState.OKAY : SiteState.ERROR;
            }
        } catch (UnknownHostException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return applicationStatus.getSiteState();
    }

    private SiteState pollHttp(Site site, ApplicationStatus applicationStatus) {
        String url = "http://" + site.getIpAddress();

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        if (site.getEnabled()) {
            try {
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
//                site.setMonitorHttp(true);
                    return SiteState.OKAY;
                }
            } catch (ConnectException e) {
//            site.setMonitorHttp(false);
//            LOGGER.info("Error " + e);
                return SiteState.ERROR;

            } catch (IOException e) {
//            site.setMonitorHttp(false);
//            e.printStackTrace();
                return SiteState.ERROR;
            } catch (Exception e) {
//            LOGGER.info("Exception was " + e);
            }
        }
        return SiteState.ERROR;
    }

    private SiteState pollSmtp(Site site, ApplicationStatus applicationStatus) {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        if (site.getEnabled()) {
            try {
                Socket clientSocket = new Socket("earth.liverton.local", 25);
                clientSocket.setSoTimeout(5000);
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));


                String sentence = inFromServer.readLine();
                bufferedWriter.write(EHLO_COMMAND);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                String sentence2 = inFromServer.readLine();
                Matcher matcher = PATTERN_IP_ADDRESS.matcher(sentence2);
                if (matcher.matches()) {
//                site.setMonitorSmtp(true);
                    return SiteState.OKAY;
                }
//            modifiedSentence = inFromServer.readLine();
//            System.out.println(modifiedSentence);
                clientSocket.close();

            } catch (Exception e) {
//            site.setMonitorSmtp(false);
//            LOGGER.info("Could not reach site " + e);
                return SiteState.ERROR;
            }
        }
        return SiteState.ERROR;
    }


    @Scheduled(cron = "0 */5 * * * *")
    public void sendEmail() {
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new HeaderRequestInterceptor(AUTHORIZATION_KEY));


        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
//        List<String> numbers = new ArrayList<>();
//        numbers.add("64212842309");
        try {
            for (Site site : repository.findAll()) {
                if (!site.getSendEmail() && !site.getAcknowledged() && site.getEnabled()) {
                    if (site.getState() == SiteState.ERROR) {
                        site.getOrganisation();

                        String test = site.getOrganisation().getTextDestination();

                        //**Multiple Email options to implement**//
//                        String emailSeparator = site.getOrganisation().getToEmail();
//                        if(emailSeparator.contains(";")){
//                            List<String> emails = Arrays.asList(emailSeparator.split("\\s*;\\s*"));
//                        }

                        Properties properties = new Properties();
                        properties.setProperty("mail.transport.protocol", "smtp");
                        properties.setProperty("mail.smtp.auth", "true");
                        properties.setProperty("mail.smtp.starttls.enable", "false");
                        properties.setProperty("mail.debug", "false");
                        properties.setProperty("mail.smtp.host", site.getOrganisation().getHost());
                        properties.setProperty("mail.smtp.port", site.getOrganisation().getPortNumber());
                        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
                        helper.setTo(site.getOrganisation().getToEmail());
                        helper.setFrom(site.getOrganisation().getFromEmail());
                        helper.setSubject(site.getSiteName() + " is Down");
                        helper.setText(
                                site.getSiteName()
                                        + " is Down. It took "
                                        + site.getAverageResponse()
                                        + " to respond. Failed to ping site "
                                        + site.getFailureCount()
                                        + " times.");
                        javaMailSender.send(mailMessage);
                        site.setSendEmail(true);
                        if (test.contains(";")) {
                            List<String> numbers2 = Arrays.asList(test.split("\\s*;\\s*"));

                            MessageRequest messageRequest = new MessageRequest(site.getSiteName() + " is down. IP Address registered to the number is " + site.getIpAddress() + " Failed to ping " + site.getFailureCount() + " times.", numbers2);
                            String result = restTemplate.postForObject("https://api.clickatell.com/rest/message", messageRequest, String.class);


//                            LOGGER.info(result.toString());
                        } else {
                            List<String> numbers = new ArrayList<>();
                            numbers.add(test);
                            MessageRequest messageRequest = new MessageRequest(site.getSiteName() + " is down. IP Address registered to the number is " + site.getIpAddress() + " Failed to ping " + site.getFailureCount() + " times.", numbers);
                            String result = restTemplate.postForObject("https://api.clickatell.com/rest/message", messageRequest, String.class);
//                            LOGGER.info(result.toString());
                        }

                    }
                }
            }
        } catch (MessagingException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }


//    @PostConstruct
//    public void init() {
//        scanSites();
//    }


    @Override
    public List<Site> findSites() {
        return new ArrayList<Site>();
    }

    @Override
    public Site newSite(String siteName, Boolean enabled, String ipAddress, SiteState siteState, NotificationAction notificationAction, String averageResponse, Organisation organisation) {
        List<ApplicationStatus> applicationStatusList = new ArrayList<>();
        Site site = new Site(siteName, enabled, ipAddress, new Date(), siteState, false, notificationAction, 0, averageResponse, false, false, false, null, organisation);
        for (ApplicationType applicationType : ApplicationType.values()) {
            applicationStatusList.add(new ApplicationStatus(applicationType, site, true));
        }
        site.setApplicationStatus(applicationStatusList);
        repository.save(site);
        return site;
    }
}
