package com.liverton.livecheck.web.form;

import com.liverton.livecheck.dao.model.Organisation;

import javax.validation.constraints.NotNull;

/**
 * Created by sshah on 10/08/2016.
 */
public class EditOrganisationForm extends Organisation {

    @NotNull(message = "ID cannot be null")
    private Long id;

    public EditOrganisationForm() {
    }

    public EditOrganisationForm(String orgName, String description, String fromEmail, String toEmail, String host, String portNumber, String textDestination, Long id) {
        super(orgName, description,fromEmail,toEmail,host,portNumber,textDestination);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
