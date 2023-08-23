package com.delivery.monitor.path;

import java.util.List;

import com.delivery.monitor.domain.Info;
import com.delivery.monitor.domain.Info.Coordinate;

public interface PathSegmentService {
    boolean hasSegments();
    void setPathSegments(Info info);
    String getNextSegment();
    List<Coordinate> splitPathInfo(Info info);
}
