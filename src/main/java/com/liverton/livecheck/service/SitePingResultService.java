package com.liverton.livecheck.service;

import com.liverton.livecheck.dao.model.SitePingResult;

import java.util.List;

/**
 * Created by sshah on 17/08/2016.
 */
public interface SitePingResultService {

    List<SitePingResult> findByDate();
}
