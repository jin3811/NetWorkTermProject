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
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.List;

// 실제 게임을 진행하는 panel
public class GamePanel extends MultiRoomJPanel {
	private RandomDefence context;
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

	List<Point> allPoints = new ArrayList<>();
	List<Point> bluePath;
	List<Point> redPath;

	private long roomNum;

	private TEAM team;

	// 추가중..
	private Thread clientReceiverThread;
	// 포탑 설치구역 가지는 객체
	private BlueArea blueAreaInstance;
	private RedArea redAreaInstance;
	// 몬스터 이동구역 가지는 객체
	private BluePath bluePathInstance;
	private RedPath redPathInstance;
	// 제한 구역 가지는 객체
	private RestrictArea restrictAreaInstance;
	// 포탑 위치 저장하는 리스트
	List<Turret> myTurrets = new ArrayList<>();
	List<Turret> enemyTurrets = new ArrayList<>();

//	List<Point> turrets = new ArrayList<>();
	// 포탑 레벨 2 위치 저장하는 리스트
//	List<Point> turrets2 = new ArrayList<>();
	// 몬스터 위치 저장하는 리스트
	List<Point> monsters = new ArrayList<>();
	// 클라이언트단 골드
	private int gold;
	private static final int MAX_LEVEL = 3;
	// ...

	public GamePanel(RandomDefence context, long roomNum, TEAM team) {
		this.context = context;
		this.roomNum = roomNum;
		this.team = team;
		this.gold = 100;

		// 추가중 ..
		blueAreaInstance = BlueArea.getInstance();
		redAreaInstance = RedArea.getInstance();

		bluePathInstance = BluePath.getInstance();
		redPathInstance = RedPath.getInstance();

		restrictAreaInstance = RestrictArea.getInstance();

		// ..

		System.out.println("GamePanel 입장");
		context.setSize(1000, 1000);
//        setPreferredSize(new Dimension(1000, 1000));
//        context.pack();
		setLayout(new BorderLayout());

		for (int i = 0; i < 1000; i += 50) {
			for (int j = 0; j < 1000; j += 50) {
				Point p = new Point(j, i);
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
		System.out.println("좌표 리스트 크기: " + allPoints.size());
		System.out.println("좌표 리스트 마지막: " + allPoints.get(399));

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

				// 해야하는 것: 클라이언트단에서 포탑 설치 및 업그레이드를 한 후 해당 정보를 서버로 전송한다.
				// 초기 설정: 내가 배정된 팀의 잔디 구역(포탑 구역)에 0레벨 포탑 설치한다(Turret) - 이 경우에는 잔디를 그려준다.
				// 해당 팀의 잔디 구역(포탑 구역)을 클릭하면 메서드를 이용해서 포탑의 레벨을 올린다(물론 돈이 있고, 최대 레벨이 아니면)
				// 포탑의 레벨이 1레벨 이상이면 그때부터 내가 배정된 팀의 잔디구역에 포탑 이미지를 그릴 수 있도록 한다.
				// 그럼 필요한 것:
				// 클릭한 구역이 내 구역인지 확인 && 몬스터 통로 구역이 아님을 확인 && 설치 불가 구역이 아님을 확인

				// 포탑 설치 가능 구역인지 확인(내 구역인지, 몬스터 통로 아닌지, 제한 구역 아닌지)
				if (isValidTurretPlacement(turretPoint, team)) {
					// 클릭된 위치의 터렛을 가져옴(몇 레벨이든 포탈 설치 가능 구역 위치엔 터렛이 존재함)
					Turret existingTurret = getTurretAtPoint(turretPoint);
					// 업그레이드가 가능한지 확인 - 가능하면 여기서 골드 차감 진행됨.
					if (isCanUpgrade(existingTurret)) {
						// 내 해당 포탑 업그레이드 실행
						existingTurret.upgrade();
						// 내 포탑 정보 서버로 전송
						sendMessageToServer(MODE.TURRET_UPDATE_MOD, new ArrayList<Turret>(myTurrets));
						System.out.println(team + "업데이트 요청함");
					}
				}
//				// 포탑 설치 가능 구역 클릭 시
//				if (!isTurretPresent(turretPoint) && isValidTurretPlacement(turretPoint, team)) {
//					// 서버에 포탑 배치 요청
//					sendTurretPlacementRequest(turretPoint);
//				}
//				// 이미 설치된 포탑 클릭 시 && 업그레이드 가능한지
//				else if (isTurretPresent(turretPoint) && isValidUpgradeTurret(turretPoint)) {
//					// 포탑 업그레이드 하고
//					// 서버에 알림
//
//				}
			}

		});

