package com.huotu.ymr.service.impl;

import com.huotu.ymr.common.CommonEnum;
import com.huotu.ymr.entity.Order;
import com.huotu.ymr.repository.OrderRepository;
import com.huotu.ymr.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xhk on 2015/12/24.
 */
@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    OrderRepository orderRepository;

    @Override
    public Order findOneByIds(Long id, Long ownerId) {
        StringBuilder hql = new StringBuilder();
        hql.append("from Order as order where order.user.id=:ownerId and order.crowdFunding.id=:id and payType=:payType");
        List<Order> list = orderRepository.queryHql(hql.toString(), query -> {
            query.setParameter("ownerId", ownerId);
            query.setParameter("id", id);
            query.setParameter("payType", CommonEnum.PayType.payed);
        });
        if(list.size()==0){
            return null;
        }else {
            return list.get(0);
        }
    }

}
