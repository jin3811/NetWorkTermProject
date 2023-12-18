package WatingRoomWindow;

import javax.naming.Context;
import javax.swing.*;

import Main.RandomDefence;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class WaitingRoomPanel extends JPanel {
	private String nickname;
    public WaitingRoomPanel(RandomDefence context ,String nickname) {
    	this.nickname = nickname;
    	context.setSize(1000,800);

    	setLayout(null);
    	
    	JPanel panelTop = new JPanel();
    	panelTop.setBounds(0, 0, getWidth(), 70);
    	this.add(panelTop);
    	panelTop.setLayout(null);
    	
    	JLabel waitRoomLabel = new JLabel("대기방");
    	waitRoomLabel.setHorizontalAlignment(SwingConstants.CENTER);
    	waitRoomLabel.setBounds(0, 0, 114, 70);
    	panelTop.add(waitRoomLabel);
    	
    	JButton createRoomBtn = new JButton("방만들기");
    	createRoomBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    		}
    	});
    	createRoomBtn.setBounds(711, 0, 114, 70);
    	panelTop.add(createRoomBtn);
    	
    	
    	// JPanel을 생성하고 추가
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(innerPanel);
        scrollPane.setBounds(0, 70, 825, 524);
        this.add(scrollPane);
    	
        
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

