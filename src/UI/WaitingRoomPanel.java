package UI;

import javax.swing.*;

import Server.Room;
import util.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class WaitingRoomPanel extends JPanel {

	private RandomDefence context;
	private String nickname;
	private Socket socket;

	private JList<String> roomList;
	private Map<String, String> gameRoomUsers; // 대기방별 유저
	private String selectedGameRoom;
	
	private ObjectInputStream objIs;
    private ObjectOutputStream objOS;
    
    DefaultListModel<String> model;
	private boolean isReady = false;

	private Thread updateRoomListThread;
	private Thread loadingThread;
	private Thread gameStartThread;
	private JLabel stateLabel;
	private TEAM myTeamColor;
	private volatile boolean stopThreads = false; // 추가중
	public WaitingRoomPanel(RandomDefence context, String nickname, Socket socket) {
		this.context = context;
		this.nickname = nickname;
		model = new DefaultListModel<>();
		gameRoomUsers = new HashMap<>(); //

		try {
			this.socket = socket; // 로그인 패널에서 소켓 가져옴
			// 스트림 초기화
			objOS = new ObjectOutputStream(socket.getOutputStream());
			objIs = new ObjectInputStream(socket.getInputStream());

			if (this.socket != null)
				System.out.println("소켓 가져오기 완료"); // 정상 확인
		} catch (Exception e) {

		}

		context.setSize(1000, 800);

		setLayout(new BorderLayout());

		roomList = new JList<>(model);

		stateLabel = new JLabel("참가자 대기중");
		stateLabel.setHorizontalAlignment(JLabel.CENTER);
		loadingThread = new PlayerWaitingThread(stateLabel);

		JPanel topPanel = new JPanel(new BorderLayout());
		JButton createRoomButton = new JButton("방 만들기");
		JButton selectButton = new JButton("게임방 입장");

		createRoomButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 방제목 입력
				String roomName = JOptionPane.showInputDialog(WaitingRoomPanel.this, "방 제목을 입력하세요: ", "방 만들기", JOptionPane.PLAIN_MESSAGE);
				if(roomName!=null & !roomName.trim().isEmpty()) {
					roomName = roomName.trim();

					// 서버에 새 방 생성 요청 보냄
					sendMessageToServer(MODE.CREATE_ROOM_MOD, roomName);

					topPanel.add(stateLabel, BorderLayout.CENTER);
					loadingThread.start();

					// 방장이 어딜 떠날라고
					selectButton.setEnabled(false);
					myTeamColor = TEAM.RED;
				}
			}
		});
		selectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 현재 선택된 채팅룸 가져오기(String)
				selectedGameRoom = roomList.getSelectedValue();
				if (selectedGameRoom == null) {
					JOptionPane.showMessageDialog(context, "게임룸을 선택해주세요.");
				}

				int a = roomList.getSelectedIndex();
				System.out.println(selectedGameRoom + a);
				topPanel.add(stateLabel, BorderLayout.CENTER);
				myTeamColor = TEAM.BLUE;
				sendMessageToServer(MODE.PARTICIPANT_MOD, selectedGameRoom);
			}
		});

		// topPanel에 방만들기 버튼 추가
	    topPanel.add(createRoomButton, BorderLayout.EAST); // Adds the button to the right side
	    add(topPanel, BorderLayout.NORTH);
		add(new JScrollPane(roomList), BorderLayout.CENTER);
		add(selectButton, BorderLayout.SOUTH);

		updateRoomListThread = new UpdateRoomList();
		updateRoomListThread.start();

		// 첫 화면 띄우기 위한 데이터 가져오기
		sendMessageToServer(MODE.GET_ROOM_MOD, null);

		setVisible(true);
	}

	// 서버에 메시지를 보내는 메소드
	private void sendMessageToServer(MODE mode, Object payload) {
		synchronized (objOS) {
		    try {
		    	if(objOS!= null) {
		    		objOS.writeObject(new MOD(mode, payload));
		    		objOS.flush();
		    	}
		    } catch (IOException ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(WaitingRoomPanel.this,
		                "서버에 메시지를 보내는데 실패했습니다: " + ex.getMessage(),
		                "통신 오류", JOptionPane.ERROR_MESSAGE);
		    }
		}
	}
	private class UpdateRoomList extends Thread{
		private Vector<Room> rooms;
		String roomName;
		@Override
		public void run() {
			while(!stopThreads) {
				synchronized (objIs) {
					try {
						MOD packet = (MOD)objIs.readObject();
						MODE mode = packet.getMode();
	
						if (mode == MODE.FAIL_CREATE_ROOM_MOD) continue;
						else if (mode == MODE.SUCCESS_GET_ROOM_MOD || mode == MODE.SUCCESS_CREATE_ROOM_MOD) {
							rooms = (Vector<Room>)packet.getPayload();
	
							System.out.println("room 개수: " + rooms.size());
	
							model.clear();
							for(Room room : rooms) {
								String roomName = room.getRoomName();
	
								model.addElement(roomName); // 모델에 방제 추가
								roomList.setSelectedValue(roomName, true);
							}
						}
						else if (mode == MODE.GAME_READY_SIGNAL_MOD) {
							long roomNum = (long)packet.getPayload();
							System.out.println(nickname + " : 게임 시작신호 받음");
							if (loadingThread != null) {
								loadingThread.interrupt();
								gameStartThread = new GameStartThread(roomNum);
								gameStartThread.start();
							}
	
						}
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
				System.out.println("아직 종료 안해서 안빠져나옴!"); 
			}
			System.out.println("빠져나옴!");
		}
	}

	private class PlayerWaitingThread extends Thread{
		private JLabel loading;
		private String origin;

		public PlayerWaitingThread(JLabel loading) {
			this.loading = loading;
			origin = loading.getText();
		}

		@Override
		public void run() {
			int a = 0;
			String last;

			while(!stopThreads) {
				last = "";
				for (int i = 0; i < a; i++) {
					last += ".";
				}
				loading.setText(origin + last);
				a = (a + 1) % 4;

				try {
					sleep(250);
				} catch (InterruptedException e) {
					System.out.println("게임 화면으로 넘어가기 위해 참여자 대기 스레드 종료");
				}
			}
		}
	}

	private class GameStartThread extends Thread {
		private long roomNum;

		public GameStartThread(long roomNum){
			this.roomNum = roomNum;
		}
		@Override
		public void run() {
			loadingThread.interrupt();
			for (int i = 5 ; i >= 0; i--) {
				stateLabel.setText(i + "초후 게임이 시작됩니다. 준비해주세요.");
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			System.out.println("화면 전환");
			System.out.println("업데이트룸스레드 종료전");
			updateRoomListThread.interrupt();
			System.out.println("로딩스레드 종료전");
			loadingThread.interrupt();
			
			System.out.println("게임스타트스레드 종료전");
			this.interrupt();
			stopAllThreads();
			context.transition(new GamePanel(context, nickname, socket, objOS, objIs, roomNum, myTeamColor));
		}
	}
	// 모든 스레드를 중지하기 위한 메소드
    private void stopAllThreads() {
        stopThreads = true;
       
//        try {
//            if (updateRoomListThread != null && updateRoomListThread.isAlive()) {
//                updateRoomListThread.join(); // 스레드가 종료될 때까지 대기
//            }
//            if (loadingThread != null && loadingThread.isAlive()) {
//                loadingThread.join(); // 스레드가 종료될 때까지 대기
//            }
//            if (gameStartThread != null && gameStartThread.isAlive()) {
//                gameStartThread.join(); // 스레드가 종료될 때까지 대기
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
