package com.huotu.ymr.controller.web;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.EnumHelper;
import com.huotu.ymr.entity.Share;
import com.huotu.ymr.model.searchCondition.ShareSearchModel;
import com.huotu.ymr.model.backend.share.BackendShareModel;
import com.huotu.ymr.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * Created by lgh on 2015/12/3.
 */
@Controller
@RequestMapping("/manager")
public class ShareManagerController {
    public static final int PAGE_SIZE=20;
    @Autowired
    ShareService shareService;

    @RequestMapping(value = "/showBottomGeneralize",method = RequestMethod.GET)
    public String showBottomGeneralize() throws Exception{
        return "manager/share/bottomGeneralize";

    }

    @RequestMapping(value = "/showIntegralConfig",method = RequestMethod.GET)
    public String showIntegralConfig() throws Exception{
        return "manager/share/integralConfig";
    }

    @RequestMapping(value = "/saveUserIntegralConfig",method = RequestMethod.POST)
    public String saveUserIntegralConfig(Integer score,Integer relayReward) throws Exception{
        return "manager/share/integralConfig";
    }

    @RequestMapping(value = "/saveMerchantIntegralConfig",method = RequestMethod.POST)
    public String saveMerchantIntegralConfig(Integer score,Integer relayReward) throws Exception{
        return "manager/share/integralConfig";
    }


    @RequestMapping(value = "/showDraftsList",method = RequestMethod.GET)
    public String showDraftsList() throws Exception{
        return "manager/share/draftsList";
    }

    /**
     * 获取爱分享文章列表(官方)
     * @param shareSearchModel  查询条件
     * @param model             返回的参数
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getYmrShareList",method = RequestMethod.GET)
    public String getYmrShareList(ShareSearchModel shareSearchModel,Model model) throws Exception {
        //todo 用户权限方面的操作
        Page<Share> shares=shareService.findPcShareList(shareSearchModel);
        model.addAttribute("allShareList", shares);//文章列表
        model.addAttribute("totalPages",shares.getTotalPages());//总页数
        model.addAttribute("totalRecords", shares.getTotalElements());//总记录数
        return "manager/share/ymrShareList";

    }
    /**
     * 获取爱分享文章列表（用户）
     * @param shareSearchModel  查询条件
     * @param model             返回的参数
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getUserShareList",method = RequestMethod.GET)
    public String getUserShareList(ShareSearchModel shareSearchModel,Model model) throws Exception {
        //todo 用户权限方面的操作
        if (shareSearchModel.getPageNoStr() < 0) {
            shareSearchModel.setPageNoStr(0);
        }

        Page<Share> shares=shareService.findPcShareList(shareSearchModel);
        model.addAttribute("allShareList", shares);//文章列表
        model.addAttribute("totalPages",shares.getTotalPages());//总页数
        model.addAttribute("pageNoStr",shareSearchModel.getPageNoStr());//当前页数
        model.addAttribute("totalRecords", shares.getTotalElements());//总记录数
        return "manager/share/userShareList";

    }

    @RequestMapping(value = "addShare")
    public String addShare(BackendShareModel backendShareModel,Model model) throws Exception{
        return "manager/share/addShare";
    }
    /**
     * 后台添加爱分享内容
     *
     * @return
     */
    @RequestMapping(value = "saveShare",method = RequestMethod.POST)
    public String saveShare(BackendShareModel backendShareModel) throws Exception{
        Share share=new Share();
        share.setCheckStatus(CommonEnum.CheckType.audit);
        share.setOwnerId(0L);//todo 当前登录的用户
        share.setScore(0);
        share.setTitle(backendShareModel.getTitle());
        share.setLinkUrl("");//todo
        share.setContent(backendShareModel.getContent());
        share.setImg("");
        share.setIntro("");//todo
        share.setOwnerType(CommonEnum.UserType.official);
        share.setReason("");
//        share.setView(0L);
//        share.setCommentQuantity(0L);
//        share.setPraiseQuantity(0L);
        share.setPostReward(0);
//        share.setEnabledRecommendProduct(false);//todo
        share.setTop(backendShareModel.getTop());
        share.setUsedScore(0);
        share.setTime(new Date());
//        share.setRelayQuantity(0L);
        share.setShareType(EnumHelper.getEnumType(CommonEnum.ShareType.class,backendShareModel.getShareType()));
        share.setUseLink(false);
        share.setRelayReward(0);
        shareService.addShare(share);
        return "redirect:getYmrShareList";
    }
}
