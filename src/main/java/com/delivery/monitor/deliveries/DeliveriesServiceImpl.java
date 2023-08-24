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

    @Override
    public void getDeliveriesInfos(int order_id) {
        if (processedOrderIds.contains(order_id)) return;
        try {
            Info info = fetchDeliveryInfoFromKakao(deliveriesMapper.getDeliveryCoordinates(order_id));
            coordinateSender.sendCoordinates(info, order_id);
            processedOrderIds.add(order_id);
        } catch (Exception e) {
            log.error("Failed to retrieve delivery information", e);
            throw new RuntimeException("Failed to retrieve delivery information", e);
        }
    }

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

    private String buildUrl(DeliveriesInfo deliveriesInfos) {
        return "https://apis-navi.kakaomobility.com/v1/directions" +
                "?origin=" + deliveriesInfos.getStartLongitude() +
                "," + deliveriesInfos.getStartLatitude() +
                "&destination=" + deliveriesInfos.getArrivalLongitude() +
                "," + deliveriesInfos.getArrivalLatitude() +
                "&waypoints=&priority=RECOMMEND&car_fuel=GASOLINE&car_hipass=false&alternatives=false&road_details=false";
    }
}
