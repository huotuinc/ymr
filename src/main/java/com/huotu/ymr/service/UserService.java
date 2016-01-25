package com.huotu.ymr.service;

import com.huotu.ymr.entity.ScoreFlow;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.model.AppUserInfoModel;
import com.huotu.ymr.model.mall.MallUserModel;
import com.huotu.ymr.model.searchCondition.ChargeSearchModel;
import com.huotu.ymr.model.searchCondition.UserSearchModel;
import org.springframework.data.domain.Page;

import java.util.Set;

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

    /**
     * 获取用户列表
     * @param userSearchModel   查询条件
     * @return
     */
    Page<User>  findPcUserList(UserSearchModel userSearchModel);


    Page<ScoreFlow> findPcChargeList(ChargeSearchModel chargeSearchModel);


    /**
     * 获取所有用户的pushToken
     * @return
     */
    Set<String> findAllPushToken();


}
