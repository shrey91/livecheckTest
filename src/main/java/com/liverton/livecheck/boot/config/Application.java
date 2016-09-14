package com.liverton.livecheck.boot.config;

import com.liverton.livecheck.dao.model.Organisation;
import com.liverton.livecheck.service.*;
import com.liverton.livecheck.service.impl.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by sshah on 8/08/2016.
 */
@Configuration
@SpringBootApplication
@ComponentScan(basePackages = "com.liverton.livecheck")
@EnableAutoConfiguration(exclude = {ThymeleafAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "com.liverton.livecheck")
@EntityScan(basePackages = "com.liverton.livecheck")
@EnableScheduling
public class Application {}
