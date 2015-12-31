package com.huotu.ymr.service.impl;

import com.huotu.ymr.entity.MyCrowdFundingFlow;
import com.huotu.ymr.repository.MyCrowdFundingFlowRepository;
import com.huotu.ymr.service.MyCrowdFundingFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xhk on 2015/12/30.
 */
@Service
public class MyCrowdFundingFlowServiceImpl implements MyCrowdFundingFlowService{

    @Autowired
    MyCrowdFundingFlowRepository myCrowdFundingFlowRepository;


    @Override
    public List<MyCrowdFundingFlow> findMyCrowdFundingsById(Long lastId, Long userId, Integer number) {
        StringBuilder hql = new StringBuilder();
        hql.append("from MyCrowdFundingFlow as my where " +
                "  booking.my.owner.id=:userId "+
                " and my.crowdFundingPublic.id<:lastId  "+
                " order by my.id desc" );
        List<MyCrowdFundingFlow> myCrowdFundingFlowList = myCrowdFundingFlowRepository.queryHql(hql.toString(), query -> {
            query.setParameter("userId", userId);
            query.setParameter("lastId", lastId);
            query.setMaxResults(number);
        });
        return myCrowdFundingFlowList;
    }
}
