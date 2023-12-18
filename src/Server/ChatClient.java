package Server;

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


public class ChatClient {

    private JFrame loginFrame, chatRoomFrame, chatFrame;
    private GamePanel gamePanel;

    private JTextField idField, portField, chatInputField;
    private JList<String> chatRoomList;
    private JTextArea chatArea;
    private DataOutputStream dos;
    private DataInputStream dis;
    private String selectedChatRoom;
    private Map<String, String> chatRoomUsers; // 채팅룸별 사용자 목록

    public ChatClient() {
        chatRoomUsers = new HashMap<>();
        initializeLoginFrame();
    }

    /*
     * 처음 실행시 보이는 로그인 화면
     * 텍스트필드 입력하고 버튼 클릭시 initializeChatRoomSelection()호출해
     * 채팅룸 선택화면으로 이동
     * */
    private void initializeLoginFrame() {
        loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 150);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new FlowLayout());

        idField = new JTextField(20);
        portField = new JTextField(20);
        JButton loginButton = new JButton("Login");

        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = idField.getText().trim();
               
                if (!userId.isEmpty()) {
                	// 처음에 서버 연결
                    connectToServer(userId);
                    initializeChatRoomSelection();
                    loginFrame.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "ID를 입력해주세요.");
                }
            }
        });

        loginFrame.add(idField);
        loginFrame.add(portField);
        loginFrame.add(loginButton);
        loginFrame.setVisible(true);
    }

    /*
     * 사용자가 채팅룸을 선택할 수 있도록 리스트 표시
     * 채팅룸 선택하고 입장버튼 클릭시 채팅화면 이동
     * */
    private void initializeChatRoomSelection() {
        chatRoomFrame = new JFrame("채팅룸 선택");
        chatRoomFrame.setSize(400, 300);
        chatRoomFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatRoomFrame.setLayout(new BorderLayout());

        // 채팅룸 목록 관리
        // model.addElement로 표시될 채팅룸을 설정하고 있음. 
        // 이는 이미 채팅룸이 정해져있다고 가정한것.
        // -> 직접 생성하도록 수정될 필요 있음.
        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("Chat Room 1"); 
        model.addElement("Chat Room 2");
        model.addElement("Chat Room 3");
        // JList<String>객체 chatRoomList에 해당 model 설정
        chatRoomList = new JList<>(model);

        // 채팅룸 사용자 정보 가져오기
        fetchChatRoomUsers();

        JButton selectButton = new JButton("채팅룸 입장");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// 현재 선택된 채팅룸 가져오기(String)
                selectedChatRoom = chatRoomList.getSelectedValue();
                if (selectedChatRoom != null) {
//                    initializeChatFrame(); // 채팅화면 초기화하는 함수 호출
                	initializeGameFrame(); // 게임 화면 초기화하는 메서드 호출
                    chatRoomFrame.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(chatRoomFrame, "채팅룸을 선택해주세요.");
                }
            }
        });

        chatRoomFrame.add(new JScrollPane(chatRoomList), BorderLayout.CENTER);
        chatRoomFrame.add(selectButton, BorderLayout.SOUTH);
        chatRoomFrame.setVisible(true);
    }
    // 채팅룸 선택 후 게임 화면으로 넘어가는 메서드를 추가
    private void initializeGameFrame() {
        // 게임 프레임을 초기화합니다.
        JFrame gameFrame = new JFrame("Random Defense Game");
        gameFrame.setSize(1000, 1000);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLayout(new BorderLayout());

        // GamePanel 인스턴스를 생성하고 프레임에 추가합니다.
        gamePanel = new GamePanel();
        gameFrame.add(gamePanel);

        gameFrame.setLocationRelativeTo(null); // 창을 화면 가운데에 위치시킵니다.
        gameFrame.setVisible(true);
        
//        new Thread(new GameServerListener()).start();
    }
    private void initializeChatFrame() {
        chatFrame = new JFrame(selectedChatRoom);
        chatFrame.setSize(500, 300);
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatInputField = new JTextField(40);
        JButton sendButton = new JButton("Send");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendChatMessage();
            }
        });

        chatFrame.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        chatFrame.add(chatInputField, BorderLayout.SOUTH);
        chatFrame.add(sendButton, BorderLayout.EAST);

        chatFrame.setVisible(true);
    }

    private void connectToServer(String userId) {
        try {
        	// 서버 연결
            Socket socket = new Socket("localhost", 9999);
            // 소켓 초기화
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            
            dos.writeUTF(userId); // 서버에 userId 전송
            System.out.println("userId: "+userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendChatMessage() {
        String message = chatInputField.getText().trim();
        if (!message.isEmpty()) {
            try {
                dos.writeUTF(selectedChatRoom + ": " + message); // 서버에 메시지 전송
                chatInputField.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 각 채팅룸에 대한 사용자 목록을 가상으로 생성하는중 -> 수정필요
    private void fetchChatRoomUsers() {
        // 서버로부터 채팅룸 사용자 정보를 가져오는 로직을 가정.
        // 실제 애플리케이션에서는 서버와의 통신을 통해 이 정보를 얻어야 함
        chatRoomUsers.put("Chat Room 1", "User1, User2");
        chatRoomUsers.put("Chat Room 2", "User3, User4");
        chatRoomUsers.put("Chat Room 3", "User5, User6");

        // 채팅룸 목록 ChatRoomList(JList)의 모델을 가져옴
        // 이 모델은 채팅룸 목록의 데이터를 관리함
        // 각 채팅룸에 해당 채팅룸의 사용자 목록 표시
        DefaultListModel<String> model = (DefaultListModel<String>) chatRoomList.getModel();
        // 모델의 모든 채팅룸 순회
        for (int i = 0; i < model.getSize(); i++) {
            String room = model.getElementAt(i);
            // chatRoomUsers 맵에서 room에 해당하는 사용자 목록 조회
            // 해당 채팅룸의 사용자 정보가 없으면 No users 반환
            String users = chatRoomUsers.getOrDefault(room, "No users");
            // 모델의 i번째 요소 업데이트
            // 출력 형식: Chat Room 1 (User1, User2)
            model.set(i, room + " (" + users + ")");
        }
    }

    public static void main(String[] args) {
        new ChatClient();
    }
    
//    private class GameServerListener implements Runnable {
//        @Override
//        public void run() {
//            try {
//                while (true) {
//                    // 서버로부터 메시지를 받아 처리합니다.
//                    String message = dis.readUTF();
//                    // 메시지에 따라 게임 패널을 업데이트합니다.
//                    // 예: gamePanel.updateGameState(message);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (dis != null) dis.close();
//                    if (socket != null) socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
}

//몬스터를 나타내는 클래스입니다.
class Monster {
 public int x, y; // 몬스터의 현재 위치입니다.
 public Monster(int startX, int startY) {
     x = startX;
     y = startY;
 }
}

//게임 패널을 나타내는 클래스입니다.
class GamePanel extends JPanel {
 private Monster monster; // 게임에 한 몬스터만 있는 경우입니다.
 private int[] pathX = {50, 200, 200, 350, 350, 500}; // 몬스터가 따라갈 x 좌표 경로입니다.
 private int[] pathY = {300, 300, 150, 150, 300, 300}; // 몬스터가 따라갈 y 좌표 경로입니다.
 private int pathIndex = 0; // 현재 몬스터가 경로의 어느 지점에 있는지 나타냅니다.

 public GamePanel() {
     monster = new Monster(pathX[0], pathY[0]); // 몬스터를 시작 지점에 배치합니다.

     // 게임의 메인 루프를 처리하는 타이머입니다.
     Timer timer = new Timer(100, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             // 몬스터가 다음 경로 지점으로 이동합니다.
             if (pathIndex < pathX.length - 1) {
                 pathIndex++;
                 monster.x = pathX[pathIndex];
                 monster.y = pathY[pathIndex];
             }
             repaint(); // 패널을 다시 그려 몬스터의 새 위치를 업데이트합니다.
         }
     });
     timer.start();
 }

 @Override
 protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     // 몬스터 이동 경로를 그립니다.
     g.setColor(Color.GRAY);
     g.drawPolyline(pathX, pathY, pathX.length);

     // 몬스터를 그립니다.
     g.setColor(Color.RED);
     g.fillOval(monster.x - 10, monster.y - 10, 20, 20); // 몬스터의 위치에 원을 그립니다.
 }
}

