package com.liverton.livecheck.dao.repository;

import com.liverton.livecheck.dao.model.Organisation;
import com.liverton.livecheck.test.support.AbstractBaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by sshah on 31/08/2016.
 */
public class OrganisationTest extends AbstractBaseTest {

    @Autowired
    private OrganisationRepository organisationRepository;

    private static final String DEFAULT_ORGNAME="Tester Organisation";

    private static final String DEFAULT_DESCRIPTION="This organisation does nothing at all.";

    private static final String FROM_EMAIL="livecheck@liverton.com";

    private static final String TO_EMAIL="shrey.shah@liverton.com";

    private static final String HOST_ADDRESS="earth.liverton.local";

    private static final String PORT_NUMBER="25";

    private static final String TEXT_DESTINATION="64212842309";



    @Before
    public void init(){
        organisationRepository.deleteAll();
    }

    @After
    public void destroy(){
        organisationRepository.deleteAll();
    }

    @Test
    public void shouldCreateOrganisation(){
        Organisation organisation = organisationRepository.save(createUnpersistedOrganisation());
        assertNotNull(organisation);
        assertEquals(DEFAULT_ORGNAME,organisation.getOrgName());

    }
    @Test
    public void shouldFindByID(){
        Organisation organisation= organisationRepository.save(createUnpersistedOrganisation());
        assertNotNull(organisationRepository.findOne(organisation.getId()));
    }
    @Test
    public void shouldHaveCreatedSingleOrganisation(){
        organisationRepository.save(createUnpersistedOrganisation());
        assertEquals(1, organisationRepository.count());
    }

    @Test
    public void shouldFindByName(){
        Organisation organisation= organisationRepository.save(createUnpersistedOrganisation());
        assertNotNull(organisationRepository.findByOrgName(organisation.getOrgName()));
    }


    @Test
    public void shouldDeleteOrganisation(){

        Organisation organisation =organisationRepository.save(createUnpersistedOrganisation());
        assertNotNull(organisation);

        organisationRepository.delete(organisation);
//        assertNull(organisationRepository.findByOrgName(DEFAULT_ORGNAME));
        assertEquals(0,organisationRepository.findByOrgName(DEFAULT_ORGNAME).size());
    }

    @Test
    public void shouldEditOrganisation(){
        Organisation organisation=organisationRepository.save(createUnpersistedOrganisation());
        organisation.setOrgName("Livenation");
        organisation.setDescription("Largest Events company");
        organisationRepository.save(organisation);

        assertNotNull(organisationRepository.findByOrgName("Livenation"));
        assertEquals("Livenation", organisation.getOrgName());  //testing the new organisation name
        assertEquals("Largest Events company", organisation.getDescription());  //testing the new organisation description
    }

    private Organisation createUnpersistedOrganisation(){
//        Organisation organisation = new Organisation("Tester Site", "This is only a test");
//        organisationRepository.save(organisation);
        return new Organisation(DEFAULT_ORGNAME,DEFAULT_DESCRIPTION,FROM_EMAIL,TO_EMAIL,HOST_ADDRESS, PORT_NUMBER,TEXT_DESTINATION);
    }

    @Test
    public void ShouldRun() {
        List<Organisation> organisationList = organisationRepository.findAll();
    }
}

