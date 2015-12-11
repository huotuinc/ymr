package com.huotu.ymr.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lgh on 2015/12/10.
 */

@Entity
@Getter
@Setter
@Cacheable(value = false)
public class MessageToUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Message message;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private User user;

    private boolean readed;
    private boolean deleted;
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedTime;
}
