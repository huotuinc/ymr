package com.huotu.ymr.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by lgh on 2015/12/1.
 */
@Controller
@RequestMapping("/manager")
public class UserManagerController {

    @RequestMapping("/index")
    public String index(Locale locale, Model model) throws Exception {
        model.addAttribute("a", "ni hao");
        System.out.println("enter");

        Map<String, Object> map = new HashMap<>();
        map.put("a", "hao");
//        return new  ModelAndView("test", map);
        return "home";
//        return viewResolver.resolveViewName("test",null);
    }
}
