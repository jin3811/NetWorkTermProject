package util;

import java.io.Serializable;
import java.util.Objects;

public class MOD implements Serializable {
    private MODE mode;
    private Object payload;

    public MOD(MODE mode, Object payload) {
        this.mode = mode;
        this.payload = payload;
    }

    public MOD(MODE mode) {
        this(mode, null);
    }

    public MOD() {
        this(null, null);
    }

    public MODE getMode() {
        return mode;
    }

    public void setMode(MODE mode) {
        this.mode = mode;
    }

    public Object getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "MOD{mod="+mode.toString()+"}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MOD mod1)) return false; // obj가 MOD 타입이라면 MOD mod1 = (MOD)obj 자동 수행
        return mod1.mode == this.mode;
    }
}
