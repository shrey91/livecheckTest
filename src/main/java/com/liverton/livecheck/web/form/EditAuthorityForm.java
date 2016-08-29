package com.liverton.livecheck.web.form;

import com.liverton.livecheck.dao.model.Authority;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by sshah on 16/08/2016.
 */
public class EditAuthorityForm extends Authority {

    @NotNull(message = "ID cannot be empty")
    private Long id;

    public EditAuthorityForm() {
    }

    public EditAuthorityForm(String role, String description, Long id) {
        super(role, description);
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
