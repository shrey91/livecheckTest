package com.liverton.livecheck.dao.repository;

import com.liverton.livecheck.dao.model.Authority;
import com.liverton.livecheck.test.support.AbstractBaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.liverton.livecheck.dao.model.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by sshah on 1/09/2016.
 */
public class UserTest extends AbstractBaseTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

    private static final String DEFAULT_USER="user";

    private static final String DEFAULT_PASSWORD="user";

    private static final Boolean DEFAULT_ENABLE=true;



    @Before
    public void init(){
        userRepository.deleteAll();
    }

    @After
    public void destroy(){
        userRepository.deleteAll();
    }

    @Test
    public void shouldCreateUser(){
        User user = userRepository.save(createUnpersistedUser());
        assertNotNull(user);
        assertEquals(DEFAULT_USER,user.getName());

    }
    @Test
    public void shouldFindByID(){
       User user= userRepository.save(createUnpersistedUser());
        assertNotNull(userRepository.findOne(user.getId()));
    }
    @Test
    public void shouldHaveCreatedSingleUser(){
        userRepository.save(createUnpersistedUser());
        assertEquals(1, userRepository.count());
    }

    @Test
    public void shouldFindByName(){
        User user = userRepository.save(createUnpersistedUser());
        assertNotNull(userRepository.findByName(user.getName()));
    }


    @Test
    public void shouldDeleteUser(){

        User user =userRepository.save(createUnpersistedUser());
        assertNotNull(user);

        userRepository.delete(user);
//        assertEquals(0,userRepository.findByName(DEFAULT_USER));
        assertNull(userRepository.findByName(DEFAULT_USER));
    }

    @Test
    public void shouldEditUser(){
        User user = userRepository.save(createUnpersistedUser());
        user.setName("test123");
        user.setPassword("test123");
        user.setUserEnabled(true);
        userRepository.save(user);

        assertNotNull(userRepository.findByName("test123"));
        assertEquals("test123", user.getName());  //testing the new user name
        assertEquals("test123", user.getPassword());  //testing the new password
        assertEquals(true, user.getUserEnabled());  //testing the new state

    }

    private User createUnpersistedUser(){
        return new User(DEFAULT_USER,DEFAULT_PASSWORD,DEFAULT_ENABLE);
    }

    @Test
    public void ShouldRun() {
        List<User> userList = userRepository.findAll();
    }
}