		synchronized (objOs) {

			try {
				objOs.writeObject(new MOD(MODE.GAME_START_MOD, roomNum));
				objOs.flush();
				
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		initTurrets();
		clientReceiverThread = new ClientReceiver();
		clientReceiverThread.start();
		setVisible(true);

		// 스레드 시작

	}

	// 서버에 객체 전송
	private void sendMessageToServer(MODE mode, Serializable payload) {
		// TODO Auto-generated method stub
		synchronized (objOs) {
			try {
				if (objOs != null) {
					objOs.writeObject(new MOD(mode, payload));
					objOs.flush();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	// 현재 클릭된 위치의 터렛이 업그레이드 가능한지 확인
	//
	private boolean isCanUpgrade(Turret existingTurret) {
		int level = existingTurret.getLevel();

		if (level >= 3) { // 3레벨이면 최대레벨이므로 false
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

	// 클릭된 위치의 터렛을 가져온다.
	private Turret getTurretAtPoint(Point point) {
		for (Turret turret : myTurrets) {
			if (turret.getPoint().equals(point)) {
				return turret;
			}
		}
		return null;
	}

	// 포탑 배치가 유효한지 확인
	private boolean isValidTurretPlacement(Point turretPoint, TEAM team) {
		return isWithinTeamArea(turretPoint, team) && !isMonsterPathArea(turretPoint) && !isRestrictedArea(turretPoint);
	}

	// 팀 구역(잔디) 내에 있는지 확인(잔디)
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

	private boolean isRestrictedArea(Point turretPoint) {
		// 1. 금지구역(빈공간, 깃발, 스포너) 가져온다.
		// 2. 클릭한 좌표가 제한구역에 포함되는지 확인한다.
		List<Point> restrictArea = restrictAreaInstance.getRestrictArea();
		return restrictArea.contains(turretPoint);
	}

	// 게임 시작시 내 팀 모든 구역에 포탑 설정(0레벨)
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

//	// 서버로부터 포탑의 위치 정보를 받음
//	private void receiveTurretDataFromServer() {
//		// 서버로부터 포탑 데이터를 수신하는 로직
//		// 예를 들어, 서버로부터 받은 포탑 위치 데이터를 turrets 리스트에 추가하거나 업데이트
//	}

//	// 서버 응답 처리
//	private void handleServerResponse(Object response) {
//		// 1. 서버로부터 응답을 받는다
//		// 2. 응답받은 것에서 turret의 Position을 꺼낸다
//		// 3. turrets에 받아온 turret의 Position을 추가 및 업데이트
//		// 4. repaint() 호출하여 다시 그린다.
//
//		repaint();
//	}

	// 1. 초기에 불려짐
	// 2. repaint() 호출시 불려짐
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// 타일맵 : 잔디
		drawGrassBackground(g); // 잔디 구역
		drawRedTeam(g); // 레드팀 깃발
		drawBlueTeam(g); // 블루팀 깃발
		drawSpawner(g); // 스포너
		drawPath_red(g); // 레드팀 몬스터 통로
		drawPath_blue(g); // 블루팀 몬스터 통로

		// List<Point> turrets의 모든 Point에 포탑 이미지 그리기
//		for (Point turret : turrets) {
//			g.drawImage(turret1Image, turret.x, turret.y, this);
//		}
		// List<Point> turrets2의 모든 Point에 포탑2(업그레이드) 이미지 그리기
//		for (Point turret : turrets2) {
//			g.drawImage(turret1Image, turret.x, turret.y, this);
//		}

		// List<Turret> myTurrets의 모든 Point에 포탑 이미지 그리기
		synchronized (myTurrets) {
			
			for (Turret turret : myTurrets) {
				if (turret.getLevel() == 0) { // 포탑 레벨이 0 -> 잔디 그리기
					g.drawImage(grassImage, turret.getPoint().x, turret.getPoint().y, this);
				} else {
					Image turretImage = getTurretImagByLevel(turret.getLevel());
					g.drawImage(turretImage, turret.getPoint().x, turret.getPoint().y, this);
				}
			}
		}
		// List<Turret> enemyTurrets의 모든 Point에 포탑 이미지 그리기
		synchronized (enemyTurrets) {
			
			for (Turret turret : enemyTurrets) {
				if (turret.getLevel() == 0) { // 포탑 레벨이 0 -> 잔디 그리기
					g.drawImage(grassImage, turret.getPoint().x, turret.getPoint().y, this);
				} else {
					Image turretImage = getTurretImagByLevel(turret.getLevel());
					g.drawImage(turretImage, turret.getPoint().x, turret.getPoint().y, this);
				}
			}
		}

		// List<Point> monsters의 모든 Point에 몬스터 이미지 그리기
		synchronized (monsters) {
			for (Point monster : monsters) {
				g.drawImage(monsterImage, monster.x, monster.y, this);
			}
		}
	}

	// 포탑 레벨에 따른 다른 터렛 이미지 반환
	private Image getTurretImagByLevel(int level) {
		switch (level) {
		case 1:
			return turret1Image;
		case 2:
			return turret2Image;
		case 3:
			return turret3Image;
		default:
			return grassImage;
		}
	}

	private void drawPath_red(Graphics g) {
		if (pathImage1 != null) {
			int pathWidth = pathImage1.getWidth(this);
			int pathHeight = pathImage1.getHeight(this);
			for (Point p : redPath) {
				g.drawImage(pathImage1, p.x, p.y, this);
			}
		}
	}

	private void drawPath_blue(Graphics g) {
		if (pathImage2 != null) {
			int pathWidth = pathImage2.getWidth(this);
			int pathHeight = pathImage2.getHeight(this);
			for (Point p : bluePath) {
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

	class ClientReceiver extends Thread {
		private List<Turret> turrets;
		MOD packet;
//		public ClientReceiver(ObjectInputStream objIs) {
//			this.objIs = objIs;
//		}
		public ClientReceiver() {
			turrets = new ArrayList<>();
		}

		@Override
		public void run() {
				try {
					while (true) {
						// 서버에서 Object 읽기

						synchronized (objIs) {
							packet = (MOD) objIs.readObject();

							MODE mode = packet.getMode();

							switch (mode) {
							// 상대 터렛 그리기
							case PNT_TURRET_MOD:
								// 1. 상대편 List<Turret>을 꺼내 turrets에 저장
								turrets.clear();
								turrets = (List<Turret>) packet.getPayload();
								// 2. 받아온 상대편 List<Turret> turrets를 enemyTurrets에 삽입.
								enemyTurrets.clear();
								enemyTurrets.addAll(turrets);
								// 3. 다시 그리기
								repaint();
								break;
							// 몬스터 그리기
							case PNT_MONSTER_MOD:
								// 1. Vector<MonsterPosPair>을 꺼낸다
								Vector<MonsterPosPair> monstersInfo = (Vector<MonsterPosPair>) packet.getPayload();
								// 2. 골드 꺼낸다(몬스터 잡아 얻은)
								gold += monstersInfo.get(0).idx;

								// 몬스터의 위치 정보 업데이트
								updateMonsters(monstersInfo);
								repaint();
								break;
							case TEST_MOD:
								System.out.println(packet.getPayload());
							}

//							objIs.reset();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("");
				}
			

		}

		private void updateMonsters(Vector<MonsterPosPair> monstersInfo) {
			// monstersInfo에서 몬스터 위치 정보를 추출하여 그리기 위한 몬스터 목록을 업데이트합니다.
			List<Point> monsterPoints = new ArrayList<>();
			for (MonsterPosPair monsterPair : monstersInfo) {
				if (monsterPair.monster != null) {
					monsterPoints.add(monsterPair.monster.getPoint());
				}
			}
			// 이제 monsterPoints에는 모든 몬스터의 위치 정보 존재
			// 이 정보를 화면에 그리기 위한 monsters를 초기화 후 저장.
			monsters.clear();
			monsters.addAll(monsterPoints);
		}
//		// 터렛 위치 업데이트하는 메서드
//		private void updateTurretLocation(Point newTurretLocation) {
//			// 중복된 터렛 위치가 없는 경우 추가
//			if (!myTurrets.contains(newTurretLocation)) {
//				myTurrets.add(newTurretLocation);
//			}
//		}
//
//		// 몬스터 위치 업데이트하는 메서드
//		private void updateMonsterLocation(Point newMonsterLocation) {
//			// 중복된 몬스터 위치가 없는 경우 추가
//			if (!monsters.contains(newMonsterLocation)) {
//				monsters.add(newMonsterLocation);
//			}
//		}
	}
}
//포탑 배치 가능 영역인지 확인
//	protected boolean isTurretPlacementArea(Point turretPoint) {
//		if(team == TEAM.RED) {
//			return isWithinRedArea(turretPoint);
//		} else if(team == TEAM.BLUE) {
//			return isWithinRedArea(turretPoint);
//		}
//		return false;
//	}
//	private boolean isWithinRedArea(Point turretPoint) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	// 서버에서 사용할 코드(미리 적어놓음)
//	public void handleTurretRequest(Point turretPoint) {
//
//	}
//

//for(int i=22;i<=29;i++) {
//g.drawImage(pathImage1, allPoints.get(i).x, allPoints.get(i).y, this);
//}
//for(int i=362;i<=369;i++) {
//g.drawImage(pathImage1, allPoints.get(i).x, allPoints.get(i).y, this);
//}
//for(int i=181;i<=188;i++) {
//g.drawImage(pathImage1, allPoints.get(i).x, allPoints.get(i).y, this);
//}
//for(int i=201;i<=208;i++) {
//g.drawImage(pathImage1, allPoints.get(i).x, allPoints.get(i).y, this);
//}
//for(int i=41;i<=161;i+=20) {
//g.drawImage(pathImage1, allPoints.get(i).x, allPoints.get(i).y, this);
//}
//for(int i=221;i<=341;i+=20) {
//g.drawImage(pathImage1, allPoints.get(i).x, allPoints.get(i).y, this);
//}
//for(int i=29;i<=169;i+=20) {
//g.drawImage(pathImage1, allPoints.get(i).x, allPoints.get(i).y, this);
//}
//for(int i=229;i<=349;i+=20) {
//g.drawImage(pathImage1, allPoints.get(i).x, allPoints.get(i).y, this);
//}