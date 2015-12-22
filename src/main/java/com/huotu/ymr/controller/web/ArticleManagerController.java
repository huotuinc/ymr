package com.huotu.ymr.controller.web;

import com.huotu.ymr.entity.Article;
import com.huotu.ymr.model.backend.article.ArticleCUDModel;
import com.huotu.ymr.model.searchCondition.ArticleSearchModel;
import com.huotu.ymr.repository.ArticleRepository;
import com.huotu.ymr.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

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
     * 获取关于美投文章列表
     * @param articleSearchModel 前台传过来的参数
     * @param model
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

    /**
     * 添加文章
     * @param articleAddModel 前台传过来的参数
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addArticle",method = RequestMethod.GET)
    public String addArticle(ArticleCUDModel articleAddModel,Model model) throws Exception {

        Article article=new Article();
        article.setTime(new Date());
        //todo 进行图片的上传；
        //article.setPicture(articleAddModel.getPicture().path());

        article.setContent(articleAddModel.getContent());
        article.setLinkUrl(articleAddModel.getLinkUrl());
        //article.setManager();//todo 管理员id
        article.setTitle(articleAddModel.getArticleTitle());
        if(articleAddModel.getUrlStatus()==1) {
            article.setUseLink(true);
        }else {
            article.setUseLink(false);
        }
        //article.setCategory(); //todo 设置文章的类型

       article=articleRepository.saveAndFlush(article);
        return "manager/article/addArticle"; //todo 返回上传状态

    }

    /**
     * 跳转到编辑文章
     * @param articleId 前台传过来的文章参数
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jumpToUpdateArticle",method = RequestMethod.GET)
    public String jumpToUpdateArticle(Long articleId,Model model) throws Exception {
        Article article=articleRepository.findOne(articleId);
        model.addAttribute("article",article);
        return "manager/article/updateArticle"; //todo 返回上传状态

    }

    /**
     * 编辑文章
     * @param articleUpdateModel 前台传过来的文章参数
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateArticle",method = RequestMethod.GET)
    public String updateArticle(ArticleCUDModel articleUpdateModel,Model model) throws Exception {

        Article article=new Article();
        article.setTime(new Date());
        //todo 进行图片的上传；
        //article.setPicture(articleAddModel.getPicture().path());

        article.setContent(articleUpdateModel.getContent());
        article.setLinkUrl(articleUpdateModel.getLinkUrl());
        //article.setManager();//todo 管理员id
        article.setTitle(articleUpdateModel.getArticleTitle());
        if(articleUpdateModel.getUrlStatus()==1) {
            article.setUseLink(true);
        }else {
            article.setUseLink(false);
        }
        //article.setCategory(); //todo 设置文章的类型

        article=articleRepository.saveAndFlush(article);
        return "manager/article/addArticle"; //todo 返回上传状态

    }

    /**
     * 跳转到添加文章页面
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jumpToAddArticle",method = RequestMethod.GET)
    public String jumpToAddArticle(Model model) throws Exception {


        return "manager/article/addArticle"; //todo 返回上传状态

    }
    /**
     * 修改文章
     * @param articleCUDModel 前台传过来的参数
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/alertArticle",method = RequestMethod.GET)
    public String alertArticle(ArticleCUDModel articleCUDModel,Model model) throws Exception {

        Article article=new Article();
        article.setTime(new Date());
        //todo 进行图片的上传；
        //article.setPicture(articleAddModel.getPicture().path());

        article.setContent(articleCUDModel.getContent());
        article.setLinkUrl(articleCUDModel.getLinkUrl());
        //article.setManager();//todo 管理员id
        article.setTitle(articleCUDModel.getArticleTitle());
        if(articleCUDModel.getUrlStatus()==1) {
            article.setUseLink(true);
        }else {
            article.setUseLink(false);
        }
        //article.setCategory(); //todo 设置文章的类型

        article=articleRepository.saveAndFlush(article);
        return "manager/article/addArticle"; //todo 返回上传状态

    }

    /**
     * 删除文章
     * @param articleId 前台传过来的参数
     * @throws Exception
     */
    @RequestMapping(value = "/delArticle",method = RequestMethod.GET)
    public void delArticle(Long articleId) throws Exception {
        Article article=articleRepository.findOne(articleId);
        System.out.print("!!!!!!!!!!!!!!!"+article+"!!!!!!!!!");
        articleRepository.delete(article);
    }


}
