package com.liverton.livecheck.dao.repository;

import com.liverton.livecheck.dao.model.Site;
import com.liverton.livecheck.dao.model.SitePingResult;
import com.liverton.livecheck.model.PingState;
import com.liverton.livecheck.model.SiteState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by sshah on 17/08/2016.
 */
@Transactional
public interface SitePingResultRepository extends JpaRepository<SitePingResult, Long> {

    SitePingResult findByDate(Date date);

    SitePingResult findByPingState(PingState pingState);

    List<SitePingResult> findTop10BySiteOrderByDateDesc(Site site);
}
