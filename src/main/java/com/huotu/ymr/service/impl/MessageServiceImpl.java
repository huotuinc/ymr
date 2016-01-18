package com.huotu.ymr.service.impl;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.huotu.ymr.entity.PushingMessage;
import com.huotu.ymr.model.AppOS;
import com.huotu.ymr.repository.PushingMessageRepository;
import com.huotu.ymr.service.CommonConfigService;
import com.huotu.ymr.service.MessageService;
import com.huotu.ymr.service.UserService;
//import com.notnoop.apns.APNS;
//import com.notnoop.apns.ApnsService;
//import com.notnoop.apns.ApnsServiceBuilder;
//import com.notnoop.exceptions.NetworkIOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by xhk on 2015/12/1.
 */
@Service
public class MessageServiceImpl implements MessageService {

    private static final Log log = LogFactory.getLog(MessageServiceImpl.class);
    @Autowired
    private Environment env;

//    private ApnsService service;

    private JPushClient client;

    @Autowired
    UserService userService;

    @Autowired
    PushingMessageRepository pushingMessageRepository;

    @Autowired
    CommonConfigService commonConfigService;

    @PostConstruct
    public void init() {
        //andriod配置
        String MASTERSECRET =commonConfigService.getMASTERSECRET();
        String APPKEY = commonConfigService.getAPPKEY();
        client = new JPushClient(MASTERSECRET, APPKEY, env.acceptsProfiles("!test"), 24 * 60 * 60);

        InputStream input;
        if (env.acceptsProfiles("test")) {
            input = MessageService.class.getResourceAsStream("Push.Development.p12");
        } else {
            input = MessageService.class.getResourceAsStream("Push.Production.p12");
        }

//        ApnsServiceBuilder builder = APNS.newService().withCert(input, "Iamhuotu");
//        if (env.acceptsProfiles("test")) {
//            builder = builder.withSandboxDestination();
//        } else {
//            builder = builder.withProductionDestination();
//        }
//
//        service = builder.build();
//        service.testConnection();
//        service.start();
    }

    @PreDestroy
    public void close() throws Exception {
//        if (service != null) {
//            service.stop();
//        }
    }



    @Override
    public boolean pushMessage(PushingMessage message) {
        //推送到ios
//        if (message.getOs() == AppOS.iOS) {
//            String payload = APNS.newPayload()
//                    .sound("default")
//                    .alertBody(message.getTitle())
//                    .alertTitle(message.getTitle())
//                    .customField("type", message.getType().getValue())
//                    .customField("data", message.getData())
//                    .customField("username", message.getUsername())
//                    .badge(1)
//                    .build();
//
//            if (message.getDeviceTokens().isEmpty()) {
//                log.debug("无信息可发");
//                disposePushingMessage(message);
//                return false;
//            }
//
//            if (log.isDebugEnabled()) {
//                log.debug("准备推送到iOS设备:" + String.join(",", message.getDeviceTokens()));
//            }
//
//            try {
//                service.push(message.getDeviceTokens(), payload);
//                disposePushingMessage(message);
//                return true;
//            } catch (NetworkIOException ex) {
//                log.error("IOS推送错误", ex);
//            }
//            return false;
//        }

        //推送到安卓
        HashMap<String, String> extras = new HashMap<>();
        extras.put("type", "" + message.getType().getValue());
        if (message.getData() != null)
            extras.put("data", message.getData());


        if (message.getUsername() != null)
            extras.put("username", message.getUsername());

        Notification notification = Notification.android(message.getTitle(), message.getTitle(), extras);

        PushPayload.Builder builder = PushPayload.newBuilder().setPlatform(Platform.android());

        if (message.getDeviceTokens() == null || message.getDeviceTokens().isEmpty()) {
            //所有用户都能接受到
            builder = builder.setAudience(Audience.all());

            if (log.isDebugEnabled()) {
                log.debug("准备推送到Android设备：全部");
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("准备推送到Android设备:" + String.join(",", message.getDeviceTokens()));
            }

            builder = builder.setAudience(Audience.alias(message.getDeviceTokens()));
        }

        try {
            //开始推送
            client.sendPush(builder.setNotification(notification).build());
            disposePushingMessage(message);
            return true;
        } catch (APIConnectionException  | APIRequestException e) {
            log.error("推送连接错误", e);
        }
        return false;
    }

    @Override
    public void pushMessages() {
        pushingMessageRepository.findAll().forEach(this::pushMessage);
    }

    private void disposePushingMessage(PushingMessage message) {
        message.getDeviceTokens().clear();

        try {
            pushingMessageRepository.delete(message);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void pushMessageToAllUser(PushingMessage message) {
        for (final AppOS os : AppOS.values()) {
            Set<String> pushTokens=userService.findAllPushToken();

            if (!pushTokens.isEmpty()) {
                PushingMessage pushing = message.clone();
                pushing.setDeviceTokens(pushTokens);
                pushing.setOs(os);
                pushingMessageRepository.save(pushing);
            }
        }

    }
}
