package Server;

import Server.Manager.GameManager;
import Server.Manager.RoomMananger;
import Server.Manager.UserManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/*
 * Server 클래스: 서버의 역할
 * 클라이언트의 연결을 관리하고, 각각의 클라이언트와 통신을 처리
 */
public class Server {
    private static final int PORT = 9999; // 서버의 포트번호
    private ServerSocket serverSocket; // 서버 소켓

    // 게임 내의 사용자, 방, 게임을 관리하는 매니저 클래스 인스턴스
    public UserManager userManager = new UserManager(this);
    public RoomMananger roomMananger = new RoomMananger(this);
    public GameManager gameManager = new GameManager(this);
    /*
     * 생성자
     * 서버 소켓 초기화한다.
     * */
    public Server() throws IOException {
        serverSocket = new ServerSocket(PORT); // 서버 소켓 초기화
        System.out.println("서버소켓 연결 완료");
    }
    /*
     * called from main
     * 서버를 시작하는 메서드
     * AcceptServer 스레드를 생성하고 시작하여 클라이언트 연결 대기
     * */
    public void startServer() {
        AcceptServer acceptServer = new AcceptServer(serverSocket); // 서버 소켓 전달
        acceptServer.start(); // 스레드 시작
    }
    
    /*
     * main 부분
     * Server 인스턴스를 생성하고 서버를 시작합니다.
     */
    public static void main(String[] args) {
        try {
            Server server = new Server(); // Server 인스턴스 생성
            server.startServer(); // 서버 시작
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * AcceptServer: Server 클래스 내부에 있는 스레드 클래스
     * 새로운 클라이언트 연결을 지속적으로 수락하는 역할
     */
    class AcceptServer extends Thread {
        private int id; // 클라이언트에게 할당할 고유 ID
        private ServerSocket serverSocket; // 클라이언트 연결을 수신하는 서버 소켓

        public AcceptServer(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
            id = 1; // 클라이언트 ID 초기화
        }

        /*
         * 스레드 실행 
         * 클라이언트의 연결을 지속적으로 수락, UserService 인스턴스를 생성하여 관리
         */
        public void run() {
            while (true) { // 사용자 접속을 계속 받음
                try {
                    Socket clientSocket = serverSocket.accept(); // client 접속 대기
                    // 새로 연결된 클라이언트를 위한 UserService 인스턴스 생성
                    UserService user = new UserService(Server.this, clientSocket, id++);
                    userManager.addUser(user); // userManager에 추가
                    user.start(); // 스레드 시작
                    System.out.println("유저 서비스 시작");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



}


