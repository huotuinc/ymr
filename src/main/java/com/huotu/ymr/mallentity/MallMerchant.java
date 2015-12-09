package com.huotu.ymr.mallentity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


/**
 * 商家
 * 说明：对应表 Swt_CustomerManage 实体类UserInfoModel
 * Created by lgh on 2015/8/26.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
@Table(name = "Swt_CustomerManage")
public class MallMerchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SC_UserID")
    private Long id;


    /**
     * 登录名
     */
    @Column(name = "SC_UserLoginName")
    private String name;


    /**
     * 密码
     */
    @Column(name = "SC_UserLoginPassword")
    private String password;


    /**
     * 昵称
     */
    @Column(name = "SC_UserNickName")
    private String nickName;


    /**
     * 手机号
     */
    @Column(name = "SC_PhoneNumber")
    private String mobile;


}
