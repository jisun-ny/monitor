package com.delivery.monitor.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Info {
    private List<Route> routes; // 전체 경로 정보

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Route {
        private int result_code; // 결과 코드
        private String result_msg; // 결과 메시지
        private Summary summary; // 경로 요약 정보
        private List<Section> sections; // 경로의 섹션 정보
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private Coordinate origin; // 시작점 좌표
        private Coordinate destination; // 도착점 좌표
        private List<Coordinate> waypoints; // 경유지 좌표
        private double duration; // 전체 소요 시간 (초 단위)
        // 추가적인 요약 정보 필드가 필요한 경우 여기에 추가
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Section {
        private int distance; // 섹션 거리
        private int duration; // 섹션 소요 시간
        private List<Road> roads; // 섹션 내 도로 정보
        // 추가적인 섹션 정보 필드가 필요한 경우 여기에 추가
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Road {
        private String name; // 도로 이름
        private int distance; // 도로 거리
        private int duration; // 도로 소요 시간
        private List<Double> vertexes; // 위도와 경도 값이 연속적으로 나열된 리스트 (홀수 인덱스: 위도, 짝수 인덱스: 경도)
        // 추가적인 도로 정보 필드가 필요한 경우 여기에 추가
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordinate {
        private double x; // 경도 (longitude)
        private double y; // 위도 (latitude)
    }
}
