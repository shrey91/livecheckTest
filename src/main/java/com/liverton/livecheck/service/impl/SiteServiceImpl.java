package com.liverton.livecheck.service.impl;

import com.liverton.livecheck.boot.config.Application;
import com.liverton.livecheck.dao.model.*;
import com.liverton.livecheck.dao.model.Site;
import com.liverton.livecheck.dao.repository.ApplicationStatusRepository;
import com.liverton.livecheck.dao.repository.SiteRepository;
import com.liverton.livecheck.dao.repository.SitePingResultRepository;
import com.liverton.livecheck.model.ApplicationType;
import com.liverton.livecheck.model.NotificationAction;
import com.liverton.livecheck.model.PingState;
import com.liverton.livecheck.model.SiteState;
import com.liverton.livecheck.service.SiteService;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.lang.*;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.liverton.livecheck.model.SiteState.ACKNOWLEDGED;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);
    private static final String AUTHORIZATION_KEY = "pHeJUYYg8r2JPjX2A.R6QDd7cUMlVTVhaS.UlM7xF.tFua_vJZhYejqrUsE1gJVNIZLrrA6SCYeZ";
    private static final String EHLO_COMMAND = "ehlo localhost";
    private static final Pattern PATTERN_IP_ADDRESS = Pattern.compile(".*\\[.*\\].*");
    private static final Pattern PATTERN_FIND_AVERAGE = Pattern.compile(".*Average.*", Pattern.MULTILINE);
    private static final Pattern PATTERN_FIND_TRY = Pattern.compile(".*try again.*");


    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }


    @Scheduled(cron = "0 * * * * *")
    public void scanSites() {
        LOGGER.info("running scheduled site scan");

        repository.findAll().parallelStream().forEach(site -> {
            for (ApplicationStatus applicationStatus : site.getApplicationStatus()) {
                if (applicationStatus.getEnabled()) {
                    if (site.getEnabled()) {
                        switch (applicationStatus.getApplicationType()) {
                            case PING:
                                handleAndUpdateApplicationState(applicationStatus, pingSite(site));
                                break;
                            case HTTP:
                                handleAndUpdateApplicationState(applicationStatus, pollHttp(site));
                                break;
                            case SMTP:
                                handleAndUpdateApplicationState(applicationStatus, pollSmtp(site));
                                break;
                        }
                    }
                }
            }
        });
    }

    private void handleAndUpdateApplicationState(ApplicationStatus applicationStatus, SiteState state) {
        if (OKAY.equals(state)) {
            applicationStatus.setFailureCount(0);
            applicationStatus.setSiteState(OKAY);
        } else if (ACKNOWLEDGED.equals(state)) {
            applicationStatus.setSiteState(ACKNOWLEDGED);
        } else {
            applicationStatus.setFailureCount((applicationStatus.getFailureCount() != null) ? applicationStatus.getFailureCount() + 1 : 1);
            if (applicationStatus.getFailureCount() >= 3 && applicationStatus.getFailureCount() < 5) {
                applicationStatus.setSiteState(SiteState.WARNING);
            } else if (applicationStatus.getFailureCount() >= 5) {
                applicationStatus.setSiteState(SiteState.ERROR);
                if (applicationStatus.getApplicationType() == ApplicationType.PING) {

                }
            }
        }
        applicationStatusRepository.save(applicationStatus);
    }

    private SiteState pingSite(Site site) {
        try {
            if (site.getEnabled()) {
                Process p1 = java.lang.Runtime.getRuntime().exec("ping -n 1 " + site.getIpAddress());
                int returnVal = p1.waitFor();
                SitePingResult sitePingResult = new SitePingResult();
                BufferedReader in = new BufferedReader(new InputStreamReader(p1.getInputStream()));
                StringBuilder builder = new StringBuilder();

                String line = null;
                while ((line = in.readLine()) != null) {
                    builder.append(line);
                    builder.append("\n");
                }
                String resultping = builder.toString();
                Matcher matcher2 = PATTERN_FIND_TRY.matcher(resultping);
                if (matcher2.find()) {
                    failedPing(site, sitePingResult);
                } else {
                    String[] results = resultping.split("Packets: ");
                    String pingstats = results[1].trim();
                    sitePingResult.setResponseTime(pingstats);
                    sitePingResult.setDate(new Date());
                    sitePingResult.setSite(site);
                    p1.waitFor();
                    Matcher matcher = PATTERN_FIND_AVERAGE.matcher(resultping);
                    if (matcher.find() && returnVal == 0) {

                        String ss = builder.toString();

                        String[] parts = ss.split("Average =");
                        LOGGER.debug(ss);
                        String pingTime = parts[1].trim();
                        LOGGER.debug(pingTime);
                        site.setAverageResponse(pingTime);

                        site.setAcknowledged(false);
                        sitePingResult.setPingState(PingState.YES);
                        site.setState(SiteState.OKAY);
                        site.setFailureCount(0);
                        site.setSendNotification(false);
                        if (!site.getEnabled()) {
                            site.setState(SiteState.DISABLED);
                        }
                    } else {
                        failedPing(site, sitePingResult);
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
        return SiteState.ERROR;
    }

    private SiteState pollHttp(Site site) {
        String url = "http://" + site.getIpAddress();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        if (site.getApplicationStatus().get(1).getApplicationType() == ApplicationType.HTTP && site.getApplicationStatus().get(1).getEnabled()) {
            if (site.getEnabled()) {
                try {
                    HttpResponse response = client.execute(request);
                    if (response.getStatusLine().getStatusCode() > 0) {
                        LOGGER.info("Received response from {} , with status code ({})", site.getSiteName(), response.getStatusLine().getStatusCode());
                        return SiteState.OKAY;
                    }
                } catch (ConnectException e) {
                    LOGGER.error("Unable to poll site ({}) for http - error {}", site.getSiteName(), e.getMessage());
                } catch (ClientProtocolException e) {
                    LOGGER.error("Unable to poll site ({}) for http - error {}", site.getSiteName(), e.getMessage());
                } catch (IOException e) {
                    LOGGER.error("Unable to poll site ({}) for http - error {}", site.getSiteName(), e.getMessage());
                }
            }
        }
        return SiteState.ERROR;
    }

    private SiteState pollSmtp(Site site) {
        if (site.getApplicationStatus().get(2).getApplicationType() == ApplicationType.SMTP && site.getApplicationStatus().get(2).getEnabled()) {
            if (site.getEnabled()) {
                try {
                    Socket clientSocket = new Socket("earth.liverton.local", 25);
                    clientSocket.setSoTimeout(1000);
                    LOGGER.info("{} Did not time out", site.getSiteName());
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    bufferedWriter.write(EHLO_COMMAND);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    StringBuilder builder = new StringBuilder();
                    String line = null;
                    for (int i = 0; i < 3; i++) {
                        line = inFromServer.readLine();
                        builder.append(line);
                        builder.append("\n");
                    }
                    String sentence2 = builder.toString();
                    Matcher matcher = PATTERN_IP_ADDRESS.matcher(sentence2);
                    if (matcher.find()) {

                        return SiteState.OKAY;
                    }
                    clientSocket.close();

                } catch (Exception e) {
                    LOGGER.error("Unable to poll site ({}) for smtp - error {}", site.getSiteName(), e.getMessage());
                }
            }
        }
        return SiteState.ERROR;
    }


    @Scheduled(cron = "0/30 * * * * *")
    public void sendEmail() {
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new HeaderRequestInterceptor(AUTHORIZATION_KEY));


        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        try {
            for (Site site : repository.findAll()) {
                if (!site.getAcknowledged() && site.getEnabled()) {
                    if (SiteState.ERROR.equals(site.getState()) && !site.getSendNotification()) {
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

                        if (test.contains(";")) {
                            List<String> numbers2 = Arrays.asList(test.split("\\s*;\\s*"));

                            MessageRequest messageRequest = new MessageRequest(site.getSiteName() + " is down. IP Address registered to the number is " + site.getIpAddress() + " Failed to ping " + site.getFailureCount() + " times.", numbers2);
                            String result = restTemplate.postForObject("https://api.clickatell.com/rest/message", messageRequest, String.class);
                            site.setSendNotification(true);
                            repository.save(site);

//                            LOGGER.debug(result.toString());
                        } else {
                            List<String> numbers = new ArrayList<>();
                            numbers.add(test);
                            MessageRequest messageRequest = new MessageRequest(site.getSiteName() + " is down. IP Address registered to the number is " + site.getIpAddress() + " Failed to ping " + site.getFailureCount() + " times.", numbers);
                            String result = restTemplate.postForObject("https://api.clickatell.com/rest/message", messageRequest, String.class);
//                            http://sms.on.net.nz/sms.cgi?number=%2b64212842309&message=Hello+Shreyansh
//                            LOGGER.debug(result.toString());
                            site.setSendNotification(true);
                            repository.save(site);
                        }

                    }
                }
            }

        } catch (MessagingException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }


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

    private void failedPing(Site site, SitePingResult sitePingResult) {

        site.setFailureCount((site.getFailureCount() != null) ? site.getFailureCount() + 1 : 1);
        site.setAverageResponse("N/A");
        sitePingResult.setPingState(PingState.NO);
        sitePingResult.setDate(new Date());
        sitePingResult.setResponseTime("Did not respond");
        if (!site.getAcknowledged()) {
            if (site.getFailureCount() >= 3 && site.getFailureCount() < 5) {
                site.setState(SiteState.WARNING);
            } else if (site.getFailureCount() >= 5) {
                site.setState(SiteState.ERROR);
            }
        } else {
            site.setState(SiteState.ACKNOWLEDGED);
            LOGGER.info("Site with name :{} has failed with count :{}", site.getSiteName(), site.getFailureCount());
        }
    }
}
