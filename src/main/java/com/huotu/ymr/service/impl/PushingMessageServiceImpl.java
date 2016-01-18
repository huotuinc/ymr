package com.huotu.ymr.service.impl;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.PushingMessage;
import com.huotu.ymr.entity.User;
import com.huotu.ymr.repository.PushingMessageRepository;
import com.huotu.ymr.service.PushingMessageService;
import com.huotu.ymr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/1/13.
 */
@Service
public class PushingMessageServiceImpl implements PushingMessageService {
    @Autowired
    PushingMessageRepository pushingMessageRepository;

    @Autowired
    UserService userService;
    @Override
    public void upgradePush(User user, String pushContent) throws Exception {

        PushingMessage pushingMessage=new PushingMessage();
        pushingMessage.setType(CommonEnum.PushMessageType.RemindMessage);
        pushingMessage.setTitle(pushContent);

//        pushingMessage.setDeviceTokens(PublicParameterHolder.get().getImei());
        pushingMessageRepository.save(pushingMessage);
        user.getPushingToken();






    }
}
