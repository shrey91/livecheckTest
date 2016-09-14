package com.liverton.livecheck.web.config;

import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by sshah on 11/08/2016.
 */
@Configuration
public class JettyConfiguration implements EmbeddedServletContainerCustomizer {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JettyConfiguration.class);

    @Value("${jetty.ssl.renegotiationAllowed}")
    private Boolean renegotiationAllowed;

    @Value("${jetty.ssl.includeCiphers}")
    private String[] includeCiphers;

    @Value("${jetty.ssl.excludeCiphers}")
    private String[] excludeCiphers;

    @Value("${jetty.ssl.excludeProtocols}")
    private String[] excludeProtocols;

    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        try {

            LOGGER.info("Customising JETTY container configuration.....");

            final JettyEmbeddedServletContainerFactory containerFactory = (JettyEmbeddedServletContainerFactory) configurableEmbeddedServletContainer;
            containerFactory.setDocumentRoot(calculateWebApplicationRoot());

            LOGGER.info("Jetty Mime Mappings({})", containerFactory.getMimeMappings());
            LOGGER.info("Jetty SSL Protocols({})", containerFactory.getSsl().getProtocol());
            LOGGER.info("Document root set as({}), path exists({})", containerFactory.getDocumentRoot(), containerFactory.getDocumentRoot().exists());

            if (!containerFactory.getDocumentRoot().exists()) {
                throw new RuntimeException("Unable to resolve web application root directory - check 'webapp' exists!");
            }

            disableJettyVersionDisplay(containerFactory);

        } catch (IOException ex) {
            LOGGER.error("Error setting application base");
        }
    }

    @Bean
    public ServletContextInitializer initializer() {
        return new ServletContextInitializer() {

            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                SessionCookieConfig configuration = servletContext.getSessionCookieConfig();
                configuration.setName(getCookieName());
            }

        };
    }

    /**
     * Default cookie name - can be overidden by extending classes
     *
     * @return
     */
    protected String getCookieName() {
        return "JSESSSIONID";
    }

    private void disableJettyVersionDisplay(JettyEmbeddedServletContainerFactory containerFactory) {
        containerFactory.addServerCustomizers(new JettyServerCustomizer() {
            @Override
            public void customize(Server server) {
                for (Connector y : server.getConnectors()) {
                    for (ConnectionFactory x : y.getConnectionFactories()) {
                        if (x instanceof HttpConnectionFactory) {
                            ((HttpConnectionFactory) x).getHttpConfiguration().setSendServerVersion(false);
                        }
                    }
                }
            }
        });
    }

    private File calculateWebApplicationRoot() throws IOException {

        final File resource = new File("./src/main/webapp");
        if (resource.exists()) {
            return resource;
        } else {
            return new ClassPathResource("WEB-INF").getFile();
        }
    }
}
