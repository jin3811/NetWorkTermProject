package Server.Manager;

import Server.*;
import util.MOD;
import util.MODE;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;
/*
 * RoomMananger클래스: 서버 내의 게임 방들을 관리
 * 새로운 방을 만들고, 사용자에게 방 변경 사항을 알리며, 사용자가 방에 들어가는 것을 처리하는 역할
 * */
public class RoomMananger {
    private Vector<Room> rooms; // 활성 게임 방들 저장
    private Server server;
    private long roomNum = 1; // 방 고유 식별자
    
    // 생성자
    public RoomMananger(Server server) {
        this.server = server;
        rooms = new Vector<>();
    }

    // 새 게임 방을 만들고 방 목록에 추가
    public synchronized Room createRoom(String name, int managerId) {
        Room r = new Room(name, managerId); // 새 방 인스턴스 생성
        rooms.add(r); // 새 방을 방 목록에 추가

        System.out.println(name + " room 만들기 성공");
        System.out.println("room개수: "+rooms.size());

        // 모든 연결된 사용자에게 현재 방 상태를 알림
        notifyRoomChange(this.server.userManager.getAllUsers());

        return r; // 새 방 반환
    }

    // 모든 연결된 사용자에게 현재 방 상태를 알림
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
    // 특정 방에 들어가려는 사용자 요청 처리
    public synchronized Room enterRoom(String roomName, int playerId) {
        Room r = null;
        for (Room room : rooms) {
            if (room.getRoomName().equals(roomName) && // 요청한 방인지 확인
                room.enterEnable()) { // 방에 들어갈 수 있는지 확인

                room.participate(playerId); // 방에 플레이어 추가
                // 방 관리자(방장) 가져오기
                UserService manager = this.server.userManager.getUserbyId(room.getManagerId());
                // 방에 참여한 플레이어 가져오기
                UserService player = this.server.userManager.getUserbyId(playerId);

                ObjectOutputStream managerOS = manager.getObjOutputStream();
                ObjectOutputStream playerOS = player.getObjOutputStream();

                // GAME_READY_SIGNAL_MOD 준비
                MOD mod = new MOD(MODE.GAME_READY_SIGNAL_MOD, roomNum);

                try {
                	// 방장 및 플레이어(클라이언트)에게 보낸다.
                    managerOS.writeObject(mod);
                    playerOS.writeObject(mod);

                    managerOS.flush();
                    playerOS.flush();
                }
                catch (Exception e) {
                    System.out.println("게임 진행 실패");
                    room.participate(-1);
                }

                r = room; // 플레이어가 들어간 방을 저장
                break;
            }
        }
        return r; // 플레이어가 들어간 방 반환
    }
    
    // 모든 활성 방들의 목록을 반환
    public Vector<Room> getRooms() {
        return rooms;
    }
}
