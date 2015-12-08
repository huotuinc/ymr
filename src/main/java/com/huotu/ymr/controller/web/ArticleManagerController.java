package com.huotu.ymr.controller.web;

import com.huotu.ymr.controller.ForView;
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
    @RequestMapping("getShareList")
    public String getShareList(HttpServletRequest request,HttpServletResponse response){

        ForView searchCondition=new ForView();
        request.getSession().setAttribute("customerId",1L);
        request.getSession().setAttribute("supplierId",2L);
        request.getSession().setAttribute("searchCondition",searchCondition);
        request.getSession().setAttribute("pageNoStr",searchCondition.pageNoStr);
        request.getSession().setAttribute("txtOrderId",searchCondition.txtOrderId);
        request.getSession().setAttribute("ddlPayStatus",searchCondition.ddlPayStatus);
        request.getSession().setAttribute("ddlShipStatus",searchCondition.ddlShipStatus);
        request.getSession().setAttribute("ddlOrderByField",searchCondition.ddlOrderByField);
        request.getSession().setAttribute("raSortType",searchCondition.raSortType);
        request.getSession().setAttribute("txtBeginTime",searchCondition.txtBeginTime);
        request.getSession().setAttribute("txtEndTime",searchCondition.txtEndTime);
        request.getSession().setAttribute("txtBeginPaytime",searchCondition.txtBeginPaytime);
        request.getSession().setAttribute("txtEndPaytime",searchCondition.txtEndPaytime);
        return "manager/share/shareList";
    }

    @RequestMapping("/view")
    public String viewController(HttpServletRequest request,HttpServletResponse response){

        return "manager/home";
    }

    @RequestMapping("getArticleList")
    public String getArticleList(HttpServletRequest request,HttpServletResponse response) {
        ForView searchCondition=new ForView();
        request.getSession().setAttribute("customerId",1L);
        request.getSession().setAttribute("supplierId",2L);
        request.getSession().setAttribute("searchCondition",searchCondition);
        request.getSession().setAttribute("pageNoStr",searchCondition.pageNoStr);
        request.getSession().setAttribute("txtOrderId",searchCondition.txtOrderId);
        request.getSession().setAttribute("ddlPayStatus",searchCondition.ddlPayStatus);
        request.getSession().setAttribute("ddlShipStatus",searchCondition.ddlShipStatus);
        request.getSession().setAttribute("ddlOrderByField",searchCondition.ddlOrderByField);
        request.getSession().setAttribute("raSortType",searchCondition.raSortType);
        request.getSession().setAttribute("txtBeginTime",searchCondition.txtBeginTime);
        request.getSession().setAttribute("txtEndTime",searchCondition.txtEndTime);
        request.getSession().setAttribute("txtBeginPaytime",searchCondition.txtBeginPaytime);
        request.getSession().setAttribute("txtEndPaytime",searchCondition.txtEndPaytime);
        return "manager/article/articleList";
    }

    }
