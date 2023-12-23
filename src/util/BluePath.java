package util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
/*
 * 싱글톤 클래스:
 * 블루팀 몬스터의 이동 경로를 정의
 * 몬스터가 맵에서 이동할 수 있는 네가지 경로를 관리
 * Path를 상속 받음
 */
public class BluePath extends Path {
	private static BluePath bluePath;
	// 생성자: 블루팀 몬스터 이동 경로 초기화
	private BluePath() {
		super();
		Direction1 = new ArrayList<Point>() {
			{
				add(allPoints.get(170));
				add(allPoints.get(150));
				add(allPoints.get(130));
				add(allPoints.get(110));
				add(allPoints.get(90));
				add(allPoints.get(70));
				add(allPoints.get(50));
				add(allPoints.get(30));
				add(allPoints.get(31));
				add(allPoints.get(32));
				add(allPoints.get(33));
				add(allPoints.get(34));
				add(allPoints.get(35));
				add(allPoints.get(36));
				add(allPoints.get(37));
			}
		};
		Direction2 = new ArrayList<Point>() {
			{
				add(allPoints.get(191));
				add(allPoints.get(192));
				add(allPoints.get(193));
				add(allPoints.get(194));
				add(allPoints.get(195));
				add(allPoints.get(196));
				add(allPoints.get(197));
				add(allPoints.get(198));
				add(allPoints.get(178));
				add(allPoints.get(158));
				add(allPoints.get(138));
				add(allPoints.get(118));
				add(allPoints.get(98));
				add(allPoints.get(78));
				add(allPoints.get(58));
			}
		};
		Direction3 = new ArrayList<Point>() {
			{
				add(allPoints.get(211));
				add(allPoints.get(212));
				add(allPoints.get(213));
				add(allPoints.get(214));
				add(allPoints.get(215));
				add(allPoints.get(216));
				add(allPoints.get(217));
				add(allPoints.get(218));
				
				add(allPoints.get(238));
				add(allPoints.get(258));
				add(allPoints.get(278));
				add(allPoints.get(298));
				add(allPoints.get(318));
				add(allPoints.get(338));
				add(allPoints.get(358));
			}
		};
		Direction4 = new ArrayList<Point>() {
			{
				add(allPoints.get(230));
				add(allPoints.get(250));
				add(allPoints.get(270));
				add(allPoints.get(290));
				add(allPoints.get(310));
				add(allPoints.get(330));
				add(allPoints.get(350));
				add(allPoints.get(370));
				
				add(allPoints.get(371));
				add(allPoints.get(372));
				add(allPoints.get(373));
				add(allPoints.get(374));
				add(allPoints.get(375));
				add(allPoints.get(376));
				add(allPoints.get(377));
			}
		};
	}
	public static BluePath getInstance() {
        // 인스턴스가 null인 경우에만 새로 생성
        if (bluePath == null) {
            bluePath = new BluePath();
        }
        return bluePath;
    }
}
