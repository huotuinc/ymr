package com.huotu.ymr.controller;

import com.huotu.ymr.base.SpringBaseTest;
import com.huotu.ymr.boot.MvcConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

/**
 * Created by lgh on 2015/12/1.
 */

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MvcConfig.class })
@Transactional
public class ShareControllerTest extends SpringBaseTest {

    @Before
    public void init() throws Exception {

    }

    @Test
    void testSearchShareList() {

    }

    @Test
    void testShare() {

    }

    @Test
    void testGetShareInfo() {

    }

    @Test
    void testSearchShareCommentList() {

    }

    @Test
    public void testSearchShareList1() throws Exception {

    }

    @Test
    public void testShare1() throws Exception {

    }

    @Test
    public void testGetShareInfo1() throws Exception {

    }

    @Test
    public void testSearchShareCommentList1() throws Exception {

    }
}
