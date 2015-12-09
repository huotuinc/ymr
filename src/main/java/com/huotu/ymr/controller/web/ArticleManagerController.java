package com.huotu.ymr.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by lgh on 2015/12/3.
 */
@Controller
@RequestMapping("/manager")
public class ArticleManagerController {


    @RequestMapping("/view")
    public String viewController(HttpServletRequest request,HttpServletResponse response){

        return "manager/home";
    }

    @RequestMapping("getArticleList")
    public String getArticleList(HttpServletRequest request,HttpServletResponse response) {

        request.getSession().setAttribute("customerId",1L);
        request.getSession().setAttribute("supplierId",2L);
        request.getSession().setAttribute("searchCondition","1");
        request.getSession().setAttribute("pageNoStr","1");
        request.getSession().setAttribute("txtOrderId","1");
        request.getSession().setAttribute("ddlPayStatus","1");
        request.getSession().setAttribute("ddlShipStatus","1");
        request.getSession().setAttribute("ddlOrderByField","1");
        request.getSession().setAttribute("raSortType","1");
        request.getSession().setAttribute("txtBeginTime","1");
        request.getSession().setAttribute("txtEndTime","1");
        request.getSession().setAttribute("txtBeginPaytime","1");
        request.getSession().setAttribute("txtEndPaytime","1");
        return "manager/article/articleList";
    }

    }
