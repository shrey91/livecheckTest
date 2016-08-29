package com.liverton.livecheck.service;

import com.liverton.livecheck.dao.model.Organisation;

import java.util.List;

/**
 * Created by sshah on 10/08/2016.
 */
public interface OrganisationService {

    List<Organisation> findByOrgName();

    List<Organisation> getAllOrganisations();
}
