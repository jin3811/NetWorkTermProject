package Component;

import java.awt.Point;
import java.awt.Rectangle;

public class Turret {
	private Point postion;
	private String team;
	private int atkPower;
	
	public Turret(Point position, String team) {
		this.postion = position;
		this.team = team;
		this.atkPower = 10;
	}

	public Point getPostion() {
		return postion;
	}

	public String getTeam() {
		return team;
	}

	public int getAtkPower() {
		return atkPower;
	}
	
	// 공격 메서드
	public void attack() {
		
	}
	
//	public Rectangle getArea(int turr) {
//
//	}
}
