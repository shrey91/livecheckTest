package com.liverton.livecheck.service.impl;

import com.liverton.livecheck.dao.model.Organisation;
import com.liverton.livecheck.dao.repository.OrganisationRepository;
import com.liverton.livecheck.service.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sshah on 10/08/2016.
 */
@Service
public class OrganisationServiceImpl implements OrganisationService {

    @Autowired
    private OrganisationRepository organisationRepository;


    @Override
    public List<Organisation> getAllOrganisations() {
        return organisationRepository.findAll();

    }

    @Override
    public List<Organisation> findByOrgName() {
        return Arrays.asList(new Organisation("Organisation","Normal Organisation"));
    }
}
