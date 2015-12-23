package com.huotu.ymr.model.mall;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Administrator on 2015/12/23.
 */
@Getter
@Setter
public class CategoryModel {
    /**
     * ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String title;
    /**
     * 二级分类
     */
    private List<CategoryModel> secondCategory;
}
