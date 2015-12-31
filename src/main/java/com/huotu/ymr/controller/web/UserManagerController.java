package com.huotu.ymr.controller.web;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.EnumHelper;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.model.ResultModel;
import com.huotu.ymr.model.backend.share.BackendUserModel;
import com.huotu.ymr.model.mall.MallUserModel;
import com.huotu.ymr.model.searchCondition.UserSearchModel;
import com.huotu.ymr.repository.UserRepository;
import com.huotu.ymr.service.DataCenterService;
import com.huotu.ymr.service.ShareService;
import com.huotu.ymr.service.UserService;
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
        MallUserModel mallUserModel=dataCenterService.getUserInfoByUserId(userId);
        Long shares=shareService.getUserShareCount(user);
        return null;

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
        CommonEnum.UserStatus userStatus=EnumHelper.getEnumType(CommonEnum.UserStatus.class, type);
        user.setUserStatus(userStatus);
        user=userRepository.save(user);
        resultModel.setCode(1);
        resultModel.setMessage(user.getUserStatus().getName());
        return resultModel;
    }






}
