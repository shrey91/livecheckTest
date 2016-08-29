package com.liverton.livecheck.web.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by sshah on 10/08/2016.
 */

public class UserForm {

    @NotEmpty(message = "User name cannot be empty")
    private String name;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotNull(message = "User has to be enabled or disabled")
    private Boolean userEnabled;

    public UserForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return userEnabled;
    }

    public void setEnabled(Boolean enabled) {
        this.userEnabled = enabled;
    }

    public UserForm(String name, String password, Boolean enabled) {
        this.name = name;
        this.password = password;
        this.userEnabled = enabled;
    }

    @Override
    public String toString() {
        return "UserForm{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + userEnabled +
                '}';
    }
}
