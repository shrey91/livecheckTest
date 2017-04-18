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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Site_id")
    private Site site;


    public SitePingResult() {
    }

    public SitePingResult(Date date, String responseTime, Site site) {
        this.date = date;
        this.responseTime = responseTime;
        this.site = site;
    }

    public SitePingResult(Date date, PingState pingState, String responseTime, Site site) {
        this.date = date;
        this.pingState = pingState;
        this.responseTime = responseTime;
        this.site = site;
    }

    @Override
    public String toString() {
        return "SitePingResult{" +
                "date=" + date +
                ", pingState=" + pingState +
                ", responseTime='" + responseTime + '\'' +
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

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public PingState getPingState() {
        return pingState;
    }

    public void setPingState(PingState pingState) {
        this.pingState = pingState;
    }

}
