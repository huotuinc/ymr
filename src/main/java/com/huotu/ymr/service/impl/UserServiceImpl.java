package com.huotu.ymr.service.impl;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.model.AppUserInfoModel;
import com.huotu.ymr.model.mall.MallUserModel;
import com.huotu.ymr.repository.UserRepository;
import com.huotu.ymr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * 用户管理
 * Created by slt on 2015/12/1.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Override
    public AppUserInfoModel getAppUserInfoModel(User user, MallUserModel mallUserModel) throws Exception {
        AppUserInfoModel appUserInfoModel=new AppUserInfoModel();
        appUserInfoModel.setUserId(mallUserModel.getUserId());
        appUserInfoModel.setUserName(mallUserModel.getUserName());
        appUserInfoModel.setHeadUrl(mallUserModel.getHeadUrl());
        appUserInfoModel.setNickName(mallUserModel.getNickName());
        appUserInfoModel.setScore(user.getScore());
        appUserInfoModel.setUserLevel(user.getUserLevel());
        appUserInfoModel.setMerchantId(mallUserModel.getMerchantId());
        appUserInfoModel.setName(mallUserModel.getName());
        appUserInfoModel.setSex(mallUserModel.getSex());
        appUserInfoModel.setMobile(mallUserModel.getMobile());
        appUserInfoModel.setIsBindMobile(mallUserModel.getIsBindMobile());
        appUserInfoModel.setToken(user.getToken());
        return appUserInfoModel;
    }

    @Override
    public User getUser(Long userId) throws Exception {
        User user=userRepository.findOne(userId);
        if(Objects.isNull(user)){
            user=new User();
            user.setId(userId);
            user.setUserLevel(CommonEnum.UserLevel.one);
            user.setScore(0);
            user.setContinuedScore(0);

        }
        //创建一个新的token
        String token= UUID.randomUUID().toString().replaceAll("-","");
        user.setToken(token);
        userRepository.save(user);
        return user;
    }
}
