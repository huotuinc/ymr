package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lgh on 2015/12/9.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class ConfigAppVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 8)
    private String versionNo;

    @Column(length = 200)
    private String sourceFileUrl;

    @Column(length = 200)
    private String differenceFileUrl;

    private boolean bigError;

    @Column(length = 32)
    private String md5value;

    @Temporal(TemporalType.DATE)
    private Date updateTime;

    @Column(length = 500)
    private String description;
}
