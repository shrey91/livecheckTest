package com.liverton.livecheck.web.form;

import com.liverton.livecheck.model.SiteState;

/**
 * Created by sshah on 15/08/2016.
 */
public class SiteStateForm extends SiteModelForm {

    public SiteStateForm() {
    }

    public SiteStateForm(String siteName, Boolean enabled, String ipAddress, SiteState state, String averageResponse) {
        super(siteName, enabled, ipAddress, state, averageResponse);
    }


}
