package Server.Manager;

import Server.*;
import util.MOD;
import util.MODE;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class RoomMananger {
    private Vector<Room> rooms; // 방 관리
    private Server server;
    private long roomNum = 1;

    public RoomMananger(Server server) {
        this.server = server;
        rooms = new Vector<>();
    }

    public synchronized Room createRoom(String name, int managerId) {
        Room r = new Room(name, managerId);
        rooms.add(r);

        System.out.println(name + " room 만들기 성공");
        System.out.println("room개수: "+rooms.size());

        notifyRoomChange(this.server.userManager.getAllUsers());

        return r;
    }

    public synchronized void notifyRoomChange(Vector<UserService> allUsers) {
        for (UserService user : allUsers) {
            try {
                ObjectOutputStream objOs = user.getObjOutputStream();
                objOs.writeObject(new MOD(MODE.SUCCESS_GET_ROOM_MOD, new Vector<>(rooms)));
                objOs.flush();
                System.out.println("user : " + user.getName() + " 에게 전송함");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized Room enterRoom(String roomName, int playerId) {
        Room r = null;
        for (Room room : rooms) {
            if (room.getRoomName().equals(roomName) && // 찾고자 하는 방인가?
                room.enterEnable()) { // 들어갈 수 있는가?

                room.participate(playerId); // 자~ 드가자~
                UserService manager = this.server.userManager.getUserbyId(room.getManagerId());
                UserService player = this.server.userManager.getUserbyId(playerId);

                ObjectOutputStream managerOS = manager.getObjOutputStream();
                ObjectOutputStream playerOS = player.getObjOutputStream();

                MOD mod = new MOD(MODE.GAME_READY_SIGNAL_MOD, roomNum);

                try {
                    managerOS.writeObject(mod);
                    playerOS.writeObject(mod);

                    managerOS.flush();
                    playerOS.flush();
                }
                catch (Exception e) {
                    System.out.println("게임 진행 실패");
                    room.participate(-1);
                }

                r = room;
                break;
            }
        }
        return r;
    }

    public Vector<Room> getRooms() {
        return rooms;
    }
}
