package com.liverton.livecheck.web.form;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by sshah on 10/08/2016.
 */
public class AuthorityForm {

    @NotEmpty(message = "A role must be assigned to user")
    private String role;

    @NotEmpty(message = "This is the description of the role")
    private String authDescription;

    public AuthorityForm() {
    }

    public AuthorityForm(String role, String description) {
        this.role = role;
        this.authDescription = description;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return authDescription;
    }

    public void setDescription(String description) {
        this.authDescription = description;
    }

    @Override
    public String toString() {
        return "AuthorityForm{" +
                "role='" + role + '\'' +
                ", description='" + authDescription + '\'' +
                '}';
    }
}
