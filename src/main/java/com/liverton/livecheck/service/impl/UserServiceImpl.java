package com.liverton.livecheck.service.impl;

import com.liverton.livecheck.dao.model.Authority;
import com.liverton.livecheck.dao.model.User;
import com.liverton.livecheck.dao.repository.AuthorityRepository;
import com.liverton.livecheck.dao.repository.UserRepository;
import com.liverton.livecheck.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

/**
 * Created by sshah on 10/08/2016.
 */
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public List<User> findUsers(){
        return Arrays.asList(new User("User","1234", true), new User("Disabled user","", false));
    }

    @Override
    public User findByName(String name) {
        return null;
    }
}
