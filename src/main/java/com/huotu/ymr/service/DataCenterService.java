package com.huotu.ymr.service;

import com.huotu.ymr.model.AppWeiXinAccreditModel;
import com.huotu.ymr.model.mall.CategoryModel;
import com.huotu.ymr.model.mall.MallGoodModel;
import com.huotu.ymr.model.mall.MallUserModel;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2015/12/22.
 */
public interface DataCenterService {


    /**
     * 获取用户信息(根据uniond)
     * @return
     */
    MallUserModel[] getUserInfoByUniond(String uniond);

    /**
     * 获取用户信息(根据手机号)
     * @return
     */
    MallUserModel getUserInfoByMobile(String mobile);
    /**
     * 获取用户信息(根据userId)
     * @return
     */
    MallUserModel getUserInfoByUserId(Long userId) throws IOException;

    /**
     * 创建一个用户
     * @return
     */
    Long createUser(AppWeiXinAccreditModel appWeiXinAccreditModel) throws IOException;

    /**
     *  获取商品分类
     * @return
     */
    List<CategoryModel> getCategory();

    /**
     * 根据分类获取商品
     * @return
     */
    MallGoodModel[] getMallGood();

    /**
     * 绑定手机号
     * @return
     */
    MallGoodModel bindingMobile();

    /**
     * 修改手机号
     * @return
     */
    MallGoodModel ModifyMobile();

    /**
     * 充值小金库
     * @return
     */
    Boolean rechargeCoffers();

    /**
     * 修改用户信息
     * @return
     */
    Boolean modifyUserInfo(Long userId,String data,Integer type) throws IOException;








}
