package com.huotu.ymr.controller.web;

import com.huotu.ymr.entity.Manager;
import com.huotu.ymr.service.LoginService;
import com.huotu.ymr.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by lgh on 2015/12/3.
 */

@Controller
@RequestMapping("/manager")
public class HomeController {

    @Autowired
    LoginService loginService;

    @Autowired
    ManagerService managerService;

    @RequestMapping("/home")
    public String home(Locale locale, Model model) throws Exception {
        model.addAttribute("a", "ni hao");
        System.out.println("enter");

        Map<String, Object> map = new HashMap<>();
        map.put("a", "hao");
        return "manager/home";
    }


    @RequestMapping("/login")
    public String login() {
        return "manager/login";
    }

    @RequestMapping("/test")
    public String getTest(Model model) throws Exception{
        model.addAttribute("tt","123");
        return "test";
    }

    /**
     * 用户登录
     * @return
     */
    @RequestMapping(value = "/managerlogin",method = RequestMethod.POST)
    public String managerLogin(String username,String password) {
        Manager manager=managerService.checkManager(username,password);
        if(manager==null){

        }else{
            loginService.newLogin(manager, password);
        }
        loginService.newLogin(manager, password);
        return "manager/home";
    }


}
