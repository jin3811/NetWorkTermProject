package Server;

import util.*;

import java.io.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    private static final int PORT = 9999;
    private ServerSocket serverSocket;
    private Vector<UserService> allUsers = new Vector<>();

    // 채팅방 로직 추가중 코드
    // Map: 키는 채팅방 이름, 값은 UsreService 인스턴스의 집합
    private Vector<Room> rooms = new Vector<>(); // 방 관리
    /*
     * 생성자
     * 서버 소켓 생성
     * */
    public Server() throws IOException {
        serverSocket = new ServerSocket(PORT); // 서버 소켓 초기화
        System.out.println("서버소켓 연결 완료");
    }
    /*
     * called from main
     * AcceptServer 인스턴스 생성
     * 실제 서버 실행
     * */
    public void startServer() {
        AcceptServer acceptServer = new AcceptServer(serverSocket); // 서버 소켓 전달
        acceptServer.start();
    }


    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Server 클래스 내부에 있는 AcceptServer 클래스
    class AcceptServer extends Thread {
        private int id;
        private ServerSocket serverSocket;

        public AcceptServer(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
            id = 1;
        }

        public void run() {
            while (true) { // 사용자 접속을 계속 받음
                try {
                    Socket clientSocket = serverSocket.accept(); // client 접속 대기
//                    System.out.println(clientSocket);

                    // 서버가 새로운 클라이언트의 연결을 수락할 때마다 UserService 인스턴스 생성
                    // 이를 allUsers 벡터에 추가
                    // 서버는 각 클라이언트와 독립적 통신 가능
                    UserService user = new UserService(Server.this, clientSocket, id++);
                    allUsers.add(user);
                    user.start();
                    System.out.println("유저 서비스 시작");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized void createRoom(String name, int managerId) {
        Room r = new Room(name, managerId);
        rooms.add(r);
        notifyRoomChange();

        System.out.println(name + " room 만들기 성공");
        System.out.println("room개수: "+rooms.size());
    }

    private void notifyRoomChange() {
        for (UserService user : allUsers) {
            try {
                ObjectOutputStream objOs = user.getObjOutputStream();
                objOs.writeObject(new MOD(MODE.SUCCESS_MOD,new Vector<>(rooms)));
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


