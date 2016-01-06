package com.huotu.ymr.controller.web;

import com.huotu.ymr.entity.Manager;
import com.huotu.ymr.model.backend.crowdFunding.Msg;
import com.huotu.ymr.service.LoginService;
import com.huotu.ymr.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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


    @RequestMapping(value="/login", method = RequestMethod.GET)
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
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "/managerLogin",method = RequestMethod.POST)
    @ResponseBody
    public Msg managerLogin(String username, String password) {
        Manager manager = managerService.checkManager(username, password);
        Msg msg = new Msg();
        if(manager==null){
            msg.setCode(401);
            msg.setMsg("success");
        }else{
            msg.setCode(200);
            msg.setMsg("success");
            loginService.newLogin(manager, password);
        }
        return msg;
    }

    /**
     * 用户成功
     * @return
     */
    @RequestMapping(value = "/loginSuccess")
    public String loginSuccess() {
        return "redirect:home";
    }
    /**
     * 用户登录失败
     * @return
     */
    @RequestMapping(value = "/loginFailed")
    public String loginFailed() {
        return "manager/login";
    }


}
