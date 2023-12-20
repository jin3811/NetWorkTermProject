package Component;

import util.MonsterPosPair;

import java.awt.Point;

public class Monster {
	int HP;
	Point point;

	public Monster(Point point) {
		this.HP = 100;
		this.point = point;
	}

	public int getHP() {
		return HP;
	}

	public void setHP(int HP) {
		this.HP = HP;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}
}
