package com.huotu.ymr.controller;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.api.ArticleSystem;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.Article;
import com.huotu.ymr.entity.Category;
import com.huotu.ymr.model.AppArticleListModel;
import com.huotu.ymr.model.AppArticleModel;
import com.huotu.ymr.model.AppCategoryModel;
import com.huotu.ymr.repository.ArticleRepository;
import com.huotu.ymr.repository.CategoryRepository;
import com.huotu.ymr.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xhk on 2015/12/1.
 */
@Controller
@RequestMapping("/article")
public class ArticleController implements ArticleSystem {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleService articleService;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    @RequestMapping("/getCategoryList")
    public ApiResult getCategoryList(Output<AppCategoryModel[]> list) throws Exception {

        List<AppCategoryModel> appCategoryList=new ArrayList<AppCategoryModel>();

        List<Category> categoryList=categoryRepository.findAll();
        for(Category category:categoryList) {
            AppCategoryModel appCategoryModel= new AppCategoryModel();
            appCategoryModel.setName(category.getName());
            appCategoryModel.setPicture(category.getPicture());
            appCategoryModel.setPId(category.getId());
            appCategoryList.add(appCategoryModel);
        }
        list.outputData(appCategoryList.toArray(new AppCategoryModel[categoryList.size()]));
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);

    }

    @Override
    @RequestMapping("/getArticleList")
    public ApiResult getArticleList(Output<AppArticleListModel[]> list, Integer categoryId, Long lastId) throws Exception {

        int number=3;//todo 每页的文章条数暂定测试条数：3
        if(lastId==null){
            lastId=articleService.getMaxId()+1;
        }//如果为空则默认第一页
        List<Article>  articleList=articleService.findArticleListFromlastIdWithNumber(categoryId, lastId, number);
        List<AppArticleListModel> appArticleListModels=new ArrayList<AppArticleListModel>();
        for(Article article:articleList){
            AppArticleListModel appArticleListModel=new AppArticleListModel();
            appArticleListModel.setPId(article.getId());
            appArticleListModel.setPicture(article.getPicture());
            appArticleListModel.setTitle(article.getTitle());
            appArticleListModel.setSummary(article.getSummary());
            appArticleListModel.setView(article.getView());
            appArticleListModels.add(appArticleListModel);
        }
        list.outputData(appArticleListModels.toArray(new AppArticleListModel[articleList.size()]));
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }

    @Override
    @RequestMapping("getArticleInfo")
    public ApiResult getArticleInfo(Output<AppArticleModel> data, Long id) throws Exception {

        Article article=articleRepository.findOne(id);
        AppArticleModel articleModel=new AppArticleModel();
        articleModel.setView(article.getView());
        articleModel.setContent(article.getContent());
        articleModel.setTitle(article.getTitle());
        articleModel.setPicture(article.getPicture());
        articleModel.setTime(article.getTime());
        data.outputData(articleModel);
        return ApiResult.resultWith(CommonEnum.AppCode.SUCCESS);
    }
}
