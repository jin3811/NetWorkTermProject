package Server;

import Component.Turret;
import Server.Manager.GameManager;
import util.*;

import java.util.*;
import java.io.*;
import java.net.Socket;

/*
 * 개별 클라이언트와 연결을 관리하는 Thread
 * 각 UserService 인스턴스는 하나의 클라이언트와 연결하는 clientSocket을 가짐
 * 해당 clientSocket을 이용해 서버-클라이언트 통신
 * 모든 UserSerivce 인스턴스는 Server의 allUsers 벡터에 저장됨
 * */

public class UserService extends Thread implements Serializable{
    private ObjectInputStream objIs;
    private ObjectOutputStream objOS;

    private String name;
    private Socket clientSocket;
    private Server server;
    private int id;

    private TEAM team;
//        private Vector<UserService> allUsers;

    public UserService(Server server, Socket clientSocket, int id) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.id = id;

        try {
            objOS = new ObjectOutputStream(clientSocket.getOutputStream());
            objIs = new ObjectInputStream(clientSocket.getInputStream());
            if(objIs != null && objOS != null)
                System.out.println("UserService Ois, Oos 초기화 완료");

        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    // 클라이언트와 지속적으로 통신하는 부분
    @Override
    public void run() { // 클라이언트에서 날린 요청을 계속 받고, 보내는 기능 수행
        Room r = null;
        long roomNum = -1;
        while(true) {
            try {
                MOD receive = (MOD)objIs.readObject();
                System.out.println("uid#" + id + " - MOD 수신함 : " + receive.toString());
                switch (receive.getMode()) {
                    case CREATE_ROOM_MOD -> {
                        r = this.server.roomMananger.createRoom((String)receive.getPayload(), id);
                        System.out.println("id : " + id + " 방 생성 요청");
                        team = TEAM.RED;
                    }
                    case GET_ROOM_MOD -> {
                        objOS.writeObject(new MOD(
                                MODE.SUCCESS_GET_ROOM_MOD,
                                new Vector<>(this.server.roomMananger.getRooms())));
                        objOS.flush();
                    }
                    case PARTICIPANT_MOD -> {
                        String roomName = (String)receive.getPayload();
                        r = this.server.roomMananger.enterRoom(roomName, this.id);
                        System.out.println("id : " + id + " 방 참가 요청");
                        team = TEAM.BLUE;
                    }
                    case GAME_START_MOD -> {
                        roomNum = (long)receive.getPayload();
                        this.server.gameManager.addGame(r, roomNum);
                        this.server.gameManager.startGame(roomNum);
                    }
                    case TURRET_UPDATE_MOD -> {
                        System.out.println("터렛 업뎃 들어옴");
                        
                        // 일단 상대를 알아온다
                        UserService enemy = this.server.userManager.getUserbyId(r.getEnemy(this.id));

                        // 게임 세션도 가져온
                        GameManager.GameSession gameRoom = this.server.gameManager.getGameRoom(roomNum);

                        // 터렛 가져오기
                        List<Turret> updateTurret = (List<Turret>)receive.getPayload();
                        
//                        for(Turret t: updateTurret) {
//                        	System.out.println("터렛 업글 정보: "+ t.getLevel());
//                        }
//                        System.out.println("========================");
                        // 자기 정보를 업데이트 한다.
                        GameManager.Player myPlayerInfo = gameRoom.getPlayer(this.id);
                        myPlayerInfo.updateTurret(new ArrayList<Turret>(updateTurret));

                        // 상대한테 알려줘서 그리게 한다.
                        ObjectOutputStream enemyOS = enemy.getObjOutputStream();
                        synchronized (enemyOS) {
                            enemyOS.writeObject(new MOD(
                                    MODE.PNT_TURRET_MOD,
                                    new ArrayList<Turret>(updateTurret)
                            ));
                            enemyOS.flush();
                        }
                        // 확인용 - 
//                        User
//                        ObjectOutputStream myOS = my
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                System.exit(123);
            }
        }
    }

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