package com.liverton.livecheck.service;

import com.liverton.livecheck.dao.model.Authority;

import java.util.List;

/**
 * Created by sshah on 10/08/2016.
 */
public interface AuthorityService {

    List<Authority> findRoles();
}
