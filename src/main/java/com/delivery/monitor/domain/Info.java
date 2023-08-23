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
        private double duration; // 전체 소요 시간 (초 단위)
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Section {
        private List<Road> roads; // 섹션 내 도로 정보
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Road {
        private List<Double> vertexes; // 위도와 경도 값이 연속적으로 나열된 리스트 (홀수 인덱스: 위도, 짝수 인덱스: 경도)
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
