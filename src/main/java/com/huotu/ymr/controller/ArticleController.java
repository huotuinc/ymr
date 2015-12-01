package com.huotu.ymr.controller;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.api.ArticleSystem;
import com.huotu.ymr.model.AppArticleListModel;
import com.huotu.ymr.model.AppArticleModel;
import com.huotu.ymr.model.AppCategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2015/12/1.
 */
@Controller
@RequestMapping("/article")
public class ArticleController implements ArticleSystem {

    @Autowired

    @Override
    @RequestMapping("/getCategoryList")
    public ApiResult getCategoryList(Output<AppCategoryModel[]> list) throws Exception {

        //AppCategoryModel[] appCategoryModel=new AppCategoryModel[1];

        return null;
    }

    @Override
    @RequestMapping("/getArticleList")
    public ApiResult getArticleList(Output<AppArticleListModel[]> list, Integer categoryId, Long lastId) throws Exception {
        return null;
    }

    @Override
    @RequestMapping("getArticleInfo")
    public ApiResult getArticleInfo(Output<AppArticleModel> data, Long id) throws Exception {
        return null;
    }
}
