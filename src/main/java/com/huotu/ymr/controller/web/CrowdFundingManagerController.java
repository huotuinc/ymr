package com.huotu.ymr.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by lgh on 2015/12/3.
 */
@Controller
@RequestMapping("/manager")
public class CrowdFundingManagerController {

    @RequestMapping("/getCrowdFundingList")
    public String getCrowdFundingList(HttpServletRequest request,HttpServletResponse response) {
        return "manager/crowdfunding/crowdFundingList";
    }
    @RequestMapping("/getCrowdFundingBookingList")
    public String getCrowdFundingBookingList(HttpServletRequest request,HttpServletResponse response) {
        return "manager/crowdfunding/crowdFundingBookingList";
    }

    @RequestMapping("/getCrowdFundingCooperationList")
    public String getCrowdFundingCooperationList(HttpServletRequest request,HttpServletResponse response) {
        return "manager/crowdfunding/crowdFundingCooperationList";
    }
    @RequestMapping("/getCrowdFundingSubscriptionList")
    public String getCrowdFundingSubscriptionList(HttpServletRequest request,HttpServletResponse response) {
        return "manager/crowdfunding/crowdFundingSubscriptionList";
    }

    @RequestMapping("/getDraftsList")
    public String getDraftsList(HttpServletRequest request,HttpServletResponse response) {
        return "manager/crowdfunding/draftsList";
    }

    @RequestMapping("/agencyFee")
    public String agencyFee(HttpServletRequest request,HttpServletResponse response) {
        return "manager/crowdfunding/agencyFee";
    }
    @RequestMapping("/addCrowd")
    public String addCrowd(HttpServletRequest request,HttpServletResponse response) {
        return "manager/crowdfunding/addCrowd";
    }

}
