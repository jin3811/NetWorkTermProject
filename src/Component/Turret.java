package Component;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import util.TEAM;

public class Turret implements Serializable {
	private static final int MAX_LEVEL = 3;

	private Point point;
	private TEAM team;
	private int atkPower;
	private int level;
	
	public Turret(Point point, TEAM team) {
		this.point = point;
		this.team = team;
		this.level = 0;
		this.atkPower = 50;
	}

	public Point getPoint() {
		return point;
	}

	public TEAM getTeam() {
		return team;
	}
	
	public int getAtkPower() {
		return atkPower;
	}
	
	public int getLevel() {
		return level;
	}
	// 공격 메서드
	public void attack(Monster target) {
		target.setHP(target.HP - atkPower);
	}
	public void upgrade() {
		if(level<MAX_LEVEL) {
			level++;
			atkPower += 10;
		}
	}
	
//	public Rectangle getArea(int turr) {
//
//	}
}
