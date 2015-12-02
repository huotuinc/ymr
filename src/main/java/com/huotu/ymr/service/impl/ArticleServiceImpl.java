package com.huotu.ymr.service.impl;

import com.huotu.ymr.entity.Article;
import com.huotu.ymr.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by xhk on 2015/12/1.
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Article> findArticleListFromlastIdWithNumber(Integer categoryId, Long lastId,int number) {

        StringBuilder hql = new StringBuilder();
        hql.append("from Article as art where art.category.id=:catId and art.id<:lastId order by art.id desc");
        Query query = entityManager.createQuery(hql.toString());
        query.setParameter("catId", categoryId);
        query.setParameter("lastId", lastId);
        query.setMaxResults(number);
        List<Article> articleList = query.getResultList();
        return articleList;
    }

    @Override
    public long getMaxId() {
        StringBuilder hql =new StringBuilder("select max(art.id) from Article as art");
        Query query=entityManager.createQuery(hql.toString());
        List<Long> maxIds=query.getResultList();
        long maxId=0L;
        if(maxIds.size()!=0){
            maxId=maxIds.get(0);
        }
        return maxId;
    }
}
