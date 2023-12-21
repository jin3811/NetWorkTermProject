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
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class WaitingRoomPanel extends MultiRoomJPanel implements TransitionDisplayCommand{

	private RandomDefence context;
	private JList<String> roomList;
	private Map<String, String> gameRoomUsers; // 대기방별 유저
	private String selectedGameRoom;
    DefaultListModel<String> model;
	private boolean isReady = false;
	private Thread updateRoomListThread;
	private Thread loadingThread;
	private Thread gameStartThread;
	private JLabel stateLabel;
	private TEAM myTeamColor;
	private volatile boolean stopThreads = false; // 추가중

	private ArrayList<Thread> threadPool = new ArrayList<>();

	public WaitingRoomPanel(RandomDefence context) {
		this.context = context;;
		model = new DefaultListModel<>();
		gameRoomUsers = new HashMap<>(); //

		System.out.println(objOs);

		context.setSize(1000, 800);

		setLayout(new BorderLayout());

		roomList = new JList<>(model);


		JPanel topPanel = new JPanel(new BorderLayout());
		JButton createRoomButton = new JButton("방 만들기");
		JButton selectButton = new JButton("게임방 입장");
		stateLabel = new JLabel(nickname + "님, 안녕하세요. 좋은 하루입니다.");

		stateLabel.setHorizontalAlignment(JLabel.CENTER);
		loadingThread = new PlayerWaitingThread(stateLabel);

		topPanel.add(stateLabel, BorderLayout.CENTER);

		createRoomButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 방제목 입력
				String roomName = JOptionPane.showInputDialog(WaitingRoomPanel.this, "방 제목을 입력하세요: ", "방 만들기", JOptionPane.PLAIN_MESSAGE);
				if(roomName!=null && !roomName.trim().isEmpty()) {
					roomName = roomName.trim();

					// 서버에 새 방 생성 요청 보냄
					sendMessageToServer(MODE.CREATE_ROOM_MOD, roomName);

					loadingThread.start();
					threadPool.add(loadingThread);

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
	}

	public void initCommunicate() {
		updateRoomListThread = new UpdateRoomList();
		updateRoomListThread.start();
		threadPool.add(updateRoomListThread);

		// 첫 화면 띄우기 위한 데이터 가져오기
		sendMessageToServer(MODE.GET_ROOM_MOD, null);
	}

	// 서버에 메시지를 보내는 메소드
	private void sendMessageToServer(MODE mode, Serializable payload) {
	    try {
	    	if(objOs!= null) {
	    		objOs.writeObject(new MOD(mode, payload));
	    		objOs.flush();
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
			while(!stopThreads) {
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
							threadPool.add(gameStartThread);
						}

					}
				}catch (Exception e) {
					// TODO: handle exception
					break;
				}
				System.out.println("UpdateRoomList 스레드 도는중..");
			}
			System.out.println("UpdateRoomList 스레드의 while문 탈출!");
		}
	}

	private class PlayerWaitingThread extends Thread{
		private JLabel loading;
		private String origin = "참가자 대기중";

		public PlayerWaitingThread(JLabel loading) {
			setName("user-" + nickname + " : 대기 라벨 출력 스레드");
			this.loading = loading;
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
					break;
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
			setName("user-" + nickname + " : 게임 시작 안내 스레드");
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
			updateRoomListThread.interrupt();
			loadingThread.interrupt();
			this.interrupt();
			stopAllThreads();
			context.transition(new GamePanel(context, roomNum, myTeamColor), WaitingRoomPanel.this);
//			this.interrupt();
		}
	}
	private void stopAllThreads() {
		stopThreads = true;
	}
	@Override
	public void execute() {
		for (Thread th : threadPool) {
			th.interrupt();
		}
		this.socket = null;
		this.objOs = null;
		this.objIs = null;
	}
}
