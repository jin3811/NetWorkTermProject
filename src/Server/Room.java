package Server;

import java.io.Serializable;
import java.util.Set;
import java.util.Vector;

public class Room implements Serializable {
    private String roomName;
    private int managerId; // 방장 id
    private int playerId; // 상대 플레이어 id

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
    public void participate(int playerId) {
        this.playerId = playerId;
    }

    public synchronized boolean enterEnable() {
        return playerId == -1;
    }

    public int getManagerId() {
        return managerId;
    }

    public int getPlayerId() { return playerId; }
}
