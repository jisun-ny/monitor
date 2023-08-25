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

    /**
     * 데이터베이스의 모든 테이블의 데이터를 삭제하고, 자동 증가 값을 재설정하는 메서드
     * 테이블의 내용과 자동 증가 ID를 초기화하여 데이터베이스를 초기 상태로 되돌림
     *
     * 주요 동작:
     * 1. 주문 상세, 장바구니, 리콜, 배송, 주문, 제품, 관리자 테이블의 데이터 삭제
     * 2. 위 테이블의 자동 증가 값 초기화
     *
     * @throws RuntimeException 데이터베이스 재설정 실패시
     */
    @Transactional
    public void resetDatabase() {
        try {
            log.info("Delete and auto increment reset");

            tableMapper.deleteBaskets();
            tableMapper.deleteRecalls();
            tableMapper.deleteOrderDetails();
            tableMapper.deleteDeliveries();
            tableMapper.deleteOrders();
            tableMapper.deleteProducts();
            tableMapper.deleteAdmins();

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
