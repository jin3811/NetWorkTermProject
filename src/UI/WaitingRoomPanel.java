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
	private String nickname; // 닉네임
	private Socket socket; // 서버와 연결된 소켓

	private JList<String> roomList; // 방들의 리스트를 표시하는 JList
	private Map<String, String> gameRoomUsers; // 각 게임방에 있는 유저들의 정보를 저장
	private String selectedGameRoom; // 사용자가 선택한 게임방 이름
	DefaultListModel<String> model;// JList를 업데이트하기 위한 모델

	// 객체 입출력 스트림
	private ObjectInputStream objIs;
	private ObjectOutputStream objOS;

	private Thread updateRoomListThread; // 방 리스트를 업데이트하는 스레드
	private Thread loadingThread; // 플레이어 대기 상태를 표시하는 스레드
	private Thread gameStartThread; // 게임 시작 카운트다운을 처리하는 스레드

	private JLabel stateLabel; // 현재 상태를 표시하는 라벨
	private TEAM myTeamColor; // 플레이어가 소속된 팀

	private volatile boolean stopThreads = false; // 스레드 종료 플래그

	// 생성자
	public WaitingRoomPanel(RandomDefence context, String nickname, Socket socket) {
		// 멤버 변수 초기화
		this.context = context;
		this.nickname = nickname;

		model = new DefaultListModel<>();
		gameRoomUsers = new HashMap<>();

		try {
			this.socket = socket; // 로그인 패널에서 소켓 가져오기
			// 스트림 초기화
			objOS = new ObjectOutputStream(socket.getOutputStream());
			objIs = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}

		context.setSize(1000, 800); // Frame 사이즈 설정
		setLayout(new BorderLayout()); // 레이아웃 설정

		// 방 리스트 설정
		roomList = new JList<>(model);

		// 상태 표시 라벨
		stateLabel = new JLabel("참가자 대기중");
		stateLabel.setHorizontalAlignment(JLabel.CENTER);
		// 플레이어 대기 스레드 생성
		loadingThread = new PlayerWaitingThread(stateLabel);

		JPanel topPanel = new JPanel(new BorderLayout()); // 상단 패널 설정
		JButton createRoomButton = new JButton("방 만들기"); // 방 만들기 버튼
		JButton selectButton = new JButton("게임방 입장"); // 게임방 입장 버튼

		// 방 만들기 버튼 이벤트 리스너 설정
		createRoomButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 방 제목 입력받기
				String roomName = JOptionPane.showInputDialog(WaitingRoomPanel.this, "방 제목을 입력하세요: ", "방 만들기",
						JOptionPane.PLAIN_MESSAGE);
				if (roomName != null & !roomName.trim().isEmpty()) {
					roomName = roomName.trim();

					// 서버에 새 방 생성 요청 보내기
					sendMessageToServer(MODE.CREATE_ROOM_MOD, roomName);

					// 상태 표시 라벨을 topPanel에 추가, 로딩 스레드 시작
					topPanel.add(stateLabel, BorderLayout.CENTER);
					loadingThread.start();

					// 방장은 방 선택 버튼을 비활성화하여 다른 방 선택 못하게 함
					selectButton.setEnabled(false);
					myTeamColor = TEAM.RED; // 방장의 팀 색상은 RED로 설정
				}
			}
		});
		// 방 선택 버튼 이벤트 리스너 설정
		selectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 선택된 게임방 이름 가져오기
				selectedGameRoom = roomList.getSelectedValue();
				if (selectedGameRoom == null) {
					JOptionPane.showMessageDialog(context, "게임룸을 선택해주세요.");
				}

				// 선택된 방의 인덱스를 확인 후 서버에 참가 요청 보내기
				int a = roomList.getSelectedIndex();
				System.out.println(selectedGameRoom + a);
				topPanel.add(stateLabel, BorderLayout.CENTER);
				myTeamColor = TEAM.BLUE; // 참가자의 팀 색상은 BLUE로 설정
				sendMessageToServer(MODE.PARTICIPANT_MOD, selectedGameRoom);
			}
		});

		// topPanel에 방만들기 버튼 추가
		topPanel.add(createRoomButton, BorderLayout.EAST);
		// topPanel, JScrollPane, selectButton 추가
		add(topPanel, BorderLayout.NORTH);
		add(new JScrollPane(roomList), BorderLayout.CENTER);
		add(selectButton, BorderLayout.SOUTH);

		// 방 리스트 업데이트하는 스레드 시작
		updateRoomListThread = new UpdateRoomList();
		updateRoomListThread.start();

		// 첫 화면 띄우기 위한 데이터 가져오기
		// 서버에 처음 방 리스트 요청
		sendMessageToServer(MODE.GET_ROOM_MOD, null);

		setVisible(true);
	}

	// 서버에 메시지를 보내는 메소드
	private void sendMessageToServer(MODE mode, Object payload) {
		synchronized (objOS) {
			try {
				if (objOS != null) {
					objOS.writeObject(new MOD(mode, payload));
					objOS.flush();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(WaitingRoomPanel.this, "서버에 메시지를 보내는데 실패했습니다: " + ex.getMessage(),
						"통신 오류", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// 서버로부터 방 리스트를 주기적으로 받아와서 업데이트하는 스레드
	private class UpdateRoomList extends Thread {
		private Vector<Room> rooms; // 서버로부터 받은 방 목록을 저장할 벡터
		String roomName; // 방 이름을 임시로 저장하는 변수

		@Override
		public void run() {
			// 스레드 중지 플래그가 false인 동안 반복
			while (!stopThreads) {
				synchronized (objIs) {
					try {
						// 서버로부터 패킷을 받음
						MOD packet = (MOD) objIs.readObject();
						MODE mode = packet.getMode();

						// 방 생성 실패 메시지는 무시하고 계속 진행
						if (mode == MODE.FAIL_CREATE_ROOM_MOD)
							continue;
						// 방 리스트를 성공적으로 받았을 경우
						else if (mode == MODE.SUCCESS_GET_ROOM_MOD || mode == MODE.SUCCESS_CREATE_ROOM_MOD) {
							rooms = (Vector<Room>) packet.getPayload();

							System.out.println("room 개수: " + rooms.size());

							// 리스트 모델을 비우고 새로운 데이터로 채우기
							model.clear();
							for (Room room : rooms) {
								String roomName = room.getRoomName();

								model.addElement(roomName); // 모델에 방 이름 추가
								roomList.setSelectedValue(roomName, true); // 해당 방을 선택 상태로 만듦
							}
						}
						// 게임 시작 신호를 받았을 경우
						else if (mode == MODE.GAME_READY_SIGNAL_MOD) {
							long roomNum = (long) packet.getPayload();
							System.out.println(nickname + " : 게임 시작신호 받음");
							// 로딩 스레드가 있다면 중단하고 게임 시작 스레드 시작
							if (loadingThread != null) {
								loadingThread.interrupt();
								gameStartThread = new GameStartThread(roomNum);
								gameStartThread.start();
							}

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("UpdateRoomList 스레드 종료 X 상태");
			}
			System.out.println("UpdateRoomList 스레드 종료!");
		}
	}

	// 플레이어가 게임 시작을 기다리는 동안 상태 메시지를 업데이트하는 스레드
	private class PlayerWaitingThread extends Thread {
		private JLabel loading; // 로딩 상태를 표시할 라벨
		private String origin; // 원래 라벨 텍스트

		public PlayerWaitingThread(JLabel loading) {
			this.loading = loading;
			origin = loading.getText();
		}

		@Override
		public void run() {
			int a = 0;
			String last;
			// 스레드 중지 플래그가 false인 동안 반복
			while (!stopThreads) {
				last = "";
				// 로딩 애니메이션 효과를 위한 점 추가
				for (int i = 0; i < a; i++) {
					last += ".";
				}
				loading.setText(origin + last); // 라벨 텍스트 업데이트
				a = (a + 1) % 4;

				try {
					sleep(250); // 0.25초마다 텍스트 업데이트
				} catch (InterruptedException e) {
					System.out.println("게임 화면으로 넘어가기 위해 참여자 대기 스레드 종료");
					break;
				}
			}
		}
	}

	// 게임 시작 카운트다운을 처리하고 게임 패널로 전환하는 스레드
	private class GameStartThread extends Thread {
		private long roomNum; // 게임 방 번호

		public GameStartThread(long roomNum) {
			this.roomNum = roomNum;
		}

		@Override
		public void run() {
			// 로딩 스레드 중지
			loadingThread.interrupt();
			// 5초 카운트다운
			for (int i = 5; i >= 0; i--) {
				stateLabel.setText(i + "초후 게임이 시작됩니다. 준비해주세요.");
				try {
					sleep(1000); // 1초 대기
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			// 모든 스레드 중지
			updateRoomListThread.interrupt();
			loadingThread.interrupt();
			this.interrupt();
			// 모든 스레드 중지하는 메소드 호출
			stopAllThreads();
			// 게임 패널로 전환
			context.transition(new GamePanel(context, nickname, socket, objOS, objIs, roomNum, myTeamColor));
		}
	}

	// 모든 스레드를 안전하게 중지하기 위한 메소드
	private void stopAllThreads() {
		// 중지 플래그를 true로 설정하여 모든 스레드가 종료되도록 함
		stopThreads = true;
	}
}
