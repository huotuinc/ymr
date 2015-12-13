package com.huotu.ymr.controller.web;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.EnumHelper;
import com.huotu.ymr.entity.Share;
import com.huotu.ymr.model.ResultModel;
import com.huotu.ymr.model.backend.share.BackendShareModel;
import com.huotu.ymr.model.searchCondition.ShareSearchModel;
import com.huotu.ymr.service.ShareService;
import com.huotu.ymr.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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

    @RequestMapping(value = "addShare")
    public String addShare(BackendShareModel backendShareModel,Model model) throws Exception{
        return "manager/share/addShare";
    }
    /**
     * 后台添加爱分享内容
     *
     * @return
     */
    @RequestMapping(value = "/saveShare",method = RequestMethod.POST)
    public String saveShare(BackendShareModel backendShareModel) throws Exception{

        Share share=new Share();
        share.setName("官方");
        share.setCheckStatus(CommonEnum.CheckType.audit);
        share.setOwnerId(0L);//todo 当前登录的用户
        share.setScore(0);
        share.setTitle(backendShareModel.getShareTitle());
        share.setLinkUrl("");//todo
        share.setContent(backendShareModel.getShareContent());
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


    /**
     * 帖子的通过与不通过
     * @param shareId   帖子ID
     * @param type      0：不通过，1：通过
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/passOrNot",method = RequestMethod.POST)
    @ResponseBody
    public ResultModel pass(Long shareId,Long type) throws Exception{
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
        if(type==0){
            share.setCheckStatus(CommonEnum.CheckType.notPass);
        }else if(type==1){
            share.setCheckStatus(CommonEnum.CheckType.pass);
        }
        share=shareService.addShare(share);
        resultModel.setCode(1);
        resultModel.setMessage(share.getCheckStatus().getName());
        return resultModel;
    }





    /**
     * 图片上传
     * @param file
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/UploadImg",method = RequestMethod.POST)
    @ResponseBody
    public ResultModel UploadImg(@RequestParam("shareImg") MultipartFile file,HttpServletResponse response) throws Exception{
        ResultModel resultModel=new ResultModel();
        //文件格式判断
        if (ImageIO.read(file.getInputStream()) == null) {
            resultModel.setCode(0);
            resultModel.setMessage("请上传图片文件！");
            return resultModel;
        }
        if (file.getSize() == 0) {
            resultModel.setCode(0);
            resultModel.setMessage("请上传图片！");
            return resultModel;
        }
        //保存图片
        String fileName = StaticResourceService.SHSRES_IMG + UUID.randomUUID().toString() + ".png";
        staticResourceService.uploadResource(fileName, file.getInputStream());
        response.setHeader("X-frame-Options", "SAMEORIGIN");
        resultModel.setCode(1);
        resultModel.setMessage("图片上传成功！");
        return resultModel;

    }
}
