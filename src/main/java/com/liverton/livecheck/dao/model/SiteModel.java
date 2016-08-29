package com.liverton.livecheck.dao.model;

import com.liverton.livecheck.model.NotificationAction;
import com.liverton.livecheck.model.SiteState;
import com.sun.istack.internal.Nullable;
import org.aspectj.weaver.ast.Or;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sshah on 8/08/2016.
 */
@Entity
public class SiteModel extends AbstractPersistable<Long> {

    @Column(nullable = false)
    private String siteName;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private SiteState state;

    @Column(nullable = false)
    private Boolean isAcknowledged;

    @Column(nullable = false)
    private NotificationAction action;

    @Column(nullable = false)
    private Integer failureCount;

    @Column(nullable = false)
    private String averageResponse;

    @Column(nullable = false)
    private Boolean sendEmail;

    @Column(nullable = false)
    private Boolean monitorHttp;

    @Column(nullable = false)
    private Boolean monitorSmtp;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Organisation_id", nullable = false)
    private Organisation organisation;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "siteModel", orphanRemoval = true)
    @Cascade(value = CascadeType.ALL)
    private List<SitePingResult> sitePingResults = new ArrayList<>();


    public SiteModel() {

    }

    public SiteModel(String siteName, Boolean enabled, String ipAddress, Date date, SiteState state, Boolean isAcknowledged, NotificationAction action, Integer failureCount, String averageResponse, Boolean sendEmail, Boolean monitorHttp, Boolean monitorSmtp, Organisation organisation) {
        this.siteName = siteName;
        this.enabled = enabled;
        this.ipAddress = ipAddress;
        this.date = date;
        this.state = state;
        this.isAcknowledged = isAcknowledged;
        this.action = action;
        this.failureCount = failureCount;
        this.averageResponse = averageResponse;
        this.sendEmail = sendEmail;
        this.monitorHttp = monitorHttp;
        this.monitorSmtp = monitorSmtp;
        this.organisation = organisation;
    }


    public Boolean getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public SiteState getState() {
        return state;
    }

    public void setState(SiteState state) {
        this.state = state;
    }

    public Boolean getAcknowledged() {
        return isAcknowledged;
    }

    public void setAcknowledged(Boolean acknowledged) {
        isAcknowledged = acknowledged;
    }

    public NotificationAction getAction() {
        return action;
    }

    public void setAction(NotificationAction action) {
        this.action = action;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }

    public String getAverageResponse() {
        return averageResponse;
    }

    public void setAverageResponse(String averageResponse) {
        this.averageResponse = averageResponse;
    }

    public List<SitePingResult> getSitePingResults() {
        return sitePingResults;
    }

    public void setSitePingResults(List<SitePingResult> sitePingResults) {
        this.sitePingResults = sitePingResults;
    }

    public void addPingResult(SitePingResult sitePingResult) {
        sitePingResult.setSiteModel(this);
        sitePingResults.add(sitePingResult);
    }

    public Boolean getMonitorHttp() {
        return monitorHttp;
    }

    public void setMonitorHttp(Boolean monitorHttp) {
        this.monitorHttp = monitorHttp;
    }

    public Boolean getMonitorSmtp() {
        return monitorSmtp;
    }

    public void setMonitorSmtp(Boolean monitorSmtp) {
        this.monitorSmtp = monitorSmtp;
    }

    @Override
    public String toString() {
        return "SiteModel{" +
                "siteName='" + siteName + '\'' +
                ", enabled=" + enabled +
                ", ipAddress='" + ipAddress + '\'' +
                ", date=" + date +
                ", state=" + state +
                ", isAcknowledged=" + isAcknowledged +
                ", action=" + action +
                ", failureCount=" + failureCount +
                ", averageResponse='" + averageResponse + '\'' +
                ", sendEmail=" + sendEmail +
                ", monitorHttp=" + monitorHttp +
                ", monitorSmtp=" + monitorSmtp +
                '}';
    }
}
