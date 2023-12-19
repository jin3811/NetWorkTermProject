package util;

public enum MODE {
    GET_ROOM_MOD, // 방 정보 주세요
    CREATE_ROOM_MOD, // 방 만들어주세요
    SUCCESS_MOD, // 통신 성공. 보내줄게 ㄱㄷ
    FAIL_MOD, // 통신 실패. 아무튼 안보냄
}
