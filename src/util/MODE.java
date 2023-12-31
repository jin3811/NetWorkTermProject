package util;

public enum MODE {
    GET_ROOM_MOD, // 방 정보 주세요
    SUCCESS_GET_ROOM_MOD, // ㅇㅋ 보내줌
    CREATE_ROOM_MOD, // 방 만들어주세요
    SUCCESS_CREATE_ROOM_MOD, // 방 만들기 성공. 사람보내줄게 ㄱㄷ
    FAIL_CREATE_ROOM_MOD, // 방 만들기 실패. 아무튼 실패
    PARTICIPANT_MOD, // 이 방 들어갈래요
    GAME_READY_SIGNAL_MOD, // 방 사람 왔다. 시작 준비 해라
    GAME_START_MOD, // 게임 시작할게요
    PNT_TURRET_MOD, // 클라야 터렛 그려라
    PNT_MONSTER_MOD, // 클라야 몬스터 그려라
    TURRET_UPDATE_MOD, // 서버야 터렛 업데이트 했다
    GAME_WIN_MOD, // 니가 이김
    GAME_LOSE_MOD, // 니가 짐
    TEST_MOD // 아무거나 보내봄
}
