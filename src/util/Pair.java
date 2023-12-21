package util;

import java.io.Serializable;

public abstract class Pair implements Serializable{
    public int idx; // 몇번쨰 path인지

    public Pair(int idx) {
        this.idx = idx;
    }
}
