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

    public RoomMananger(Server server) {
        this.server = server;
        rooms = new Vector<>();
    }

    public synchronized void createRoom(String name, int managerId) {
        Room r = new Room(name, managerId);
        rooms.add(r);

        System.out.println(name + " room 만들기 성공");
        System.out.println("room개수: "+rooms.size());

        notifyRoomChange(this.server.userManager.getAllUsers());
    }

    public void notifyRoomChange(Vector<UserService> allUsers) {
        for (UserService user : allUsers) {
            try {
                ObjectOutputStream objOs = user.getObjOutputStream();
                objOs.writeObject(new MOD(MODE.SUCCESS_MOD, new Vector<>(rooms)));
                objOs.flush();
                System.out.println("user : " + user.getName() + " 에게 전송함");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public Vector<Room> getRooms() {
        return rooms;
    }
}
