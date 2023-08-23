package com.delivery.monitor.orders;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.delivery.monitor.domain.Orders;
import com.delivery.monitor.domain.Products;
import com.delivery.monitor.orderDetails.OrderDetailsGenerator;
import com.delivery.monitor.products.ProductsMapper;
import com.github.javafaker.Faker;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrdersGenerator {

    private final ProductsMapper productsMapper;
    private final OrdersMapper ordersMapper;
    private final OrderDetailsGenerator orderDetailsGenerator;

    // 주기적으로 자동 주문을 생성하고 DB에 삽입하는 메서드
    @Transactional
    @Scheduled(initialDelay = 3000, fixedRate = 180000)
    public void autoInsertOrders() {
        try {
            processOrders();
        } catch (Exception e) {
            handleException(e, "An error occurred while processing orders");
        }
    }

    // 주문을 처리하는 로직을 수행하는 메서드
    private void processOrders() throws IOException {
        Faker faker = new Faker();
        List<Orders> jsonOrderInfo = readOrdersFromJson(getClass().getResourceAsStream("/static/Orders.json"));
        Orders orderInfo = jsonOrderInfo.get(faker.random().nextInt(jsonOrderInfo.size()));
        List<Products> randomProducts = productsMapper.getRandomProducts(faker.random().nextInt(1, 5));
        Orders order = createOrder(randomProducts.size(), randomProducts.stream().mapToInt(Products::getPrice).sum(), orderInfo);
        insertOrders(order, randomProducts);
        orderDetailsGenerator.autoInsertOrderDetails(ordersMapper.getLastInsertOrderId(), randomProducts);
    }

    // 주문 정보를 생성하는 메서드
    private Orders createOrder(int totalOrdered, int totalPrice, Orders orderInfo) {
        return Orders.builder()
                .customer_name(orderInfo.getCustomer_name())
                .quantity_ordered(totalOrdered)
                .total_price(totalPrice)
                .latitude(orderInfo.getLatitude())
                .longitude(orderInfo.getLongitude())
                .build();
    }

    // 주문 정보를 DB에 삽입하고 재고를 감소시키는 메서드
    private void insertOrders(Orders orders, List<Products> randomProducts) {
        try {
            ordersMapper.autoInsertOrders(orders);
            for (Products product : randomProducts) {
                productsMapper.inventoryReduction(product.getProduct_id(), 1);
            }
        } catch (Exception e) {
            log.error("An error occurred while inserting orders", e);
            throw new RuntimeException("An error occurred while inserting orders", e);
        }
    }

    // JSON 파일에서 주문 정보를 읽어오는 메서드
    private List<Orders> readOrdersFromJson(InputStream inputStream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            return new GsonBuilder().create().fromJson(reader, new TypeToken<List<Orders>>() {
            }.getType());
        }
    }

    // 일반적인 예외 처리를 위한 메서드
    private void handleException(Exception e, String errorMessage) {
        log.error(errorMessage, e);
        throw new RuntimeException(errorMessage, e);
    }
}
