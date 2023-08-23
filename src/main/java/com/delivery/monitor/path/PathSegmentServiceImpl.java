package com.delivery.monitor.path;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.delivery.monitor.domain.Info;
import com.delivery.monitor.domain.Info.Coordinate;

@Service
public class PathSegmentServiceImpl implements PathSegmentService {

    // 위도와 경도의 쌍을 저장할 리스트
    private List<Coordinate> pathCoordinates = new ArrayList<>();
    // 현재 처리할 좌표의 인덱스
    private int currentCoordinateIndex = 0;

    @Override
    public boolean hasSegments() {
        return !pathCoordinates.isEmpty(); // 좌표가 있는지 확인
    }

    @Override
    public void setPathSegments(Info info) {
        this.pathCoordinates = splitPathInfo(info); // 좌표를 분리하여 리스트에 저장
        this.currentCoordinateIndex = 0; // 현재 인덱스를 초기화
    }

    @Override
    public String getNextSegment() {
        if (hasSegments() && currentCoordinateIndex < pathCoordinates.size()) {
            Coordinate coordinate = pathCoordinates.get(currentCoordinateIndex++); // 다음 좌표를 가져옴
            return coordinate.getX() + "," + coordinate.getY(); // 위도와 경도를 문자열로 반환
        }
        return null; // 좌표가 없을 경우 null 반환
    }

    @Override
    public List<Coordinate> splitPathInfo(Info info) {
        List<Coordinate> coordinates = new ArrayList<>(); // 좌표를 저장할 리스트

        // 경로, 섹션, 도로를 순회하면서 위도와 경도의 쌍을 추출
        for (Info.Route route : info.getRoutes()) {
            for (Info.Section section : route.getSections()) {
                for (Info.Road road : section.getRoads()) {
                    List<Double> vertexes = road.getVertexes();

                    // 연속된 위도와 경도 값을 쌍으로 만들어 리스트에 추가
                    for (int i = 0; i + 1 < vertexes.size(); i += 2) {
                        double latitude = vertexes.get(i);
                        double longitude = vertexes.get(i + 1);
                        coordinates.add(new Info.Coordinate(longitude, latitude)); // 쌍을 리스트에 추가
                    }
                }
            }
        }
        return coordinates; // 좌표 리스트 반환
    }
}
