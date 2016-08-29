package com.liverton.livecheck.service.impl;

import com.liverton.livecheck.dao.model.*;
import com.liverton.livecheck.dao.repository.SiteRepository;
import com.liverton.livecheck.dao.repository.SitePingResultRepository;
import com.liverton.livecheck.model.PingState;
import com.liverton.livecheck.model.SiteState;
import com.liverton.livecheck.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
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

public class SiteServiceImpl implements SiteService {

    @Autowired
    private SiteRepository repository;

    @Autowired
    SitePingResultRepository sitePingResultRepository;

    @Autowired
    private JavaMailSender javaMailSender;


    private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);
    private static final String AUTHORIZATION_KEY = "pHeJUYYg8r2JPjX2A.R6QDd7cUMlVTVhaS.UlM7xF.tFua_vJZhYejqrUsE1gJVNIZLrrA6SCYeZ";


    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

//    public void savePingResult(){
//
//    }


    @Scheduled(cron = "0 * * * * *")
    public void scanSites() {
//        Boolean keepRunning = true;
//        while (keepRunning) {
        try {

            for (Site site : repository.findAll()) {
                if(site.getEnabled()) {
                    Process p1 = java.lang.Runtime.getRuntime().exec("ping " + site.getIpAddress());
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
                    String[] results = resultping.split("Packets: ");
                    String pingstats = results[1].trim();
                    sitePingResult.setResponseTime(pingstats);
                    sitePingResult.setDate(new Date());
                    sitePingResult.setSite(site);
                    p1.waitFor();

                    if (returnVal == 0) {

                        String ss = builder.toString();
                        String[] parts = ss.split("Average =");
                        String pingTime = parts[1].trim();
                        site.setAverageResponse(pingTime);
                        site.setFailureCount(0);
                        site.setState(OKAY);
                        site.setAcknowledged(false);
                        sitePingResult.setPingState(PingState.YES);
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
                            site.setSendEmail(false);

                        } else {
                            LOGGER.info("Site with name :{} has failed with count :{}", site.getSiteName(), site.getFailureCount());
                        }
                    }
                    sitePingResultRepository.save(sitePingResult);
                    pollHttp(site);
                    pollSmtp(site);
                    repository.save(site);
                }
            }
        } catch (UnknownHostException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    private void pollHttp(Site site) {
        String url = "http://" + site.getIpAddress();

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        try {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                site.setMonitorHttp(true);
            }
        } catch (ConnectException e) {
            site.setMonitorHttp(false);
            LOGGER.info("Error " + e);
        } catch (IOException e) {
            site.setMonitorHttp(false);
            e.printStackTrace();
        } catch (Exception e) {
            LOGGER.info("Exception was " + e);
        }

    }

    private void pollSmtp(Site site) {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        try {
            Socket clientSocket = new Socket("earth.liverton.local", 25);
            clientSocket.setSoTimeout(5000);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            Pattern pattern = Pattern.compile(".*\\[.*\\].*");

            String sentence = inFromServer.readLine();
            bufferedWriter.write("ehlo localhost");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            String sentence2 = inFromServer.readLine();
            Matcher matcher = pattern.matcher(sentence2);
            if (matcher.matches()) {
                site.setMonitorSmtp(true);
            }
//            modifiedSentence = inFromServer.readLine();
//            System.out.println(modifiedSentence);
            clientSocket.close();
        } catch (Exception e) {
            site.setMonitorSmtp(false);
            LOGGER.info("Could not reach site " + e);
        }
    }


    @Scheduled(cron = "0 * * * * *")
    public void sendEmail() {
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new HeaderRequestInterceptor(AUTHORIZATION_KEY));


        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        List<String> numbers = new ArrayList<>();
        numbers.add("64212842309");
        try {
            for (Site site : repository.findAll()) {
                if (!site.getSendEmail() && !site.getAcknowledged() && site.getEnabled()) {
                    if (site.getState() == SiteState.ERROR) {
                        Properties properties = new Properties();
                        properties.setProperty("mail.transport.protocol", "smtp");
                        properties.setProperty("mail.smtp.auth", "true");
                        properties.setProperty("mail.smtp.starttls.enable", "false");
                        properties.setProperty("mail.debug", "false");
                        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
                        helper.setTo("shrey.shah@liverton.com");
                        helper.setFrom("livecheck@liverton.com");
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
                        MessageRequest messageRequest = new MessageRequest(site.getSiteName() + " is down. IP Address registered to the number is " + site.getIpAddress() + " Failed to ping " + site.getFailureCount() + " times.", numbers);
                        String result = restTemplate.postForObject("https://api.clickatell.com/rest/message", messageRequest, String.class);
                        LOGGER.info(result.toString());
                    }
                }
            }
        } catch (MessagingException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }


    @PostConstruct
    public void init() {
        scanSites();
    }


    @Override
    public List<com.liverton.livecheck.service.domain.Site> findSites() {
        return Arrays.asList(new com.liverton.livecheck.service.domain.Site("Test Site", true), new com.liverton.livecheck.service.domain.Site("Disabled Site", false));
    }
}
