package util;

public enum MODE {
    GET_ROOM_MOD, // 방 정보 요청
    SUCCESS_GET_ROOM_MOD, // 방 목록 전송에 성공
    CREATE_ROOM_MOD, // 방 생성 요청
    SUCCESS_CREATE_ROOM_MOD, // 방 생성 성공
    FAIL_CREATE_ROOM_MOD, // 방 생성 실패
    PARTICIPANT_MOD, // 방 참가 요청
    GAME_READY_SIGNAL_MOD, // 방에 사람 왔으니 시작할 준비 신호
    GAME_START_MOD, // 게임 시작 신호
    PNT_TURRET_MOD, // 클라이언트에게 터렛 그리라는 신호
    PNT_MONSTER_MOD, // 클라이언트에게 몬스터 그리라는 신호
    TURRET_UPDATE_MOD, // 서버에게 터렛 업데이트했음을 알리는 신호
    GAME_WIN_MOD, // 게임 승리 알림
    GAME_LOSE_MOD, // 게임 패배 알림
    MODIFY_LIFE_MOD, // 생명력 수정 알림
}
