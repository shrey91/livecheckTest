package com.liverton.livecheck.service.impl;

import com.liverton.livecheck.boot.config.Application;
import com.liverton.livecheck.dao.model.ApplicationStatus;
import com.liverton.livecheck.dao.repository.ApplicationStatusRepository;
import com.liverton.livecheck.service.ApplicationStatusService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sshah on 29/08/2016.
 */
public class ApplicationStatusServiceImpl implements ApplicationStatusService {

    @Autowired
    private ApplicationStatusRepository applicationStatusRepository;

    @Override
    public List<ApplicationStatus> getAllApplicationStatus(){
        return applicationStatusRepository.findAll();
    }


}
