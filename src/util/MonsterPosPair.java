package util;

import Component.Monster;

import java.io.Serializable;

public class MonsterPosPair extends Pair implements Serializable {
    public Monster monster; // 위치

    public MonsterPosPair(int idx, Monster monster) {
        super(idx);
        this.monster = monster;
    }
}