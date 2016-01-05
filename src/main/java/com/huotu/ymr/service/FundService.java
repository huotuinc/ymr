package com.huotu.ymr.service;

import com.huotu.ymr.entity.CrowdFunding;
import com.huotu.ymr.entity.User;

/**
 * 资金服务
 * Created by xhk on 2015/12/24.
 */
public interface FundService {

    /**
     * 退款
     * @param crowdFunding 项目
     * @param ownerId 用户id
     */
    boolean refundByUserId(CrowdFunding crowdFunding, Long ownerId) throws Exception;

    /**
     * 金额转换积分
     * 用户增长当前用户积分与累计获得积分
     * 当用户转换积分达到升等资格时升等（一等升二等）
     * @param ownerId 用户id
     * @param money 金额
     */
    void increaseIntegralByMoney(Long ownerId, Double money);

    /**
     * 转发积分升级
     * 用户增长当前用户积分与累计获得积分
     * 当用户转换积分达到升等资格时升等（一等升二等）
     * @param user 用户user
     * @param score 转发积分
     */
    User increaseIntegralByScore(User user, Integer score);
}
