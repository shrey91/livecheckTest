package com.liverton.livecheck.dao.model;

import com.liverton.livecheck.model.PingState;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by sshah on 17/08/2016.
 */

@Entity
public class SitePingResult extends AbstractPersistable<Long> {

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private PingState pingState;

    @Column(nullable = false)
    private String responseTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Site_id")
    private SiteModel siteModel;


    public SitePingResult() {
    }

    public SitePingResult(Date date, String responseTime, SiteModel siteModel) {
        this.date = date;
        this.responseTime = responseTime;
        this.siteModel = siteModel;
    }

    public SitePingResult(Date date, PingState pingState, String responseTime, SiteModel siteModel) {
        this.date = date;
        this.pingState = pingState;
        this.responseTime = responseTime;
        this.siteModel = siteModel;
    }

    @Override
    public String toString() {
        return "SitePingResult{" +
                "date=" + date +
                ", pingState=" + pingState +
                ", responseTime='" + responseTime + '\'' +
                ", siteModel=" + siteModel +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public SiteModel getSiteModel() {
        return siteModel;
    }

    public void setSiteModel(SiteModel siteModel) {
        this.siteModel = siteModel;
    }

    public PingState getPingState() {
        return pingState;
    }

    public void setPingState(PingState pingState) {
        this.pingState = pingState;
    }

}
