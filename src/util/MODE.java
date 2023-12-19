package util;

public enum MODE {
    GET_ROOM_MOD, // 방 정보 주세요
    SUCCESS_GET_ROOM_MOD, // ㅇㅋ 보내줌
    CREATE_ROOM_MOD, // 방 만들어주세요
    SUCCESS_CREATE_ROOM_MOD, // 방 만들기 성공. 사람보내줄게 ㄱㄷ
    FAIL_CREATE_ROOM_MOD, // 방 만들기 실패. 아무튼 실패
    PARTICIPANT_MOD, // 이 방 들어갈래요
    GAME_START_SIGNAL_MOD, // 방 사람 왔다. 시작 준비 해라
}
