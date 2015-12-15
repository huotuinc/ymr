package com.huotu.ymr.controller.web;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.CrowdFunding;
import com.huotu.ymr.model.searchCondition.CrowdFundingSearchModel;
import com.huotu.ymr.repository.CrowdFundingRepository;
import com.huotu.ymr.service.CrowdFundingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by lgh on 2015/12/3.
 */
@Controller
@RequestMapping("/manager")
public class CrowdFundingManagerController {

    @Autowired
    CrowdFundingService crowdFundingService;

    @Autowired
    CrowdFundingRepository crowdFundingRepository;

    /**
     * 获取众筹列表
     * @param crowdFundingSearchModel 前台传过来的参数
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/getCrowdFundingList",method = RequestMethod.GET)
    public String getCrowdFundingList(CrowdFundingSearchModel crowdFundingSearchModel,Model model) throws Exception {

        Page<CrowdFunding> crowdFundingPages=crowdFundingService.findCrowdFundingPage(crowdFundingSearchModel);
        model.addAttribute("allcrowdFundingList", crowdFundingPages);//文章列表

        model.addAttribute("pageNoStr",crowdFundingSearchModel.getPageNoStr());//当前页数
        return "manager/crowdfunding/crowdFundingList";
    }

    /**
     * 获取众筹详细的项目人信息
     * @param crowdFundingsId 前台传过来的众筹id
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/jumpToCFDetail",method = RequestMethod.GET)
    public String jumpToCFDetail(Long crowdFundingsId,Model model) throws Exception {
        CrowdFunding crowdFunding=crowdFundingRepository.findOne(crowdFundingsId);
        if(crowdFunding.getCrowdFundingType()== CommonEnum.CrowdFundingType.booking){
            model.addAttribute("crowdFunding", crowdFunding);//todo 获取预约信息
            return "manager/crowdfunding/crowdFundingDetail";
        }else if(crowdFunding.getCrowdFundingType()== CommonEnum.CrowdFundingType.cooperation){
            model.addAttribute("crowdFunding", crowdFunding);//todo 获取合作信息
            return "manager/crowdfunding/crowdFundingDetail";
        }else if(crowdFunding.getCrowdFundingType()== CommonEnum.CrowdFundingType.subscription) {
            model.addAttribute("crowdFunding", crowdFunding);//todo 获取认购信息
            return "manager/crowdfunding/crowdFundingDetail";
        }
        return null;
    }


    /**
     * 审核众筹信息
     * @param crowdFundingsId 前台传过来的众筹id
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/auditCrowdFunding",method = RequestMethod.GET)
    public String auditCrowdFunding(Long crowdFundingsId,Model model) throws Exception {
        CrowdFunding crowdFunding=crowdFundingRepository.findOne(crowdFundingsId);
        model.addAttribute("crowdFunding", crowdFunding);
        return "manager/crowdfunding/crowdFundingDetail";
    }

    /**
     * 关闭众筹
     * @param crowdFundingsId 前台传过来的众筹id
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/closeCrowdFunding",method = RequestMethod.GET)
    public void closeCrowdFunding(Long crowdFundingsId) throws Exception {
        CrowdFunding crowdFunding=crowdFundingRepository.findOne(crowdFundingsId);
        crowdFunding.setCheckStatus(CommonEnum.CheckType.close);
        crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);
    }

    /**
     * 开启众筹
     * @param crowdFundingsId 前台传过来的众筹id
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/openCrowdFunding",method = RequestMethod.GET)
    public void openCrowdFunding(Long crowdFundingsId) throws Exception {
        CrowdFunding crowdFunding=crowdFundingRepository.findOne(crowdFundingsId);
        crowdFunding.setCheckStatus(CommonEnum.CheckType.open);
        crowdFunding=crowdFundingRepository.saveAndFlush(crowdFunding);
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
