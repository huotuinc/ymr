package com.huotu.ymr.controller.web;

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

    @RequestMapping(value = "/getUserList",method = RequestMethod.GET)
    public String getUserList(String keywords,Integer pageNo,String keyType,Model model) throws Exception {
        return "manager/user/userList";

    }


}
