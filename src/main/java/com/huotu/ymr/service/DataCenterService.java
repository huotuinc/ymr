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
     * @param userId 用户ID
     * @return User的model信息
     * @throws IOException 当http连接出现问题时抛出异常
     */
    MallUserModel getUserInfoByUserId(Long userId) throws IOException;


    /**
     * 根据微信授权信息去商城创建用户
     * @param appWeiXinAccreditModel    用户授权信息
     * @return                          UserId
     * @throws IOException 当http连接出现问题时抛出异常
     */
    Long createUser(AppWeiXinAccreditModel appWeiXinAccreditModel) throws IOException;

    /**
     * 根据手机号去商城创建用户
     * @param mobile        手机号
     * @param code          验证码
     * @return
     * @throws IOException
     */
    Long createUser(String mobile,String code) throws IOException;


    /**
     * 发送验证码
     * @param mobile    手机号
     * @param second    发送间隔秒数
     * @throws IOException
     */
    void sendMallCode(String mobile,int second) throws IOException;

    /**
     *  获取商品分类
     * @return
     */
    List<CategoryModel> getCategory() throws IOException;

    /**
     * 根据分类获取商品
     * @return
     */
    List<MallGoodModel> getMallGood(String catPath,String title,Integer pageNo) throws IOException;

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
