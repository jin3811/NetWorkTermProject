package util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
/*
 * RestrictArea: 제한 구역을 관리하는 싱글톤 클래스
 * 포탑 설치가 불가능한 구역을 정의 (깃발, 스포너, 좌우상단 가운데 두 타일씩)
 */
public class RestrictArea {
	public static RestrictArea restrictArea;
	List<Point> allPoints = new ArrayList<>();
	List<Point> area;

	private RestrictArea() {
		// 전체 모든 영역 Point 생성
		for (int i = 0; i < 1000; i += 50) {
			for (int j = 0; j < 1000; j += 50) {
				Point p = new Point(j, i);
				allPoints.add(p);
			}
		}
		// 제한 구역 설정
		area = new ArrayList<>() {
			{
				// 상단 레드 깃발
				add(allPoints.get(0));
				add(allPoints.get(1));
				add(allPoints.get(20));
				add(allPoints.get(21));
				
				// 상단 가운데 빈 영역
				add(allPoints.get(9));
				add(allPoints.get(10));
				
				// 상단 블루 깃발
				add(allPoints.get(18));
				add(allPoints.get(19));
				add(allPoints.get(38));
				add(allPoints.get(39));
				
				// 가운데 왼쪽 빈 영역
				add(allPoints.get(180));
				add(allPoints.get(181));
				
				// 스포너
				add(allPoints.get(189));
				add(allPoints.get(190));
				add(allPoints.get(209));
				add(allPoints.get(210));
				
				// 가운데 오른쪽 빈 영역
				add(allPoints.get(199));
				add(allPoints.get(219));
				
				// 하단 레드 깃발
				add(allPoints.get(360));
				add(allPoints.get(361));
				add(allPoints.get(380));
				add(allPoints.get(381));
				
				// 하단 가운데 빈 영역
				add(allPoints.get(389));
				add(allPoints.get(390));
				
				// 하단 블루 깃발
				add(allPoints.get(378));
				add(allPoints.get(379));
				add(allPoints.get(398));
				add(allPoints.get(399));
			}
		};
	}

	public static RestrictArea getInstance() {
		// 인스턴스가 null인 경우에만 새로 생성
		if (restrictArea == null) {
			restrictArea = new RestrictArea();
		}
		return restrictArea;
	}

	// 제한 구역 반환
	public List<Point> getRestrictArea() {
		return area;
	}
}
