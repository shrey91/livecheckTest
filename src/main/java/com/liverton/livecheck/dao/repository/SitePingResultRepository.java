package com.liverton.livecheck.dao.repository;

import com.liverton.livecheck.dao.model.SitePingResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

/**
 * Created by sshah on 17/08/2016.
 */
public interface SitePingResultRepository extends JpaRepository<SitePingResult, Long> {

    SitePingResult findByDate(Date date);
}
