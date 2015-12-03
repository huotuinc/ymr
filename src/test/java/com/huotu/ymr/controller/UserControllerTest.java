package com.huotu.ymr.controller;

import com.huotu.ymr.base.SpringBaseTest;
import com.huotu.ymr.boot.MvcConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

/**
 * Created by lgh on 2015/12/1.
 */

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MvcConfig.class })
@Transactional
public class UserControllerTest extends SpringBaseTest {
    @Autowired
    private WebApplicationContext context;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testLogin() throws Exception {

    }

    @Test
    public void testSendSMS() throws Exception {

    }

    @Test
    public void testBindMobile() throws Exception {

    }
}