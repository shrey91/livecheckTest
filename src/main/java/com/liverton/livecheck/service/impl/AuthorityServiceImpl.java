package com.liverton.livecheck.service.impl;

import com.liverton.livecheck.dao.model.Authority;
import com.liverton.livecheck.dao.repository.AuthorityRepository;
import com.liverton.livecheck.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sshah on 10/08/2016.
 */
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;
//
//    @PostConstruct
//    public void init(){
//        authorityRepository.save((new Authority("Report", "Report User")));
//    }

    @Override
    public List<Authority> findRoles() {
        return Arrays.asList(new Authority("User", "Regular User"), new Authority("Admin", "Administrator"));
    }
}
