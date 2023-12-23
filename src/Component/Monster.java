package Component;

import util.MonsterPosPair;

import java.awt.Point;
import java.io.Serializable;
import java.util.Objects;
/*
 * Monster 클래스: 몬스터는 체력(HP)과 위치(Point)를 가진다.
 */
public class Monster implements Serializable {
	int HP; // 체력
	Point point; // 위치

	// 생성자: 위치 입력받아 새 몬스터 객체 생성
	public Monster(Point point) {
		this.HP = 24;
		this.point = point;
	}
	// 복사 생성자: 기존 몬스터 객체 복사 새 몬스터 생성
	public Monster(Monster m) {
		this.HP = m.HP;
		this.point = new Point(m.point);
	}
	// getter 및 setter
	public int getHP() {
		return HP;
	}
	
	public void setHP(int HP) {
		this.HP = HP;
	}
	
	public Point getPoint() {
		return new Point(point);
	}
	
	public void setPoint(Point point) {
		this.point = point;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Monster monster = (Monster) o;
		return HP == monster.HP && Objects.equals(point, monster.point);
	}

	@Override
	public int hashCode() {
		return Objects.hash(HP, point);
	}
}
