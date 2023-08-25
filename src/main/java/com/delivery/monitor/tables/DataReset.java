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

            // Delete dependent tables first
            tableMapper.deleteBaskets();
            tableMapper.deleteRecalls();
            tableMapper.deleteOrderDetails();
            tableMapper.deleteDeliveries();
            tableMapper.deleteOrders();
            tableMapper.deleteProducts();
            tableMapper.deleteAdmins();

            // Reset auto increment values
            tableMapper.resetAdminsAutoIncrement();
            tableMapper.resetProductsAutoIncrement();
            tableMapper.resetOrdersAutoIncrement();
            tableMapper.resetDeliveriesAutoIncrement();
            tableMapper.resetRecallsAutoIncrement();
            tableMapper.resetBasketsAutoIncrement();
            tableMapper.resetOrderDetailsAutoIncrement();

            log.info("reset complete");
        } catch (Exception e) {
            log.error("Database reset failed", e);
            throw new RuntimeException("Database reset failed", e);
        }
    }
}
