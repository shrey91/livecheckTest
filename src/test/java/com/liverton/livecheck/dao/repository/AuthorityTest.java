package com.liverton.livecheck.dao.repository;

import com.liverton.livecheck.dao.model.Authority;
import com.liverton.livecheck.test.support.AbstractBaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by sshah on 31/08/2016.
 */
public class AuthorityTest extends AbstractBaseTest {

    @Autowired
    private AuthorityRepository authorityRepository;

    private static final String DEFAULT_ROLE = "admin";

    private static final String DEFAULT_DESCRIPTION = "Administrator for the site";


    @Before
    public void init() {
        authorityRepository.deleteAll();
    }

    @After
    public void destroy() {
        authorityRepository.deleteAll();
    }

    @Test
    public void shouldCreateAuthority() {
        Authority authority = authorityRepository.save(createUnpersistedAuthority());
        assertNotNull(authority);
        assertEquals(DEFAULT_ROLE, authority.getRole());

    }

    @Test
    public void shouldFindByID() {
        Authority authority = authorityRepository.save(createUnpersistedAuthority());
        assertNotNull(authorityRepository.findOne(authority.getId()));
    }

    @Test
    public void shouldHaveCreatedSingleAuthority() {
        authorityRepository.save(createUnpersistedAuthority());
        assertEquals(1, authorityRepository.count());
    }

    @Test
    public void shouldFindByName() {
        Authority authority = authorityRepository.save(createUnpersistedAuthority());
        assertNotNull(authorityRepository.findByRole(authority.getRole()));
    }


    @Test
    public void shouldDeleteOrganisation() {

        Authority authority = authorityRepository.save(createUnpersistedAuthority());
        assertNotNull(authority);

        authorityRepository.delete(authority);
        assertNull(authorityRepository.findByRole(DEFAULT_ROLE));
    }

    @Test
    public void shouldEditOrganisation() {
        Authority authority = authorityRepository.save(createUnpersistedAuthority());
        authority.setRole("user");
        authority.setDescription("Just a normal user");
        authorityRepository.save(authority);

        assertNotNull(authorityRepository.findByRole("user"));
        assertEquals("user", authority.getRole());  //testing the new authority role
        assertEquals("Just a normal user", authority.getDescription());  //testing the new authority description
    }

    private Authority createUnpersistedAuthority() {
//        Organisation organisation = new Organisation("Tester Site", "This is only a test");
//        organisationRepository.save(organisation);
        return new Authority(DEFAULT_ROLE, DEFAULT_DESCRIPTION);
    }

    @Test
    public void ShouldRun() {
        List<Authority> authorityList = authorityRepository.findAll();
    }
}
