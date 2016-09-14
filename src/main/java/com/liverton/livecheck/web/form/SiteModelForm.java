package com.liverton.livecheck.web.form;

import com.liverton.livecheck.dao.model.ApplicationStatus;
import com.liverton.livecheck.dao.model.Organisation;
import com.liverton.livecheck.dao.model.SitePingResult;
import com.liverton.livecheck.model.NotificationAction;
import com.liverton.livecheck.model.SiteState;
import com.liverton.livecheck.service.domain.Site;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by sshah on 9/08/2016.
 */

public class SiteModelForm {


    private Long id;

    @NotEmpty(message = "Site name cannot be empty")
    private String siteName;

    @NotNull(message = "Enabled cannot be empty")
    private Boolean enabled;

    @NotEmpty(message = "IP Address cannot be empty")
    private String ipAddress;

    private String pingTime;

    private String smsDelay;

    private NotificationAction action;

    private String ping;

    private Organisation organisation;

    private SiteState state;

    private String averageResponse;

//    private SitePingResult sitePingResult;

    private List<ApplicationStatus> applicationStatus;



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

    public NotificationAction getAction() {
        return action;
    }

    public void setAction(NotificationAction action) {
        this.action = action;
    }

    public String getPing() {
        return ping;
    }

    public void setPing(String ping) {
        this.ping = ping;
    }

    public String getPingTime() {
        return pingTime;
    }

    public void setPingTime(String pingTime) {
        this.pingTime = pingTime;
    }


    public String getSmsDelay() {
        return smsDelay;
    }

    public void setSmsDelay(String smsDelay) {
        this.smsDelay = smsDelay;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public SiteState getState() {
        return state;
    }

    public void setState(SiteState state) {
        this.state = state;
    }

    public String getAverageResponse() {
        return averageResponse;
    }

    public void setAverageResponse(String averageResponse) {
        this.averageResponse = averageResponse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //    public SitePingResult getSitePingResult() {
//        return sitePingResult;
//    }
//
//    public void setSitePingResult(SitePingResult sitePingResult) {
//        this.sitePingResult = sitePingResult;
//    }

    public List<ApplicationStatus> getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(List<ApplicationStatus> applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public SiteModelForm() {
    }

//    public SiteModelForm(String siteName, Boolean enabled, String ipAddress) {
//        this.siteName = siteName;
//        this.enabled = enabled;
//        this.ipAddress = ipAddress;
//    }
//
//    public SiteModelForm(String siteName, Boolean enabled, String ipAddress, SiteState state, String averageResponse) {
//        this.siteName = siteName;
//        this.enabled = enabled;
//        this.ipAddress = ipAddress;
//        this.state = state;
//        this.averageResponse = averageResponse;
//    }

    public SiteModelForm(String siteName, Boolean enabled, String ipAddress, String pingTime, String smsDelay, NotificationAction action, String ping, List<ApplicationStatus> applicationStatus, Organisation organisation, SiteState state, String averageResponse,Long id) {
        this.siteName = siteName;
        this.enabled = enabled;
        this.ipAddress = ipAddress;
        this.pingTime = pingTime;
        this.smsDelay = smsDelay;
        this.action = action;
        this.ping = ping;
        this.applicationStatus = applicationStatus;
        this.organisation = organisation;
        this.state = state;
        this.averageResponse = averageResponse;
        this.id = id;
    }

    @Override
    public String toString() {
        return "SiteModelForm{" +
                "siteName='" + siteName + '\'' +
                ", enabled=" + enabled +
                ", ipAddress='" + ipAddress + '\'' +
                ", pingTime='" + pingTime + '\'' +
                ", smsDelay='" + smsDelay + '\'' +
                ", action=" + action +
                ", ping='" + ping + '\'' +
                ", organisation=" + organisation +
                ", state=" + state +
                ", averageResponse='" + averageResponse + '\'' +
//                ", sitePingResult=" + sitePingResult +
                '}';
    }
}
