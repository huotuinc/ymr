/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.ymr.service.impl;

import com.huotu.ymr.common.ConfigKey;
import com.huotu.ymr.entity.Config;
import com.huotu.ymr.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by lgh on 2015/9/6.
 */

@Service
public class StartService implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    ConfigRepository configRepository;
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            Config configGT =configRepository.findOne(ConfigKey.GLOBAL_TRANSMIT);
            Config configTT =configRepository.findOne(ConfigKey.GLOBAL_TOTAL);
            Config bottom=configRepository.findOne(ConfigKey.BOTTOM_GENERALIZE);
            Config userGT=configRepository.findOne(ConfigKey.USER_TRANSMIT);
            Config userTT=configRepository.findOne(ConfigKey.USER_TOTAL);
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
        }
    }

}
