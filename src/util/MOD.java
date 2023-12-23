package util;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
/*
 * 서버와 클라이언트 간 객체 전송에 사용되는 클래스
 * MODE(통신 모드)와 Object payload(담기는 실제 데이터)를 정의
 */
public class MOD implements Serializable {

    @Serial
    private static final long serialVersionUID = 267326879574723932L;
    private MODE mode; // 통신 모드
    private Object payload; // 담기는 데이터

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
