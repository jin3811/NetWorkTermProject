package UI;

import javax.swing.*;

import Server.Room;
import util.MODE;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class WaitingRoomPanel extends JPanel {

	private String nickname;
	private Socket socket;

	private DataOutputStream dos;
	private DataInputStream dis;
	private JList<String> roomList;
	private Map<String, String> gameRoomUsers; // 대기방별 유저
	private String selectedGameRoom;
	
	private ObjectInputStream objIs;
    private ObjectOutputStream objOS;
    
    DefaultListModel<String> model;
	private boolean isReady = false;

	public WaitingRoomPanel(RandomDefence context, String nickname, Socket socket) {

		this.nickname = nickname;
		model = new DefaultListModel<>();
		gameRoomUsers = new HashMap<>(); //

		try {
			this.socket = socket; // 로그인 패널에서 소켓 가져옴
			// 스트림 초기화
			objOS = new ObjectOutputStream(socket.getOutputStream());
			objIs = new ObjectInputStream(socket.getInputStream());

			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
<<<<<<< HEAD
			
			objOS = new ObjectOutputStream(socket.getOutputStream());
			objIs = new ObjectInputStream(socket.getInputStream());
			
=======

>>>>>>> main
			if (this.socket != null)
				System.out.println("소켓 가져오기 완료"); // 정상 확인
		} catch (Exception e) {

		}

		context.setSize(1000, 800);

		setLayout(new BorderLayout());

		// 채팅룸 목록 관리
		// model.addElement로 표시될 채팅룸을 설정하고 있음.
		// 이는 이미 채팅룸이 정해져있다고 가정한것.
		// -> 직접 생성하도록 수정될 필요 있음.
//		DefaultListModel<String> model = new DefaultListModel<>();
//		model.addElement("Chat Room 1");
//		model.addElement("Chat Room 2");
//		model.addElement("Chat Room 3");
		// JList<String>객체 roomList에 해당 model 설정
		roomList = new JList<>(model);
		
		JPanel topPanel = new JPanel(new BorderLayout());
		JButton createRoomButton = new JButton("방 만들기");
		createRoomButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 방제목 입력
				String roomName = JOptionPane.showInputDialog(WaitingRoomPanel.this, "방 제목을 입력하세요: ", "방 만들기", JOptionPane.PLAIN_MESSAGE);
				if(roomName!=null & !roomName.trim().isEmpty()) {
					roomName = roomName.trim();
					
					
					// 서버에 새 방 생성 요청 보냄
					sendMessageToServer(roomName);
					
					
					
				}
				// 일반적으로 여기서 서버에 새 방을 생성해달라는 요청을 보냅니다.
		        // 현재 예시에서는 시연을 위해 로컬 모델에 새 방을 추가하는 코드를 사용합니다.
				
		        // 새 방 이름 생성 예: "Game Room " + 다음 숫자
//		        String newRoomName = "Game Room " + (model.getSize() + 1);

		        // 모델에 새 방 추가
//		        model.addElement(newRoomName);

		        // 선택 사항: 목록에서 새 방을 선택
//		        roomList.setSelectedValue(newRoomName, true);

		        // 서버에 연결되어 있다면 여기서 서버에 방 생성 요청을 보냅니다.
		        // 서버로부터 방 생성 확인을 받으면, 서버는 새로운 방 목록을 모든 클라이언트에게 업데이트합니다.
		        // 서버 요청을 위한 장소 예약:
		        // sendMessageToServer("CREATE_ROOM " + newRoomName);
				
			}
		});
		

	 
		// 게임룸 사용자 정보 가져오기
		fetchGameRoomUsers();

		JButton selectButton = new JButton("게임방 입장");
		selectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 현재 선택된 채팅룸 가져오기(String)
				selectedGameRoom = roomList.getSelectedValue();
				if (selectedGameRoom != null) {
//                    initializeChatFrame(); // 채팅화면 초기화하는 함수 호출
//                	initializeGameFrame(); // 게임 화면 초기화하는 메서드 호출
//                    chatRoomFrame.setVisible(false);
					
					
				} else {
					JOptionPane.showMessageDialog(context, "게임룸을 선택해주세요.");
				}
			}
		});
		
		// topPanel에 방만들기 버튼 추가 
	    topPanel.add(createRoomButton, BorderLayout.EAST); // Adds the button to the right side
	    add(topPanel, BorderLayout.NORTH);
		add(new JScrollPane(roomList), BorderLayout.CENTER);
		add(selectButton, BorderLayout.SOUTH);

		Thread updateRoomListThread = new UpdateRoomList();
		updateRoomListThread.start();

		setVisible(true);
	}

	// 각 채팅룸에 대한 사용자 목록을 가상으로 생성하는중 -> 수정필요
	private void fetchGameRoomUsers() {
		// 서버로부터 채팅룸 사용자 정보를 가져오는 로직을 가정.
		// 실제 애플리케이션에서는 서버와의 통신을 통해 이 정보를 얻어야 함
		gameRoomUsers.put("Game Room 1", "User1, User2");
		gameRoomUsers.put("Game Room 2", "User3, User4");
		gameRoomUsers.put("Game Room 3", "User5, User6");

		// 채팅룸 목록 ChatRoomList(JList)의 모델을 가져옴
		// 이 모델은 채팅룸 목록의 데이터를 관리함
		// 각 채팅룸에 해당 채팅룸의 사용자 목록 표시
		DefaultListModel<String> model = (DefaultListModel<String>) roomList.getModel();
		// 모델의 모든 채팅룸 순회
		for (int i = 0; i < model.getSize(); i++) {
			String room = model.getElementAt(i);
			// chatRoomUsers 맵에서 room에 해당하는 사용자 목록 조회
			// 해당 채팅룸의 사용자 정보가 없으면 No users 반환
			String users = gameRoomUsers.getOrDefault(room, "No users");
			// 모델의 i번째 요소 업데이트
			// 출력 형식: Chat Room 1 (User1, User2)
			model.set(i, room + " (" + users + ")");
		}
	}
	// 서버에 메시지를 보내는 메소드
	private void sendMessageToServer(String message) {
	    try {
	    	if(objOS!= null) {
	    		objOS.writeObject(new util.MOD(MODE.CREATE_ROOM_MOD, message));
	    		objOS.flush();
	    	}
	    } catch (IOException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(WaitingRoomPanel.this,
	                "서버에 메시지를 보내는데 실패했습니다: " + ex.getMessage(),
	                "통신 오류", JOptionPane.ERROR_MESSAGE);
	    }
	}
	private class UpdateRoomList extends Thread{
		private Vector<Room> rooms;
		String roomName;
		@Override
		public void run() {
			while(true) {
//				try {
//					rooms = (Vector<Room>)objIs.readObject();
//					System.out.println("room 개수: " + rooms.size());
//					SwingUtilities.invokeLater(new Runnable() {
//						
//						@Override
//						public void run() {
//							model.clear();
//							for(Room room : rooms) {
//								roomName = room.getRoomName();
//								model.addElement(roomName); // 모델에 방제 추가
//							}
//							if(!rooms.isEmpty()) {
//								roomList.setSelectedValue(roomName, true);
//							}
//						}
//					});
//				}catch (Exception e) {
//					// TODO: handle exception
//				}
				try {
					rooms = (Vector<Room>)objIs.readObject();
					System.out.println("room 개수: " + rooms.size());
					for(Room room : rooms) {
						String roomName = room.getRoomName();

						model.addElement(roomName); // 모델에 방제 추가
						roomList.setSelectedValue(roomName, true);
					}
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}
	
//    private void addWaitingRoom(String roomTitle, JPanel panel) {
//        JButton waitingRoomButton = new JButton(roomTitle);
//        waitingRoomButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                int answer = JOptionPane.showConfirmDialog(null, "입장하시겠습니까?", roomTitle, JOptionPane.YES_NO_OPTION);
//                if (answer == JOptionPane.YES_OPTION) {
//                    // 입장 시 동작 기능 구현해야함
//                }
//            }
//        });
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int buttonWidth = (int) (screenSize.getWidth() * 0.8);
//        int buttonHeight = 40;
//        waitingRoomButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
//
//        panel.add(Box.createVerticalStrut(10));
//        panel.add(waitingRoomButton);
//        panel.add(Box.createVerticalStrut(10));
//
//        panel.revalidate();
//        panel.repaint();
//    }
}
