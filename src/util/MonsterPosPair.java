package util;

import Component.Monster;

import java.io.Serializable;
import java.util.Objects;

public class MonsterPosPair extends Pair implements Serializable {
    public Monster monster; // 위치

    public MonsterPosPair(int idx, Monster monster) {
        super(idx);
        this.monster = monster;
    }

    public MonsterPosPair(MonsterPosPair mpp) {
        super(mpp.idx);
        this.monster = new Monster(mpp.monster);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonsterPosPair that = (MonsterPosPair) o;
        return Objects.equals(monster, that.monster);
    }

    @Override
    public int hashCode() {
        return Objects.hash(monster);
    }
}