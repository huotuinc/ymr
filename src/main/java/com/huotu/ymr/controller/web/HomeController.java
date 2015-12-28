package com.huotu.ymr.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by lgh on 2015/12/3.
 */

@Controller
@RequestMapping("/manager")
public class HomeController {
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
}
