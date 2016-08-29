package com.liverton.livecheck.service;

/**
 * Created by sshah on 16/08/2016.
 */
public interface SecurityService {

    String findLoggedInUsername();

    void autologin(String username, String password);

}
