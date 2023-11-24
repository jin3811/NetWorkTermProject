import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.*;

public class WatingRoomList {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WatingRoomList window = new WatingRoomList();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WatingRoomList() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panelTop = new JPanel();
		panelTop.setBounds(0, 0, 986, 86);
		frame.getContentPane().add(panelTop);
		panelTop.setLayout(null);

		JButton btnNewButton = new JButton("방 만들기");
		btnNewButton.setBounds(388, 36, 202, 40);
		panelTop.add(btnNewButton);

		JLabel watingRoomLabel = new JLabel("대기방");
		watingRoomLabel.setHorizontalAlignment(SwingConstants.CENTER);
		watingRoomLabel.setBounds(460, 0, 54, 32);
		panelTop.add(watingRoomLabel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 84, 986, 669);
		frame.getContentPane().add(scrollPane);

		JPanel panelBottom = new JPanel();
		scrollPane.setViewportView(panelBottom);
		panelBottom.setLayout(new BoxLayout(panelBottom, BoxLayout.Y_AXIS));

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String roomTitle = JOptionPane.showInputDialog("방 제목 입력");
				addWatingRoom(roomTitle, panelBottom);
			}

		});
	}

	private void addWatingRoom(String roomTitle, JPanel panel) {
		JButton waitingRoomButton = new JButton(roomTitle);
		waitingRoomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int answer = JOptionPane.showConfirmDialog(null, "입장하시겠습니까?", roomTitle, JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					// 입장 시 동작 기능 구현해야함
				}
			}
		});
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int buttonWidth = (int) (screenSize.getWidth() * 0.8);
		int buttonHeight = 40; // 버튼 높이는 40으로 고정, 필요에 따라 조절 가능
		waitingRoomButton.setMaximumSize(new Dimension(buttonWidth, buttonHeight)); // 버튼 사이즈 설정

		// 위아래 간격을 위한 빈 공간 추가
		panel.add(Box.createVerticalStrut(10)); // 간격은 10(임시)
		panel.add(waitingRoomButton);
		panel.add(Box.createVerticalStrut(10)); // 간격은 10(임시)

		panel.revalidate();
		panel.repaint();
	}
}
