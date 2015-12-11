package com.huotu.ymr.mallentity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


/**
 * 商城用户表
 * 说明：对应表Hot_UserBaseInfo 实体：UserBaseInfoModel
 * Created by lgh on 2015/8/26.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
@Table(name = "Hot_UserBaseInfo")
public class MallUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UB_UserID")
    private Long id;

    /**
     * 用户名
     */
    @Column(nullable = false, name = "UB_UserLoginName")
    private String username;

    /**
     * 密码
     */
    @Column(name = "UB_UserLoginPassword")
    private String password;
    /**
     * 用户性别 F:女，M：男，未知
     */
    @Column(name = "UB_UserGender")
    private String gender;

    /**
     * 手机号
     */
    @Column(name = "UB_UserMobile")
    private String mobile;

    /**
     * 真实姓名
     */
    @Column(name = "UB_UserRealName")
    private String realName;

    /**
     * 微信头像
     */
    @Column(name = "UB_WxHeadImg")
    private String wxHeadUrl;

    /**
     * 微信昵称
     */
    @Column(name = "UB_WxNickName")
    private String wxNickName;

    /**
     * 注册时间
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "UB_UserRegTime")
    private Date regTime;

    /**
     * 用户类型  0表示普通会员，1表示小伙伴(分销商)，-1表示超级小伙伴
     */
    @Column(name = "UB_UserType")
    private Integer type;

    /**
     * 所属商家
     */
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "UB_CustomerID")
    private MallMerchant merchant;

    /**
     * 用户头像
     */
    @Column(name = "UB_UserFace")
    private String userFace;
}
