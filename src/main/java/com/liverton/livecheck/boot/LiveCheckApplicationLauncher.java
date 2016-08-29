package com.liverton.livecheck.boot;

import com.liverton.livecheck.boot.config.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by sshah on 8/08/2016.
 */
public class LiveCheckApplicationLauncher {

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}
