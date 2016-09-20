package com.liverton.livecheck.dao.repository;

import com.liverton.livecheck.dao.model.ApplicationStatus;
import com.liverton.livecheck.dao.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by sshah on 29/08/2016.
 */
public interface ApplicationStatusRepository extends JpaRepository<ApplicationStatus, Long> {

    List<Site> findBySite(Site site);

}
