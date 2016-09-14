package com.liverton.livecheck.service;

import com.liverton.livecheck.dao.model.Organisation;
import com.liverton.livecheck.dao.model.Site;
import com.liverton.livecheck.model.NotificationAction;
import com.liverton.livecheck.model.SiteState;
import com.liverton.livecheck.web.form.SiteModelForm;

import java.util.Date;
import java.util.List;

/**
 * Created by sshah on 8/08/2016.
 */
public interface SiteService {

    List<Site> findSites();

    Site newSite(String siteName, Boolean enabled, String ipAddress, SiteState siteState, NotificationAction notificationAction, String averageResponse, Organisation organisation);



}
