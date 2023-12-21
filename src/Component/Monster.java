package Component;

import util.MonsterPosPair;

import java.awt.Point;
import java.io.Serializable;
import java.util.Objects;

public class Monster implements Serializable {
	int HP;
	Point point;

	public Monster(Point point) {
		this.HP = 100;
		this.point = point;
	}

	public Monster(Monster m) {
		this.HP = m.HP;
		this.point = new Point(m.point);
	}

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
