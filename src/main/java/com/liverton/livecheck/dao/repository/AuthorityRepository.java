package com.liverton.livecheck.dao.repository;

import com.liverton.livecheck.dao.model.Authority;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by sshah on 8/08/2016.
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long>{

    Authority findByRole(String role);
}
