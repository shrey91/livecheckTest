package com.liverton.livecheck.dao.repository;

import com.liverton.livecheck.dao.model.SiteModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by sshah on 8/08/2016.
 */

public interface SiteModelRepository extends JpaRepository<SiteModel, Long> {

    List<SiteModel> findBySiteName(String siteName);
}
