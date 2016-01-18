package com.huotu.ymr.entity;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.model.AppOS;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Administrator on 2016/1/12.
 */
@Entity
@Getter
@Setter
@Cacheable(value = false)
public class PushingMessage implements Cloneable {

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public PushingMessage clone() {
        PushingMessage message = new PushingMessage();
        message.setTitle(this.title);
        message.setType(this.type);
        message.setOs(this.os);
        message.setData(this.data);
        message.setDeviceTokens(this.deviceTokens);
        message.setId(this.id);
        message.setUsername(this.username);
        return message;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private CommonEnum.PushMessageType type;
    /**
     * 消息的附件数据
     */
    private String data;
    /**
     * 消息的标题
     */
    private String title;
    @Column(nullable = false)
    private AppOS os;

    /**
     * 接受消息的用户名(只用于app端判断)
     */
    @Column(length = 20)
    private String username;

    @ElementCollection
    private Set<String> deviceTokens;
}
