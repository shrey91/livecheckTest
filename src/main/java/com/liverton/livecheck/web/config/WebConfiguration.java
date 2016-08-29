package com.liverton.livecheck.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by sshah on 8/08/2016.
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/").setViewName("hello");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/welcome").setViewName("welcome");
        registry.addViewController("/newsite").setViewName("newsite");
        registry.addViewController("/existingsites").setViewName("existingsites");
        registry.addViewController(("/newuser")).setViewName("newuser");
        registry.addViewController("/users").setViewName("users");
        registry.addViewController("/organisations").setViewName("organisations");
        registry.addViewController("/addAuth").setViewName("addauthority");
        registry.addViewController("/authorities").setViewName("authorities");
        registry.addViewController("/newOrganisation").setViewName("newOrganisation");
        registry.addViewController("/editAuth").setViewName("editauthorities");
        registry.addViewController("/edituser").setViewName("edituser");
        registry.addViewController("/viewpingresult").setViewName("viewpingresult");
        registry.addViewController("/vieworganisationsites").setViewName("vieworganisationsites");
        registry.addViewController("/sitedisable").setViewName("sitedisable");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/").addResourceLocations("/src/main/webapp/resources", "classpath:/resources/");
    }


}
