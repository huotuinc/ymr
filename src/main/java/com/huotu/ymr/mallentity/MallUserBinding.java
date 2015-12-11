/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.ymr.mallentity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by luffy on 2015/11/13.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Entity
@Setter
@Getter
@Cacheable(value = false)
@Table(name = "Hot_UserOAuthBinding")
@Description("用户微信关联信息")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MallUserBinding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UOB_ID")
    private Long id;
    @OneToOne
    @JoinColumn(name = "UOB_UB_UserID")
    @Description("关联用户信息")
    private MallUser userInfo;
    @Column(name = "UOB_Identification", length = 50)
    private String openId;
    @Column(name = "UOB_UnionId", length = 50)
    private String unionId;
    @Column(name = "UOB_CustomerId")
    private Long merchantId;


    @Override
    public String toString() {
        return "UserBinding{" +
                "id=" + id +
                ", userInfo=" + userInfo +
                ", openId='" + openId + '\'' +
                ", unionId='" + unionId + '\'' +
                ", merchantId='" + merchantId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MallUserBinding that = (MallUserBinding) o;
        if (getId() != null)
            return Objects.equals(getId(), that.getId());
        return Objects.equals(id, that.id) &&
                Objects.equals(userInfo, that.userInfo) &&
                Objects.equals(openId, that.openId) &&
                Objects.equals(unionId, that.unionId) &&
                Objects.equals(merchantId, that.merchantId);
    }

    @Override
    public int hashCode() {
        if (getId() != null)
            return Objects.hash(getId());
        return Objects.hash(id, userInfo, openId, unionId,merchantId);
    }
}
