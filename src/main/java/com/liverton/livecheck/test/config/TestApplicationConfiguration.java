package com.liverton.livecheck.test.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by jlister on 16/12/2015.
 */
@Configuration
@ComponentScan(basePackages = {
        "com.liverton.livecheck.dao"
})
@ContextConfiguration(name = "test.properties")
public class TestApplicationConfiguration {
}
