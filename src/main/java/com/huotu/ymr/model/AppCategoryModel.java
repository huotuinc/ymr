package com.huotu.ymr.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 分类
 * Created by lgh on 2015/11/26.
 */
@Getter
@Setter
public class AppCategoryModel {
    private Integer pId;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 图片
     */
    private String picture;
}
