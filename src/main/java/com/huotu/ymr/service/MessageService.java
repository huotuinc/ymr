package com.huotu.ymr.service;

import com.huotu.ymr.entity.PushingMessage;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * 推送服务
 * Created by slt on 2015/12/18.
 */
public interface MessageService {
    /**
     * 推送消息
     * <p>成功以后将被删除</p>
     * @param message  待推送的消息
     * @return true 表示成功推送
     */
    @Transactional
    boolean pushMessage(PushingMessage message);

    /**
     * 立即推送未推送的消息
     */
    @Transactional
    void pushMessages();


    /**
     * 立即推送该消息到所有用户
     * @param message 待发送消息
     */
    @Transactional
    void pushMessageToAllUser(PushingMessage message);

}
