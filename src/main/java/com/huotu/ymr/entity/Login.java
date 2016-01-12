package com.huotu.ymr.entity;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by slt on 2015/6/10.
 * Modify by cwb on 2015/7/15
 * 后台可登录用户
 *
 * @author luffy luffy.ja at gmail.com
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"loginName"}))
@Inheritance(strategy = InheritanceType.JOINED)
@Cacheable(value = false)
public abstract class Login implements UserDetails,Serializable {
    private static final long serialVersionUID = -349012453592429794L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 账号名称
     */
    @Column
    private String loginName;
    /**
     * 账号密码
     */
    private String password;
    /**
     * 是否可用
     */
    private boolean enabled = true;

    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginDate;
    /**
     * 上次登录IP
     */
    private String lastLoginIP;


    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    @Override
    public String getUsername() {
        return loginName;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
    }

}
