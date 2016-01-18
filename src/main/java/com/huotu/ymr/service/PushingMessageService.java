package com.huotu.ymr.service;

import com.huotu.ymr.entity.User;

/**
 * Created by xhk on 2015/12/24.
 */

public interface PushingMessageService {


    /**
     * 升级推送
     * @param user        用户
     * @param pushContent   推送内容
     * @throws Exception
     */
    void upgradePush(User user,String pushContent) throws Exception;
}
