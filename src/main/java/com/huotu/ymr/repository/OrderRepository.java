package com.huotu.ymr.repository;

import com.huotu.ymr.entity.Order;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by xhk on 2015/12/10.
 */
public interface OrderRepository extends JpaRepository<Order, String>,ClassicsRepository<Order>,JpaSpecificationExecutor<Order> {

}
