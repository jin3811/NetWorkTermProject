package UI;
// 안씀
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.net.*;
@Deprecated
public class RoomPanel extends JPanel {
	private String nickname;
	private Socket socket;

	private DataOutputStream dos;
	private DataInputStream dis;
	private Map<String, JLabel> userReadyLabels; // 유저 닉네임과 준비 상태 라벨을 매핑합니다.
	private JButton readyButton;
	private boolean isReady = false; // 사용자의 준비 상태를 추적합니다.
	
	private JPanel usersPanel;

	public RoomPanel(RandomDefence context, String nickname, Socket socket) {
		this.nickname = nickname;
		this.socket = socket;
		usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS)); // 사용자 패널 레이아웃 설정

		userReadyLabels = new HashMap<>();

		try {
			// 스트림 초기화
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			System.out.println("소켓 가져오기 완료"); // 정상 확인
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setLayout(new BorderLayout());
		context.setSize(1000, 800);
		
		
        // 사용자 행을 추가하는 메서드 호출 (예시 사용자)
        addUserRow("User1", true); // 사용자 1은 준비 상태
        addUserRow("User2", false); // 사용자 2는 준비 안 됨

        // 중앙에 사용자 패널 추가
        add(usersPanel, BorderLayout.CENTER);
        

        // 하단에 준비 버튼 추가
        readyButton = new JButton("준비하기");
//        readyButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // 준비 상태 토글
//                isReady = !isReady;
//                updateReadyStatus(nickname, isReady);
//                
//                // 서버에 준비 상태 전송
//                sendReadyStatusToServer(isReady);
//                
//                // 모든 사용자가 준비되었는지 확인, 모두 준비되면 게임 시작
//                // 이 로직은 서버에서 처리되어야 합니다.
//                if (checkAllUsersReady()) {
////                    context.startGame();
//                }
//            }
//        });

        add(readyButton, BorderLayout.SOUTH);

	}
	private void addUserRow(String username, boolean isReady) {
        JPanel userRow = new JPanel(); // 개별 사용자 행 패널
        userRow.setLayout(new BorderLayout()); // 행 레이아웃 설정
        userRow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 여백 설정

        JLabel nameLabel = new JLabel(username); // 사용자 이름 라벨
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // 라벨 여백 설정

        JLabel statusLabel = new JLabel(isReady ? "Ready" : "Not Ready"); // 준비 상태 라벨
        statusLabel.setForeground(isReady ? Color.GREEN : Color.RED); // 준비 상태에 따른 색상 설정

        userRow.add(nameLabel, BorderLayout.WEST); // 이름 라벨을 행의 왼쪽에 추가
        userRow.add(statusLabel, BorderLayout.EAST); // 준비 상태 라벨을 행의 오른쪽에 추가

        usersPanel.add(userRow); // 사용자 행을 사용자 패널에 추가
    }
//	private void addUserRow(JPanel panel, String username) {
//        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.X_AXIS));
//        JPanel userRow = new JPanel(new BorderLayout());
//        JLabel nameLabel = new JLabel(username);
//
//        userRow.add(nameLabel, BorderLayout.SOUTH);
//        panel.add(userRow);
//        
//        JLabel lblUser = new JLabel("User2");
//        userRow.add(lblUser, BorderLayout.WEST);
//    }
//
//    private void updateReadyStatus(String username, boolean isReady) {
//        // GUI에서 사용자의 준비 상태를 업데이트
//        JLabel readyLabel = userReadyLabels.get(username);
//        if (readyLabel != null) {
//            readyLabel.setText(isReady ? "준비됨" : "준비 안됨");
//        }
//    }
//
//    private void sendReadyStatusToServer(boolean isReady) {
//        // 서버에 준비 상태를 전송
//        try {
//            dos.writeUTF(nickname + " " + (isReady ? "READY" : "NOT_READY"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private boolean checkAllUsersReady() {
//        // 모든 사용자가 준비되었는지 확인하는 메소드
//        // 실제 애플리케이션에서는 이 검사가 서버에서 수행되어야 합니다.
//        for (JLabel label : userReadyLabels.values()) {
//            if (label.getText().equals("준비 안됨")) {
//                return false;
//            }
//        }
//        return true;
//    }
}
