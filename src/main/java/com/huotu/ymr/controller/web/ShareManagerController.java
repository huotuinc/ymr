package com.huotu.ymr.controller.web;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.Share;
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



    @RequestMapping(value = "/getShareList",method = RequestMethod.GET)
    public String getShareList(String keywords,Integer pageNo,String keyType,Model model) throws Exception {
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }
        Page<Share> shares=shareService.findPcShareList(keywords,keyType,pageNo,PAGE_SIZE);
        long totalRecords = shares.getTotalElements();
        int numEl =  shares.getNumberOfElements();
        if(numEl==0) {
            pageNo=shares.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            shares=shareService.findPcShareList(keywords, keyType, pageNo, PAGE_SIZE);
            totalRecords = shares.getTotalElements();
        }
        model.addAttribute("allLinkList", shares);
        model.addAttribute("totalPages",shares.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("totalRecords", totalRecords);
        return "manager/share/shareList";

    }
    /**
     * 后台添加爱分享内容
     *
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(value = "addShare",method = RequestMethod.POST)
    public String addShare(String title, String content,
                           String imgUrl, Long userId,
                           CommonEnum.ShareType shareType,
                           Boolean top,Integer postReward,
                           Integer relayReward,Integer score) throws Exception{
        Share share=new Share();
        share.setCheckStatus(2);
        share.setOwnerId(0L);//todo 当前登录的用户
        share.setScore(score);
        share.setTitle(title);
        share.setLinkUrl("");//todo
        share.setContent(content);
        share.setImg(imgUrl);
        share.setIntro("");//todo
        share.setOwnerType(1);
        share.setReason("");
//        share.setView(0L);
//        share.setCommentQuantity(0L);
//        share.setPraiseQuantity(0L);
        share.setPostReward(postReward);
        share.setStatus(true);
        share.setTop(top);
        share.setUsedScore(0);
        share.setTime(new Date());
//        share.setRelayQuantity(0L);
        share.setShareType(shareType);
        share.setUseLink(false);
        share.setRelayReward(relayReward);
        shareService.addShare(share);
        return "manager";
    }
}
