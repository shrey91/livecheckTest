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
    private List<SiteModel> siteModels = new ArrayList<>();

    public List<SiteModel> getSiteModels() {
        return this.siteModels = siteModels;
    }

    public void setSiteModels(List<SiteModel> siteModels) {
        this.siteModels = siteModels;
    }

    public void addSiteModel(SiteModel siteModel) {
        siteModel.setOrganisation(this);
        siteModels.add(siteModel);
    }

    public void removeSiteModel(SiteModel siteModel) {
        siteModels.remove(siteModel);
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

    public Organisation(String orgName, String description, List<SiteModel> siteModels) {
        this.orgName = orgName;
        this.description = description;
        this.siteModels = siteModels;
    }

    @Override
    public String toString() {
        return "Organisation{" +
                "orgName='" + orgName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
