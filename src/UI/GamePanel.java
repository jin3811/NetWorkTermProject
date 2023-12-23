package UI;

import javax.imageio.ImageIO;
import javax.naming.ldap.SortKey;
import javax.swing.*;

import Component.Monster;
import Component.Turret;
import Server.Room;
import util.BlueArea;
import util.BluePath;
import util.MOD;
import util.MODE;
import util.RedArea;
import util.RedPath;
import util.RestrictArea;
import util.TEAM;
import util.MonsterPosPair;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

// 실제 게임을 진행하는 panel(클라이언트)
public class GamePanel extends JPanel {
	private RandomDefence context;
	// 사용할 이미지들
	private Image grassImage;
	private Image pathImage1;
	private Image pathImage2;
	private Image redTeamImage;
	private Image blueTeamImage;
	private Image spawnerImage;
	private Image turret1Image;
	private Image turret2Image;
	private Image turret3Image;
	private Image monsterImage;

	// 더블 버퍼링 용도
	private Image bufferImage;
	private Graphics bufferGraphics;

	// 몬스터들이 이동하는 Point들의 List
	List<Point> allPoints = new ArrayList<>();
	List<Point> bluePath;
	List<Point> redPath;

	// 객체 입출력 스트림
	private ObjectOutputStream objOs;
	private ObjectInputStream objIs;

	// 클라이언트 소켓
	private Socket socket;

	// 팀, 골드, 생명을 표시할 라벨
	private JLabel teamLabel;
	private JLabel goldLabel;
	private JLabel lifeLabel;

	// 우측에 팀, 골드, 생명 표시할 Panel
	private JPanel statusPanel;

	private TEAM team; // 팀
	private int gold; // 골드
	private int life; // 체력
	private static final int MAX_LEVEL = 3; // 터렛 최대 레벨

	private long roomNum; // 방번호

	// 게임 종료 시 띄울 메시지
	private String endGameMessage = null;

	// 클라이언트가 서버로부터 객체를 받기 위한 스레드
	private Thread clientReceiverThread;

	// 터렛 설치구역 가지는 싱글톤 객체
	private BlueArea blueAreaInstance;
	private RedArea redAreaInstance;

	// 몬스터 이동구역 가지는 싱글톤 객체
	private BluePath bluePathInstance;
	private RedPath redPathInstance;

	// 제한 구역 가지는 싱글톤 객체
	private RestrictArea restrictAreaInstance;

	// 터렛 위치 저장하는 리스트
	CopyOnWriteArrayList<Turret> myTurrets = new CopyOnWriteArrayList<>();
	CopyOnWriteArrayList<Turret> enemyTurrets = new CopyOnWriteArrayList<>();

	// 몬스터 위치 저장하는 리스트
	CopyOnWriteArrayList<Point> monsters = new CopyOnWriteArrayList<>();

