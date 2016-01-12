package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 抽象具体的设备 todo (暂时未使用该类)
 * @author slt
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class Device {

    /**
     * 唯一识别的imei，即使是不同种类的平台也需要保证生成的imei是完全不同的
     */
    @Id
    @Column(length = 50)
    private String imei;
    /**
     * 用于推送的token
     */
    @Column(length = 64)
    private String pushingToken;
    /**
     * 进入本系统的时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date systemDate;
}