package com.huotu.ymr.service;

import com.huotu.ymr.entity.MyCrowdFundingFlow;

import java.util.List;

/**
 * 用户众筹项目流水操作
 * Created by xhk on 2015/12/30.
 */
public interface MyCrowdFundingFlowService {
    /**
     *获取一页我的众筹
     * @param lastId  上一页最后一个众筹id
     * @param userId  用户id
     * @param number  一个众筹项目数量
     * @return
     */
    List<MyCrowdFundingFlow> findMyCrowdFundingsById(Long lastId,Long userId,Integer number);

}
