package util;

import Component.Monster;

public class MonsterPosPair extends Pair {
    public Monster monster; // 위치

    public MonsterPosPair(int idx, Monster monster) {
        super(idx);
        this.monster = monster;
    }
}