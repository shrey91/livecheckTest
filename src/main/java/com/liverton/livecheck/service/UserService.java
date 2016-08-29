package com.liverton.livecheck.service;

import com.liverton.livecheck.dao.model.User;

import java.util.List;

/**
 * Created by sshah on 10/08/2016.
 */
public interface UserService {

    List<User> findUsers();

    User findByName(String name);
}
