package com.delivery.monitor.path;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.delivery.monitor.domain.Info;
import com.delivery.monitor.domain.Info.Coordinate;

@Service
public class PathSegmentServiceImpl implements PathSegmentService {

    // 경로의 위도와 경도 쌍을 저장할 리스트
    private List<Coordinate> pathCoordinates = new ArrayList<>();
    // 현재 처리 중인 좌표의 인덱스
    private int currentCoordinateIndex = 0;
    // 현재 처리 중인 주문 ID
    private int orderId;

    @Override
    public boolean hasSegments() {
        return !pathCoordinates.isEmpty(); // 좌표가 있으면 true, 없으면 false 반환
    }

    @Override
    public void setPathSegments(Info info, int order_id) {
        this.pathCoordinates = splitPathInfo(info); // 경로 정보에서 좌표를 분리하여 저장
        this.currentCoordinateIndex = 0; // 현재 인덱스 초기화
        this.orderId = order_id; // 주문 ID 저장
    }

    @Override
    public String getNextSegment() {
        if (hasSegments() && currentCoordinateIndex < pathCoordinates.size()) {
            Coordinate coordinate = pathCoordinates.get(currentCoordinateIndex++); // 다음 좌표 쌍을 가져옴
            // 주문 ID와 함께 위도와 경도를 문자열로 반환
            return "order_id:" + orderId + ";" + coordinate.getX() + "," + coordinate.getY();
        }
        return null; // 좌표가 없을 경우 null 반환
    }

    @Override
    public List<Coordinate> splitPathInfo(Info info) {
        List<Coordinate> coordinates = new ArrayList<>(); // 좌표 쌍을 저장할 리스트 생성

        // 경로, 섹션, 도로 순으로 순회하여 좌표를 추출
        for (Info.Route route : info.getRoutes()) {
            for (Info.Section section : route.getSections()) {
                for (Info.Road road : section.getRoads()) {
                    List<Double> vertexes = road.getVertexes();

                    // 위도와 경도 값을 쌍으로 만들어 좌표 리스트에 추가
                    for (int i = 0; i + 1 < vertexes.size(); i += 2) {
                        double latitude = vertexes.get(i);
                        double longitude = vertexes.get(i + 1);
                        coordinates.add(new Info.Coordinate(longitude, latitude)); // 좌표 쌍 추가
                    }
                }
            }
        }
        return coordinates; // 추출한 좌표 리스트 반환
    }
}

