package com.liverton.livecheck.dao.model;

import com.liverton.livecheck.model.ApplicationType;
import com.liverton.livecheck.model.SiteState;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.hibernate.annotations.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Created by sshah on 29/08/2016.
 */
@Entity
public class ApplicationStatus extends AbstractPersistable<Long> {

    @Column
    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType;

    @Column
    private SiteState siteState;

    @Column
    private Boolean enabled;

    @Column(nullable = false)
    private Integer failureCount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Site_id")
    private Site site;


    public ApplicationStatus() {
    }

    public ApplicationStatus(ApplicationType applicationType, Site site, Boolean enabled) {
        this.applicationType = applicationType;
        this.site = site;
        siteState = SiteState.OKAY;
        this.enabled = enabled;
        failureCount = 0;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }


    public SiteState getSiteState() {
        return siteState;
    }

    public void setSiteState(SiteState siteState) {
        this.siteState = siteState;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "ApplicationStatus{" +
                "applicationType=" + applicationType +
                ", siteState=" + siteState +
                ", enabled=" + enabled +
                ", failureCount=" + failureCount +
                '}';
    }
}
