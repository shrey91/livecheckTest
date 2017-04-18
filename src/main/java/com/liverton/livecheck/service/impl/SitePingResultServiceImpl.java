package com.liverton.livecheck.service.impl;

import com.liverton.livecheck.dao.model.SitePingResult;
import com.liverton.livecheck.dao.repository.SitePingResultRepository;
import com.liverton.livecheck.service.SitePingResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sshah on 17/08/2016.
 */
@Service
public class SitePingResultServiceImpl implements SitePingResultService {

    @Autowired
    private SitePingResultRepository sitePingResultRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }


    @Override
    public List<SitePingResult> findByDate() {
        return sitePingResultRepository.findAll();
    }
}
