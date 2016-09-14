package com.liverton.livecheck.dao.repository;

import com.liverton.livecheck.dao.model.ApplicationStatus;
import com.liverton.livecheck.dao.model.Organisation;
import com.liverton.livecheck.dao.model.Site;
import com.liverton.livecheck.model.ApplicationType;
import com.liverton.livecheck.model.NotificationAction;
import com.liverton.livecheck.model.SiteState;
import com.liverton.livecheck.test.support.AbstractBaseTest;
import org.junit.After;
import org.junit.Before;import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;


/**
 * Created by sshah on 31/08/2016.
 */
public class SiteTest extends AbstractBaseTest {
    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

    private static final String DEFAULT_SITENAME="TestSite123";

    private static final String DEFAULT_IPADDRESS="192.1.1.1";

    private static final Boolean DEFAULT_ENABLE=true;

    private static final String DEFAULT_ORGNAME="Tester Organisation";

    private static final String DEFAULT_DESCRIPTION="This organisation does nothing at all.";

    private static final String FROM_EMAIL="livecheck@liverton.com";

    private static final String TO_EMAIL="shrey.shah@liverton.com";

    private static final String HOST_ADDRESS="earth.liverton.local";

    private static final String PORT_NUMBER="25";

    private static final String TEXT_DESTINATION="64212842309";




    @Before
    public void init(){
        siteRepository.deleteAll();
    }

    @After
    public void destroy(){
        siteRepository.deleteAll();
    }

    @Test
    public void shouldCreateSite(){
        Site site = siteRepository.save(createUnpersistedSite());
        assertNotNull(site);
        assertEquals(DEFAULT_SITENAME,site.getSiteName());

    }
    @Test
    public void shouldFindByID(){
        Site site= siteRepository.save(createUnpersistedSite());
        assertNotNull(siteRepository.findOne(site.getId()));
    }
//    @Test
//    public void shouldHaveCreatedSingleSite(){
//        siteRepository.save(createUnpersistedSite());
//        assertEquals(1, siteRepository.count());
//    }

    @Test
    public void shouldFindByName(){
        Site site = siteRepository.save(createUnpersistedSite());
        assertNotNull(siteRepository.findBySiteName(site.getSiteName()));
    }


//    @Test
//    public void shouldDeleteSite(){
//
//        Site site =siteRepository.save(createUnpersistedSite());
//        assertNotNull(site);
//
//        siteRepository.delete(site);
//        assertEquals(0,siteRepository.findBySiteName(DEFAULT_SITENAME).size());
////        assertNull(siteRepository.findBySiteName(DEFAULT_SITENAME));
//    }

    @Test
    public void shouldEditSite(){
        Site site=siteRepository.save(createUnpersistedSite());
        site.setSiteName("test123");
        site.setIpAddress("www.google.com");
        site.setAverageResponse("8ms");
        site.setEnabled(true);
        siteRepository.save(site);

        assertNotNull(siteRepository.findBySiteName("test123"));
        assertEquals("test123", site.getSiteName());  //testing the new site name
        assertEquals("www.google.com", site.getIpAddress());  //testing the new ip address
        assertEquals("8ms", site.getAverageResponse()); //testing the new average response
        assertEquals(true, site.getEnabled());  //testing the new state

    }

    private Site createUnpersistedSite(){
        Organisation organisation = new Organisation(DEFAULT_ORGNAME,DEFAULT_DESCRIPTION,FROM_EMAIL,TO_EMAIL,HOST_ADDRESS,PORT_NUMBER,TEXT_DESTINATION);
        List<ApplicationStatus> applicationStatusList = new ArrayList<>();

        organisationRepository.save(organisation);
        return new Site(DEFAULT_SITENAME,DEFAULT_ENABLE,DEFAULT_IPADDRESS,new Date(), SiteState.OKAY,false, NotificationAction.EMAIL,0,"0ms",true,false,false,applicationStatusList,organisation);
    }

    @Test
    public void ShouldRun() {
        List<Site> siteList = siteRepository.findAll();
    }
}