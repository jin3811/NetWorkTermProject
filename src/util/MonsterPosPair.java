package util;

import Component.Monster;

import java.io.Serializable;
import java.util.Objects;
/*
 * Pair 추상 클래스를 상속받는 클래스:
 * Monster와 idx 저장하는 클래스
 */
public class MonsterPosPair extends Pair implements Serializable {
    public Monster monster; // 몬스터 객체

    public MonsterPosPair(int idx, Monster monster) {
        super(idx); // 인덱스 설정
        this.monster = monster; // 몬스터 객체 초기화
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