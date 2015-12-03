package com.huotu.ymr.controller;

import com.huotu.ymr.base.SpringBaseTest;
import com.huotu.ymr.boot.MvcConfig;
import com.huotu.ymr.service.CrowdFoundingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

/**
 * Created by xhk on 2015/12/3.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MvcConfig.class })
@Transactional
public class CrowdFundingControllerTest extends SpringBaseTest {

    @Autowired
    CrowdFoundingService crowdFoundingService;

    @Test
    public void testGetCrowdFundingList() throws Exception {

    }

    @Test
    public void testGetCrowFindingInfo() throws Exception {

    }

    @Test
    public void testRaiseBooking() throws Exception {

    }

    @Test
    public void testPay() throws Exception {

    }

    @Test
    public void testCallBackWeiXin() throws Exception {

    }

    @Test
    public void testCallBackAlipay() throws Exception {

    }

    @Test
    public void testGetBookingList() throws Exception {

    }

    @Test
    public void testRaiseCooperation() throws Exception {

    }

    @Test
    public void testGetRaiseCooperationList() throws Exception {

    }

    @Test
    public void testGoCooperation() throws Exception {

    }

    @Test
    public void testRaiseSubscription() throws Exception {

    }

    @Test
    public void testGetSubscriptionList() throws Exception {

    }
}