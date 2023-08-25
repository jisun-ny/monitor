package com.delivery.monitor.deliveries;

import com.delivery.monitor.domain.Info;
import com.delivery.monitor.domain.Info.Route;
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
        Route route = info.getRoutes().get(0);
        List<Info.Coordinate> coordinates = extractCoordinates(route);
        long totalDeliveryTimeInMillis = (long) (route.getSummary().getDuration() * 1000);
        long delayPerCoordinate = totalDeliveryTimeInMillis / coordinates.size();

        AtomicReference<ScheduledFuture<?>> scheduledFutureRef = new AtomicReference<>();

        Runnable task = new Runnable() {
            private int index = 0;

            @Override
            public void run() {
                if (index < coordinates.size()) {
                    messageService.sendMessage(buildMessage(coordinates.get(index), order_id));
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
        return order_id + ", " + coordinate.getY() + ", " + coordinate.getX(); // 순서를 맞춰서 반환
    }

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
