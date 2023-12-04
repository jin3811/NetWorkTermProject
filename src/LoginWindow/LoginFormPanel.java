package LoginWindow;

import Main.RandomDefence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFormPanel extends JPanel {

    private JLabel ipLabel = new JLabel("IP");
    private JLabel portLabel = new JLabel("port");
    private JLabel nicknameLabel = new JLabel("nickname");

    private JLabel loginFailLabel = new JLabel();

    private final int TFSIZE = 15;
    private JTextField iptfield = new JTextField(TFSIZE);
    private JTextField porttfield = new JTextField(TFSIZE);
    private JTextField nicknametfield = new JTextField(TFSIZE);
    private JButton accessBtn = new JButton("access");

    private Dimension labelSize = new Dimension(80, 30);
    private Dimension buttonSize = new Dimension(100, 25);

//    private JPanel ipPanel = new JPanel(leftSortLayout);
//    private JPanel portPanel = new JPanel(leftSortLayout);
//    private JPanel btnPanel = new JPanel(leftSortLayout);

    private RandomDefence context;

    private GridBagConstraints gbc;
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

    private void setWindow() {
        context.setSize(600, 600);
    }

    private void setComponentSize() {
        ipLabel.setSize(labelSize);
        portLabel.setSize(labelSize);
        nicknameLabel.setSize(labelSize);

        accessBtn.setSize(buttonSize);

        loginFailLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setEventListener() {
        accessBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = iptfield.getText();
                String port = porttfield.getText();
                String nickname = nicknametfield.getText();

                if (isIpFormat(ip) && isPortFormat(port) && !nickname.isEmpty()) {
                    System.out.println(ip + ":" + port + " " + nickname + " 접속시도");
                    context.transition(LoginFormPanel.this, new TestPanel(context, nickname));
                }
                else {
                    System.out.println("ip 또는 port 번호를 제대로 입력해주세요.");
                    loginFailLabel.setText("ip 또는 port 번호를 제대로 입력해주세요.");
                    loginFailLabel.setForeground(Color.red);
                }
//                서버에 연결하는 부분
//                try {
//                    Socket socket = new Socket(ip, port);
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
            }
        });
    }

    // 입력한 ip가 localhost, 또는 ipv4 형식에 맞게 입력되었는지 테스트
    private boolean isIpFormat(String ip) {
        return ip.matches("localhost|((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
    }

    // 입력한 port가 범위에 맞게 입력되었는지 테스트
    private boolean isPortFormat(String port) {
        try {
            int portTest = Integer.parseInt(port);
            return portTest >= 0 && portTest <= 65535;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

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
}
