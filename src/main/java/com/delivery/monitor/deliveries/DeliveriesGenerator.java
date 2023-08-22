package com.delivery.monitor.deliveries;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.delivery.monitor.baskets.BasketsGenerator;
import com.delivery.monitor.domain.Deliveries;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveriesGenerator {

    private final DeliveriesMapper deliveriesMapper;
    private final BasketsGenerator basketsGenerator;

    // 주어진 orderId와 productId를 기반으로 Deliveries 객체를 생성하고 DB에 삽입하는 메서드
    @Transactional
    public void autoInsertDeliveries(int orderId, int productId) {
        log.info("배송 등록");
        try {
            deliveriesMapper.autoInsertDeliveries(createDeliveries(orderId));
            basketsGenerator.autoInsertBaskets(productId, deliveriesMapper.getLastInsertDeliveriesId());
        } catch (NumberFormatException e) {
            handleNumberFormatException(e);
        } catch (Exception e) {
            handleGeneralException(e);
        }
    }

    // orderId를 기반으로 Deliveries 객체를 생성하는 메서드
    private Deliveries createDeliveries(int orderId) {
        return Deliveries.builder()
                .order_id(orderId)
                .delivery_status("배송중")
                .latitude(new BigDecimal(37.52318))
                .longitude(new BigDecimal(126.95853))
                .build();
    }

    // 숫자 형식 예외 처리를 위한 메서드
    private void handleNumberFormatException(NumberFormatException e) {
        log.error("Number format exception occurred while creating deliveries object", e);
        throw new IllegalArgumentException("Invalid number format in deliveries details", e);
    }

    // 일반 예외 처리를 위한 메서드
    private void handleGeneralException(Exception e) {
        log.error("An unexpected error occurred while inserting deliveries", e);
        throw new RuntimeException("Unexpected error inserting deliveries", e);
    }
}
