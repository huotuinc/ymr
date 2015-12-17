package com.huotu.ymr.controller.web;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.Config;
import com.huotu.ymr.entity.Share;
import com.huotu.ymr.model.ResultModel;
import com.huotu.ymr.model.backend.share.BackendShareModel;
import com.huotu.ymr.model.searchCondition.ShareSearchModel;
import com.huotu.ymr.repository.ConfigRepository;
import com.huotu.ymr.service.ShareService;
import com.huotu.ymr.service.StaticResourceService;
import com.sun.jndi.toolkit.url.Uri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by lgh on 2015/12/3.
 */
@Controller
@RequestMapping("/manager")
public class ShareManagerController {
    public static final int PAGE_SIZE=20;
    @Autowired
    ShareService shareService;

    @Autowired
    StaticResourceService staticResourceService;

    @Autowired
    ConfigRepository configRepository;


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
        List<BackendShareModel> backendShareModels=new ArrayList<>();
        for(Share s:shares){
            BackendShareModel backendShareModel=new BackendShareModel();
            backendShareModel.setShareTitle(s.getTitle());
            backendShareModel.setId(s.getId());
            backendShareModel.setTop(s.getTop());
            backendShareModel.setTime(s.getTime());
            backendShareModel.setUserType(s.getOwnerType().getName());
            backendShareModel.setPraiseQuantity(s.getPraiseQuantity());
            backendShareModel.setRelayQuantity(s.getRelayQuantity());
            backendShareModel.setView(s.getView());
            backendShareModel.setCheckType(s.getCheckStatus().getName());
            backendShareModels.add(backendShareModel);
        }
        model.addAttribute("allShareList", backendShareModels);//文章列表model
        model.addAttribute("pageNo",shareSearchModel.getPageNoStr());//当前页数
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

    /**
     * 进入新增帖子页面
     * @param share
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addShare")
    public String addShare(Share share,Model model) throws Exception{
        Config configGT =configRepository.findOne("GlobalTransmit");
        Config configTT =configRepository.findOne("GlobalTotal");
        if(configGT==null){
            configGT=new Config();
            configGT.setKey("GlobalTransmit");
            configGT.setValue("0");
            configRepository.save(configGT);
        }
        if(configTT==null){
            configTT=new Config();
            configTT.setKey("GlobalTotal");
            configTT.setValue("0");
            configRepository.save(configTT);
        }
        model.addAttribute("GlobalTransmit",configGT.getValue());
        model.addAttribute("GlobalTotal",configTT.getValue());
        model.addAttribute("shareTypes",CommonEnum.ShareType.values());
        return "manager/share/addShare";
    }

    /**
     * 进入修改帖子页面
     * @param shareId   帖子ID
     * @param model     返回的model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/modifyShare",method = RequestMethod.GET)
    public String modifyShare(Long shareId,Model model) throws Exception{
        if(shareId==null){
            throw new Exception("参数错误！,参数为空！");
        }
        Share share=shareService.findOneShare(shareId);
        if(share==null){
            throw new Exception("找不到帖子了~或许已被删除~");
        }
        share.setImg(staticResourceService.getResource(share.getImg()).toString());
        Config configGT =configRepository.findOne("GlobalTransmit");
        Config configTT =configRepository.findOne("GlobalTotal");
        if(configGT==null){
            configGT=new Config();
            configGT.setKey("GlobalTransmit");
            configGT.setValue("0");
            configRepository.save(configGT);
        }
        if(configTT==null){
            configTT=new Config();
            configTT.setKey("GlobalTotal");
            configTT.setValue("0");
            configRepository.save(configTT);
        }
        model.addAttribute("GlobalTransmit",configGT.getValue());
        model.addAttribute("GlobalTotal",configTT.getValue());
        model.addAttribute("shareTypes",CommonEnum.ShareType.values());
        model.addAttribute("share",share);
        return "manager/share/addShare";
    }
    /**
     * 后台添加和修改爱分享内容
     *
     * @return
     */
    @RequestMapping(value = "/saveShare",method = RequestMethod.POST)
    public String saveShare(Share share,HttpServletRequest request) throws Exception{
       String contextPath= request.getContextPath();
        //新增
        if(share.getId()==null){
            Uri uri=new Uri(share.getImg());
            String imgPath=uri.getPath().substring(uri.getPath().indexOf(contextPath)+contextPath.length());
            share.setImg(imgPath);
            share.setOwnerId(123L);//todo 官方ID
            share.setName("官方");
            share.setOwnerType(CommonEnum.UserType.official);
            share.setIntro("");//todo 简介从内容中获取
            share.setTime(new Date());
            share.setPostReward(0);
            share.setReason("");
            share.setTop(false);
            share.setUsedScore(0);
            share.setCheckStatus(CommonEnum.CheckType.audit);
            shareService.saveShare(share);
        //修改
        }else{
            Share modifyShare=shareService.findOneShare(share.getId());
            Uri uri=new Uri(share.getImg());
            String imgPath=uri.getPath().substring(uri.getPath().indexOf(contextPath)+contextPath.length());
            modifyShare.setImg(imgPath);
            modifyShare.setTitle(share.getTitle());
            modifyShare.setContent(share.getContent());
            modifyShare.setUseLink(share.getUseLink());
            modifyShare.setLinkUrl(share.getLinkUrl());
            modifyShare.setEnabledRecommendProduct(share.getEnabledRecommendProduct());
            shareService.saveShare(modifyShare);
        }
        return "redirect:getYmrShareList";
    }


    /**
     * 帖子的操作，0：置顶，1：审核通过，2：取消置顶，3：审核不通过， 4：删除,5:移至草稿箱
     * @param shareId   帖子ID
     * @param type      操作类型
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/setShare",method = RequestMethod.POST)
    @ResponseBody
    public ResultModel setShare(Long shareId,Integer type) throws Exception{
        ResultModel resultModel=new ResultModel();
        if(shareId==null){
            resultModel.setCode(0);
            resultModel.setMessage("参数异常");
            return resultModel;
        }
        Share share=shareService.findOneShare(shareId);
        if(Objects.isNull(share)){
            resultModel.setCode(0);
            resultModel.setMessage("没找到该帖子，或许已被删除!");
        }
        switch (type){
            case 0:
                share.setTop(true);
                break;
            case 1:
                share.setCheckStatus(CommonEnum.CheckType.pass);
                break;
            case 2:
                share.setTop(false);
                break;
            case 3:
                share.setCheckStatus(CommonEnum.CheckType.notPass);
                break;
            case 4:
                share.setCheckStatus(CommonEnum.CheckType.delete);
                break;
            case 5:
                break;
            default:
                break;
        }
        share=shareService.saveShare(share);
        resultModel.setCode(1);
        resultModel.setMessage(share.getCheckStatus().getName());
        return resultModel;
    }

//    /**
//     * 置顶与取消
//     * @param shareId   帖子ID
//     * @param type
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "/setTopOrNot")
//    @ResponseBody
//    public ResultModel setTopOrNot(Long shareId,Long type) throws Exception{
//        ResultModel resultModel=new ResultModel();
//        if(shareId==null){
//            resultModel.setCode(0);
//            resultModel.setMessage("参数异常");
//            return resultModel;
//        }
//        Share share=shareService.findOneShare(shareId);
//        if(Objects.isNull(share)){
//            resultModel.setCode(0);
//            resultModel.setMessage("没找到该帖子，或许已被删除!");
//        }
//
//
//
//
//    }






}
