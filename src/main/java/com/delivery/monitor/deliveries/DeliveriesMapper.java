package com.delivery.monitor.deliveries;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.delivery.monitor.domain.Deliveries;
import com.delivery.monitor.domain.DeliveriesInfo;

@Mapper
public interface DeliveriesMapper {
    void autoInsertDeliveries(Deliveries deliveries);
    int getLastInsertDeliveriesId();
    DeliveriesInfo getDeliveryCoordinates(int order_id);

    List<Deliveries> getDeliveryListByOrderId(int order_id);
}
