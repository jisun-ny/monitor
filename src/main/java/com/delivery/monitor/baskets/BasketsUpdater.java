package com.delivery.monitor.baskets;

import java.util.List;
import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.delivery.monitor.deliveries.DeliveriesMapper;
import com.delivery.monitor.domain.Baskets;
import com.delivery.monitor.domain.Deliveries;
import com.delivery.monitor.domain.Orders;
import com.delivery.monitor.orders.OrdersMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BasketsUpdater {

    private final BasketsMapper basketsMapper;
    private final OrdersMapper ordersMapper;
    private final DeliveriesMapper deliveriesMapper;
    private final Random random = new Random();
    
    private List<Baskets> basketsList; // Store the baskets list to avoid repeated calls
    
    @Scheduled(fixedRate = 10000) // Run every 30 seconds
    @Transactional
    public void updateBasketsData() {
        try {
            List<Orders> ordersList = ordersMapper.getAllOrders();
            basketsList = basketsMapper.getAllBaskets(); // Fetch baskets only once
            
            for (Orders order : ordersList) {
                int orderId = order.getOrder_id();
                List<Deliveries> deliveryList = deliveriesMapper.getDeliveryListByOrderId(orderId);
                
                if (!deliveryList.isEmpty()) {
                    for (Deliveries delivery : deliveryList) {
                        if (order.getLatitude() != delivery.getLatitude() || order.getLongitude() != delivery.getLongitude()) {
                            updateBaskets(); // Update baskets only if coordinates don't match
                        }
                    }
                }
            }
        } catch (Exception e) {
            handleGeneralException(e);
        }
    }

    private void updateBaskets() {
        for (Baskets basket : basketsList) {
            double newTemperature;
            double newHumidity;

            int temperatureChange = random.nextInt(4); 
            int humidityChange = random.nextInt(4);    

            if (temperatureChange == 0) {
                newTemperature = basket.getTemperature() -0.5; 
            } else if (temperatureChange == 1) {
                newTemperature = basket.getTemperature() + 0.5;
            } else {
                newTemperature = basket.getTemperature();
            }

            if (humidityChange == 0) {
                newHumidity = basket.getHumidity() - 0.7; 
            } else if (humidityChange == 1) {
                newHumidity = basket.getHumidity() + 0.7; 
            } else {
                newHumidity = basket.getHumidity(); 
            }

            Baskets updatedBasket = updateBasket(basket.getBasket_id(), newTemperature, newHumidity);

            basketsMapper.updateBasketData(updatedBasket);
        }
    }

    private Baskets updateBasket(int basket_id, double newTemperature, double newHumidity) {
        return Baskets.builder()
                .basket_id(basket_id)
                .temperature(newTemperature)
                .humidity(newHumidity)
                .build();
    }

    private void handleGeneralException(Exception e) {
        log.error("An unexpected error occurred while updating baskets", e);
        throw new RuntimeException("Unexpected error updating baskets", e);
    }
}