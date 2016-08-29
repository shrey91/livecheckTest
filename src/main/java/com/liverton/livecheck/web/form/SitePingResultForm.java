package com.liverton.livecheck.web.form;

import com.liverton.livecheck.dao.model.SiteModel;
import com.liverton.livecheck.model.PingState;

import java.util.Date;

/**
 * Created by sshah on 17/08/2016.
 */
public class SitePingResultForm {

    private String responseTime;

    private PingState pingState;

    private SiteModel siteModel;

    public SitePingResultForm() {
    }

    public SitePingResultForm( String responseTime, PingState pingState, SiteModel siteModel) {
        this.responseTime = responseTime;
        this.pingState = pingState;
        this.siteModel = siteModel;
    }


    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public PingState getPingState() {
        return pingState;
    }

    public void setPingState(PingState pingState) {
        this.pingState = pingState;
    }

    public SiteModel getSiteModel() {
        return siteModel;
    }

    public void setSiteModel(SiteModel siteModel) {
        this.siteModel = siteModel;
    }

    @Override
    public String toString() {
        return "SitePingResultForm{" +
                ", responseTime='" + responseTime + '\'' +
                ", pingState=" + pingState +
                ", siteModel=" + siteModel +
                '}';
    }
}

