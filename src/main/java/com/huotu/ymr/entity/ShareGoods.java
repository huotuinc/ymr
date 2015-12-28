package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 爱分享对应的商城商品
 * Created by lgh on 2015/12/9.
 */

@Entity
@Getter
@Setter
@Cacheable(value = false)
public class ShareGoods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Share share;

    /**
     * 商品Id
     */
    private Long goodsId;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 图片
     */
    private String pictureUrl;
    /**
     * 会员价(购买的价格)
     */
    private Double price;
    /**
     * 原价
     */
    private Double originalPrice;

    /**
     * 积分
     */
    private Integer integral;
}
