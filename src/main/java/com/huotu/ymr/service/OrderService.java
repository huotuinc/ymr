package com.huotu.ymr.service;

import com.huotu.ymr.entity.Order;

/**
 * Created by xhk on 2015/12/24.
 */

public interface OrderService {
    /**
     * 获取订单数据
     * @param id  项目id
     * @param ownerId  订单人id
     * @return
     */
    Order findOneByIds(Long id, Long ownerId);
}
