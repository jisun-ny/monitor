package com.delivery.monitor.deliveries;

import org.springframework.stereotype.Component;

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
    private final DeliveriesService deliveriesService;

    /**
     * 주어진 주문 ID와 상품 ID를 기반으로 배송 정보를 생성하고 데이터베이스에 삽입합니다.
     *
     * @param orderId   주문 ID
     * @param productId 상품 ID
     */
    public void autoInsertDeliveries(int orderId, int productId) {
        try {
            deliveriesMapper.autoInsertDeliveries(createDeliveries(orderId));
            basketsGenerator.autoInsertBaskets(productId, deliveriesMapper.getLastInsertDeliveriesId());
            deliveriesService.getDeliveriesInfos(orderId);
        } catch (NumberFormatException e) {
            handleNumberFormatException(e);
        } catch (Exception e) {
            handleGeneralException(e);
        }
    }

    /**
     * 주어진 주문 ID를 기반으로 배송 정보 객체를 생성합니다.
     *
     * @param orderId 주문 ID
     * @return 생성된 배송 정보 객체
     */
    private Deliveries createDeliveries(int orderId) {
        return Deliveries.builder()
                .order_id(orderId)
                .delivery_status("배송중")
                .latitude(37.52318)
                .longitude(126.95853)
                .build();
    }

    /**
     * 숫자 형식 변환 예외를 처리하는 메서드입니다.
     *
     * @param e 예외 객체
     */
    private void handleNumberFormatException(NumberFormatException e) {
        log.error("Number format exception occurred while creating deliveries object", e);
        throw new IllegalArgumentException("Invalid number format in deliveries details", e);
    }

    /**
     * 일반 예외를 처리하는 메서드입니다.
     *
     * @param e 예외 객체
     */
    private void handleGeneralException(Exception e) {
        log.error("An unexpected error occurred while inserting deliveries", e);
        throw new RuntimeException("Unexpected error inserting deliveries", e);
    }
}
