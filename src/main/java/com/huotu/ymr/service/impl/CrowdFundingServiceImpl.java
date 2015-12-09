package com.huotu.ymr.service.impl;

import com.huotu.ymr.entity.CrowdFunding;
import com.huotu.ymr.entity.CrowdFundingPublic;
import com.huotu.ymr.repository.CrowdFundingPublicRepository;
import com.huotu.ymr.repository.CrowdFundingRepository;
import com.huotu.ymr.service.CrowdFundingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xhk on 2015/12/2.
 */
@Service
public class CrowdFundingServiceImpl implements CrowdFundingService {

//    @Autowired
//    private EntityManager entityManager;
    @Autowired
    private CrowdFundingRepository crowdFundingRepository;

    @Autowired
    private CrowdFundingPublicRepository crowdFundingPublicRepository;

    @Override
    public List<CrowdFundingPublic> findCrowdListFromLastIdWithNumber(Long crowdId, Long lastId, int number) {
        StringBuilder hql = new StringBuilder();
        hql.append("from CrowdFundingPublic as crowd where crowd.crowdFunding.id=:crowdId and crowd.id<:lastId order by crowd.id desc");
        List<CrowdFundingPublic> crowdList = crowdFundingPublicRepository.queryHql(hql.toString(), query -> {
            query.setParameter("crowdId", crowdId);
        query.setParameter("lastId", lastId);
        query.setMaxResults(number);
        });
        return crowdList;
//        StringBuilder hql = new StringBuilder();
//        hql.append("from CrowdFundingPublic as crowd where crowd.crowdFunding.id=:crowdId and crowd.id<:lastId order by crowd.id desc");
//        Query query = entityManager.createQuery(hql.toString());
//        query.setParameter("crowdId", crowdId);
//        query.setParameter("lastId", lastId);
//        query.setMaxResults(number);
//        List<CrowdFundingPublic> crowdList = query.getResultList();
//        return crowdList;
    }

    @Override
    public long getMaxId() {
        StringBuilder hql =new StringBuilder("select max(crowd.id) from CrowdFundingPublic as crowd");
        List<Long> maxIds = crowdFundingPublicRepository.queryHql(hql.toString(), null);
        long maxId = 0L;
        if (maxIds.size()>0&&maxIds.get(0)!=null) {
            maxId = maxIds.get(0);
        }
        return maxId;
//        StringBuilder hql =new StringBuilder("select max(crowd.id) from CrowdFundingPublic as crowd");
//        Query query=entityManager.createQuery(hql.toString());
//        List<Long> maxIds=query.getResultList();
//        long maxId=0L;
//        if(maxIds.size()>0&&maxIds.get(0)!=null){
//            maxId=maxIds.get(0);
//        }
//        return maxId;
    }

    @Override
    public List<CrowdFundingPublic> searchCooperationgList(String key, Long lastId, Long crowdId, int number) {

        StringBuilder hql=new StringBuilder("from CrowdFundingPublic as crowd where crowd.crowdFunding.id=:crowdId " +
                " and crowd.id<:lastId" +
                " and crowd.name like :key"+
                " order by crowd.id desc");
        List<CrowdFundingPublic> crowdList = crowdFundingPublicRepository.queryHql(hql.toString(), query -> {
            query.setParameter("key","%"+key+"%");
        query.setParameter("crowdId", crowdId);
        query.setParameter("lastId", lastId);
        query.setMaxResults(number);
        });
        return crowdList;
//        StringBuilder hql=new StringBuilder("from CrowdFundingPublic as crowd where crowd.crowdFunding.id=:crowdId " +
//                " and crowd.id<:lastId" +
//                " and crowd.name like :key"+
//                " order by crowd.id desc");
//        Query query=entityManager.createQuery(hql.toString());
//        query.setParameter("key","%"+key+"%");
//        query.setParameter("crowdId", crowdId);
//        query.setParameter("lastId", lastId);
//        query.setMaxResults(number);
//        List<CrowdFundingPublic> crowdList = query.getResultList();
//        return crowdList;
    }

    @Override
    public long getCrowdFundingMaxId() {

        StringBuilder hql =new StringBuilder("select max(crowd.id) from CrowdFunding as crowd");
        List<Long> maxIds = crowdFundingPublicRepository.queryHql(hql.toString(), null);
        long maxId = 0L;
        if (maxIds.size()>0&&maxIds.get(0)!=null) {
            maxId = maxIds.get(0);
        }
        return maxId;
//        StringBuilder hql =new StringBuilder("select max(crowd.id) from CrowdFunding as crowd");
//        Query query=entityManager.createQuery(hql.toString());
//        List<Long> maxIds=query.getResultList();
//        long maxId=0L;
//        if(maxIds.size()>0&&maxIds.get(0)!=null){
//            maxId=maxIds.get(0);
//        }
//        return maxId;
    }

    @Override
    public List<CrowdFunding> searchCrowdFundingList(Long lastId, int number) {

        StringBuilder hql = new StringBuilder();
        hql.append("from CrowdFunding as crowd where crowd.id<:lastId " +
                "order by crowd.id desc" );
        List<CrowdFunding> crowdList = crowdFundingRepository.queryHql(hql.toString(), query -> {
        query.setParameter("lastId", lastId);
        query.setMaxResults(number);
        });
        return crowdList;
//        StringBuilder hql = new StringBuilder();
////        hql.append("from CrowdFunding as crowd left join crowd.visibleLevel as vlevel " +
////                " where  crowd.id<:lastId and vlevel.visibleLevel=:level order by crowd.id desc");
//        hql.append("from CrowdFunding as crowd where crowd.id<:lastId " +
//                " and crowd.visibleLevel like :level order by crowd.id desc" );
//        Query query = entityManager.createQuery(hql.toString());
//        query.setParameter("level","%"+userLevel.getValue()+"%");
//        query.setParameter("lastId", lastId);
//        query.setMaxResults(number);
//        List<CrowdFunding> crowdList = query.getResultList();
//        return crowdList;
    }
}
