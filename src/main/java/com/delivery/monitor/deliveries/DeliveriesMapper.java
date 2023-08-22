package com.delivery.monitor.deliveries;

import org.apache.ibatis.annotations.Mapper;

import com.delivery.monitor.domain.Deliveries;
import com.delivery.monitor.domain.DeliveriesInfo;

@Mapper
public interface DeliveriesMapper {
    void autoInsertDeliveries(Deliveries deliveries);
    int getLastInsertDeliveriesId();
    DeliveriesInfo getDeliveryCoordinates(int order_id);
}
