package com.liverton.livecheck.service;

import com.liverton.livecheck.dao.model.ApplicationStatus;

import java.util.List;

/**
 * Created by sshah on 29/08/2016.
 */
public interface ApplicationStatusService {


    List<ApplicationStatus> getAllApplicationStatus();

}
