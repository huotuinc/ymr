package com.huotu.ymr.service;

import com.huotu.ymr.entity.Manager;

/**
 * Created by xhk on 2015/12/29.
 * 后台管理员服务
 */
public interface ManagerService {
    /**
     * 获取管理员表相应管理员
     * @param username 管理员登录用户名
     * @param password 管理员登录密码
     * @return
     */
    Manager checkManager(String username, String password);

    /**
     * 注册管理员表相应管理员
     * @param username 管理员登录用户名
     * @param password 管理员登录密码
     * @return
     */
    Manager saveManager(String username, String password);
}
