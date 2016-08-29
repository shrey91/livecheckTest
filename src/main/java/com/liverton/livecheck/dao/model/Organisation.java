package com.liverton.livecheck.dao.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.jpa.domain.AbstractPersistable;

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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "organisation", orphanRemoval = true)
    @Cascade(value = CascadeType.ALL)
    private List<Site> sites = new ArrayList<>();

    public List<Site> getSites() {
        return this.sites = sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
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

    public Organisation(String orgName, String description, List<Site> sites) {
        this.orgName = orgName;
        this.description = description;
        this.sites = sites;
    }

    @Override
    public String toString() {
        return "Organisation{" +
                "orgName='" + orgName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
