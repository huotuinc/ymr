package com.huotu.ymr.service.impl;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.CrowdFunding;
import com.huotu.ymr.entity.CrowdFundingBooking;
import com.huotu.ymr.entity.CrowdFundingPublic;
import com.huotu.ymr.model.searchCondition.CrowdFundingSearchModel;
import com.huotu.ymr.repository.CrowdFundingPublicRepository;
import com.huotu.ymr.repository.CrowdFundingRepository;
import com.huotu.ymr.service.CrowdFundingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xhk on 2015/12/2.
 */
@Service
public class CrowdFundingServiceImpl implements CrowdFundingService {

    @Autowired
    private CrowdFundingRepository crowdFundingRepository;

    @Autowired
    private CrowdFundingPublicRepository crowdFundingPublicRepository;

    @Override
    public List<CrowdFundingPublic> findCrowdListFromLastIdWithNumber(Long crowdId, Long lastId, int number) {
        StringBuilder hql = new StringBuilder();
        hql.append("from CrowdFundingPublic as crowd where crowd.crowdFunding.id=:crowdId " +
               // " and crowd.status=:status " +
                " and crowd.id<:lastId " +
                " order by crowd.id desc");
        List<CrowdFundingPublic> crowdList = crowdFundingPublicRepository.queryHql(hql.toString(), query -> {
            query.setParameter("crowdId", crowdId);
            //query.setParameter("status", 1);
        query.setParameter("lastId", lastId);
        query.setMaxResults(number);
        });
        return crowdList;
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
    }

//    @Override
//    public List<CrowdFundingPublic> searchCooperationgList(String key, Long lastId, Long crowdId, int number) {
//
//        StringBuilder hql=new StringBuilder("from CrowdFundingPublic as crowd where crowd.crowdFunding.id=:crowdId " +
//                " and crowd.status=:status" +
//                " and crowd.id<:lastId" +
//                " and crowd.name like :key"+
//                " order by crowd.id desc");
//        List<CrowdFundingPublic> crowdList = crowdFundingPublicRepository.queryHql(hql.toString(), query -> {
//            query.setParameter("key","%"+key+"%");
//            query.setParameter("status", 1);
//        query.setParameter("crowdId", crowdId);
//        query.setParameter("lastId", lastId);
//        query.setMaxResults(number);
//        });
//        return crowdList;
//    }

    @Override
    public long getCrowdFundingMaxId() {

        StringBuilder hql =new StringBuilder("select max(crowd.id) from CrowdFunding as crowd");
        List<Long> maxIds = crowdFundingPublicRepository.queryHql(hql.toString(), null);
        long maxId = 0L;
        if (maxIds.size()>0&&maxIds.get(0)!=null) {
            maxId = maxIds.get(0);
        }
        return maxId;

    }

    @Override
    public List<CrowdFunding> searchCrowdFundingList(String key,Long lastId, int number) {

        StringBuilder hql = new StringBuilder();
        hql.append("from CrowdFunding as crowd where crowd.id<:lastId " +
                " and crowd.name like :key "+
                " and (crowd.checkStatus =:status0  "+
                " or crowd.checkStatus =:status1)  "+
                " order by crowd.id desc" );
        List<CrowdFunding> crowdList = crowdFundingRepository.queryHql(hql.toString(), query -> {
            query.setParameter("key","%"+key+"%");
        query.setParameter("lastId", lastId);
            query.setParameter("status0", CommonEnum.CheckType.pass);
            query.setParameter("status1", CommonEnum.CheckType.open);
        query.setMaxResults(number);
        });
        return crowdList;

    }

    @Override
    public List<CrowdFundingPublic> getPublicByCrowdId(Long crowdId) {
        StringBuilder hql = new StringBuilder();
        hql.append("from CrowdFundingPublic as public where public.crowdFunding.id=:crowdId " +
                " and public.status=:status " );
                //" order by crowd.id desc" );
        List<CrowdFundingPublic> crowdList = crowdFundingRepository.queryHql(hql.toString(), query -> {
            //query.setParameter("key","%"+key+"%");
            query.setParameter("crowdId", crowdId);
            query.setParameter("status", 1);
           // query.setMaxResults(number);
        });
        return crowdList;
    }

    @Override
    public List<CrowdFundingBooking> getBookingByPublicId(Long crowdId,Long id) {
        StringBuilder hql = new StringBuilder();
        hql.append("from CrowdFundingBooking as booking where booking.crowdFunding.id=:crowdId " +
                " and booking.status=:status " +
                " and booking.crowdFundingPublic.id=:id " );
        //" order by crowd.id desc" );
        List<CrowdFundingBooking> crowdList = crowdFundingRepository.queryHql(hql.toString(), query -> {
            //query.setParameter("key","%"+key+"%");
            query.setParameter("crowdId", crowdId);
            query.setParameter("status", 1);
            query.setParameter("id",id);
            // query.setMaxResults(number);
        });
        return crowdList;
    }

    @Override
    public Page<CrowdFunding> findCrowdFundingPage(CrowdFundingSearchModel searchModel) {
        Sort sort;
        Sort.Direction direction = searchModel.getRaSortType() == 0 ? Sort.Direction.DESC : Sort.Direction.ASC;
        switch (searchModel.getSort()) {
            case 1:
                //浏览量
                sort = new Sort(direction, "view");
                break;
            case 2:
                //转发量
                sort = new Sort(direction, "relayQuantity");
                break;
            default:
                sort = new Sort(direction, "time");
                break;
        }
        return  crowdFundingRepository.findAll(new Specification<CrowdFunding>() {
            @Override
            public Predicate toPredicate(Root<CrowdFunding> root, CriteriaQuery<?> query, CriteriaBuilder cb){
                //Predicate predicate = cb.all();//cb.equal(root.get("manager").as(Manager.class), articleSearchModel.getManager());
                // if (!StringUtils.isEmpty(articleSearchModel.getArticleTitle())){
                Predicate  predicate = cb.and(cb.like(root.get("name").as(String.class),"%"+searchModel.getCrowdFundingTitle()+"%"));
                //}
                if(searchModel.getCrowdFundingType()!=-1){
                    //@ManyToOne
                    //SetJoin<Category,Article> depJoin = root.join(root.getModel().get, JoinType.LEFT);
                    CommonEnum.CrowdFundingType [] crowdFundingTypes={CommonEnum.CrowdFundingType.booking,CommonEnum.CrowdFundingType.cooperation,CommonEnum.CrowdFundingType.subscription};
                    predicate = cb.and(predicate,cb.equal(root.get("crowdFundingType").as(CommonEnum.CrowdFundingType.class), crowdFundingTypes[searchModel.getCrowdFundingType()]));
                }
                if(!StringUtils.isEmpty(searchModel.getStartTime())){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = sdf.parse(searchModel.getStartTime());
                    } catch (ParseException e) {
                        throw  new RuntimeException("字符串转日期失败");
                    }
                    predicate= cb.and(predicate,cb.greaterThanOrEqualTo(root.get("time").as(Date.class),date));
                }
                if(!StringUtils.isEmpty(searchModel.getEndTime())){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = sdf.parse(searchModel.getEndTime());
                    } catch (ParseException e) {
                        throw  new RuntimeException("字符串转日期失败");
                    }
                    predicate= cb.and(predicate,cb.lessThanOrEqualTo(root.get("time").as(Date.class),date));
                }
                return predicate;
            }
        },new PageRequest(searchModel.getPageNoStr(), 20,sort));
    }
}
