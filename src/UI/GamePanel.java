package UI;

import javax.imageio.ImageIO;
import javax.naming.ldap.SortKey;
import javax.swing.*;

import Component.Turret;
import Server.Room;
import util.MOD;
import util.MODE;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.List;
// 실제 게임을 진행하는 panel
public class GamePanel extends JPanel {
	private RandomDefence context;
	private Image grassImage;
	private Image pathImage1;
	private Image pathImage2;
	private Image redTeamImage;
	private Image blueTeamImage;
	private Image spawnerImage;
	private Image turret1Image;
	private Image turret2Image;
	private Image monsterImage;

	// 포탑 위치 저장하는 리스트
	List<Point> turrets = new ArrayList<>();

	List<Point> allPoints = new ArrayList<>();
	List<Point> bluePath;
	List<Point> redPath;

	private ObjectOutputStream objOs;
	private ObjectInputStream objIs;
	private Socket socket;
	private long roomNum;

	public GamePanel(RandomDefence context, String nickname, Socket socket, long roomNum) {
		this.context = context;
		this.socket = socket;
		this.roomNum = roomNum;

		System.out.println("GamePanel 입장");
		context.setSize(1000, 1000);
//        setPreferredSize(new Dimension(1000, 1000));
//        context.pack();
		setLayout(new BorderLayout());
		
		for(int i=0;i<1000;i+=50) {
			for(int j=0;j<1000;j+=50) {
				Point p = new Point(j,i);
				allPoints.add(p);
			}
		}
		redPath = new ArrayList<Point>() {
			{
				// 윗 부분
				add(allPoints.get(22));
				add(allPoints.get(23));
				add(allPoints.get(24));
				add(allPoints.get(25));
				add(allPoints.get(26));
				add(allPoints.get(27));
				add(allPoints.get(28));
				add(allPoints.get(29));
				add(allPoints.get(41));
				add(allPoints.get(49));
				add(allPoints.get(61));
				add(allPoints.get(69));
				add(allPoints.get(81));
				add(allPoints.get(89));
				add(allPoints.get(101));
				add(allPoints.get(109));
				add(allPoints.get(121));
				add(allPoints.get(129));
				add(allPoints.get(141));
				add(allPoints.get(149));
				add(allPoints.get(161));
				add(allPoints.get(169));
				add(allPoints.get(181));
				add(allPoints.get(182));
				add(allPoints.get(183));
				add(allPoints.get(184));
				add(allPoints.get(185));
				add(allPoints.get(186));
				add(allPoints.get(187));
				add(allPoints.get(188));
				
				// 아랫 부분
				add(allPoints.get(201));
				add(allPoints.get(202));
				add(allPoints.get(203));
				add(allPoints.get(204));
				add(allPoints.get(205));
				add(allPoints.get(206));
				add(allPoints.get(207));
				add(allPoints.get(208));

				
				add(allPoints.get(221));
				add(allPoints.get(229));
				add(allPoints.get(241));
				add(allPoints.get(249));
				add(allPoints.get(261));
				add(allPoints.get(269));
				add(allPoints.get(281));
				add(allPoints.get(289));
				add(allPoints.get(301));
				add(allPoints.get(309));
				add(allPoints.get(321));
				add(allPoints.get(329));
				add(allPoints.get(341));
				add(allPoints.get(349));
				
				
				add(allPoints.get(362));
				add(allPoints.get(363));
				add(allPoints.get(364));
				add(allPoints.get(365));
				add(allPoints.get(366));
				add(allPoints.get(367));
				add(allPoints.get(368));
				add(allPoints.get(369));
				
			}
		};
		bluePath = new ArrayList<Point>() {
			{
				// 윗 부분
				add(allPoints.get(30));
				add(allPoints.get(31));
				add(allPoints.get(32));
				add(allPoints.get(33));
				add(allPoints.get(34));
				add(allPoints.get(35));
				add(allPoints.get(36));
				add(allPoints.get(37));
				add(allPoints.get(50));
				add(allPoints.get(58));
				add(allPoints.get(70));
				add(allPoints.get(78));
				add(allPoints.get(90));
				add(allPoints.get(98));
				add(allPoints.get(110));
				add(allPoints.get(118));
				add(allPoints.get(130));
				add(allPoints.get(138));
				add(allPoints.get(150));
				add(allPoints.get(158));
				add(allPoints.get(170));
				add(allPoints.get(178));
				add(allPoints.get(191));
				add(allPoints.get(192));
				add(allPoints.get(193));
				add(allPoints.get(194));
				add(allPoints.get(195));
				add(allPoints.get(196));
				add(allPoints.get(197));
				add(allPoints.get(198));
				
				// 아랫 부분
				add(allPoints.get(211));
				add(allPoints.get(212));
				add(allPoints.get(213));
				add(allPoints.get(214));
				add(allPoints.get(215));
				add(allPoints.get(216));
				add(allPoints.get(217));
				add(allPoints.get(218));

				add(allPoints.get(230));
				add(allPoints.get(238));
				add(allPoints.get(250));
				add(allPoints.get(258));
				add(allPoints.get(270));
				add(allPoints.get(278));
				add(allPoints.get(290));
				add(allPoints.get(298));
				add(allPoints.get(310));
				add(allPoints.get(318));
				add(allPoints.get(330));
				add(allPoints.get(338));
				add(allPoints.get(350));
				add(allPoints.get(358));
				
				
				add(allPoints.get(370));
				add(allPoints.get(371));
				add(allPoints.get(372));
				add(allPoints.get(373));
				add(allPoints.get(374));
				add(allPoints.get(375));
				add(allPoints.get(376));
				add(allPoints.get(377));
				
			}
		};
		System.out.println("좌표 리스트 크기: "+allPoints.size());
		System.out.println("좌표 리스트 마지막: "+ allPoints.get(399));
//		for(Point p: allPoints) {
//			System.out.println("좌표: " + p);
//		}
		
		try {
			grassImage = ImageIO.read(getClass().getResource("/Image/grass.png"));
			pathImage1 = ImageIO.read(getClass().getResource("/Image/ground1.png"));
			pathImage2 = ImageIO.read(getClass().getResource("/Image/ground2.png"));
			redTeamImage = ImageIO.read(getClass().getResource("/Image/red.png"));
			blueTeamImage = ImageIO.read(getClass().getResource("/Image/blue.png"));
			spawnerImage = ImageIO.read(getClass().getResource("/Image/spawner.png"));
			turret1Image = ImageIO.read(getClass().getResource("/Image/turret1.png")); // 추가 예정
			turret2Image = ImageIO.read(getClass().getResource("/Image/turret2.png")); // 추가 예정
			monsterImage = ImageIO.read(getClass().getResource("/Image/monster.png")); // 추가 예정
		} catch (IOException e) {
			e.printStackTrace();
		}

		// GamePanel에 MouserListener 추가
		addMouseListener(new MouseAdapter() {
			// 마우스 클릭시 호출
			@Override
			public void mouseClicked(MouseEvent e) {
				Point clickPoint = e.getPoint();
				System.out.println("클릭: " + clickPoint);
				
				// 클릭 point의 타일 좌상단 구석 계산
				int tileSize = 50;
				int tileX = (clickPoint.x / tileSize) * tileSize;
				int tileY = (clickPoint.y / tileSize) * tileSize;

				Point turretPoint = new Point(tileX, tileY);
//				Rectangle turretRect = new Rectangle(tileX, tileY, tileSize, tileSize);
				
				// 서버에 포탑 배치 요청
				sendTurretRequest(turretPoint);
				
				// 잔디 타일 위를 클릭했는지 확인하고 포탑 위치 추가
				if (isGrassTile(clickPoint)) {
					// 서버에 포탑 배치 요청
					sendTurretRequest(turretPoint);
				}
			}

			
		});

		try {
			objOs = new ObjectOutputStream(socket.getOutputStream());
			objIs = new ObjectInputStream(socket.getInputStream());

			objOs.writeObject(new MOD(MODE.GAME_START_MOD, roomNum));
			objOs.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		setVisible(true);
	}
	// 서버에서 사용할 코드(미리 적어놓음)
	public void handleTurretRequest(Point turretPoint) {
		
	}
	// 서버에 포탑 배치 요청 보내기
	private void sendTurretRequest(Point turretPoint) {
		// TODO Auto-generated method stub
		
	}
	// 서버 응답 처리
	private void handleServerResponse(Object response) {
		// 1. 서버로부터 응답을 받는다
		// 2. 응답받은 것에서 turret의 Position을 꺼낸다
		// 3. turrets에 받아온 turret의 Position을 추가한다.
		// 4. repaint() 호출하여 다시 그린다.
		
		repaint();
	}
	// 클릭한 위치가 잔디 타일 위인지 확인하는 메소드
	private boolean isGrassTile(Point clickPoint) {
		// 임시로 잔디 타일이라고 가정하고 true 반환
		// 실제로는 잔디 타일 위치를 계산하여 확인해야 함.
		return true;
	}

	// 1. 초기에 불려짐
	// 2. repaint() 호출시 불려짐
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// 타일맵 : 잔디
		drawGrassBackground(g);
		drawRedTeam(g);
		drawBlueTeam(g);
		drawSpawner(g);
		drawPath_red(g);
		drawPath_blue(g);
		
		// 포탑 위치에 포탑 이미지 그리기
		for (Point turret : turrets) {
			g.drawImage(turret1Image, turret.x, turret.y, this);
		}

	}

