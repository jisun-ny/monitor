package com.delivery.monitor.deliveries;

import com.delivery.monitor.domain.Info;
import com.delivery.monitor.domain.Info.Section;
import com.delivery.monitor.message.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoordinateSender {

    private final MessageService messageService;
    private final ScheduledExecutorService executor;

    public void sendCoordinates(Info info, int order_id) {
        List<Info.Coordinate> coordinates = extractCoordinates(info);

        // 전체 경로의 소요 시간 (초 단위)를 밀리초로 변환
        long totalDeliveryTimeInMillis = (long) (info.getRoutes().get(0).getSummary().getDuration() * 1000);

        // 각 좌표에 할당된 시간 계산
        long delayPerCoordinate = totalDeliveryTimeInMillis / coordinates.size();

        AtomicReference<ScheduledFuture<?>> scheduledFutureRef = new AtomicReference<>();

        Runnable task = new Runnable() {
            private int index = 0;

            @Override
            public void run() {
                if (index < coordinates.size()) {
                    Info.Coordinate coordinate = coordinates.get(index);
                    String message = buildMessage(coordinate, order_id);
                    messageService.sendMessage(message);
                    index++;
                } else {
                    ScheduledFuture<?> scheduledFuture = scheduledFutureRef.get();
                    if (scheduledFuture != null) {
                        scheduledFuture.cancel(false);
                    }
                }
            }
        };

        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(task, 0, delayPerCoordinate,
                TimeUnit.MILLISECONDS);
        scheduledFutureRef.set(scheduledFuture);
    }

    private String buildMessage(Info.Coordinate coordinate, int order_id) {
        return "{\"order_id\": " + order_id + ", \"latitude\": " + coordinate.getX() + ", \"longitude\": "
                + coordinate.getY() + "}";
    }

    private List<Info.Coordinate> extractCoordinates(Info info) {
        List<Info.Coordinate> coordinates = new ArrayList<>();

        // 시작점 좌표 추가
        coordinates.add(info.getRoutes().get(0).getSummary().getOrigin());

        // 각 섹션의 도로별 좌표 추가
        for (Section section : info.getRoutes().get(0).getSections()) {
            for (Info.Road road : section.getRoads()) {
                for (int i = 0; i < road.getVertexes().size(); i += 2) {
                    double latitude = road.getVertexes().get(i);
                    double longitude = road.getVertexes().get(i + 1);
                    coordinates.add(new Info.Coordinate(longitude, latitude));
                }
            }
        }

        // 도착점 좌표 추가
        coordinates.add(info.getRoutes().get(0).getSummary().getDestination());
        return coordinates;
    }
}
