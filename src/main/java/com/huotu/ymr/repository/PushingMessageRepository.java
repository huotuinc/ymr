package com.huotu.ymr.repository;

import com.huotu.ymr.entity.PushingMessage;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator on 2016/1/12.
 */
public interface PushingMessageRepository extends JpaRepository<PushingMessage, Long>,ClassicsRepository<PushingMessage>,JpaSpecificationExecutor<PushingMessage> {

}
