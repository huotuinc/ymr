package com.huotu.ymr.service.impl;

import com.huotu.ymr.entity.CrowdFundingPublic;
import com.huotu.ymr.service.CrowdFoundingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by xhk on 2015/12/2.
 */
@Service
public class CrowdFoundingServiceImpl implements CrowdFoundingService{

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<CrowdFundingPublic> findCrowdListFromLastIdWithNumber(Long crowdId, Long lastId, int number) {
        StringBuilder hql = new StringBuilder();
        hql.append("from CrowdFundingPublic as crowd where crowd.crowdFunding.id=:crowdId and crowd.id<:lastId order by crowd.id desc");
        Query query = entityManager.createQuery(hql.toString());
        query.setParameter("catId", crowdId);
        query.setParameter("lastId", lastId);
        query.setMaxResults(number);
        List<CrowdFundingPublic> crowdList = query.getResultList();
        return crowdList;
    }

    @Override
    public long getMaxId() {
        StringBuilder hql =new StringBuilder("select max(crowd.id) from CrowdFundingPublic as crowd");
        Query query=entityManager.createQuery(hql.toString());
        List<Long> maxIds=query.getResultList();
        long maxId=0L;
        if(maxIds.size()!=0){
            maxId=maxIds.get(0);
        }
        return maxId;
    }
}
