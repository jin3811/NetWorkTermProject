package util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class BlueArea {
	public static BlueArea blueArea;
	List<Point> allPoints = new ArrayList<>();
	List<Point> area;
	private BlueArea() {
		for(int i=0;i<1000;i+=50) {
			for(int j=0;j<1000;j+=50) {
				Point p = new Point(j,i);
				allPoints.add(p);
			}
		}
		area = new ArrayList<>(){
			{
				// 1사분면
				add(allPoints.get(51));
				add(allPoints.get(52));
				add(allPoints.get(53));
				add(allPoints.get(54));
				add(allPoints.get(55));
				add(allPoints.get(56));
				add(allPoints.get(57));
				
				add(allPoints.get(71));
				add(allPoints.get(72));
				add(allPoints.get(73));
				add(allPoints.get(74));
				add(allPoints.get(75));
				add(allPoints.get(76));
				add(allPoints.get(77));
				
				add(allPoints.get(91));
				add(allPoints.get(92));
				add(allPoints.get(93));
				add(allPoints.get(94));
				add(allPoints.get(95));
				add(allPoints.get(96));
				add(allPoints.get(97));
				
				add(allPoints.get(111));
				add(allPoints.get(112));
				add(allPoints.get(113));
				add(allPoints.get(114));
				add(allPoints.get(115));
				add(allPoints.get(116));
				add(allPoints.get(117));
			
				add(allPoints.get(131));
				add(allPoints.get(132));
				add(allPoints.get(133));
				add(allPoints.get(134));
				add(allPoints.get(135));
				add(allPoints.get(136));
				add(allPoints.get(137));
				
				add(allPoints.get(151));
				add(allPoints.get(152));
				add(allPoints.get(153));
				add(allPoints.get(154));
				add(allPoints.get(155));
				add(allPoints.get(156));
				add(allPoints.get(157));
				
				add(allPoints.get(171));
				add(allPoints.get(172));
				add(allPoints.get(173));
				add(allPoints.get(174));
				add(allPoints.get(175));
				add(allPoints.get(176));
				add(allPoints.get(177));
				
				
				// 4사분면
				add(allPoints.get(231));
				add(allPoints.get(232));
				add(allPoints.get(233));
				add(allPoints.get(234));
				add(allPoints.get(235));
				add(allPoints.get(236));
				add(allPoints.get(237));
				
				add(allPoints.get(251));
				add(allPoints.get(252));
				add(allPoints.get(253));
				add(allPoints.get(254));
				add(allPoints.get(255));
				add(allPoints.get(256));
				add(allPoints.get(257));
				
				add(allPoints.get(271));
				add(allPoints.get(272));
				add(allPoints.get(273));
				add(allPoints.get(274));
				add(allPoints.get(275));
				add(allPoints.get(276));
				add(allPoints.get(277));
				
				add(allPoints.get(291));
				add(allPoints.get(292));
				add(allPoints.get(293));
				add(allPoints.get(294));
				add(allPoints.get(295));
				add(allPoints.get(296));
				add(allPoints.get(297));
				
				add(allPoints.get(311));
				add(allPoints.get(312));
				add(allPoints.get(313));
				add(allPoints.get(314));
				add(allPoints.get(315));
				add(allPoints.get(316));
				add(allPoints.get(317));
				
				add(allPoints.get(331));
				add(allPoints.get(332));
				add(allPoints.get(333));
				add(allPoints.get(334));
				add(allPoints.get(335));
				add(allPoints.get(336));
				add(allPoints.get(337));
				
				add(allPoints.get(351));
				add(allPoints.get(352));
				add(allPoints.get(353));
				add(allPoints.get(354));
				add(allPoints.get(355));
				add(allPoints.get(356));
				add(allPoints.get(357));
				
				// 상단
				add(allPoints.get(11));
				add(allPoints.get(12));
				add(allPoints.get(13));
				add(allPoints.get(14));
				add(allPoints.get(15));
				add(allPoints.get(16));
				add(allPoints.get(17));
				// 우상단
				add(allPoints.get(59));
				add(allPoints.get(79));
				add(allPoints.get(99));
				add(allPoints.get(119));
				add(allPoints.get(139));
				add(allPoints.get(159));
				add(allPoints.get(179));
				// 우하단
				add(allPoints.get(239));
				add(allPoints.get(259));
				add(allPoints.get(279));
				add(allPoints.get(299));
				add(allPoints.get(319));
				add(allPoints.get(339));
				add(allPoints.get(359));
				// 하단
				add(allPoints.get(391));
				add(allPoints.get(392));
				add(allPoints.get(393));
				add(allPoints.get(394));
				add(allPoints.get(395));
				add(allPoints.get(396));
				add(allPoints.get(397));
			}
		};
	}
	public static BlueArea getInstance() {
        // 인스턴스가 null인 경우에만 새로 생성
        if (blueArea == null) {
            blueArea = new BlueArea();
        }
        return blueArea;
    }
	 public List<Point> getRedArea() {
	        return area;
	    }

	
}
