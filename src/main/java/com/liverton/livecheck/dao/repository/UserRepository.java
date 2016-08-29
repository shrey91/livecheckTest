package com.liverton.livecheck.dao.repository;

import com.liverton.livecheck.dao.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by sshah on 8/08/2016.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);
}
