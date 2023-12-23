package util;

import java.io.Serializable;
/**
 * Pair 클래스: MonsterPosPair에 상속할 추상 클래스
 */
public abstract class Pair implements Serializable{
    public int idx; // 몇번째 path인지

    public Pair(int idx) {
        this.idx = idx; 
    }
}
