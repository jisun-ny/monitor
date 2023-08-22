package com.delivery.monitor.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Baskets {
    private int basket_id;
    private int product_id;
    private int delivery_id;
    private BigDecimal temperature;
    private BigDecimal humidity;
}
