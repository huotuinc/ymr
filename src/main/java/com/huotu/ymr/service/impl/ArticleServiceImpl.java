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
        StringBuffer hql = new StringBuffer();
        if (lastId == 0L) {
            hql.append("from Article where category.id=:catId and catId<:lastId");
        } else {
            hql.append("from Article where category.id=:catId");
        }
        Query query = entityManager.createQuery(hql.toString());
        query.setParameter("catId", categoryId);
        query.setParameter("lastId", lastId);
        query.setMaxResults(number);
        List<Article> articleList = query.getResultList();
        return articleList;
    }
}
