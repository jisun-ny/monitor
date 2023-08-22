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
public class Deliveries {
    private int delivery_id;
    private int order_id;
    private String delivery_status;
    private double latitude;
    private double longitude;
    private Timestamp delivery_date;
}
