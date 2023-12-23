package util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 몬스터의 이동 경로를 정의하는 추상 클래스
 * 몬스터가 게임 맵에서 이동할 수 있는 네가지 경로 지정
 */
public abstract class Path {
    protected List<Point> allPoints = new ArrayList<>();
    protected List<Point> Direction1;
    protected List<Point> Direction2;
    protected List<Point> Direction3;
    protected List<Point> Direction4;
    
    // 생성자: 게임 맵의 모든 포인트 초기화
    protected Path() {
        for(int i=0;i<1000;i+=50) {
            for(int j=0;j<1000;j+=50) {
                Point p = new Point(j,i);
                allPoints.add(p);
            }
        }
    }

    // 각 방향의 이동 경로 반환 메소드
    public List<Point> getDirection1() {
        return Direction1;
    }

    public List<Point> getDirection2() {
        return Direction2;
    }

    public List<Point> getDirection3() {
        return Direction3;
    }

    public List<Point> getDirection4() {
        return Direction4;
    }
}
