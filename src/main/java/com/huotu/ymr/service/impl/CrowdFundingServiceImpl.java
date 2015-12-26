package com.huotu.ymr.service.impl;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.CrowdFunding;
import com.huotu.ymr.entity.CrowdFundingBooking;
import com.huotu.ymr.entity.CrowdFundingMoneyRange;
import com.huotu.ymr.entity.CrowdFundingPublic;
import com.huotu.ymr.model.searchCondition.CrowdFundingPublicSearchModel;
import com.huotu.ymr.model.searchCondition.CrowdFundingSearchModel;
import com.huotu.ymr.model.searchCondition.DraftSearchModel;
import com.huotu.ymr.repository.CrowdFundingBookingRepository;
import com.huotu.ymr.repository.CrowdFundingMoneyRangeRepository;
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

    @Autowired
    private CrowdFundingBookingRepository crowdFundingBookingRepository;

    @Autowired
    private CrowdFundingMoneyRangeRepository crowdFundingMoneyRangeRepository;
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
                Predicate predicate = cb.notEqual(root.get("checkStatus").as(CommonEnum.CheckType.class), CommonEnum.CheckType.draft);

                 if (!StringUtils.isEmpty(searchModel.getCrowdFundingTitle())){
                  predicate = cb.and(cb.like(root.get("name").as(String.class),"%"+searchModel.getCrowdFundingTitle()+"%"));
                }
                if(searchModel.getCrowdFundingType()!=-1){
                    //@ManyToOne
                    //SetJoin<Category,Article> depJoin = root.join(root.getModel().get, JoinType.LEFT);
                    CommonEnum.CrowdFundingType [] crowdFundingTypes={CommonEnum.CrowdFundingType.booking,CommonEnum.CrowdFundingType.cooperation,CommonEnum.CrowdFundingType.subscription};
                    predicate = cb.and(predicate,cb.equal(root.get("crowdFundingType").as(CommonEnum.CrowdFundingType.class), crowdFundingTypes[searchModel.getCrowdFundingType()-1]));
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

    @Override
    public Page<CrowdFunding> findDraftPage(DraftSearchModel draftSearchModel) {
        Sort sort;
        Sort.Direction direction = draftSearchModel.getRaSortType() == 0 ? Sort.Direction.DESC : Sort.Direction.ASC;
        switch (draftSearchModel.getSort()) {
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
                Predicate predicate = cb.equal(root.get("checkStatus").as(CommonEnum.CheckType.class), CommonEnum.CheckType.draft);
                 if (!StringUtils.isEmpty(draftSearchModel.getDraftTitle())){
                predicate = cb.and(cb.like(root.get("name").as(String.class),"%"+draftSearchModel.getDraftTitle()+"%"));
                }
                if(draftSearchModel.getDraftType()!=-1) {
                    //@ManyToOne
                    //SetJoin<Category,Article> depJoin = root.join(root.getModel().get, JoinType.LEFT);
                    CommonEnum.CrowdFundingType [] crowdFundingTypes={CommonEnum.CrowdFundingType.booking,CommonEnum.CrowdFundingType.cooperation,CommonEnum.CrowdFundingType.subscription};
                    predicate = cb.and(predicate,cb.equal(root.get("crowdFundingType").as(CommonEnum.CrowdFundingType.class), crowdFundingTypes[draftSearchModel.getDraftType()-1]));
                }
                if(!StringUtils.isEmpty(draftSearchModel.getStartTime())){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = sdf.parse(draftSearchModel.getStartTime());
                    } catch (ParseException e) {
                        throw  new RuntimeException("字符串转日期失败");
                    }
                    predicate= cb.and(predicate,cb.greaterThanOrEqualTo(root.get("time").as(Date.class),date));
                }
                if(!StringUtils.isEmpty(draftSearchModel.getEndTime())){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = sdf.parse(draftSearchModel.getEndTime());
                    } catch (ParseException e) {
                        throw  new RuntimeException("字符串转日期失败");
                    }
                    predicate= cb.and(predicate,cb.lessThanOrEqualTo(root.get("time").as(Date.class),date));
                }
                return predicate;
            }
        },new PageRequest(draftSearchModel.getPageNoStr(), 20,sort));
    }

    @Override
    public Page<CrowdFundingPublic> findPublicPage(CrowdFundingPublicSearchModel crowdFundingPublicSearchModel) {
        int size=20;
       if(crowdFundingPublicSearchModel.getPublicType()==1&&crowdFundingPublicSearchModel.getPeopleType()==-1){
            size=5;
        }
        Sort sort;
        Sort.Direction direction = crowdFundingPublicSearchModel.getRaSortType() == 0 ? Sort.Direction.DESC : Sort.Direction.ASC;
        switch (crowdFundingPublicSearchModel.getSort()) {
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
        return  crowdFundingPublicRepository.findAll(new Specification<CrowdFundingPublic>() {
            @Override
            public Predicate toPredicate(Root<CrowdFundingPublic> root, CriteriaQuery<?> query, CriteriaBuilder cb){
                //Predicate predicate = cb.equal(root.get("checkStatus").as(CommonEnum.CheckType.class), CommonEnum.CheckType.draft);
               // if (!StringUtils.isEmpty(crowdFundingPublicSearchModel.getDraftTitle())){
                Predicate predicate = cb.and(cb.like(root.get("name").as(String.class),"%"+crowdFundingPublicSearchModel.getCrowdFundingPublicName()+"%"));
                //}
                    if (!StringUtils.isEmpty(crowdFundingPublicSearchModel.getCrowdFundingId() )) {
                        //@ManyToOne
                        //SetJoin<Category,Article> depJoin = root.join(root.getModel().get, JoinType.LEFT);
                        predicate = cb.and(predicate, cb.equal(root.get("crowdFunding").get("id").as(Long.class), crowdFundingPublicSearchModel.getCrowdFundingId()));
                    }
                if(!StringUtils.isEmpty(crowdFundingPublicSearchModel.getStartTime())){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = sdf.parse(crowdFundingPublicSearchModel.getStartTime());
                    } catch (ParseException e) {
                        throw  new RuntimeException("字符串转日期失败");
                    }
                    predicate= cb.and(predicate,cb.greaterThanOrEqualTo(root.get("time").as(Date.class),date));
                }
                if(!StringUtils.isEmpty(crowdFundingPublicSearchModel.getEndTime())){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = sdf.parse(crowdFundingPublicSearchModel.getEndTime());
                    } catch (ParseException e) {
                        throw  new RuntimeException("字符串转日期失败");
                    }
                    predicate= cb.and(predicate,cb.lessThanOrEqualTo(root.get("time").as(Date.class),date));
                }
                return predicate;
            }
        },new PageRequest(crowdFundingPublicSearchModel.getPageNoStr(), size,sort));
    }

    @Override
    public List<CrowdFundingBooking> findBookingsByPublic(CrowdFundingPublic crowdFundingPublic,Long crowdId) {
//        Sort sort;
//        Sort.Direction direction = crowdFundingPublicSearchModel.getRaSortType() == 0 ? Sort.Direction.DESC : Sort.Direction.ASC;
//        switch (crowdFundingPublicSearchModel.getSort()) {
//            case 1:
//                //浏览量
//                sort = new Sort(direction, "view");
//                break;
//            case 2:
//                //转发量
//                sort = new Sort(direction, "relayQuantity");
//                break;
//            default:
//                sort = new Sort(direction, "time");
//                break;
//        }
//        return  crowdFundingBookingRepository.findAll(new Specification<CrowdFundingBooking>() {
//            @Override
//            public Predicate toPredicate(Root<CrowdFundingBooking> root, CriteriaQuery<?> query, CriteriaBuilder cb){
//                //Predicate predicate = cb.equal(root.get("checkStatus").as(CommonEnum.CheckType.class), CommonEnum.CheckType.draft);
//                // if (!StringUtils.isEmpty(crowdFundingPublicSearchModel.getDraftTitle())){
//                Predicate predicate = cb.and(cb.like(root.get("name").as(String.class),"%"+crowdFundingPublicSearchModel.getCrowdFundingPublicName()+"%"));
//                //}
//                if (!StringUtils.isEmpty(crowdFundingPublicSearchModel.getCrowdFundingId() )) {
//                    //@ManyToOne
//                    //SetJoin<Category,Article> depJoin = root.join(root.getModel().get, JoinType.LEFT);
//                    predicate = cb.and(predicate, cb.equal(root.get("crowdFunding").get("id").as(Long.class), crowdFundingPublicSearchModel.getCrowdFundingId()));
//                }
//                if(!StringUtils.isEmpty(crowdFundingPublicSearchModel.getStartTime())){
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Date date = null;
//                    try {
//                        date = sdf.parse(crowdFundingPublicSearchModel.getStartTime());
//                    } catch (ParseException e) {
//                        throw  new RuntimeException("字符串转日期失败");
//                    }
//                    predicate= cb.and(predicate,cb.greaterThanOrEqualTo(root.get("time").as(Date.class),date));
//                }
//                if(!StringUtils.isEmpty(crowdFundingPublicSearchModel.getEndTime())){
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Date date = null;
//                    try {
//                        date = sdf.parse(crowdFundingPublicSearchModel.getEndTime());
//                    } catch (ParseException e) {
//                        throw  new RuntimeException("字符串转日期失败");
//                    }
//                    predicate= cb.and(predicate,cb.lessThanOrEqualTo(root.get("time").as(Date.class),date));
//                }
//                return predicate;
//            }
//        },new PageRequest(crowdFundingPublicSearchModel.getPageNoStr(), 20,sort));
        int number =20;
        StringBuilder hql = new StringBuilder();
        hql.append("from CrowdFundingBooking as booking where " +
                "  booking.crowdFunding.id=:crowdId "+
                " and booking.crowdFundingPublic.id=:crowdFundingPublic  "+
                " order by booking.id desc" );
        List<CrowdFundingBooking> bookingList = crowdFundingBookingRepository.queryHql(hql.toString(), query -> {
            query.setParameter("crowdId", crowdId);
            query.setParameter("crowdFundingPublic", crowdFundingPublic.getId());
            query.setMaxResults(number);
        });
        return bookingList;
    }

    @Override
    public Page<CrowdFundingBooking> findBookingPages(CrowdFundingPublicSearchModel crowdFundingPublicSearchModel) {

        Sort sort;
        Sort.Direction direction = crowdFundingPublicSearchModel.getRaSortType() == 0 ? Sort.Direction.DESC : Sort.Direction.ASC;
        switch (crowdFundingPublicSearchModel.getSort()) {
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
        return  crowdFundingBookingRepository.findAll(new Specification<CrowdFundingBooking>() {
            @Override
            public Predicate toPredicate(Root<CrowdFundingBooking> root, CriteriaQuery<?> query, CriteriaBuilder cb){
                //Predicate predicate = cb.equal(root.get("checkStatus").as(CommonEnum.CheckType.class), CommonEnum.CheckType.draft);
                // if (!StringUtils.isEmpty(crowdFundingPublicSearchModel.getDraftTitle())){
                Predicate predicate = cb.and(cb.like(root.get("name").as(String.class),"%"+crowdFundingPublicSearchModel.getCrowdFundingPublicName()+"%"));
                //}
                if (!StringUtils.isEmpty(crowdFundingPublicSearchModel.getCrowdFundingId() )) {
                    //@ManyToOne
                    //SetJoin<Category,Article> depJoin = root.join(root.getModel().get, JoinType.LEFT);
                    predicate = cb.and(predicate, cb.equal(root.get("crowdFunding").get("id").as(Long.class), crowdFundingPublicSearchModel.getCrowdFundingId()));
                }
                if(!StringUtils.isEmpty(crowdFundingPublicSearchModel.getStartTime())){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = sdf.parse(crowdFundingPublicSearchModel.getStartTime());
                    } catch (ParseException e) {
                        throw  new RuntimeException("字符串转日期失败");
                    }
                    predicate= cb.and(predicate,cb.greaterThanOrEqualTo(root.get("time").as(Date.class),date));
                }
                if(!StringUtils.isEmpty(crowdFundingPublicSearchModel.getEndTime())){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = sdf.parse(crowdFundingPublicSearchModel.getEndTime());
                    } catch (ParseException e) {
                        throw  new RuntimeException("字符串转日期失败");
                    }
                    predicate= cb.and(predicate,cb.lessThanOrEqualTo(root.get("time").as(Date.class),date));
                }
                return predicate;
            }
        },new PageRequest(crowdFundingPublicSearchModel.getPageNoStr(), 20,sort));
    }

    @Override
    public List<CrowdFundingMoneyRange> findRangesByCrowdFunding(CrowdFunding crowdFunding) {
        StringBuilder hql = new StringBuilder();
        hql.append("from CrowdFundingMoneyRange as range where " +
                "  range.crowdFunding.id=:crowdId ");
        List<CrowdFundingMoneyRange> rangeList = crowdFundingMoneyRangeRepository.queryHql(hql.toString(), query -> {
            query.setParameter("crowdId", crowdFunding.getId());

        });
        return rangeList;
    }

    @Override
    public void deleteRangesByCrowdFunding(CrowdFunding crowdFunding) {
        StringBuilder hql = new StringBuilder();
        hql.append("delete from CrowdFundingMoneyRange as range where " +
                "  range.crowdFunding.id=:crowdId ");
        crowdFundingMoneyRangeRepository.executeHql(hql.toString(), query -> {
            query.setParameter("crowdId", crowdFunding.getId());
        });
    }

    @Override
    public CrowdFundingPublic findPublicByCFAndUserId(Long crowdId, Long userId) {
        StringBuilder hql = new StringBuilder();
        hql.append("from CrowdFundingPublic as public where " +
                "  public.crowdFunding.id=:crowdId " +
                " and public.ownerId=:userId");
        List<CrowdFundingPublic> publicList = crowdFundingPublicRepository.queryHql(hql.toString(), query -> {
            query.setParameter("crowdId", crowdId);
            query.setParameter("userId", userId);
        });
        if(publicList.size()==0){
            return null;
        }else {
            return publicList.get(0);
        }
    }

    @Override
    public CrowdFundingBooking findBookingByCFAndUserId(Long crowdId, Long userId) {
        StringBuilder hql = new StringBuilder();
        hql.append("from CrowdFundingBooking as booking where " +
                "  booking.crowdFunding.id=:crowdId " +
                " and booking.ownerId=:userId");
        List<CrowdFundingBooking> bookingList = crowdFundingBookingRepository.queryHql(hql.toString(), query -> {
            query.setParameter("crowdId", crowdId);
            query.setParameter("userId", userId);
        });
        if(bookingList.size()==0){
            return null;
        }else {
            return bookingList.get(0);
        }
    }
}
