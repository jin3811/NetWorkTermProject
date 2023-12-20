package Server;

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
    private InputStream is;
    private OutputStream os;
    private ObjectInputStream objIs;
    private ObjectOutputStream objOS;

    private String name;
    private Socket clientSocket;
    private Server server;
    private int id;
//        private Vector<UserService> allUsers;

    public UserService(Server server, Socket clientSocket, int id) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.id = id;

        try {
            os = clientSocket.getOutputStream();
            objOS = new ObjectOutputStream(os);
            is = clientSocket.getInputStream();
            objIs = new ObjectInputStream(is);
            if(objIs != null && objOS != null)
                System.out.println("UserService dis, dos 초기화 완료");

        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    // 모든 유저에게 보내는 기능
    public void broadCast() {

    }
    // 클라이언트와 지속적으로 통신하는 부분
    @Override
    public void run() { // 클라이언트에서 날린 요청을 계속 받고, 보내는 기능 수행
        Room r = null;
        while(true) {
            try {
                MOD receive = (MOD)objIs.readObject();
                System.out.println("uid#" + id + " - MOD 수신함 : " + receive.toString());
                switch (receive.getMode()) {
                    case CREATE_ROOM_MOD -> {
                        r = this.server.roomMananger.createRoom((String)receive.getPayload(), id);
                        System.out.println("id : " + id + " 방 생성 요청");
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
                    }
                    case GAME_START_MOD -> {
                        long roomNum = (long)receive.getPayload();
                        this.server.gameManager.addGame(r, roomNum);
                        this.server.gameManager.startGame(roomNum);
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
}