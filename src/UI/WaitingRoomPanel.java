package UI;

import javax.swing.*;

import Server.Room;
import util.*;

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
    
    DefaultListModel<String> model = new DefaultListModel<>();
	private boolean isReady = false;

	public WaitingRoomPanel(RandomDefence context, String nickname, Socket socket) {

		this.nickname = nickname;
		gameRoomUsers = new HashMap<>(); //

		try {
			this.socket = socket; // 로그인 패널에서 소켓 가져옴
			// 스트림 초기화
			objOS = new ObjectOutputStream(socket.getOutputStream());
			objIs = new ObjectInputStream(socket.getInputStream());

			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());

			if (this.socket != null)
				System.out.println("소켓 가져오기 완료"); // 정상 확인
		} catch (Exception e) {

		}

		context.setSize(1000, 800);

		setLayout(new BorderLayout());

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
					sendMessageToServer(MODE.CREATE_ROOM_MOD, roomName);
				}
			}
		});

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

		sendMessageToServer(MODE.GET_ROOM_MOD, null);

		setVisible(true);
	}

	// 서버에 메시지를 보내는 메소드
	private void sendMessageToServer(MODE mode, String message) {
	    try {
	    	if(objOS!= null) {
	    		objOS.writeObject(new MOD(mode, message));
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
		@Override
		public void run() {
			while(true) {
				try {
					MOD packet = (MOD)objIs.readObject();
					if (packet.getMode() == MODE.FAIL_MOD) continue;

					rooms = (Vector<Room>)packet.getPayload();

					System.out.println("room 개수: " + rooms.size());

					model.clear();
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
}
