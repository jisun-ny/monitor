package com.delivery.monitor.baskets;

import org.apache.ibatis.annotations.Mapper;

import com.delivery.monitor.domain.Baskets;

@Mapper
public interface BasketsMapper {
    void autoInsertBaskets(Baskets baskets);
}
