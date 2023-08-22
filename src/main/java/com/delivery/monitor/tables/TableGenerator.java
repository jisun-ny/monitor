package com.delivery.monitor.tables;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TableGenerator {

    private final TableMapper tableMapper;

    @Transactional
    public void resetDatabase() {
        try {
            log.info("테이블 삭제 시작");
            dropTables();
            log.info("테이블 삭제 완료");

            log.info("테이블 생성 시작");
            createTables();
            log.info("테이블 생성 완료");
        } catch (Exception e) {
            log.error("Database reset failed", e);
            throw new RuntimeException("Database reset failed", e);
        }
    }

    private void dropTables() {
        tableMapper.dropOrderDetails();
        tableMapper.dropRecalls();
        tableMapper.dropBaskets();
        tableMapper.dropDeliveries();
        tableMapper.dropOrders();
        tableMapper.dropProducts();
        tableMapper.dropAdmins();
    }

    private void createTables() {
        tableMapper.createAdmins();
        tableMapper.createProducts();
        tableMapper.createOrders();
        tableMapper.createDeliveries();
        tableMapper.createBaskets();
        tableMapper.createRecalls();
        tableMapper.createOrderDetails();
    }
}
