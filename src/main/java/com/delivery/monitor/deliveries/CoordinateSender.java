package com.delivery.monitor.deliveries;

import com.delivery.monitor.domain.Info;
import com.delivery.monitor.domain.Info.Route;
import com.delivery.monitor.domain.Info.Section;
import com.delivery.monitor.message.MessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoordinateSender {

    private final MessageService messageService;
    private final ScheduledExecutorService executor;

    /**
     * 좌표를 보내는 메서드.
     * 
     * @param info     경로 정보
     * @param order_id 주문 ID
     */
    public void sendCoordinates(Info info, int order_id) {
        try {
            Route route = validateInfoAndGetRoute(info);
            List<Info.Coordinate> coordinates = extractCoordinates(route);
            scheduleCoordinateSending(coordinates, route.getSummary().getDuration(), order_id);
        } catch (Exception e) {
            log.error("Failed to send coordinates for order_id: " + order_id, e);
        }
    }

    /**
     * 경로 정보의 유효성을 검사하고 Route 객체를 반환.
     * 
     * @param info 경로 정보
     * @return Route 객체
     */
    private Route validateInfoAndGetRoute(Info info) {
        if (info == null || info.getRoutes() == null || info.getRoutes().isEmpty()) {
            throw new IllegalArgumentException("Invalid Info object");
        }
        return info.getRoutes().get(0);
    }

    /**
     * 좌표를 일정 간격으로 보내기 위해 스케줄링.
     * 
     * @param coordinates       좌표 리스트
     * @param durationInSeconds 전체 배송 시간 (초)
     * @param order_id          주문 ID
     */
    private void scheduleCoordinateSending(List<Info.Coordinate> coordinates, double durationInSeconds, int order_id) {
        long totalDeliveryTimeInMillis = (long) (durationInSeconds * 1000);
        long delayPerCoordinate = totalDeliveryTimeInMillis / coordinates.size();
        AtomicReference<ScheduledFuture<?>> scheduledFutureRef = new AtomicReference<>();

        Runnable task = createTask(coordinates, order_id, scheduledFutureRef);
        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(task, 0, delayPerCoordinate,
                TimeUnit.MILLISECONDS);
        scheduledFutureRef.set(scheduledFuture);
    }

    /**
     * 스케줄링할 작업을 생성.
     * 
     * @param coordinates        좌표 리스트
     * @param order_id           주문 ID
     * @param scheduledFutureRef 작업 참조 변수
     * @return Runnable 작업
     */
    private Runnable createTask(List<Info.Coordinate> coordinates, int order_id,
            AtomicReference<ScheduledFuture<?>> scheduledFutureRef) {
        return new Runnable() {
            private int index = 0;

            @Override
            public void run() {
                try {
                    if (index < coordinates.size()) {
                        messageService.sendMessage(buildMessage(coordinates.get(index), order_id));
                        index++;
                    } else {
                        cancelScheduledFuture(scheduledFutureRef);
                    }
                } catch (Exception e) {
                    log.error("Failed to process coordinate for order_id: " + order_id, e);
                    cancelScheduledFuture(scheduledFutureRef);
                }
            }
        };
    }

    /**
     * 스케줄링 작업 취소.
     * 
     * @param scheduledFutureRef 작업 참조 변수
     */
    private void cancelScheduledFuture(AtomicReference<ScheduledFuture<?>> scheduledFutureRef) {
        ScheduledFuture<?> scheduledFuture = scheduledFutureRef.get();
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

    /**
     * 메시지 생성.
     *
     * @param coordinate 좌표
     * @param order_id   주문 ID
     * @return 생성된 메시지 문자열 (일반 문자열)
     */
    private String buildMessage(Info.Coordinate coordinate, int order_id) {
        try {
            StringBuilder message = new StringBuilder();
            message.append(order_id)
                    .append(", ").append(coordinate.getY())
                    .append(", ").append(coordinate.getX());
            return message.toString();
        } catch (Exception e) {
            log.error("Error occurred while building the message", e);
            return null;
        }
    }

    /**
     * 경로에서 좌표를 추출.
     * 
     * @param route 경로 정보
     * @return 좌표 리스트
     */
    private List<Info.Coordinate> extractCoordinates(Route route) {
        List<Info.Coordinate> coordinates = new ArrayList<>();
        coordinates.add(route.getSummary().getOrigin());
        for (Section section : route.getSections()) {
            for (Info.Road road : section.getRoads()) {
                for (int i = 0; i < road.getVertexes().size(); i += 2) {
                    double latitude = road.getVertexes().get(i);
                    double longitude = road.getVertexes().get(i + 1);
                    coordinates.add(new Info.Coordinate(latitude, longitude));
                }
            }
        }
        coordinates.add(route.getSummary().getDestination());
        return coordinates;
    }
}
