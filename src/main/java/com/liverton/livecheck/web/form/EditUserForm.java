package com.liverton.livecheck.web.form;

import com.liverton.livecheck.dao.model.Authority;
import com.liverton.livecheck.dao.model.User;
import org.slf4j.Logger;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by sshah on 16/08/2016.
 */
public class EditUserForm extends UserForm {

    @NotNull(message = "ID Cannot be null")
    private Long id;

    public EditUserForm() {
    }


    public EditUserForm(String name, String password, Boolean userEnabled, Long id) {
        super(name, password, userEnabled);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
