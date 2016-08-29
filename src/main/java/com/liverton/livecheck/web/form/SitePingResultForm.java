package com.liverton.livecheck.web.form;

import com.liverton.livecheck.dao.model.Site;
import com.liverton.livecheck.model.PingState;

/**
 * Created by sshah on 17/08/2016.
 */
public class SitePingResultForm {

    private String responseTime;

    private PingState pingState;

    private Site site;

    public SitePingResultForm() {
    }

    public SitePingResultForm( String responseTime, PingState pingState, Site site) {
        this.responseTime = responseTime;
        this.pingState = pingState;
        this.site = site;
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

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Override
    public String toString() {
        return "SitePingResultForm{" +
                ", responseTime='" + responseTime + '\'' +
                ", pingState=" + pingState +
                ", site=" + site +
                '}';
    }
}

