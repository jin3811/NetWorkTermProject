//package Server;
//import java.io.*;
//import java.net.*;
//import java.util.*;
//
//public class Server {
//	
//	private static final int PORT = 9999;
//	private ServerSocket serverSocket;
//	private Socket clientSocket;
//	private Vector<UserService> allUsers = new Vector<UserService>();
//	public static void main(String[] args) {
//		// 대기방들의 리스트
////		private ArrayList<WaitingRoom> waitingRoomList = new ArrayList<>();
//		try {
//			serverSocket = new ServerSocket(PORT); // 서버 소켓
//			System.out.println("서버소켓 연결 완료");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		/// 새로운 client accept하는 클래스
//		AcceptServer acceptServer = new AcceptServer(); // 멀티 스레드 객체 생성
//		acceptServer.start();
//	}
//	
//	// 새로운 Client accept하는 클래스
//	class AcceptServer extends Thread{
//		public void run() {
//			while(true) { // 사용자 접속을 계속 받음
//				try {
//					clientSocket = serverSocket.accept(); // client 접속 대기
//					System.out.println(clientSocket);
//					// user마다 thread 생성
//					UserService user = new UserService(clientSocket);
//					allUsers.add(user);
//					user.start();
//				}catch (Exception e) {
//					
//				}
//			}
//		}
//	}
//	// 서버에서 관리하는 User Thread - 클라이언트와 통신하는 스레드
//	// 이를 allUsers에 넣어서 관리함.
//	class UserService extends Thread{
//		private InputStream is;
//        private OutputStream os;
//        private DataInputStream dis;
//        private DataOutputStream dos;
//        
//        private String name;
//        private Socket clientSocket;
//        private Vector<UserService> allUsers;
//        
//		public UserService(Socket clientSocket) {
//			this.clientSocket = clientSocket;
//			this.allUsers = allUsers;
//			try {
//				is = clientSocket.getInputStream();
//				dis = new DataInputStream(is);
//				os = clientSocket.getOutputStream();
//				dos = new DataOutputStream(os);
//				
//				String line = dis.readUTF();
//				String[] msg = line.split("");
//				name = msg[1].trim();
//				System.out.println(name+"입장");
//			}catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//		}
//		// 모든 유저에게 보내는 기능
//		public void broadCast() {
//			
//		}
//		@Override
//		public void run() { // 클라이언트에서 날린 요청을 계속 받고, 보내는 기능 수행
//			while(true) {
//				try {
//					// 특정 클라이언트로부터 데이터를 받고
//					String msg = dis.readUTF();
//					// 모든 클라이언트에게 보내기 수행해야 함.
//					
//				}catch (Exception e) {
//
//					
//				}
//			}
//		}
//	}
//}
//
//

package Server;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
   
    private static final int PORT = 9999;
    private ServerSocket serverSocket;
    private Vector<UserService> allUsers = new Vector<>();

    // 채팅방 로직 추가중 코드
    // Map: 키는 채팅방 이름, 값은 UsreService 인스턴스의 집합
    private Map<String, Set<UserService>> rooms = new ConcurrentHashMap<>(); // 채팅방 관리
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
        private ServerSocket serverSocket;
        
        public AcceptServer(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }
        
        public void run() {
            while (true) { // 사용자 접속을 계속 받음
                try {
                    Socket clientSocket = serverSocket.accept(); // client 접속 대기
                    System.out.println(clientSocket);
                    
                    // 서버가 새로운 클라이언트의 연결을 수락할 때마다 UserService 인스턴스 생성
                    // 이를 allUsers 벡터에 추가
                    // 서버는 각 클라이언트와 독립적 통신 가능
                    UserService user = new UserService(clientSocket);
                    allUsers.add(user);
                    user.start();
                    System.out.println("유저 서비스 시작");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /*
     * 개별 클라이언트와 연결을 관리하는 Thread
     * 각 UserService 인스턴스는 하나의 클라이언트와 연결하는 clientSocket을 가짐
     * 해당 clientSocket을 이용해 서버-클라이언트 통신
     * 모든 UserSerivce 인스턴스는 allUsers 벡터에 저장됨
     * */
   class UserService extends Thread{
	   	private InputStream is;
        private OutputStream os;
        private DataInputStream dis;
        private DataOutputStream dos;
        
        private String name; // 유저 이름
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
            
            String line = dis.readUTF(); // 
            String[] msg = line.split(""); // 
            name = msg[1].trim(); // 유저 이름 저장
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
               System.out.println("서버가 받음"+msg);
               // 모든 클라이언트에게 보내기 수행해야 함.
               
            }catch (Exception e) {

               
            }
         }
      }
   }
}


