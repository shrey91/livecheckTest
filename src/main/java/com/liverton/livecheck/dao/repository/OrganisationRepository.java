package com.liverton.livecheck.dao.repository;

import com.liverton.livecheck.dao.model.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by sshah on 10/08/2016.
 */
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {

    List<Organisation> findByOrgName(String orgName);


}
