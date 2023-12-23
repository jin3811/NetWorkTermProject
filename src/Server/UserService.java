package Server;

import Component.Turret;
import Server.Manager.GameManager;
import util.*;

import java.util.*;
import java.io.*;
import java.net.Socket;

/*
 * UserService 클래스: 서버와 개별 클라이언트 간의 연결을 관리하는 스레드
 * 하나의 클라이언트와 연결을 담당하는 clientSocket 가지고, 이를 이용해 통신
 * */
public class UserService extends Thread implements Serializable{
    private ObjectInputStream objIs;
    private ObjectOutputStream objOS;

    private Socket clientSocket; // 클라이언트와 연결하는 소켓
    private Server server; // UserService가 속한 서버 객체
    private int id; // 클라이언트 고유 ID

    private TEAM team; // 클라이언트 팀 정보

    /*
     * 생성자
     * 클라이언트 소켓과 서버 객체를 받아 초기화하고,
     * 입출력 스트림 설정
     */
    public UserService(Server server, Socket clientSocket, int id) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.id = id;

        try {
            objOS = new ObjectOutputStream(clientSocket.getOutputStream());
            objIs = new ObjectInputStream(clientSocket.getInputStream());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

   /*
    * 클라이언트로부터 요청을 계속 받아 처리하고,
    * 필요한 정보를 클라이언트로 보내는 기능 수행
    * */
    @Override
    public void run() { 
        Room r = null; // 사용자가 속한 게임 방
        long roomNum = -1; // 게임 방 번호
        while(true) {
            try {
            	// 클라이언트로부터 객체 수신
                MOD receive = (MOD)objIs.readObject();
                System.out.println("uid#" + id + " - MOD 수신함 : " + receive.toString());
                
                // 받은 요청(MOD에 담긴 MODE)에 따라 처리
                switch (receive.getMode()) {
                    case CREATE_ROOM_MOD -> { // 방 생성 요청 받은 경우
                    	// 새 방 생성
                        r = this.server.roomMananger.createRoom((String)receive.getPayload(), id);
                        System.out.println("id : " + id + " 방 생성 요청");
                        team = TEAM.RED; // 방장은 레드팀
                    }
                    case GET_ROOM_MOD -> { // 방 목록 요청 받은 경우
                    	// 현재 존재하는 Room들의 목록을 클라이언트에 전송
                        objOS.writeObject(new MOD(
                                MODE.SUCCESS_GET_ROOM_MOD,
                                new Vector<>(this.server.roomMananger.getRooms())));
                        objOS.flush();
                    }
                    case PARTICIPANT_MOD -> { // 방 참가 요청 받은 경우
                    	// 클라이언트가 선택한 방에 참가
                        String roomName = (String)receive.getPayload();
                        r = this.server.roomMananger.enterRoom(roomName, this.id);
                        System.out.println("id : " + id + " 방 참가 요청");
                        team = TEAM.BLUE; // 참가자는 블루팀
                    }
                    case GAME_START_MOD -> { // 게임 시작 요청을 받은 경우
                    	// 게임을 시작
                        roomNum = (long)receive.getPayload();
                        this.server.gameManager.addGame(r, roomNum);
                        this.server.gameManager.startGame(roomNum);
                    }
                    case TURRET_UPDATE_MOD -> { // 터렛 업데이트 요청 받은 경우
                        // 일단 상대 UserService를 가져온다.
                        UserService enemy = this.server.userManager.getUserbyId(r.getEnemy(this.id));

                        // 해당 게임 세션도 가져온다.
                        GameManager.GameSession gameRoom = this.server.gameManager.getGameRoom(roomNum);

                        // 클라이언트에서 받은 터렛 정보 가져오기
                        List<Turret> updateTurret = (List<Turret>)receive.getPayload();
                        
                        // 자기 정보를 업데이트 한다.
                        GameManager.Player myPlayerInfo = gameRoom.getPlayer(this.id);
                        myPlayerInfo.updateTurret(new ArrayList<Turret>(updateTurret));

                        // 변경된 터렛 정보를 상대 플레이어(클라이언트)에게 전송하여 그릴 수 있도록 한다.
                        ObjectOutputStream enemyOS = enemy.getObjOutputStream();
                        synchronized (enemyOS) {
                            enemyOS.writeObject(new MOD(
                                    MODE.PNT_TURRET_MOD,
                                    new ArrayList<Turret>(updateTurret)
                            ));
                            enemyOS.flush();
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                System.exit(123);
            }
        }
    }

    // 입출력 객체 스트림 및 사용자 정보 getter들
    public ObjectOutputStream getObjOutputStream() {
        return this.objOS;
    }

    public ObjectInputStream getObjectInputStream() {
        return this.objIs;
    }

    public int getUserID() {
        return id;
    }

    public TEAM getTeam() {
        return team;
    }
}