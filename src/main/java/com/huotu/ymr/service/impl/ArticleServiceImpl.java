package com.huotu.ymr.service.impl;

import com.huotu.ymr.entity.Article;
import com.huotu.ymr.model.searchCondition.ArticleSearchModel;
import com.huotu.ymr.repository.ArticleRepository;
import com.huotu.ymr.service.ArticleService;
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
 * Created by xhk on 2015/12/1.
 */
@Service
public class ArticleServiceImpl implements ArticleService {


    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<Article> findArticleListFromLastIdWithNumber(Integer categoryId, Long lastId, int number) {

        StringBuilder hql = new StringBuilder();
        hql.append("from Article as art where art.category.id=:catId and art.id<:lastId order by art.id desc");
        List<Article> list = articleRepository.queryHql(hql.toString(), query -> {
            query.setParameter("catId", categoryId);
            query.setParameter("lastId", lastId);
            query.setMaxResults(number);
        });

        return list;
    }

    @Override
    public long getMaxId() {

        StringBuilder hql = new StringBuilder("select max(art.id) from Article as art");
        List<Long> maxIds = articleRepository.queryHql(hql.toString(), null);
        long maxId = 0L;
        if (maxIds.size()>0&&maxIds.get(0)!=null) {
            maxId = maxIds.get(0);
        }
        return maxId;
    }

    @Override
    public Page<Article> findArticlePage(ArticleSearchModel articleSearchModel) {
        Sort sort;
        Sort.Direction direction = articleSearchModel.getRaSortType() == 0 ? Sort.Direction.DESC : Sort.Direction.ASC;
        switch (articleSearchModel.getSort()) {
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
        return  articleRepository.findAll(new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb){
                //Predicate predicate = cb.all();//cb.equal(root.get("manager").as(Manager.class), articleSearchModel.getManager());
               // if (!StringUtils.isEmpty(articleSearchModel.getArticleTitle())){
                Predicate  predicate = cb.and(cb.like(root.get("title").as(String.class),"%"+articleSearchModel.getArticleTitle()+"%"));
                //}
                String[] category={"公司介绍","自传故事","学院介绍","美容知识"};
                if(articleSearchModel.getArticleType()!=-1){
                    //@ManyToOne
                    //SetJoin<Category,Article> depJoin = root.join(root.getModel().get, JoinType.LEFT);
                    predicate = cb.and(predicate,cb.equal(root.get("category").get("name").as(String.class), category[articleSearchModel.getArticleType() - 1]));
                }
                if(!StringUtils.isEmpty(articleSearchModel.getStartTime())){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = sdf.parse(articleSearchModel.getStartTime());
                    } catch (ParseException e) {
                        throw  new RuntimeException("字符串转日期失败");
                    }
                    predicate= cb.and(predicate,cb.greaterThanOrEqualTo(root.get("time").as(Date.class),date));
                }
                if(!StringUtils.isEmpty(articleSearchModel.getEndTime())){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = sdf.parse(articleSearchModel.getEndTime());
                    } catch (ParseException e) {
                        throw  new RuntimeException("字符串转日期失败");
                    }
                    predicate= cb.and(predicate,cb.lessThanOrEqualTo(root.get("time").as(Date.class),date));
                }
                return predicate;
            }
        },new PageRequest(articleSearchModel.getPageNoStr(), 20,sort));
    }

}
