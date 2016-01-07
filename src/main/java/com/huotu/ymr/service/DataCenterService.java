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
    MallUserModel[] getUserInfoByUniond(String uniond) throws IOException;

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
    List<CategoryModel[]> getCategory(Long merchantId) throws IOException;

    /**
     * 根据分类获取商品
     * @return
     */
    MallGoodModel[] getMallGood();

    /**
     * 绑定手机号
     */
    void bindingMobile(Long userId,String mobile) throws IOException;

    /**
     * 修改手机号
     */
    void  ModifyMobile(Long userId,String mobile) throws IOException;

    /**
     * 充值小金库
     */
    Boolean rechargeCoffers();

    /**
     * 修改用户信息
     * 上传类型：0：图片，1：昵称，2：姓名，3：性别，4：联系电话(绑定手机)
     */
    void modifyUserInfo(Long userId,String data,Integer type) throws IOException;








}
