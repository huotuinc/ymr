package com.huotu.ymr.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.ViewResolver;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by lgh on 2015/11/20.
 */
@Controller
public class UserController {

    @Autowired
    private ViewResolver viewResolver;

    @RequestMapping("/index")
    public String index(Locale locale, Model model) throws Exception {
        model.addAttribute("a", "ni hao");
        System.out.println("enter");

        Map<String, Object> map = new HashMap<>();
        map.put("a", "hao");
//        return new  ModelAndView("test", map);
        return "test";
//        return viewResolver.resolveViewName("test",null);
    }
}
