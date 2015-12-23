package com.huotu.ymr.controller.web;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.PublicManagerParameterHolder;
import com.huotu.ymr.entity.Article;
import com.huotu.ymr.entity.Category;
import com.huotu.ymr.model.manager.MngPublicModel;
import com.huotu.ymr.model.searchCondition.ArticleSearchModel;
import com.huotu.ymr.repository.ArticleRepository;
import com.huotu.ymr.repository.ManagerRepository;
import com.huotu.ymr.service.ArticleService;
import com.huotu.ymr.service.CategoryService;
import com.huotu.ymr.service.StaticResourceService;
import com.sun.jndi.toolkit.url.Uri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Date;

/**
 * Created by lgh on 2015/12/3.
 */
@Controller
@RequestMapping("/manager")
public class ArticleManagerController {

    @Autowired
    StaticResourceService staticResourceService;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleService articleService;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    CategoryService categoryService;

    /**
     * 获取关于美投文章列表
     *
     * @param articleSearchModel 前台传过来的参数
     * @param model
     * @throws Exception
     */
    @RequestMapping(value = "/getArticleList", method = RequestMethod.GET)
    public String getArticleList(ArticleSearchModel articleSearchModel, Model model) throws Exception {
        Page<Article> articlePages = articleService.findArticlePage(articleSearchModel);
        model.addAttribute("allArticleList", articlePages);//文章列表
        model.addAttribute("pageNoStr", articleSearchModel.getPageNoStr());//当前页数
        return "manager/article/articleList";

    }

    /**
     * 后台添加和修改文章内容
     *
     * @return
     */
    @RequestMapping(value = "/saveArticle", method = RequestMethod.POST)
    public String saveArticle(Article article, HttpServletRequest request) throws Exception {
        MngPublicModel mpm = PublicManagerParameterHolder.get();
        String contextPath = request.getContextPath();
        //新增
        if (article.getId() == null) {
            Uri uri = new Uri(article.getPicture());
            String imgPath = uri.getPath().substring(uri.getPath().indexOf(contextPath) + contextPath.length());
            article.setPicture(imgPath);
            //article.setManager(managerRepository.findOne(mpm.getManager().getId()));//todo 管理员id
            String articleType=request.getParameter("articleType");
            String typeName=null;
            switch (articleType){
                case "company":
                    typeName="公司介绍";
                    break;
                case "autobiography":
                    typeName="自传故事";
                    break;
                case "college":
                    typeName="学院介绍";
                    break;
                case "hairdressing":
                    typeName="美容知识";
                    break;
            }
            Category category=categoryService.getArticleTypeByName(typeName);
            article.setCategory(category);
            article.setTime(new Date());
            article.setCheckType(CommonEnum.CheckType.pass);
            article=articleRepository.save(article);
            //修改
        } else {
            Article modifyArticle = articleRepository.findOne(article.getId());
            //URI uri=staticResourceService.getResource(article.getPicture());
            Uri uri = new Uri(article.getPicture());
            String imgPath = uri.getPath().substring(uri.getPath().indexOf(contextPath) + contextPath.length());
            modifyArticle.setPicture(imgPath);
            modifyArticle.setTitle(article.getTitle());
            modifyArticle.setContent(article.getContent());
            modifyArticle.setUseLink(article.getUseLink());
            modifyArticle.setLinkUrl(article.getLinkUrl());
            modifyArticle.setTime(new Date());
            //article.setManager(managerRepository.findOne(mpm.getManager().getId()));//todo 文章id是取创建者的还是修改者的
            modifyArticle=articleRepository.save(modifyArticle);
        }
        return "redirect:getArticleList";
    }


    /**
     * 跳转到编辑文章
     *
     * @param articleId 前台传过来的文章参数
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jumpToSaveArticle", method = RequestMethod.GET)
    public String jumpToSaveArticle(Long articleId, Model model) throws Exception {
        Article article = articleRepository.findOne(articleId);
        URI uri=staticResourceService.getResource(article.getPicture());
        article.setPicture(uri.toString());
        model.addAttribute("article", article);
        model.addAttribute("articleTypes", CommonEnum.ArticleType.values());
        return "manager/article/addArticle"; //todo 返回上传状态
    }

    /**
     * 跳转到添加文章页面
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jumpToAddArticle", method = RequestMethod.GET)
    public String jumpToAddArticle(Article article, Model model) throws Exception {
        model.addAttribute("articleTypes", CommonEnum.ArticleType.values());
        return "manager/article/addArticle"; //todo 返回上传状态
    }


    /**
     * 删除文章
     *
     * @param articleId 前台传过来的参数
     * @throws Exception
     */
    @RequestMapping(value = "/delArticle", method = RequestMethod.GET)
    public void delArticle(Long articleId) throws Exception {
        Article article = articleRepository.findOne(articleId);
        articleRepository.delete(article);
    }


}
