package com.delivery.monitor.orderDetails;

import org.apache.ibatis.annotations.Mapper;

import com.delivery.monitor.domain.OrderDetails;

@Mapper
public interface OrderDetailsMapper {
    void autoInsertOrderDetails(OrderDetails orderDetails);
}
