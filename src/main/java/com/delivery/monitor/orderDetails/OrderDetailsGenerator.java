package com.delivery.monitor.orderDetails;

import org.springframework.dao.DataAccessException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.delivery.monitor.deliveries.DeliveriesGenerator;
import com.delivery.monitor.domain.OrderDetails;
import com.delivery.monitor.domain.Products;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderDetailsGenerator {

    private final OrderDetailsMapper orderDetailsMapper;
    private final DeliveriesGenerator deliveriesGenerator;

    // 주어진 orderId와 randomProducts 리스트를 사용하여 OrderDetails 객체를 생성하고 DB에 삽입하는 메서드
    public void autoInsertOrderDetails(int orderId, final List<Products> randomProducts) {
        try {
            randomProducts.forEach(product -> insertOrderDetailAndDelivery(orderId, product));
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        }
    }

    // 주어진 orderId와 product를 사용하여 OrderDetails 객체와 Deliveries 객체를 생성하고 DB에 삽입하는 메서드
    private void insertOrderDetailAndDelivery(int orderId, Products product) {
        orderDetailsMapper.autoInsertOrderDetails(createOrderDetails(orderId, product));
        deliveriesGenerator.autoInsertDeliveries(orderId, product.getProduct_id());
    }

    // 주어진 orderId와 product를 사용하여 OrderDetails 객체를 생성하는 메서드
    private OrderDetails createOrderDetails(int orderId, Products product) {
        return OrderDetails.builder()
                .order_id(orderId)
                .product_id(product.getProduct_id())
                .quantity(1)
                .price(product.getPrice())
                .build();
    }

    // 데이터 액세스 예외 처리를 위한 메서드
    private void handleDataAccessException(DataAccessException e) {
        log.error("An error occurred while inserting order details", e);
        throw new RuntimeException("An error occurred while inserting order details", e);
    }
}