	private void drawPath_red(Graphics g) {
		if (pathImage1 != null) {
			
			int pathWidth = pathImage1.getWidth(this);
			int pathHeight = pathImage1.getHeight(this);
//			for(int i=22;i<=29;i++) {
//				g.drawImage(pathImage1, allPoints.get(i).x, allPoints.get(i).y, this);
//			}
//			for(int i=362;i<=369;i++) {
//				g.drawImage(pathImage1, allPoints.get(i).x, allPoints.get(i).y, this);
//			}
//			for(int i=181;i<=188;i++) {
//				g.drawImage(pathImage1, allPoints.get(i).x, allPoints.get(i).y, this);
//			}
//			for(int i=201;i<=208;i++) {
//				g.drawImage(pathImage1, allPoints.get(i).x, allPoints.get(i).y, this);
//			}
//			for(int i=41;i<=161;i+=20) {
//				g.drawImage(pathImage1, allPoints.get(i).x, allPoints.get(i).y, this);
//			}
//			for(int i=221;i<=341;i+=20) {
//				g.drawImage(pathImage1, allPoints.get(i).x, allPoints.get(i).y, this);
//			}
//			for(int i=29;i<=169;i+=20) {
//				g.drawImage(pathImage1, allPoints.get(i).x, allPoints.get(i).y, this);
//			}
//			for(int i=229;i<=349;i+=20) {
//				g.drawImage(pathImage1, allPoints.get(i).x, allPoints.get(i).y, this);
//			}
			for(Point p: redPath) {
				g.drawImage(pathImage1, p.x, p.y, this);
			}
		}
	}
	private void drawPath_blue(Graphics g) {
		if (pathImage2 != null) {
			int pathWidth = pathImage2.getWidth(this);
			int pathHeight = pathImage2.getHeight(this);
			for(Point p: bluePath) {
				g.drawImage(pathImage2, p.x, p.y, this);
			}
		}
		
	}


