package com.liverton.livecheck.dao.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by sshah on 8/08/2016.
 */
@Entity
public class Authority extends AbstractPersistable<Long>{

    @Column
    private String role;

    @Column
    private String authDescription;

    public Authority() {
    }

    public Authority(String role, String description) {
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
        return "Authority{" +
                "role='" + role + '\'' +
                ", description='" + authDescription + '\'' +
                '}';
    }
}
