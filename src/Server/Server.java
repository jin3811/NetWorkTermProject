package Server;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	
	private static final int PORT = 9999;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private Vector<UserService> allUsers = new Vector<UserService>();
	public static void main(String[] args) {
		// 대기방들의 리스트
//		private ArrayList<WaitingRoom> waitingRoomList = new ArrayList<>();
		try {
			serverSocket = new ServerSocket(PORT); // 서버 소켓
			System.out.println("서버소켓 연결 완료");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/// 새로운 client accept하는 클래스
		AcceptServer acceptServer = new AcceptServer(); // 멀티 스레드 객체 생성
		acceptServer.start();
	}
	
	// 새로운 Client accept하는 클래스
	class AcceptServer extends Thread{
		public void run() {
			while(true) { // 사용자 접속을 계속 받음
				try {
					clientSocket = serverSocket.accept(); // client 접속 대기
					System.out.println(clientSocket);
					// user마다 thread 생성
					UserService user = new UserService(clientSocket);
					allUsers.add(user);
					user.start();
				}catch (Exception e) {
					
				}
			}
		}
	}
	// 서버에서 관리하는 User Thread - 클라이언트와 통신하는 스레드
	// 이를 allUsers에 넣어서 관리함.
	class UserService extends Thread{
		private InputStream is;
        private OutputStream os;
        private DataInputStream dis;
        private DataOutputStream dos;
        
        private String name;
        private Socket clientSocket;
        private Vector<UserService> allUsers;
        
		public UserService(Socket clientSocket) {
			this.clientSocket = clientSocket;
			this.allUsers = allUsers;
			try {
				is = clientSocket.getInputStream();
				dis = new DataInputStream(is);
				os = clientSocket.getOutputStream();
				dos = new DataOutputStream(os);
				
				String line = dis.readUTF();
				String[] msg = line.split("");
				name = msg[1].trim();
				System.out.println(name+"입장");
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		// 모든 유저에게 보내는 기능
		public void broadCast() {
			
		}
		@Override
		public void run() { // 클라이언트에서 날린 요청을 계속 받고, 보내는 기능 수행
			while(true) {
				try {
					// 특정 클라이언트로부터 데이터를 받고
					String msg = dis.readUTF();
					// 모든 클라이언트에게 보내기 수행해야 함.
					
				}catch (Exception e) {

					
				}
			}
		}
	}
}


