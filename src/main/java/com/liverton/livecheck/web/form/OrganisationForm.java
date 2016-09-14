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

    @NotEmpty(message = "From Email cannot be empty")
    private String fromEmail;

    @NotEmpty(message = "To Email cannot be empty")
    private String toEmail;

    @NotEmpty(message = "Host cannot be empty")
    private String host;

    @NotEmpty(message = "Port Number cannot be empty")
    private String portNumber;

    @NotEmpty(message = "Text Destination cannot be empty")
    private String textDestination;



    public OrganisationForm() {
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

    //    public OrganisationForm(String orgName, String description) {
//        this.orgName = orgName;
//        this.description = description;
//    }

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

    public OrganisationForm(String orgName, String description, String fromEmail, String toEmail, String host, String portNumber, String textDestination) {
        this.orgName = orgName;
        this.description = description;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.host = host;
        this.portNumber = portNumber;
        this.textDestination = textDestination;
    }

    @Override
    public String toString() {
        return "OrganisationForm{" +
                "orgName='" + orgName + '\'' +
                ", description='" + description + '\'' +
                ", fromEmail='" + fromEmail + '\'' +
                ", toEmail='" + toEmail + '\'' +
                ", host='" + host + '\'' +
                ", portNumber='" + portNumber + '\'' +
                ", textDestination='" + textDestination + '\'' +
                '}';
    }
}
