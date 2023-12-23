package util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
/*
 * 싱글톤 클래스:
 * 레드팀 포탑 설치 가능한 구역의 목록을 가진다. 
 * */
public class RedArea {
	public static RedArea redArea;
	List<Point> allPoints = new ArrayList<>();
	List<Point> area;
	// 생성자: allPoints 생성 후 이를 이용한 area 설정
	private RedArea() {
		for(int i=0;i<1000;i+=50) {
			for(int j=0;j<1000;j+=50) {
				Point p = new Point(j,i);
				allPoints.add(p);
			}
		}
		area = new ArrayList<>(){
			{
				// 2사분면
				add(allPoints.get(42));
				add(allPoints.get(43));
				add(allPoints.get(44));
				add(allPoints.get(45));
				add(allPoints.get(46));
				add(allPoints.get(47));
				add(allPoints.get(48));
				
				add(allPoints.get(62));
				add(allPoints.get(63));
				add(allPoints.get(64));
				add(allPoints.get(65));
				add(allPoints.get(66));
				add(allPoints.get(67));
				add(allPoints.get(68));
				
				add(allPoints.get(82));
				add(allPoints.get(83));
				add(allPoints.get(84));
				add(allPoints.get(85));
				add(allPoints.get(86));
				add(allPoints.get(87));
				add(allPoints.get(88));
				
				add(allPoints.get(102));
				add(allPoints.get(103));
				add(allPoints.get(104));
				add(allPoints.get(105));
				add(allPoints.get(106));
				add(allPoints.get(107));
				add(allPoints.get(108));
				
				add(allPoints.get(122));
				add(allPoints.get(123));
				add(allPoints.get(124));
				add(allPoints.get(125));
				add(allPoints.get(126));
				add(allPoints.get(127));
				add(allPoints.get(128));
				
				add(allPoints.get(142));
				add(allPoints.get(143));
				add(allPoints.get(144));
				add(allPoints.get(145));
				add(allPoints.get(146));
				add(allPoints.get(147));
				add(allPoints.get(148));
				
				add(allPoints.get(162));
				add(allPoints.get(163));
				add(allPoints.get(164));
				add(allPoints.get(165));
				add(allPoints.get(166));
				add(allPoints.get(167));
				add(allPoints.get(168));
				
				// 3사분면
				add(allPoints.get(222));
				add(allPoints.get(223));
				add(allPoints.get(224));
				add(allPoints.get(225));
				add(allPoints.get(226));
				add(allPoints.get(227));
				add(allPoints.get(228));
				
				add(allPoints.get(242));
				add(allPoints.get(243));
				add(allPoints.get(244));
				add(allPoints.get(245));
				add(allPoints.get(246));
				add(allPoints.get(247));
				add(allPoints.get(248));
				
				add(allPoints.get(262));
				add(allPoints.get(263));
				add(allPoints.get(264));
				add(allPoints.get(265));
				add(allPoints.get(266));
				add(allPoints.get(267));
				add(allPoints.get(268));
				
				add(allPoints.get(282));
				add(allPoints.get(283));
				add(allPoints.get(284));
				add(allPoints.get(285));
				add(allPoints.get(286));
				add(allPoints.get(287));
				add(allPoints.get(288));
				
				add(allPoints.get(302));
				add(allPoints.get(303));
				add(allPoints.get(304));
				add(allPoints.get(305));
				add(allPoints.get(306));
				add(allPoints.get(307));
				add(allPoints.get(308));
				
				add(allPoints.get(322));
				add(allPoints.get(323));
				add(allPoints.get(324));
				add(allPoints.get(325));
				add(allPoints.get(326));
				add(allPoints.get(327));
				add(allPoints.get(328));
				
				add(allPoints.get(342));
				add(allPoints.get(343));
				add(allPoints.get(344));
				add(allPoints.get(345));
				add(allPoints.get(346));
				add(allPoints.get(347));
				add(allPoints.get(348));
				
				// 상단
				add(allPoints.get(2));
				add(allPoints.get(3));
				add(allPoints.get(4));
				add(allPoints.get(5));
				add(allPoints.get(6));
				add(allPoints.get(7));
				add(allPoints.get(8));
				// 좌상단
				add(allPoints.get(40));
				add(allPoints.get(60));
				add(allPoints.get(80));
				add(allPoints.get(100));
				add(allPoints.get(120));
				add(allPoints.get(140));
				add(allPoints.get(160));
				// 좌하단
				add(allPoints.get(220));
				add(allPoints.get(240));
				add(allPoints.get(260));
				add(allPoints.get(280));
				add(allPoints.get(300));
				add(allPoints.get(320));
				add(allPoints.get(340));
				// 하단
				add(allPoints.get(382));
				add(allPoints.get(383));
				add(allPoints.get(384));
				add(allPoints.get(385));
				add(allPoints.get(386));
				add(allPoints.get(387));
				add(allPoints.get(388));
			}
		};
	}
	public static RedArea getInstance() {
        // 인스턴스가 null인 경우에만 새로 생성
        if (redArea == null) {
            redArea = new RedArea();
        }
        return redArea;
    }
	// 레드팀 포탑 설치 가능 구역 목록 반환
	public List<Point> getRedArea() {
		return area;
	}

	
}
