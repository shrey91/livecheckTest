package com.liverton.livecheck.service.domain;

/**
 * Created by sshah on 8/08/2016.
 */
public class Site {

    private final String name;

    private final boolean enabled;

    public Site(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
