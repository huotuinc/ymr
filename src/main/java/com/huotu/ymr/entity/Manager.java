package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

/**
 * Created by Administrator on 2015/12/11.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class Manager {

    private Long id;

    private String name;

    private String password;
}
