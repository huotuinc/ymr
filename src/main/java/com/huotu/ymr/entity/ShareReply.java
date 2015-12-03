package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 爱分享回复表
 * Created by slt on 2015/12/3.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class ShareReply {
    /**
     * id
     */
    @Id
    private Long id;
}
