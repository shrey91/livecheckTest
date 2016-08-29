package com.liverton.livecheck.web.form;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by sshah on 10/08/2016.
 */
public class OrganisationForm {

    @NotEmpty(message = "Organisation Name cannot be empty")
    private String orgName;

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    public OrganisationForm() {
    }

    public OrganisationForm(String orgName, String description) {
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

    @Override
    public String toString() {
        return "OrganisationForm{" +
                "orgName='" + orgName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
