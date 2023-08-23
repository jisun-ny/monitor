package com.delivery.monitor.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    private int order_id;
    private String customer_name;
    private int quantity_ordered;
    private int total_price;
    private double latitude;
    private double longitude;
    private Timestamp order_date;
    
}