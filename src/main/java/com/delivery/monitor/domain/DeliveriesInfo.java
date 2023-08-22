package com.delivery.monitor.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveriesInfo {
    private double startLatitude;
    private double startLongitude;
    private double arrivalLatitude;
    private double arrivalLongitude;
}
