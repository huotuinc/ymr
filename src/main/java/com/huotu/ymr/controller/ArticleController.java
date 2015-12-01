package com.huotu.ymr.controller;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.api.ArticleSystem;
import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.model.AppArticleListModel;
import com.huotu.ymr.model.AppArticleModel;
import com.huotu.ymr.model.AppCategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
@Controller
@RequestMapping("/article")
public class ArticleController implements ArticleSystem {

    @Autowired
    ArticleRepository articleRepository;
    @Override
    @RequestMapping("/getCategoryList")
    public ApiResult getCategoryList(Output<AppCategoryModel[]> list) throws Exception {

        List<AppCategoryModel> categoryList=new ArrayList<AppCategoryModel>();
        categoryList=articleRepository.findAll();

        list.outputData(categoryList.toArray(new AppCategoryModel[categoryList.size()]));
        return ApiResult.resultWith(CommonEnum.SUCCESS);

    }

    @Override
    @RequestMapping("/getArticleList")
    public ApiResult getArticleList(Output<AppArticleListModel[]> list, Integer categoryId, Long lastId) throws Exception {

        int number=0;
        List<AppArticleListModel> articleList=articleRepository.findArticleListFromlastIdWithNumber(categoryId, lastId, number);

        list.outputData(articleList.toArray(new AppArticleListModel[articleList.size()]));
        return ApiResult.resultWith(CommonEnum.SUCCESS);
    }

    @Override
    @RequestMapping("getArticleInfo")
    public ApiResult getArticleInfo(Output<AppArticleModel> data, Long id) throws Exception {

        AppArticleModel article=articleRepository.findOne(id);
        data.outputData(article);
        return ApiResult.resultWith(CommonEnum.SUCCESS);
    }
}
