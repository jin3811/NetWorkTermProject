package util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class RedPath extends Path {
	private static RedPath redPath;

	private RedPath() {
		super();
		Direction1 = new ArrayList<Point>() {
			{
				add(allPoints.get(169));
				add(allPoints.get(149));
				add(allPoints.get(129));
				add(allPoints.get(109));
				add(allPoints.get(89));
				add(allPoints.get(69));
				add(allPoints.get(49));
				add(allPoints.get(29));
				add(allPoints.get(28));
				add(allPoints.get(27));
				add(allPoints.get(26));
				add(allPoints.get(25));
				add(allPoints.get(24));
				add(allPoints.get(23));
				add(allPoints.get(22));
			}
		};
		Direction2 = new ArrayList<Point>() {
			{
				add(allPoints.get(188));
				add(allPoints.get(187));
				add(allPoints.get(186));
				add(allPoints.get(185));
				add(allPoints.get(184));
				add(allPoints.get(183));
				add(allPoints.get(182));
				add(allPoints.get(181));
				
				add(allPoints.get(161));
				add(allPoints.get(141));
				add(allPoints.get(121));
				add(allPoints.get(101));
				add(allPoints.get(81));
				add(allPoints.get(61));
				add(allPoints.get(41));
			}
		};
		Direction3 = new ArrayList<Point>() {
			{
				add(allPoints.get(208));
				add(allPoints.get(207));
				add(allPoints.get(206));
				add(allPoints.get(205));
				add(allPoints.get(204));
				add(allPoints.get(203));
				add(allPoints.get(202));
				add(allPoints.get(201));
				
				add(allPoints.get(221));
				add(allPoints.get(241));
				add(allPoints.get(261));
				add(allPoints.get(281));
				add(allPoints.get(301));
				add(allPoints.get(321));
				add(allPoints.get(341));
			}
		};
		Direction4 = new ArrayList<Point>() {
			{
				add(allPoints.get(229));
				add(allPoints.get(249));
				add(allPoints.get(269));
				add(allPoints.get(289));
				add(allPoints.get(309));
				add(allPoints.get(329));
				add(allPoints.get(349));
				add(allPoints.get(369));
				
				add(allPoints.get(368));
				add(allPoints.get(367));
				add(allPoints.get(366));
				add(allPoints.get(365));
				add(allPoints.get(364));
				add(allPoints.get(363));
				add(allPoints.get(362));
			}
		};
	}
	public static RedPath getInstance() {
        // 인스턴스가 null인 경우에만 새로 생성
        if (redPath == null) {
            redPath = new RedPath();
        }
        return redPath;
    }
}
