package util;

import Component.Turret;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

public class MOD implements Serializable {

    private MODE mode;
//    private Object payload;
    private Serializable payload;

    public MOD(MODE mode, Serializable payload) {
        this.mode = mode;
        this.payload = payload;
//        if (payload == null) {
//            this.payload = null;
//        }
//        else if (payload instanceof Vector<?> pay) {
//            this.payload = new Data((Vector<MonsterPosPair>) pay);
//        }
//        else if(payload instanceof ArrayList<?> pay) {
//            this.payload = new Data((ArrayList<Turret>)pay);
//        }
//        else if(payload instanceof String pay) {
//            this.payload = new Data((String) pay);
//        }
//        else {
//            this.payload = new Data((long) payload);
//        }
    }

    public MODE getMode() {
        return mode;
    }

    public void setMode(MODE mode) {
        this.mode = mode;
    }

    public Serializable getPayload() {
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
