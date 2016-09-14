package com.liverton.livecheck.test.support;

import com.liverton.livecheck.test.config.TestApplicationConfiguration;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jlister on 16/12/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfiguration.class)
public class AbstractBaseTest {
}
