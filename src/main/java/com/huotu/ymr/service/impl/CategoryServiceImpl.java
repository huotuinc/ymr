package com.huotu.ymr.service.impl;

import com.huotu.ymr.entity.Category;
import com.huotu.ymr.repository.CategoryRepository;
import com.huotu.ymr.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章类型服务实现类
 * Created by xhk on 2015/12/18.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category getArticleTypeByName(String name) {

        StringBuilder hql = new StringBuilder();
        hql.append("from Category as cat where cat.name=:name");
        List<Category> list = categoryRepository.queryHql(hql.toString(), query -> {
            query.setParameter("name", name);
        });
        return list.get(0);
    }
}
