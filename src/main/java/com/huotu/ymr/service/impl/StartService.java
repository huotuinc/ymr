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
import com.huotu.ymr.entity.Manager;
import com.huotu.ymr.entity.PushingMessage;
import com.huotu.ymr.model.AppOS;
import com.huotu.ymr.repository.ConfigRepository;
import com.huotu.ymr.repository.ManagerRepository;
import com.huotu.ymr.repository.PushingMessageRepository;
import com.huotu.ymr.repository.UserRepository;
import com.huotu.ymr.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by lgh on 2015/9/6.
 */

@Service
public class StartService implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    ConfigRepository configRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    PushingMessageRepository pushingMessageRepository;

    @Autowired
    private Environment env;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    LoginService loginService;
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            if (managerRepository.count()==0){
                Manager manager = new Manager();
                manager.setLoginName("admin");
                loginService.newLogin(manager,"admin");
            }
            if(configRepository.count()==0){
                Config  configGT=new Config();
                configGT.setKey(ConfigKey.GLOBAL_TRANSMIT);
                configGT.setValue("0");
                configRepository.save(configGT);

                Config configTT=new Config();
                configTT.setKey(ConfigKey.GLOBAL_TOTAL);
                configTT.setValue("-1");
                configRepository.save(configTT);

                Config bottom=new Config();
                bottom.setKey(ConfigKey.BOTTOM_GENERALIZE);
                bottom.setValue("");
                configRepository.save(bottom);

                Config userPT=new Config();
                userPT.setKey(ConfigKey.USER_POST);
                userPT.setValue("0");
                configRepository.save(userPT);

                Config userGT=new Config();
                userGT.setKey(ConfigKey.USER_TRANSMIT);
                userGT.setValue("0");
                configRepository.save(userGT);

                Config userTT=new Config();
                userTT.setKey(ConfigKey.USER_TOTAL);
                userTT.setValue("0");
                configRepository.save(userTT);

                Config upT=new Config();
                upT.setKey(ConfigKey.UPGRADE_INTEGRAL);
                upT.setValue("-1");
                configRepository.save(upT);

            }


            if(env.acceptsProfiles("test")){
                if(pushingMessageRepository.count()==0){
                    PushingMessage pushingMessage=new PushingMessage();
                    pushingMessage.setTitle("slt向大家问好");
                    pushingMessage.setType(CommonEnum.PushMessageType.RemindMessage);
                    pushingMessage.setOs(AppOS.Android);
                    pushingMessageRepository.save(pushingMessage);
                }
            }
        }
    }

}
