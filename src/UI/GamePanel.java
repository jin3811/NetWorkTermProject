package UI;

import javax.imageio.ImageIO;
import javax.swing.*;

import Server.Room;
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
	private Image pathImage;
	private Image redTeamImage;
	private Image blueTeamImage;
	private Image spawnerImage;
	private Image turret1Image;
	private Image turret2Image;
	private Image monsterImage;

	// 포탑 위치 저장하는 리스트
	List<Point> turrets = new ArrayList<>();

	List<Point> allPoints = new ArrayList<>();
	public GamePanel(RandomDefence context, String nickname, Socket socket) {
		this.context = context;

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
		System.out.println("좌표 리스트 크기: "+allPoints.size());
		System.out.println("좌표 리스트 마지막: "+ allPoints.get(399));
//		for(Point p: allPoints) {
//			System.out.println("좌표: " + p);
//		}
		
		try {
			grassImage = ImageIO.read(getClass().getResource("/Image/grass.png"));
			pathImage = ImageIO.read(getClass().getResource("/Image/ground.png"));
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
				
				// 잔디 타일 위를 클릭했는지 확인하고 포탑 위치 추가
				if (isGrassTile(clickPoint)) {
					// 타일 크기
					int tileSize = 50;

					// 클릭된 위치를 타일 크기로 나눈 몫에 타일 크기를 곱해서 정렬
					int tileX = (clickPoint.x / tileSize) * tileSize;
					int tileY = (clickPoint.y / tileSize) * tileSize;

					Point turretPoint = new Point(tileX, tileY);
					
					turrets.add(turretPoint);
					repaint(); // 포탑을 다시 그립니다.
				}
			}
		});

		setVisible(true);
	}

	// 클릭한 위치가 잔디 타일 위인지 확인하는 메소드
	private boolean isGrassTile(Point clickPoint) {
		// 임시로 잔디 타일이라고 가정하고 true 반환
		// 실제로는 잔디 타일 위치를 계산하여 확인해야 함.
		return true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// 타일맵 : 잔디
		drawGrassBackground(g);
		drawRedTeam(g);
		drawBlueTeam(g);
		drawSpawner(g);
		drawPath(g);

		// 포탑 위치에 포탑 이미지 그리기
		for (Point turret : turrets) {
			g.drawImage(turret1Image, turret.x, turret.y, this);
		}
//		if (pathImage != null) {
//			int pathWidth = pathImage.getWidth(this);
//			int pathHeight = pathImage.getHeight(this);
//			g.drawImage(pathImage, 0, 0, pathWidth, pathHeight, this);
//		}

	}

	private void drawPath(Graphics g) {
		if (pathImage != null) {

			int pathWidth = pathImage.getWidth(this);
			int pathHeight = pathImage.getHeight(this);

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
			g.drawImage(redTeamImage, 0, 450, redWidth, redHeight, this);
			g.drawImage(redTeamImage, 0, 900, redWidth, redHeight, this);
		}
	}

	private void drawBlueTeam(Graphics g) {
		if (blueTeamImage != null) {
			int blueWidth = blueTeamImage.getWidth(this);
			int blueHeight = blueTeamImage.getHeight(this);
			g.drawImage(blueTeamImage, 900, 0, blueWidth, blueHeight, this);
			g.drawImage(blueTeamImage, 900, 450, blueWidth, blueHeight, this);
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
