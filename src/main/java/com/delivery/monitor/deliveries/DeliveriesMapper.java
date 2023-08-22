package com.delivery.monitor.deliveries;

import org.apache.ibatis.annotations.Mapper;

import com.delivery.monitor.domain.Deliveries;

@Mapper
public interface DeliveriesMapper {
    void autoInsertDeliveries(Deliveries deliveries);
    int getLastInsertDeliveriesId();
}
