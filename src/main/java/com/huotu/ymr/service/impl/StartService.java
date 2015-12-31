/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.ymr.service.impl;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.common.ConfigKey;
import com.huotu.ymr.entity.Config;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.repository.ConfigRepository;
import com.huotu.ymr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by lgh on 2015/9/6.
 */

@Service
public class StartService implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    ConfigRepository configRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(Objects.isNull(userRepository.findOne(1234L))){
            User user=new User();
            user.setId(1234L);
            user.setUserLevel(CommonEnum.UserLevel.one);
            user.setToken(UUID.randomUUID().toString().replaceAll("-",""));
            user.setContinuedScore(0);
            user.setUserStatus(CommonEnum.UserStatus.freeze);
            userRepository.save(user);
        }
        if(Objects.isNull(userRepository.findOne(5678L))){
            User user=new User();
            user.setId(5678L);
            user.setUserLevel(CommonEnum.UserLevel.one);
            user.setToken(UUID.randomUUID().toString().replaceAll("-",""));
            user.setContinuedScore(0);
            user.setUserStatus(CommonEnum.UserStatus.normal);
            userRepository.save(user);
        }
        if (event.getApplicationContext().getParent() == null) {
            Config configGT =configRepository.findOne(ConfigKey.GLOBAL_TRANSMIT);
            Config configTT =configRepository.findOne(ConfigKey.GLOBAL_TOTAL);
            Config bottom=configRepository.findOne(ConfigKey.BOTTOM_GENERALIZE);
            Config userGT=configRepository.findOne(ConfigKey.USER_TRANSMIT);
            Config userTT=configRepository.findOne(ConfigKey.USER_TOTAL);
            Config userPT=configRepository.findOne(ConfigKey.USER_POST);
            Config upT=configRepository.findOne(ConfigKey.UPGRADE_INTEGRAL);
            if(configGT==null){
                configGT=new Config();
                configGT.setKey(ConfigKey.GLOBAL_TRANSMIT);
                configGT.setValue("0");
                configRepository.save(configGT);
            }
            if(configTT==null){
                configTT=new Config();
                configTT.setKey(ConfigKey.GLOBAL_TOTAL);
                configTT.setValue("-1");
                configRepository.save(configTT);
            }
            if(bottom==null){
                bottom=new Config();
                bottom.setKey(ConfigKey.BOTTOM_GENERALIZE);
                bottom.setValue("");
                configRepository.save(bottom);
            }
            if(userPT==null){
                userPT=new Config();
                userPT.setKey(ConfigKey.USER_POST);
                userPT.setValue("0");
                configRepository.save(userPT);
            }
            if(userGT==null){
                userGT=new Config();
                userGT.setKey(ConfigKey.USER_TRANSMIT);
                userGT.setValue("0");
                configRepository.save(userGT);
            }
            if(userTT==null){
                userTT=new Config();
                userTT.setKey(ConfigKey.USER_TOTAL);
                userTT.setValue("0");
                configRepository.save(userTT);
            }
            if(upT==null){
                upT=new Config();
                upT.setKey(ConfigKey.UPGRADE_INTEGRAL);
                upT.setValue("-1");
                configRepository.save(upT);
            }
        }
    }

}
