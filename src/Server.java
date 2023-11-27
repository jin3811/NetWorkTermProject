import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
public class Server {
	
	private static final int PORT = 9999;
	
	public static void main(String[] args) {
		private ServerSocket serverSocket = null;
		
		// 대기방들의 리스트
		private ArrayList<WaitingRoom> waitingRoomList = new ArrayList<>();
		private ArrayList<>
		try {
			serverSocket = new ServerSocket(PORT); // 서버 소켓
			System.out.println("서버소켓 연결 완료");
			
			while(true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("클라이언트 접속");
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
	
	}
}

class WaitingRoom implements Runnable{
	private Socket clientSocket;
	
	public WaitingRoomThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	@Override
	public void run() {
		
	}
	
}
class ServerThread extends Thread{
	private String name; // 이름
	Socket clinet
	
}
