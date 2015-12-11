package com.huotu.ymr.controller.web;

import com.huotu.ymr.entity.Article;
import com.huotu.ymr.model.SearchCondition.ArticleSearchModel;
import com.huotu.ymr.repository.ArticleRepository;
import com.huotu.ymr.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by lgh on 2015/12/3.
 */
@Controller
@RequestMapping("/manager")
public class ArticleManagerController {


    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleService articleService;

    /**
     * 获取爱分享文章列表
     * @param articleSearchModel 前台传过来的参数
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getArticleList",method = RequestMethod.GET)
    public String getArticleList(ArticleSearchModel articleSearchModel,Model model) throws Exception {
        Page<Article> articlePages=articleService.findArticlePage(articleSearchModel);
        model.addAttribute("allArticleList", articlePages);//文章列表
        model.addAttribute("totalPages",articlePages.getTotalPages());//总页数
        model.addAttribute("pageNoStr",articleSearchModel.getPageNoStr());//当前页数
        model.addAttribute("totalRecords", articlePages.getTotalElements());//总记录数
        return "manager/article/articleList";

    }

    @RequestMapping("/view")
    public String viewController(HttpServletRequest request,HttpServletResponse response){

        return "manager/home";
    }

//    @RequestMapping("/getArticleList")
//    public String getArticleList(HttpServletRequest request,HttpServletResponse response) {
//
//        request.getSession().setAttribute("customerId",1L);
//        request.getSession().setAttribute("supplierId",2L);
//        request.getSession().setAttribute("searchCondition","1");
//        request.getSession().setAttribute("pageNoStr","1");
//        request.getSession().setAttribute("txtOrderId","1");
//        request.getSession().setAttribute("ddlPayStatus","1");
//        request.getSession().setAttribute("ddlShipStatus","1");
//        request.getSession().setAttribute("ddlOrderByField","1");
//        request.getSession().setAttribute("raSortType","1");
//        request.getSession().setAttribute("txtBeginTime","1");
//        request.getSession().setAttribute("txtEndTime","1");
//        request.getSession().setAttribute("txtBeginPaytime","1");
//        request.getSession().setAttribute("txtEndPaytime","1");
//        return "manager/article/articleList";
//    }

    }
