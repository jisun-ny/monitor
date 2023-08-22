package com.delivery.monitor.tables;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataReset {

    private final TableMapper tableMapper;

    @Transactional
    public void resetDatabase() {
        try {
            log.info("Delete and auto increment reset");
            tableMapper.deleteOrderDetails();
            tableMapper.resetOrderDetailsAutoIncrement();
            tableMapper.deleteBaskets();
            tableMapper.resetBasketsAutoIncrement();
            tableMapper.deleteRecalls();
            tableMapper.resetRecallsAutoIncrement();
            tableMapper.deleteDeliveries();
            tableMapper.resetDeliveriesAutoIncrement();
            tableMapper.deleteOrders();
            tableMapper.resetOrdersAutoIncrement();
            tableMapper.deleteProducts();
            tableMapper.resetProductsAutoIncrement();
            tableMapper.deleteAdmins();
            tableMapper.resetAdminsAutoIncrement();
            log.info("reset complete");
        } catch (Exception e) {
            log.error("Database reset failed", e);
            throw new RuntimeException("Database reset failed", e);
        }
    }
}
