package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LoginFormPanel extends JPanel {

	// 화면에 표시할 라벨
    private JLabel ipLabel = new JLabel("IP");
    private JLabel portLabel = new JLabel("port");
    private JLabel nicknameLabel = new JLabel("nickname");
    // 로그인 실패시 표시할 라벨
    private JLabel loginFailLabel = new JLabel();

    private final int TFSIZE = 15;
    // 입력 필드 라벨
    private JTextField iptfield = new JTextField(TFSIZE);
    private JTextField porttfield = new JTextField(TFSIZE);
    private JTextField nicknametfield = new JTextField(TFSIZE);
    // 버튼
    private JButton accessBtn = new JButton("access");

    private Dimension labelSize = new Dimension(80, 30);
    private Dimension buttonSize = new Dimension(100, 25);

    private Socket socket; // connectToServer() 에서 초기화됨

    private RandomDefence context;

    private GridBagConstraints gbc;
    // 생성자
    public LoginFormPanel(RandomDefence context) {
        this.context = context;

        setLayout(new GridLayout(10, 10, 10, 10));
        setPreferredSize(new Dimension(300, 300));

        setWindow();
        // 컴포넌트들의 크기를 결정한다.
        setComponentSize();

        // 이벤트 리스너 등록
        setEventListener();

        // 화면에 배치함
        setDisplay();
    }
    // 프레임 크기 설정
    private void setWindow() {
        context.setSize(600, 600);
    }
    // 컴포넌트 크기 결정
    private void setComponentSize() {
        ipLabel.setSize(labelSize);
        portLabel.setSize(labelSize);
        nicknameLabel.setSize(labelSize);

        accessBtn.setSize(buttonSize);

        loginFailLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }
    // 이벤트 리스너 등록
    private void setEventListener() {
        accessBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = iptfield.getText();
                String port = porttfield.getText();
                String nickname = nicknametfield.getText();
                // IP, port, nickname 모두 입력 시 서버에 연결
                if (isIpFormat(ip) && isPortFormat(port) && !nickname.isEmpty()) {
                    System.out.println(ip + ":" + port + " " + nickname + " 접속시도");
                    connectToServer(ip, port, nickname); // 서버 연결 부분: 소켓 초기화
                    // WaitingRoomPanel로 전환
                    context.transition(new WaitingRoomPanel(context, nickname, socket));
                }
                else {
                    System.out.println("ip 또는 port 번호를 제대로 입력해주세요.");
                    loginFailLabel.setText("ip 또는 port 번호를 제대로 입력해주세요.");
                    loginFailLabel.setForeground(Color.red);
                }
            }
        });
    }

    // 입력한 ip가 localhost, 또는 ipv4 형식에 맞게 입력되었는지 확인
    private boolean isIpFormat(String ip) {
        return true;
    }

    // 입력한 port가 범위에 맞게 입력되었는지 확인
    private boolean isPortFormat(String port) {
//        try {
//            int portTest = Integer.parseInt(port);
//            return portTest >= 0 && portTest <= 65535;
//        }
//        catch (NumberFormatException e) {
//            return false;
//        }
    	return true;
    }
    // 모든 요소들을 추가
    private void setDisplay() {
        add(ipLabel);
        add(iptfield);

        add(portLabel);
        add(porttfield);

        add(nicknameLabel);
        add(nicknametfield);

        add(accessBtn);
        add(loginFailLabel);
    }
    // 서버 소켓에 연결
    private void connectToServer(String ip, String port, String nickname) {
        try {
        	// 받아온 ip, port로 소켓 연결
            socket = new Socket("localhost", 9999);//Integer.parseInt(port));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
