package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 栏目分类
 * 例如：自传故事 学院介绍 美容知识
 * Created by lgh on 2015/11/26.
 */

@Entity
@Getter
@Setter
@Cacheable(value = false)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 分类名称
     */
    @Column(length = 10)
    private String name;
    /**
     * 图片
     */
    @Column(length = 200)
    private String picture;
    /**
     * 排序
     * 按升序排列
     */
    private Integer sort;
}
