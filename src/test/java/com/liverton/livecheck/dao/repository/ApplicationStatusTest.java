package com.liverton.livecheck.dao.repository;

import com.liverton.livecheck.boot.config.Application;
import com.liverton.livecheck.dao.model.ApplicationStatus;
import com.liverton.livecheck.dao.model.Organisation;
import com.liverton.livecheck.dao.model.Site;
import com.liverton.livecheck.model.ApplicationType;
import com.liverton.livecheck.model.NotificationAction;
import com.liverton.livecheck.model.SiteState;
import com.liverton.livecheck.test.support.AbstractBaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by sshah on 2/09/2016.
 */
public class ApplicationStatusTest extends AbstractBaseTest {

    @Autowired
    private ApplicationStatusRepository applicationStatusRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private OrganisationRepository organisationRepository;



    @Before
    public void init(){
        applicationStatusRepository.deleteAll();
    }

    @After
    public void destroy(){
        applicationStatusRepository.deleteAll();
    }

    @Test
    public void shouldCreateApplicationStatus(){

        ApplicationStatus applicationStatus = applicationStatusRepository.save(createUnpersistedApplicationStatus());
        assertNotNull(applicationStatus);
        assertEquals(ApplicationType.HTTP,applicationStatus.getApplicationType());

    }
    @Test
    public void shouldFindByID(){
        ApplicationStatus applicationStatus= applicationStatusRepository.save(createUnpersistedApplicationStatus());
        assertNotNull(applicationStatusRepository.findOne(applicationStatus.getId()));
    }
    @Test
    public void shouldHaveCreatedSingleApplicationStatus(){
        applicationStatusRepository.save(createUnpersistedApplicationStatus());
        assertEquals(1, applicationStatusRepository.count());
    }

//    @Test
//    public void shouldFindByName(){
//        ApplicationStatus applicationStatus = applicationStatusRepository.save(createUnpersistedApplicationStatus());
//        assertNotNull(applicationStatusRepository.findBySite(applicationStatus.getSite()));
//    }


    @Test
    public void shouldDeleteApplicationType(){

        ApplicationStatus applicationStatus = applicationStatusRepository.save(createUnpersistedApplicationStatus());
        assertNotNull(applicationStatus);

        applicationStatusRepository.delete(applicationStatus);
//        assertNull(organisationRepository.findByOrgName(DEFAULT_ORGNAME));
        assertEquals(0,applicationStatusRepository.findAll().size());
    }

    @Test
    public void shouldEditApplicationType(){
        ApplicationStatus applicationStatus = applicationStatusRepository.save(createUnpersistedApplicationStatus());
        applicationStatus.setSiteState(SiteState.ERROR);
        applicationStatus.setApplicationType(ApplicationType.PING);
        applicationStatus.setFailureCount(8);
        applicationStatusRepository.save(applicationStatus);

        assertNotNull(applicationStatusRepository.findAll());
        assertEquals(SiteState.ERROR, applicationStatus.getSiteState());  //testing the new site state
        assertEquals(ApplicationType.PING, applicationStatus.getApplicationType());  //testing the new application type
//        assertEquals("8",applicationStatus.getFailureCount());
    }

    private ApplicationStatus createUnpersistedApplicationStatus(){

        Organisation organisation = new Organisation("Tester Site", "This is only a test","livecheck@liverton.com","shrey.shah@liverton.com","earth.liverton.local","25","64212842309");
        List<ApplicationStatus> applicationStatusList = new ArrayList<>();
        organisationRepository.save(organisation);
        Site site = new Site("Google", true,"www.google.com", new Date(), SiteState.OKAY,false, NotificationAction.EMAIL,0,"0ms",true,false,false,applicationStatusList,organisation);
        siteRepository.save(site);
        return new ApplicationStatus(ApplicationType.HTTP,site, true);
    }

    public void createSite(){
        Organisation organisation = new Organisation("Tester Site", "This is only a test");
        List<ApplicationStatus> applicationStatusList = new ArrayList<>();
        organisationRepository.save(organisation);
        Site site = new Site("Google", true,"www.google.com", new Date(), SiteState.OKAY,false, NotificationAction.EMAIL,0,"0ms",true,false,false,applicationStatusList,organisation);
        siteRepository.save(site);
    }
    @Test
    public void ShouldRun() {
        List<ApplicationStatus> applicationStatusList = applicationStatusRepository.findAll();
    }
}

