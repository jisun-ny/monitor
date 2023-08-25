package com.delivery.monitor.deliveries;

import com.delivery.monitor.domain.DeliveriesInfo;
import com.delivery.monitor.domain.Info;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveriesServiceImpl implements DeliveriesService {

    private final HashSet<Integer> processedOrderIds = new HashSet<>();
    private final DeliveriesMapper deliveriesMapper;
    private final CoordinateSender coordinateSender;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    /**
     * 주어진 order_id에 대한 배달 정보를 가져와 처리하는 메서드.
     * 이미 처리된 주문 ID인 경우, 추가 처리 없이 반환함.
     * 
     * @param order_id 주문 ID
     */
    @Override
    public void getDeliveriesInfos(int order_id) {
        if (processedOrderIds.contains(order_id))
            return;
        try {
            Info info = fetchDeliveryInfoFromKakao(deliveriesMapper.getDeliveryCoordinates(order_id));
            coordinateSender.sendCoordinates(info, order_id);
            processedOrderIds.add(order_id);
        } catch (Exception e) {
            log.error("Failed to retrieve delivery information", e);
            throw new RuntimeException("Failed to retrieve delivery information", e);
        }
    }

    /**
     * Kakao API를 사용하여 배달 정보를 가져오는 메서드.
     *
     * @param deliveriesInfos 배달 정보
     * @return Info Kakao API에서 가져온 배달 정보
     */
    private Info fetchDeliveryInfoFromKakao(DeliveriesInfo deliveriesInfos) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        ResponseEntity<String> response = new RestTemplate().exchange(buildUrl(deliveriesInfos), HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("API request failed: {}", response.getStatusCode());
            throw new RuntimeException("API request failed: " + response.getStatusCode());
        }
        String json = response.getBody();
        if (json == null) {
            log.error("No response data");
            throw new RuntimeException("No response data");
        }
        return new Gson().fromJson(json, Info.class);
    }

    /**
     * 배달 정보를 기반으로 Kakao API에 요청할 URL을 생성하는 메서드.
     *
     * @param deliveriesInfos 배달 정보
     * @return String 생성된 URL
     */
    private String buildUrl(DeliveriesInfo deliveriesInfos) {
        return "https://apis-navi.kakaomobility.com/v1/directions" +
                "?origin=" + deliveriesInfos.getStartLongitude() +
                "," + deliveriesInfos.getStartLatitude() +
                "&destination=" + deliveriesInfos.getArrivalLongitude() +
                "," + deliveriesInfos.getArrivalLatitude() +
                "&waypoints=&priority=RECOMMEND&car_fuel=GASOLINE&car_hipass=false&alternatives=false&road_details=false";
    }
}