	private void drawSpawner(Graphics g) {
		if (spawnerImage != null) {
			int spawnerWidth = spawnerImage.getWidth(this);
			int spawnerHeight = spawnerImage.getHeight(this);
			g.drawImage(spawnerImage, 450, 450, spawnerWidth, spawnerHeight, this);
		}
	}

	private void drawRedTeam(Graphics g) {
		if (redTeamImage != null) {
			int redWidth = redTeamImage.getWidth(this);
			int redHeight = redTeamImage.getHeight(this);
			g.drawImage(redTeamImage, 0, 0, redWidth, redHeight, this);
//			g.drawImage(redTeamImage, 0, 450, redWidth, redHeight, this);
			g.drawImage(redTeamImage, 0, 900, redWidth, redHeight, this);
		}
	}

	private void drawBlueTeam(Graphics g) {
		if (blueTeamImage != null) {
			int blueWidth = blueTeamImage.getWidth(this);
			int blueHeight = blueTeamImage.getHeight(this);
			g.drawImage(blueTeamImage, 900, 0, blueWidth, blueHeight, this);
//			g.drawImage(blueTeamImage, 900, 450, blueWidth, blueHeight, this);
			g.drawImage(blueTeamImage, 900, 900, blueWidth, blueHeight, this);
		}

	}

	private void drawGrassBackground(Graphics g) {
		if (grassImage != null) {
			int tileWidth = grassImage.getWidth(this);
			int tileHeight = grassImage.getHeight(this);
			System.out.println("tileWidth, tileHeight = " + tileWidth + ", " + tileHeight);
			System.out.println("전체 너비 " + getWidth());
			System.out.println("전체 높이 " + getHeight());
			for (int y = 0; y < getHeight(); y += tileHeight) {
				for (int x = 0; x < getWidth(); x += tileWidth) {
					g.drawImage(grassImage, x, y, tileWidth, tileHeight, this);
				}
			}
		}
	}
}
