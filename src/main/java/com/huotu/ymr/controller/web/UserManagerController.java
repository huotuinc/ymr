package com.huotu.ymr.controller.web;

import com.huotu.ymr.model.searchCondition.UserSearchModel;
import com.huotu.ymr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by lgh on 2015/12/1.
 */
@Controller
@RequestMapping("/manager")
public class UserManagerController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/getUserList",method = RequestMethod.GET)
    public String getUserList(UserSearchModel userSearchModel,Model model) throws Exception {
//        //todo 用户权限方面的操作
//        if (userSearchModel.getPageNoStr() < 0) {
//            userSearchModel.setPageNoStr(0);
//        }
//        Page<User> shares=userService.
////        List<BackendShareModel> backendShareModels=new ArrayList<>();
//        for(Share s:shares){
//            BackendShareModel backendShareModel=new BackendShareModel();
//            backendShareModel.setShareTitle(s.getTitle());
//            backendShareModel.setId(s.getId());
//            backendShareModel.setTop(s.getTop());
//            backendShareModel.setTime(s.getTime());
//            backendShareModel.setUserType(s.getOwnerType().getName());
//            backendShareModel.setPraiseQuantity(s.getPraiseQuantity());
//            backendShareModel.setRelayQuantity(s.getRelayQuantity());
//            backendShareModel.setView(s.getView());
//            backendShareModel.setCheckType(s.getCheckStatus().getName());
//            backendShareModels.add(backendShareModel);
//        }
//        model.addAttribute("allShareList", backendShareModels);//文章列表model
//        model.addAttribute("pageNo",userSearchModel.getPageNoStr());//当前页数
//        model.addAttribute("totalPages",shares.getTotalPages());//总页数
//        model.addAttribute("totalRecords", shares.getTotalElements());//总记录数
        return "manager/user/userList";
    }


}
