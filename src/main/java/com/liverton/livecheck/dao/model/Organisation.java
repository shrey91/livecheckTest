package com.liverton.livecheck.dao.model;

import org.hibernate.annotations.*;
import org.springframework.data.jpa.domain.AbstractPersistable;
import javax.persistence.OrderBy;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshah on 10/08/2016.
 */
@Entity
public class Organisation extends AbstractPersistable<Long> {

    @Column(nullable = false)
    private String orgName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String fromEmail;

    @Column(nullable = false)
    private String toEmail;

    @Column(nullable = false)
    private String host;

    @Column(nullable = false)
    private String portNumber;

    @Column(nullable = false)
    private String textDestination;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "organisation")
    @Fetch(FetchMode.SELECT)
    @Cascade(value = CascadeType.ALL)
    @OrderBy("siteName")
       private List<Site> sites = new ArrayList<>();

    public List<Site> getSites() {
        return this.sites = sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public String getTextDestination() {
        return textDestination;
    }

    public void setTextDestination(String textDestination) {
        this.textDestination = textDestination;
    }

    public void addSiteModel(Site site) {
        site.setOrganisation(this);
        sites.add(site);
    }

    public void removeSiteModel(Site site) {
        sites.remove(site);
    }

    public Organisation() {
    }

    public Organisation(String orgName, String description) {
        this.orgName = orgName;
        this.description = description;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Organisation(String orgName, String description, String fromEmail, String toEmail, String host, String portNumber, String textDestination) {
        this.orgName = orgName;
        this.description = description;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.host = host;
        this.portNumber = portNumber;
        this.textDestination = textDestination;

    }

//    public Organisation(String orgName, String description, List<Site> sites) {
//        this.orgName = orgName;
//        this.description = description;
//        this.sites = sites;
//    }

    @Override
    public String toString() {
        return "Organisation{" +
                "textDestination='" + textDestination + '\'' +
                ", portNumber='" + portNumber + '\'' +
                ", hostName='" + host + '\'' +
                ", toEmail='" + toEmail + '\'' +
                ", fromEmail='" + fromEmail + '\'' +
                ", description='" + description + '\'' +
                ", orgName='" + orgName + '\'' +
                '}';
    }
}
