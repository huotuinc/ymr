package com.huotu.ymr.controller.web;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.EnumHelper;
import com.huotu.ymr.entity.Report;
import com.huotu.ymr.entity.ShareComment;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.model.ResultModel;
import com.huotu.ymr.model.backend.crowdFunding.Msg;
import com.huotu.ymr.model.backend.share.BackendUserModel;
import com.huotu.ymr.model.backend.user.ReportDetailModel;
import com.huotu.ymr.model.searchCondition.ReportSearchModel;
import com.huotu.ymr.model.searchCondition.UserSearchModel;
import com.huotu.ymr.repository.ReportRepository;
import com.huotu.ymr.repository.ShareCommentRepository;
import com.huotu.ymr.repository.UserRepository;
import com.huotu.ymr.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by lgh on 2015/12/1.
 */
@Controller
@RequestMapping("/manager")
public class UserManagerController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DataCenterService dataCenterService;

    @Autowired
    ShareService shareService;



    @Autowired
    ReportService reportService;

    @Autowired
    ShareCommentRepository shareCommentRepository;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    ShareCommentService shareCommentService;

    @RequestMapping(value = "/getUserList",method = RequestMethod.GET)
    public String getUserList(UserSearchModel userSearchModel,Model model) throws Exception {
//        //todo 用户权限方面的操作
        if (userSearchModel.getPageNoStr() < 0) {
            userSearchModel.setPageNoStr(0);
        }
        Page<User> users=userService.findPcUserList(userSearchModel);
        List<BackendUserModel> backendUserModels=new ArrayList<>();
        for(User u:users){
            BackendUserModel backendUserModel=new BackendUserModel();
            //从数据中心获取用户数据 todo
            backendUserModel.setId(u.getId());
            backendUserModel.setScore(u.getScore());
            backendUserModel.setMobile("18012345678");
            backendUserModel.setLevel("一级");
            backendUserModel.setName("slt");
            backendUserModel.setUserStatus(u.getUserStatus()==null?0:u.getUserStatus().getValue());
            backendUserModels.add(backendUserModel);
        }
        model.addAttribute("allUserList", backendUserModels);//用户列表model
        model.addAttribute("pageNo",userSearchModel.getPageNoStr());//当前页数
        model.addAttribute("totalPages",users.getTotalPages());//总页数
        model.addAttribute("totalRecords", users.getTotalElements());//总记录数
        return "manager/user/userList";
    }

    @RequestMapping(value = "/lookUserInfo",method = RequestMethod.GET)
    public String lookUserInfo(@RequestParam(required = true)Long userId,Model model)throws Exception{
        User user=userService.getUser(userId);
//        MallUserModel mallUserModel=dataCenterService.getUserInfoByUserId(userId);
        Long shares=shareService.getUserShareCount(user);
        //参加众筹的次数
        //充值记录
        return "manager/user/DetailedUserInfo";

    }







    /**
     * 用户的操作，0：正常，1：禁言，2：冻结
     * @param userId    用户ID
     * @param type      操作类型
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/setUser",method = RequestMethod.POST)
    @ResponseBody
    public ResultModel setUser(@RequestParam(required = true)Long userId,
                               @RequestParam(required = true)Integer type) throws Exception{
        ResultModel resultModel=new ResultModel();
        User user=userRepository.findOne(userId);
        if(Objects.isNull(user)){
            resultModel.setCode(0);
            resultModel.setMessage("没找到该用户!");
        }
        CommonEnum.UserStatus userStatus= EnumHelper.getEnumType(CommonEnum.UserStatus.class, type);
        user.setUserStatus(userStatus);
        user=userRepository.save(user);
        resultModel.setCode(1);
        resultModel.setMessage(user.getUserStatus().getName());
        return resultModel;
    }



    /**
     * 获取举报列表
     *
     * @param reportSearchModel 前台传过来的参数
     * @param model
     * @throws Exception
     */
    @RequestMapping(value = "/getReportList", method = RequestMethod.GET)
    public String getReportList(ReportSearchModel reportSearchModel, Model model) throws Exception {
        Page<Report> reportPages = reportService.findReportPage(reportSearchModel);
        model.addAttribute("allReportList", reportPages);//文章列表
        model.addAttribute("pageNoStr", reportSearchModel.getPageNoStr());//当前页数
        return "manager/user/reportList";

    }
    /**
     * 处理被举报用户信息
     * @param reportsId 要处理的用户id
     *             删除评论：0
     *             永久禁言：1
     *             冻结用户：2
     * @param type  对用户的处理方式
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/solveReport",method = RequestMethod.POST)
    @ResponseBody
    public Msg solveReport(Long reportsId,Integer type) throws Exception {
        Msg msg = new Msg();
        if(type==0){
            //todo 用户禁言管理数据库更新
            //ShareComment shareComment=shareCommentRepository.findOne(reportsId);//todo 删除评论
            ShareComment shareComment=shareCommentRepository.findOne(reportsId);
            shareCommentService.deleteComment(shareComment.getCommentPath());
//            shareCommentRepository.delete(reportsId);
        }else if(type==1){
           User user=userRepository.findOne(reportsId); //todo 永久禁言
           user.setUserStatus(CommonEnum.UserStatus.notalk);
           user=userRepository.saveAndFlush(user);
        }else if(type==2){
            User user=userRepository.findOne(reportsId); //todo 冻结用户
            user.setUserStatus(CommonEnum.UserStatus.freeze);
            user=userRepository.saveAndFlush(user);
        }
        msg.setCode(200);
        msg.setMsg("success");
        return msg;
    }

    /**
     * 解除被举报用户的惩处
     * @param reportsId 要处理的用户id
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/releaseReport",method = RequestMethod.POST)
    @ResponseBody
    public Msg releaseReport(Long reportsId) throws Exception {
        Msg msg = new Msg();
        Report report=reportRepository.findOne(reportsId);
        User user=userRepository.findOne(report.getTo().getId());//todo 解除禁言
        user.setUserStatus(CommonEnum.UserStatus.normal);
        report.setHasSolved(1);
        report=reportRepository.saveAndFlush(report);
        user=userRepository.saveAndFlush(user);
        msg.setCode(200);
        msg.setMsg("success");
        return msg;
    }


    /**
     * 解除被举报用户的惩处
     * @param reportsId 要处理的用户id
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/showReportDetail",method = RequestMethod.GET)
    public String showReportDetail(ReportDetailModel reportDetailModel,Long reportsId) throws Exception {
        Report report=reportRepository.findOne(reportsId);
        User user=userRepository.findOne(report.getTo().getId());

        reportDetailModel.setTime(report.getTime());
        reportDetailModel.setContent(report.getShareComment().getContent());
        //reportDetailModel.setPhone();//todo 得到用户手机
        //reportDetailModel.setUserName();//todo 得到用户名字
        reportDetailModel.setLevel(user.getUserLevel());
        return "manager/user/reportDetail";
    }


    /**
     * 获取商家的商品分类
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getCatList",method = RequestMethod.GET)
    public ResultModel getCatList() throws Exception{
//        List<Cat>
        ResultModel resultModel=new ResultModel();
        return resultModel;

    }


    /**
     * 获取商家的商品
     * @param catId     商品所属分类
     * @param pageNo    分页
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getGoodsList",method = RequestMethod.GET)
    public ResultModel getGoodsList(Long catId,Long pageNo) throws Exception{
        ResultModel resultModel=new ResultModel();
        return resultModel;
    }




}
