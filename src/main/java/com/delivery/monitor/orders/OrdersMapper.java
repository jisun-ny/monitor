package com.delivery.monitor.orders;

import org.apache.ibatis.annotations.Mapper;

import com.delivery.monitor.domain.Orders;

@Mapper
public interface OrdersMapper {
    void autoInsertOrders(Orders orders);
    int getLastInsertOrderId();
}
