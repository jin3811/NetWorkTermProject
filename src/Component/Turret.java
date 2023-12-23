package Component;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import util.TEAM;
/**
 * Turret 클래스:
 * 위치(Point), 팀(TEAM), 공격력(atkPower), 레벨(level)을 가진다.
 */
public class Turret implements Serializable {
	private static final int MAX_LEVEL = 3;

	private Point point; // 위치
	private TEAM team; // 팀
	private int atkPower; // 공격력
	private int level; // 레벨

	// 생성자: 위치와 팀 입력받아 새로운 터렛 생성
	public Turret(Point point, TEAM team) {
		this.point = point;
		this.team = team;
		this.level = 0;
		this.atkPower = 0;
	}
	
	// getter 메소드들
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

	// 공격 메소드: 몬스터 체력을 포탑 공격력만큼 감소
	public void attack(Monster target) {
		target.setHP(target.HP - atkPower);
	}

	// 포탑 업그레이드 메소드: 포탑 레벨 1증가, 레벨에 따른 공격력 설정
	public void upgrade() {
		if (level < MAX_LEVEL) {
			 level++;
		        switch (level) {
		            case 1:
		                atkPower = 6;
		                break;
		            case 2:
		                atkPower = 8;
		                break;
		            case 3:
		                atkPower = 12;
		                break;
		        }
		}
	}

}
