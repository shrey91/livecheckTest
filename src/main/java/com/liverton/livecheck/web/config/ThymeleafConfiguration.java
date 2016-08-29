package com.liverton.livecheck.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafView;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * Created by sshah on 11/08/2016.
 */
@Configuration
public class ThymeleafConfiguration  {

    @Value("${thymeleaf.cache}")
    protected boolean thymeleafCache;

    @Value("${thymeleaf.templateMode}")
    protected String thymeleafMode;

    @Value("${thymeleaf.templateSuffix}")
    protected String thymeleafSuffix;

    @Value("${thymeleaf.encoding}")
    protected String thymeleafEncoding;

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(defaultTemplateResolver());
        templateEngine.addTemplateResolver(commonTemplateResolver());
        templateEngine.addDialect(new SpringSecurityDialect());

        return templateEngine;
    }

    @Bean
    public ServletContextTemplateResolver defaultTemplateResolver() {
        ServletContextTemplateResolver defaultTemplateResolver = new ServletContextTemplateResolver();
        defaultTemplateResolver.setPrefix("/WEB-INF/");
        defaultTemplateResolver.setSuffix(thymeleafSuffix);
        defaultTemplateResolver.setTemplateMode(thymeleafMode);
        defaultTemplateResolver.setCharacterEncoding(thymeleafEncoding);
        defaultTemplateResolver.setOrder(1);
        defaultTemplateResolver.setCacheable(thymeleafCache);

        return defaultTemplateResolver;
    }

    @Bean
    public TemplateResolver commonTemplateResolver() {
        TemplateResolver commonFragmentResolver = new ClassLoaderTemplateResolver();
        commonFragmentResolver.setPrefix("web-common/");
        commonFragmentResolver.setSuffix(thymeleafSuffix);
        commonFragmentResolver.setTemplateMode(thymeleafMode);
        commonFragmentResolver.setCharacterEncoding(thymeleafEncoding);
        commonFragmentResolver.setOrder(2);
        commonFragmentResolver.setCacheable(thymeleafCache);
        return commonFragmentResolver;
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setViewClass(ThymeleafView.class);
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding(thymeleafEncoding);
        resolver.setOrder(2);
        resolver.setCache(thymeleafCache);
        return resolver;
    }


}
