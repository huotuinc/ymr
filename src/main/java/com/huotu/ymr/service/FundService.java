package com.huotu.ymr.service;

import com.huotu.ymr.entity.CrowdFunding;

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
     * @param ownerId 用户id
     * @param money 金额
     */
    void increaseIntegral(Long ownerId, Double money);
}
