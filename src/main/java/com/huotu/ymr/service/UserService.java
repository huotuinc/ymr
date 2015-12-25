package com.huotu.ymr.service;

import com.huotu.ymr.entity.User;
import com.huotu.ymr.model.AppUserInfoModel;
import com.huotu.ymr.model.mall.MallUserModel;

/**
 * 用户管理
 * Created by slt on 2015/12/2.
 */
public interface UserService {
    /**
     * 根据User和MallUserModel返回AppUserInfoModel
     * @param user
     * @param mallUserModel
     * @return
     * @throws Exception
     */
    AppUserInfoModel getAppUserInfoModel(User user,MallUserModel mallUserModel)throws Exception;

    /**
     * 根据userId，返回User对象，如果没有则创建一个
     * @param userId        用户ID
     * @return
     * @throws Exception
     */
    User getUser(Long userId) throws Exception;


}