	// 생성자
	public GamePanel(RandomDefence context, String nickname, Socket socket, ObjectOutputStream objOs,
			ObjectInputStream objIs, long roomNum, TEAM team) {
		this.context = context;
		this.socket = socket; // 소켓
		this.objOs = objOs;
		this.objIs = objIs;
		this.roomNum = roomNum; // 방번호
		this.team = team; // 팀
		this.gold = 100; // 초기 gold 100
		this.life = 5; // 초기 life 5

		// 각 팀별 포탑 설치 가능 구역, 각 팀별 몬스터 이동 구역, 제한 구역(설치불가) 정보를 가진 싱글톤 객체
		blueAreaInstance = BlueArea.getInstance();
		redAreaInstance = RedArea.getInstance();
		bluePathInstance = BluePath.getInstance();
		redPathInstance = RedPath.getInstance();
		restrictAreaInstance = RestrictArea.getInstance();

		System.out.println("GamePanel 입장");
		context.setSize(1220, 1050); // Frame 사이즈 조절
		setLayout(new BorderLayout()); // 레이아웃 설정

		// 상태 정보(팀,골드,생명)를 보여주기 위한 패널
		statusPanel = new JPanel();
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
		statusPanel.setPreferredSize(new Dimension(200, 1000));

		// 팀 정보를 표시할 라벨
		teamLabel = new JLabel("Team: " + (this.team == TEAM.RED ? "Red" : "Blue"));
		statusPanel.add(teamLabel);

		// 골드를 표시할 라벨
		goldLabel = new JLabel("Gold: " + this.gold);
		statusPanel.add(goldLabel);

		// 생명을 표시할 라벨
		lifeLabel = new JLabel("Life: " + this.life);
		statusPanel.add(lifeLabel);

		// 상태 정보 패널을 GamePanel에 추가
		this.add(statusPanel, BorderLayout.EAST);

		// 게임 상태(골드, 팀 정보 등)를 업데이트하는 메소드
		updateGameStatus();

		// 50x50 이미지들을 1000x1000 화면에 그릴 것이므로 이를 List<Point>에 넣음.
		for (int i = 0; i < 1000; i += 50) {
			for (int j = 0; j < 1000; j += 50) {
				Point p = new Point(j, i);
				allPoints.add(p);
			}
		}
		// 몬스터 이동 통로(레드)
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
		// 몬스터 이동 통로(블루)
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
		System.out.println("좌표 리스트 크기: " + allPoints.size());
		System.out.println("좌표 리스트 마지막: " + allPoints.get(399));

		// 사용할 이미지 가져오기
		try {
			grassImage = ImageIO.read(getClass().getResource("/Image/grass.png"));
			pathImage1 = ImageIO.read(getClass().getResource("/Image/ground1.png"));
			pathImage2 = ImageIO.read(getClass().getResource("/Image/ground2.png"));
			redTeamImage = ImageIO.read(getClass().getResource("/Image/red.png"));
			blueTeamImage = ImageIO.read(getClass().getResource("/Image/blue.png"));
			spawnerImage = ImageIO.read(getClass().getResource("/Image/spawner.png"));
			turret1Image = ImageIO.read(getClass().getResource("/Image/turret1.png"));
			turret2Image = ImageIO.read(getClass().getResource("/Image/turret2.png"));
			turret3Image = ImageIO.read(getClass().getResource("/Image/turret3.png"));
			monsterImage = ImageIO.read(getClass().getResource("/Image/monster.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// GamePanel에 MouseListener 추가
		addMouseListener(new MouseAdapter() {
			// 마우스 클릭시 호출
			@Override
			public void mouseClicked(MouseEvent e) {
				Point clickPoint = e.getPoint();
				System.out.println("클릭: " + clickPoint);

				// 클릭한 Point의 좌상단 구석을 turretPoint로 설정
				int tileSize = 50;
				int tileX = (clickPoint.x / tileSize) * tileSize;
				int tileY = (clickPoint.y / tileSize) * tileSize;
				Point turretPoint = new Point(tileX, tileY); // turretPoint 지정

				// 해야하는 것: 클라이언트단에서 포탑 설치 및 업그레이드를 한 후 해당 정보를 서버로 전송한다.
				// 초기 설정: 내가 배정된 팀의 잔디 구역(포탑 구역)에 0레벨 포탑 설치한다(Turret) - 이 경우에는 잔디를 그려준다.
				// 해당 팀의 잔디 구역(포탑 구역)을 클릭하면 메서드를 이용해서 포탑의 레벨을 올린다(물론 돈이 있고, 최대 레벨이 아니면)
				// 포탑의 레벨이 1레벨 이상이면 그때부터 내가 배정된 팀의 잔디구역에 포탑 이미지를 그릴 수 있도록 한다.
				// 그럼 필요한 것:
				// 클릭한 구역이 내 구역인지 확인 && 몬스터 통로 구역이 아님을 확인 && 설치 불가 구역이 아님을 확인

				// 터렛 설치 가능 구역인지 확인(내 구역인지, 몬스터 통로 아닌지, 제한 구역 아닌지)
				if (isValidTurretPlacement(turretPoint, team)) {
					// 클릭된 위치의 터렛을 가져온다. (몇 레벨이든 터렛 설치 가능 구역 위치엔 터렛이 존재하는 것으로 설정되어 있음(0레벨일 때는 안보임))
					Turret existingTurret = getTurretAtPoint(turretPoint);
					// 업그레이드가 가능한지 확인 -> 가능하면 여기서 골드 차감 진행
					if (isCanUpgrade(existingTurret)) {
						// 내 해당 포탑 업그레이드 실행
						existingTurret.upgrade();
						// 업그레이드가 반영된 내 포탑 정보(ArrayList<Turret>) 서버로 전송
						sendMessageToServer(MODE.TURRET_UPDATE_MOD, new ArrayList<Turret>(myTurrets));
						System.out.println(team + "업데이트 요청함");
					}
				}
			}
		});

		// 서버에 게임 시작한다고 알림
		synchronized (objOs) {
			try {
				objOs.reset();
				objOs.writeObject(new MOD(MODE.GAME_START_MOD, roomNum));
				objOs.flush();

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		initTurrets(); // 내 포탑 설치 가능 구역에 모두 0레벨 터렛 설치
		clientReceiverThread = new ClientReceiver(); // 서버로부터 객체를 받는 스레드
		clientReceiverThread.start(); // 스레드 시작
		setVisible(true);
	}

	// 포탑 배치가 유효한지 확인
	private boolean isValidTurretPlacement(Point turretPoint, TEAM team) {
		return isWithinTeamArea(turretPoint, team) && !isMonsterPathArea(turretPoint) && !isRestrictedArea(turretPoint);
	}
	// 팀 구역(잔디) 내에 있는지 확인
	private boolean isWithinTeamArea(Point turretPoint, TEAM team) {
		// 1. 내 팀에 따라 팀구역 List<Point> 가져오기
		// 2. 클릭한 좌표가 팀구역에 포함되어 있는지 확인
		List<Point> teamArea = (team == TEAM.RED) ? redAreaInstance.getRedArea() : blueAreaInstance.getBlueArea();
		return teamArea.contains(turretPoint);
	}
	// 몬스터 통로인지 확인
	private boolean isMonsterPathArea(Point turretPoint) {
		// 1. 모든 몬스터 통로를 가져온다.
		// 2. 클릭한 좌표가 몬스터 통로에 포함하는지 확인한다.
		List<Point> paths = new ArrayList<>();
		paths.addAll(bluePathInstance.getDirection1());
		paths.addAll(bluePathInstance.getDirection2());
		paths.addAll(bluePathInstance.getDirection3());
		paths.addAll(bluePathInstance.getDirection4());
		paths.addAll(redPathInstance.getDirection1());
		paths.addAll(redPathInstance.getDirection2());
		paths.addAll(redPathInstance.getDirection3());
		paths.addAll(redPathInstance.getDirection4());
		return paths.contains(turretPoint);
	}
	// 제한 구역인지 확인
	private boolean isRestrictedArea(Point turretPoint) {
		// 1. 금지구역(빈공간, 깃발, 스포너) 가져온다.
		// 2. 클릭한 좌표가 제한구역에 포함되는지 확인한다.
		List<Point> restrictArea = restrictAreaInstance.getRestrictArea();
		return restrictArea.contains(turretPoint);
	}
	// 서버에 객체 전송하는 메서드
	private void sendMessageToServer(MODE mode, Object payload) {
		synchronized (objOs) {
			try {
				if (objOs != null) {
					objOs.reset(); // 보내는 건 MOD 객체에 mode와 payload를 실어 보낸다.
					objOs.writeObject(new MOD(mode, payload));
					objOs.flush();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	// 현재 클릭된 위치의 터렛이 업그레이드 가능한지 확인, 가능한 상태라면 골드 차감
	private boolean isCanUpgrade(Turret existingTurret) {
		int level = existingTurret.getLevel();
		
		if (level >= MAX_LEVEL) { // 3레벨이면 최대레벨이므로 false
			return false;
		}
		
		if (level == 0) { // 0렙이면 100골드 차감
			if (gold >= 100) {
				gold -= 100;
				return true;
			}
			return false;
		} else if (level == 1) { // 1렙이면 300골드 차감
			if (gold >= 300) {
				gold -= 300;
				return true;
			}
			return false;
		} else if (level == 2) { // 2렙이면 500골드 차감
			if (gold >= 500) {
				gold -= 500;
				return true;
			}
			return false;
		}
		return false;
	}
	// 클릭된 Point의 터렛을 가져온다.
	private Turret getTurretAtPoint(Point point) {
		for (Turret turret : myTurrets) {
			if (turret.getPoint().equals(point)) {
				return turret;
			}
		}
		return null;
	}
	// 게임 시작시 내 팀 모든 터렛 설치 가능 구역에 0레벨 포탑 설정
	private void initTurrets() {
		// 내 팀구역 가져오기
		List<Point> teamArea = (team == TEAM.RED) ? redAreaInstance.getRedArea() : blueAreaInstance.getBlueArea();
		// 내 팀구역 Point에 Turret 설치
		for (Point point : teamArea) {
			Turret turret = new Turret(point, team);
			myTurrets.add(turret);
		}
		// 서버에 터렛 업데이트 했다고 알리기
		sendMessageToServer(MODE.TURRET_UPDATE_MOD, new ArrayList<Turret>(myTurrets));
	}
	// teamLabel, goldLabel, lifeLabel을 다시 그려주는 메서드
	public void updateGameStatus() {
		// 팀 라벨의 텍스트 업데이트
		teamLabel.setText("Team: " + (this.team == TEAM.RED ? "Red" : "Blue"));

		// 골드 라벨의 텍스트 업데이트
		goldLabel.setText("Gold: " + this.gold);

		// 라이프 라벨 텍스트 업데이트
		lifeLabel.setText("Life: " + this.life);

		// Label을 다시 그리도록 요청
		teamLabel.repaint();
		goldLabel.repaint();
		lifeLabel.repaint();
	}
	// 게임 종료 메시지 그리기 요청
	public void displayEndGameMessage(String message) {
		this.endGameMessage = message;
		repaint();
	}
	// 1. 초기에 불려짐
	// 2. repaint() 호출시 불려짐
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// 버퍼 이미지 초기화
		if (bufferImage == null) {
			bufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			bufferGraphics = bufferImage.getGraphics();
		}
		
		// 게임 그리기
		drawGame(bufferGraphics);
		
		// 게임 종료 후 게임 종료 메시지가 설정되면 보여짐
		if (endGameMessage != null) {
			bufferGraphics.setColor(new Color(0, 0, 0, 128));
			bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
			bufferGraphics.setColor(Color.WHITE);
			bufferGraphics.setFont(new Font("MALGUNSL", Font.BOLD, 64));
			FontMetrics fm = bufferGraphics.getFontMetrics();
			int statusPanelWidth = statusPanel.getPreferredSize().width;
			int gamePanelWidth = getWidth() - statusPanelWidth;
			int x = (gamePanelWidth - fm.stringWidth(endGameMessage)) / 2;
			int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
			bufferGraphics.drawString(endGameMessage, x, y);
		}

		// 버퍼 이미지를 화면에 그리기
		g.drawImage(bufferImage, 0, 0, this);
	}
	// 게임에 필요한 모든 요소 그리기
	private void drawGame(Graphics g) {
		drawGrassBackground(g); // 잔디 구역
		drawRedTeam(g); // 레드팀 깃발
		drawBlueTeam(g); // 블루팀 깃발
		drawSpawner(g); // 스포너
		drawPath_red(g); // 레드팀 몬스터 통로
		drawPath_blue(g); // 블루팀 몬스터 통로
		drawTurrets(g, myTurrets); // 내 터렛
		drawTurrets(g, enemyTurrets); // 상대 터렛
		drawMonsters(g); // 몬스터
	}
	// CopyOnWriteArrayList<Turret> 을 받아서 터렛 그리기
	private void drawTurrets(Graphics g, CopyOnWriteArrayList<Turret> turrets) {
		synchronized (turrets) {
			for (Turret turret : turrets) {
				if (turret.getLevel() == 0) { // 포탑 레벨이 0 -> 잔디 그리기
					g.drawImage(grassImage, turret.getPoint().x, turret.getPoint().y, this);
				} else { // 그 외 -> 포탑 레벨에 맞는 이미지 그리기
					Image turretImage = getTurretImagByLevel(turret.getLevel());
					g.drawImage(turretImage, turret.getPoint().x, turret.getPoint().y, this);
				}
			}
		}
	}
	// 포탑 레벨에 따른 다른 터렛 이미지 반환
	private Image getTurretImagByLevel(int level) {
		switch (level) {
		case 1:
			return turret1Image; // 1레벨 -> 1레벨 포탑 이미지
		case 2:
			return turret2Image; // 2레벨 -> 2레벨 포탑 이미지
		case 3:
			return turret3Image; // 3레벨 -> 3레벨 포탑 이미지
		default:
			return grassImage;
		}
	}
	// CopyOnWriteArrayList<Point> monsters의 모든 Point에 몬스터 이미지 그리기
	private void drawMonsters(Graphics g) {
		synchronized (monsters) {
			for (Point monster : monsters) {
				g.drawImage(monsterImage, monster.x, monster.y, this);
			}
		}
	}
	// 레드팀 몬스터 이동구역 그리기
	private void drawPath_red(Graphics g) {
		if (pathImage1 != null) {
			int pathWidth = pathImage1.getWidth(this);
			int pathHeight = pathImage1.getHeight(this);
			for (Point p : redPath) {
				g.drawImage(pathImage1, p.x, p.y, this);
			}
		}
	}
	// 블루팀 몬스터 이동구역 그리기
	private void drawPath_blue(Graphics g) {
		if (pathImage2 != null) {
			int pathWidth = pathImage2.getWidth(this);
			int pathHeight = pathImage2.getHeight(this);
			for (Point p : bluePath) {
				g.drawImage(pathImage2, p.x, p.y, this);
			}
		}

	}
	// 스포너 그리기
	private void drawSpawner(Graphics g) {
		if (spawnerImage != null) {
			int spawnerWidth = spawnerImage.getWidth(this);
			int spawnerHeight = spawnerImage.getHeight(this);
			g.drawImage(spawnerImage, 450, 450, spawnerWidth, spawnerHeight, this);
		}
	}
	// 레드팀 깃발 그리기
	private void drawRedTeam(Graphics g) {
		if (redTeamImage != null) {
			int redWidth = redTeamImage.getWidth(this);
			int redHeight = redTeamImage.getHeight(this);
			g.drawImage(redTeamImage, 0, 0, redWidth, redHeight, this);
			g.drawImage(redTeamImage, 0, 900, redWidth, redHeight, this);
		}
	}
	// 블루팀 깃발 그리기
	private void drawBlueTeam(Graphics g) {
		if (blueTeamImage != null) {
			int blueWidth = blueTeamImage.getWidth(this);
			int blueHeight = blueTeamImage.getHeight(this);
			g.drawImage(blueTeamImage, 900, 0, blueWidth, blueHeight, this);
			g.drawImage(blueTeamImage, 900, 900, blueWidth, blueHeight, this);
		}

	}
	// 잔디 그리기
	private void drawGrassBackground(Graphics g) {
		if (grassImage != null) {
			int tileWidth = grassImage.getWidth(this);
			int tileHeight = grassImage.getHeight(this);
			for (int y = 0; y < getHeight(); y += tileHeight) {
				for (int x = 0; x < getWidth(); x += tileWidth) {
					g.drawImage(grassImage, x, y, tileWidth, tileHeight, this);
				}
			}
		}
	}
	// 클라이언트가 서버로부터 객체를 수신하는 스레드
	class ClientReceiver extends Thread {
		private List<Turret> turrets;
		MOD packet;
		int dropGold;
		String teamColor;

		public ClientReceiver() {
			turrets = new ArrayList<>();
		}

		@Override
		public void run() {
			try {
				while (true) {
					synchronized (objIs) {
						// 서버에서 객체 읽기
						packet = (MOD) objIs.readObject();
						
						// MOD 객체에서 MODE 꺼내기
						MODE mode = packet.getMode();
						
						switch (mode) { // MODE에 따른 분기
						case PNT_TURRET_MOD: // 상대 터렛 그리기
							// 1. 서버로부터 상대편 List<Turret>을 받아 turrets에 저장
							turrets.clear();
							turrets = (List<Turret>) packet.getPayload();
							
							// 2. 받아온 상대편 List<Turret> turrets를 enemyTurrets에 삽입.
							enemyTurrets.clear();
							enemyTurrets.addAll(turrets);
							
							// 3. 다시 그리기
							repaint();
							break;
						case PNT_MONSTER_MOD: // 몬스터 그리기
							// 1. 서버로부터 Vector<MonsterPosPair>을 받는다.
							Vector<MonsterPosPair> monstersInfo = (Vector<MonsterPosPair>) packet.getPayload();
							
							// 2. 몬스터를 잡아 얻은 골드를 꺼낸다  *0번째 요소 idx에 골드 정보가 있음
							dropGold = monstersInfo.get(0).idx;
							System.out.println("dropGold: " + dropGold);
							gold += dropGold;
							System.out.println("gold: " + gold);
							
							// 3. 몬스터의 위치 정보 업데이트
							updateMonsters(monstersInfo);
							// 4. 팀, 골드, 생명 상태 업데이트
							updateGameStatus(); 
							// 5. 다시 그리기
							repaint();
							break;
						case MODIFY_LIFE_MOD: // 생명력이 깎임.
							// 1. 서버로부터 남은 생명력 받기
							int remainLife = (int) packet.getPayload();
							System.out.println("남은 라이프: " + remainLife);
							// 2. life 재설정
							life = remainLife;
							// 3. 팀, 골드, 생명 상태 업데이트
							updateGameStatus();
							// 4. 다시 그리기
							repaint();
							break;
						case GAME_WIN_MOD: // 이겼을 때
							teamColor = (team == TEAM.RED) ? "레드팀" : "블루팀";
							System.out.println(teamColor + ": " + "게임에서 승리하였습니다!");
							displayEndGameMessage(teamColor + ": " + "게임에서 승리하였습니다!");
							break;
						case GAME_LOSE_MOD: // 졌을 때
							teamColor = (team == TEAM.RED) ? "레드팀" : "블루팀";
							System.out.println(teamColor + ": " + "게임에서 패배하였습니다!");
							displayEndGameMessage(teamColor + ": " + "게임에서 패배하였습니다..");
							break;
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// monstersInfo에서 몬스터 위치 정보를 추출하여 그리기 위한 몬스터 목록을 업데이트
		private void updateMonsters(Vector<MonsterPosPair> monstersInfo) {
			CopyOnWriteArrayList<Point> monsterPoints = new CopyOnWriteArrayList<>();
			synchronized (monsterPoints) {
				for (MonsterPosPair monsterPair : monstersInfo) {
					if (monsterPair.monster != null) {
						monsterPoints.add(monsterPair.monster.getPoint());
					}
				}
				// 이제 monsterPoints에는 모든 몬스터의 위치 정보 존재
				// 이 정보를 화면에 그리기 위한 monsters를 초기화 후 저장.
				synchronized (monsters) {
					monsters.clear();
					monsters.addAll(monsterPoints);
				}
			}
		}
	}
}