package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatClient {

    private JFrame loginFrame, chatRoomFrame, chatFrame;
    private JTextField idField, chatInputField;
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
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = idField.getText().trim();
               
                if (!userId.isEmpty()) {
                    connectToServer(userId);
                    initializeChatRoomSelection();
                    loginFrame.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "ID를 입력해주세요.");
                }
            }
        });

        loginFrame.add(idField);
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
                    initializeChatFrame(); // 채팅화면 초기화하는 함수 호출
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
        // 서버에 연결하고 스트림을 초기화
        try {
            Socket socket = new Socket("localhost", 9999);
            dos = new DataOutputStream(socket.getOutputStream());
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
}