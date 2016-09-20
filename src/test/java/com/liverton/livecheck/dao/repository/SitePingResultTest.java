package com.liverton.livecheck.dao.repository;

import com.liverton.livecheck.dao.model.ApplicationStatus;
import com.liverton.livecheck.dao.model.Organisation;
import com.liverton.livecheck.dao.model.Site;
import com.liverton.livecheck.dao.model.SitePingResult;
import com.liverton.livecheck.model.NotificationAction;
import com.liverton.livecheck.model.PingState;
import com.liverton.livecheck.model.SiteState;
import com.liverton.livecheck.test.support.AbstractBaseTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by sshah on 2/09/2016.
 */
public class SitePingResultTest extends AbstractBaseTest {

    @Autowired
    private SitePingResultRepository sitePingResultRepository;

    private static final String DEFAULT_RESPONSETIME = "0ms";

    private static final String DEFAULT_ORGNAME="Tester Organisation";

    private static final String DEFAULT_DESCRIPTION="This organisation does nothing at all.";

    private static final String FROM_EMAIL="livecheck@liverton.com";

    private static final String TO_EMAIL="shrey.shah@liverton.com";

    private static final String HOST_ADDRESS="earth.liverton.local";

    private static final String PORT_NUMBER="25";

    private static final String TEXT_DESTINATION="64212842309";


    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Before
    public void init(){
        sitePingResultRepository.deleteAll();
    }

    @After
    public void destroy(){
        sitePingResultRepository.deleteAll();
    }

    @Test
    public void shouldCreateSitePingResult(){
        SitePingResult sitePingResult = sitePingResultRepository.save(createUnpersistedSitePingResult());
        assertNotNull(sitePingResult);
        assertEquals(DEFAULT_RESPONSETIME,sitePingResult.getResponseTime());
    }

    @Test
    public void shouldFindByResponseTime(){
        SitePingResult sitePingResult = sitePingResultRepository.save(createUnpersistedSitePingResult());
        assertNotNull(DEFAULT_RESPONSETIME, sitePingResult.getResponseTime());
    }
    @Test
    public void shouldHaveCreatedSingleSitePing(){
        sitePingResultRepository.save(createUnpersistedSitePingResult());
        assertEquals(1, sitePingResultRepository.count());
    }

    @Test
    public void shouldFindByDate(){
        SitePingResult sitePingResult = sitePingResultRepository.save(createUnpersistedSitePingResult());
        Assert.assertNotNull(sitePingResultRepository.findByDate(sitePingResult.getDate()));
    }


    @Test
    public void shouldDeleteSitePingResult(){

        SitePingResult sitePingResult = sitePingResultRepository.save(createUnpersistedSitePingResult());
        Assert.assertNotNull(sitePingResult);

        sitePingResultRepository.delete(sitePingResult);
        assertEquals(null,sitePingResultRepository.findByPingState(PingState.NO));
//        assertNull(siteRepository.findBySiteName(DEFAULT_SITENAME));
    }

    @Test
    public void shouldEditSitePingResult(){
        SitePingResult sitePingResult = sitePingResultRepository.save(createUnpersistedSitePingResult());
        sitePingResult.setDate(new Date());
        sitePingResult.setResponseTime("25ms");
        sitePingResult.setPingState(PingState.NO);
        sitePingResultRepository.save(sitePingResult);
        Assert.assertNotNull(sitePingResultRepository.findByPingState(PingState.NO));
        assertEquals("25ms", sitePingResult.getResponseTime());  //testing the new response time
        assertEquals(PingState.NO, sitePingResult.getPingState()); //testing the new ping state

    }

    private SitePingResult createUnpersistedSitePingResult(){
        Organisation organisation = new Organisation(DEFAULT_ORGNAME,DEFAULT_DESCRIPTION,FROM_EMAIL,TO_EMAIL,HOST_ADDRESS,PORT_NUMBER,TEXT_DESTINATION);
        List<ApplicationStatus> applicationStatusList = new ArrayList<>();
        organisationRepository.save(organisation);
        Site site = new Site("Google", true,"www.google.com", new Date(), SiteState.OKAY,false, NotificationAction.EMAIL,0,"0ms",true,false,false,applicationStatusList,organisation);
        siteRepository.save(site);
        return new SitePingResult(new Date(),PingState.YES,DEFAULT_RESPONSETIME,site);
    }

    @Test
    public void ShouldRun() {
        List<SitePingResult> sitePingResultList = sitePingResultRepository.findAll();
    }


}
