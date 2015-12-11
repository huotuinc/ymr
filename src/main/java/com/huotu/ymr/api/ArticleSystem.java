package com.huotu.ymr.api;

import com.huotu.common.api.ApiResult;
import com.huotu.common.api.Output;
import com.huotu.ymr.model.AppArticleListModel;
import com.huotu.ymr.model.AppArticleModel;
import com.huotu.ymr.model.AppCategoryModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 文章系统接口
 * Created by lgh on 2015/11/26.
 */
public interface ArticleSystem {

    /**
     * 获取分类列表
     *
     * @param list
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getCategoryList(Output<AppCategoryModel[]> list) throws Exception;

    /**
     * 获取文章列表
     *
     * @param list
     * @param categoryId 分类Id 大于0
     * @param lastId     上一页最后一个文章Id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getArticleList(Output<AppArticleListModel[]> list, Integer categoryId, Long lastId) throws Exception;


    /**
     * 获取文章内容
     *
     * @param data
     * @param id 文章Id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    ApiResult getArticleInfo(Output<AppArticleModel> data, Long id) throws Exception;


}
