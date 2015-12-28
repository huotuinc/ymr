package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Administrator on 2015/12/11.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class Manager extends Login {

    private String managerField;

    public String getManagerField() {
        return managerField;
    }

    public void setManagerField(String managerField) {
        this.managerField = managerField;
    }


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );
    }
}