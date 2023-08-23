package com.delivery.monitor.publisher;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.delivery.monitor.mqtt.MonitorService;
import com.delivery.monitor.path.PathSegmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduledPublisherServiceImpl implements ScheduledPublisherService {

    private final MonitorService monitorService;
    private final PathSegmentService pathSegmentService;

    // 비동기로 실행되며, 10초마다 경로 세그먼트를 보내는 메서드
    @Async("taskExecutor")
    @Scheduled(fixedRate = 10000)
    public void sendPathSegment() {
        if (pathSegmentService.hasSegments()) { // 경로 세그먼트가 있는지 확인
            String segment = pathSegmentService.getNextSegment(); // 다음 세그먼트를 가져옴
            if (segment != null) {
                monitorService.publishMessage(segment); // 세그먼트가 있으면 메시지로 발행
            }
        }
    }
}
