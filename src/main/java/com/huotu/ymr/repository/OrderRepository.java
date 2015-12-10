package com.huotu.ymr.repository;

import com.huotu.ymr.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by xhk on 2015/12/10.
 */
public interface OrderRepository extends JpaRepository<Order, String> {
}
