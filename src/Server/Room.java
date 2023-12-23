package Server;

import java.io.Serializable;
import java.util.Set;
import java.util.Vector;
/*
 * Room 클래스: 게임 방의 정보를 저장 및 관리
 * */
public class Room implements Serializable {
    private String roomName; // 방 이름
    private int managerId; // 방장 Id
    private int playerId; // 참가자(상대 플레이어) Id

    // 생성자
    public Room(String roomName, int managerId) {
        this.roomName = roomName;
        this.managerId = managerId;
        this.playerId = -1;
    }
    public Room(Room n){
        this.roomName = n.getRoomName();
    }
    public String getRoomName() {
    	return roomName;
    }
    // 참가자 Id 설정 메소드
    public void participate(int playerId) {
        this.playerId = playerId;
    }
    // 방 입장이 가능한지 판단하는 메소드
    // playerId가 -1이면 참가자가 없으므로 입장 가능 
    public synchronized boolean enterEnable() {
        return playerId == -1;
    }
    // 방장의 Id 반환하는 메소드
    public int getManagerId() {
        return managerId;
    }
    // 참가자(상대 플레이어) Id 반환하는 메소드
    public int getPlayerId() { return playerId; }
    
    // 내 Id 기준 상대방 Id 반환하는 메소드
    // 자신이 방장-> 참가자 Id 반환, 아니면 방장 Id 반환
    public int getEnemy(int myId) {
        return managerId == myId ? playerId : managerId;
    }
}
