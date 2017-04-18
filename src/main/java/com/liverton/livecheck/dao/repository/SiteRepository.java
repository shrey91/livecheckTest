package com.liverton.livecheck.dao.repository;

import com.liverton.livecheck.dao.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by sshah on 8/08/2016.
 */
@Transactional
public interface SiteRepository extends JpaRepository<Site, Long> {

    List<Site> findBySiteName(String siteName);

    List<Site> findAllByOrderBySiteName();
//    List<Site> findBySite(Site site);

    List<Site> findAllByOrderByOrganisationIdDesc();
}
