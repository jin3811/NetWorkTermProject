package Server;

import java.io.*;
import java.net.Socket;

/*
 * 개별 클라이언트와 연결을 관리하는 Thread
 * 각 UserService 인스턴스는 하나의 클라이언트와 연결하는 clientSocket을 가짐
 * 해당 clientSocket을 이용해 서버-클라이언트 통신
 * 모든 UserSerivce 인스턴스는 Server의 allUsers 벡터에 저장됨
 * */

public class UserService extends Thread{
    private InputStream is;
    private OutputStream os;
    private DataInputStream dis;
    private DataOutputStream dos;

    private String name;
    private Socket clientSocket;
//        private Vector<UserService> allUsers;

    public UserService(Socket clientSocket) {
        this.clientSocket = clientSocket;
//         this.allUsers = allUsers;
        try {
            is = clientSocket.getInputStream();
            dis = new DataInputStream(is);
            os = clientSocket.getOutputStream();
            dos = new DataOutputStream(os);
            if(dis != null && dos != null)
                System.out.println("UserService dis, dos 초기화 완료");

//            String line = dis.readUTF(); //
//            String[] msg = line.split(""); //
//            name = msg[1].trim(); // 유저 이름 읽어옴
            System.out.println(name+" 입장");
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
                // 특정 클라이언트로부터 데이터를 받고
                String msg = dis.readUTF();
                // System.out.println("서버가 받음"+msg);
                // 모든 클라이언트에게 보내기 수행해야 함.


            }
            catch (Exception e) {


            }
        }
    }
}