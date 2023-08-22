package com.delivery.monitor.baskets;

import java.math.BigDecimal;
import java.util.Random;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.delivery.monitor.domain.Baskets;
import com.delivery.monitor.products.ProductsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BasketsGenerator {

    private static final Random random = new Random();

    private final ProductsMapper productsMapper;
    private final BasketsMapper basketsMapper;

    // 범위 내에서 랜덤한 double 값 생성하는 메서드
    private double randomDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    // 주어진 productId와 deliveryId를 기반으로 Baskets 객체를 생성하고 DB에 삽입하는 메서드
    @Transactional
    public void autoInsertBaskets(int productId, int deliveryId) {
        log.info("바구니 등록");
        try {
            double tempMin, tempMax, humidMin, humidMax;
            // 제품 카테고리 별로 온도 및 습도 범위 설정
            switch (productsMapper.getCategoryByProductsId(productId)) {
                case "야채/과일":
                    tempMin = 4; tempMax = 6; humidMin = 58; humidMax = 62; break;
                case "육류":
                    tempMin = 0; tempMax = 4; humidMin = 48; humidMax = 52; break;
                case "수산물":
                    tempMin = -2; tempMax = 2; humidMin = 53; humidMax = 57; break;
                case "유제품":
                    tempMin = 2; tempMax = 6; humidMin = 63; humidMax = 67; break;
                default:
                    tempMin = 2; tempMax = 5; humidMin = 38; humidMax = 42; break;
            }
            
            basketsMapper.autoInsertBaskets(createBasket(productId, deliveryId, tempMin, tempMax, humidMin, humidMax));
        } catch (NumberFormatException e) {
            handleNumberFormatException(e);
        } catch (Exception e) {
            handleGeneralException(e);
        }
    }

    // Baskets 객체를 생성하는 메서드
    private Baskets createBasket(int productId, int deliveryId, double tempMin, double tempMax, double humidMin,
            double humidMax) {
        return Baskets.builder()
                .product_id(productId)
                .delivery_id(deliveryId)
                .temperature(BigDecimal.valueOf(randomDouble(tempMin, tempMax)))
                .humidity(BigDecimal.valueOf(randomDouble(humidMin, humidMax)))
                .build();
    }

    // 숫자 형식 예외 처리를 위한 메서드
    private void handleNumberFormatException(NumberFormatException e) {
        log.error("Number format exception occurred while creating basket object", e);
        throw new IllegalArgumentException("Invalid number format in basket details", e);
    }

    // 일반 예외 처리를 위한 메서드
    private void handleGeneralException(Exception e) {
        log.error("An unexpected error occurred while inserting basket", e);
        throw new RuntimeException("Unexpected error inserting basket", e);
    }
}
