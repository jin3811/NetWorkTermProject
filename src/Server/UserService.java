package Server;

import util.MOD;
import util.MODE;

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
            is = clientSocket.getInputStream();
            objIs = new ObjectInputStream(is);
            os = clientSocket.getOutputStream();
            objOS = new ObjectOutputStream(os);
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
        while(true) {
            try {
                MOD receive = (MOD)objIs.readObject();
                System.out.println("MOD 수신함 : " + receive.toString());
                switch (receive.getMode()) {
                    case CREATE_ROOM_MOD -> {
                        this.server.createRoom(receive.getAdditionalData(), id);
                    }
                }
            }
            catch (Exception e) {
                System.out.println("방 못만듬");
            }
        }
    }

    public ObjectOutputStream getObjOutputStream() {
        return this.objOS;
    }
}