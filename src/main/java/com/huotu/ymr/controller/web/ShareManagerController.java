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
public class ShareManagerController {


    @RequestMapping("/getShareList")
    public String getShareList(Locale locale, Model model) throws Exception {

        System.out.println("enter12123");

        return "manager/share/shareList";

    }
}
