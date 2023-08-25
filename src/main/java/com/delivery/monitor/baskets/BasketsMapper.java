package com.delivery.monitor.baskets;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.delivery.monitor.domain.Baskets;


@Mapper
public interface BasketsMapper {
    void autoInsertBaskets(Baskets baskets);
    List<Baskets> getAllBaskets();
    void updateBasketData(Baskets updatedBasket);

    Baskets getBasketsByOrderId(int order_id);
    
}