package Server.Manager;

import Component.Monster;
import Server.*;
import util.*;
import Component.*;

import java.awt.Point;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameManager {
    private Server server;
    private ConcurrentHashMap<Long, GameSession> games; // 게임 세션 관리 맵
    // 몬스터 이동 경로 및 포탑 설치 가능 구역 인스턴스
    private final BluePath bluePath = BluePath.getInstance();
    private final RedPath redPath = RedPath.getInstance();
    private final BlueArea blueArea = BlueArea.getInstance();
    private final RedArea redArea = RedArea.getInstance();
    private final Random randGenerator = new Random();

    // 생성자: 서버 인스턴스 초기화 & games 맵 초기화
    public GameManager(Server server) {
        this.server = server;
        games = new ConcurrentHashMap<>();
    }

    // GameSession 스레드 생성하는 메소드
    // 해당 메소드는 한번만 호출되도록 설정
    // 이유: GameSession 스레드가 두개돌면 MonsterUpdateThread 스레드가 두개 생성되어 의도치 않은 결과 초래
    public synchronized void addGame(Room r, long roomNum) {
    	// 해당 roomNum에 대한 GameSession이 존재하는지 확인
    	if(games.containsKey(roomNum)) {
    		// 이미 존재하는 경우에는 아무것도 안하도록 한다.
    		return;
    	}
    	// 존재하지 않는 경우, 새로운 GameSession을 만들어 맵에 추가
        UserService manager = this.server.userManager.getUserbyId(r.getManagerId());
        UserService player = this.server.userManager.getUserbyId(r.getPlayerId());

        GameSession newSession = new GameSession(new Player(manager), new Player(player));
        // putIfAbsent를 사용하여 이미 존재하는 경우에는 추가하지 않음
        games.putIfAbsent(roomNum, newSession);
    }

    // 게임 시작 메소드
    public void startGame(long roomNum) {
    	GameSession session = games.get(roomNum);
        if (session != null && !session.isRunning()) {
            try{
                session.start(); // GameSession 스레드 시작
            }
            catch(IllegalThreadStateException e1) {
                System.out.println("게임이 이미 시작됨");
            }
        }
    }

    // 플에이어 관리하는 내부 클래스
    public class Player {
        protected UserService user;
        protected int life;
        protected final List<MonsterPosPair> monsters = new CopyOnWriteArrayList<>();
        protected List<Turret> turrets = new CopyOnWriteArrayList<>();
        private static final double TURRET_RANGE = 300; // 이 값은 게임의 실제 단위에 맞춰 조정해야 함

        public Player(UserService user) {
            this.user = user;
            this.life = 10;
        }

        public void decreaseLife() {
            life -= 1;
        }

        public synchronized int getLife() {
            return life;
        }
        private Point getPoint(int idx, Point point, Object path) throws IndexOutOfBoundsException {
            Point genPoint = null;
            Path p = (Path)path;
            switch (idx) {
                case 1 -> {
                    try {
                        int curIdx = p.getDirection1().indexOf(point);
                        genPoint = p.getDirection1().get(curIdx + 1);
                    }
                    catch(NullPointerException e) {
                        genPoint = redPath.getDirection1().get(0);
                    }
                }
                case 2 -> {
                    try {
                        int curIdx = p.getDirection2().indexOf(point);
                        genPoint = p.getDirection2().get(curIdx + 1);
                    }
                    catch(NullPointerException e) {
                        genPoint = p.getDirection2().get(0);
                    }
                }
                case 3 -> {
                    try {
                        int curIdx = p.getDirection3().indexOf(point);
                        genPoint = p.getDirection3().get(curIdx + 1);
                    }
                    catch(NullPointerException e) {
                        genPoint = p.getDirection3().get(0);
                    }
                }
                case 4 -> {
                    try {
                        int curIdx = p.getDirection4().indexOf(point);
                        genPoint = p.getDirection4().get(curIdx + 1);
                    }
                    catch(NullPointerException e) {
                        genPoint = p.getDirection4().get(0);
                    }
                }
            }
            return genPoint;
        }

        // 몬스터 처리 수행 메소드
        public Vector<MonsterPosPair> monsterProcess(Object path) {
            // 기존의 몬스터들을 한칸씩 이동시킨다
            synchronized(monsters) {
                for (MonsterPosPair m : monsters) {
                    try {
                        m.monster.setPoint(getPoint(m.idx, m.monster.getPoint(), path));
                    } catch (IndexOutOfBoundsException e) {
                        decreaseLife(); // 깃발에 닿으면 생명력 감소
                        monsters.remove(m); // 깃발에 닿은 몬스터 삭제
                    }
                }
            }
            // 새로운 몬스터를 생성한 후, MonsterPosPair 리스트에 넣는다
            int newMonsterPathIdx = randGenerator.nextInt(4) + 1;
            Monster newMonster = new Monster(getPoint(newMonsterPathIdx, null, path));

            monsters.add(new MonsterPosPair(
                            newMonsterPathIdx,
                            newMonster));

            // 터렛에 의한 피격처리 후 얻은 골드
            int plusGold = turretAttackProcess();

            // 깊은복사한 Vector를 최종 전달
            // 참고) 0번째 요소에는 처리된 몬스터를 바탕으로 몇골드를 얻게 되는지를 추가한다.
            Vector<MonsterPosPair> result = new Vector<>(monsters);
            result.insertElementAt(new MonsterPosPair(plusGold, null), 0);
           
            // 최종 처리된 몬스터 목록 반환
            return result; 
        }

        // 터렛의 공격을 처리 메소드
        public int turretAttackProcess() {
            int earnedGold = 0;

            // 각 터렛에 대해 실행
            for (Turret turret : turrets) {
                if (turret.getLevel() > 0 && turret.getTeam() == user.getTeam()) { // 0 레벨은 공격 불가
                    MonsterPosPair target = findClosestMonster(turret.getPoint());
                    if (target != null) {
                    	
                        // 몬스터 공격 로직
                        turret.attack(target.monster);

                        // 몬스터가 죽었는지 체크
                        if (target.monster.getHP() <= 0) {
                            monsters.remove(target);
                            earnedGold += 100; // 얻는 금액: 100골드
                        }
                    }
                }
            }

            return earnedGold; // 골드 반환
        }

        // 가까운 몬스터를 찾아 반환하는 메소드
        private MonsterPosPair findClosestMonster(Point turretPosition) {
            MonsterPosPair closestMonster = null;
            double closestDistance = Double.MAX_VALUE;

            // MonsterPosPair 리스트를 순회하며 가장 가까운 몬스터를 찾음
            for (MonsterPosPair monsterPair : monsters) {
                double distance = turretPosition.distance(monsterPair.monster.getPoint());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestMonster = monsterPair;
                }
            }

            // 몬스터가 터렛의 사정거리 내에 있으면 반환
            return closestDistance <= TURRET_RANGE ? closestMonster : null;
        }
        
        // 플레이어의 터렛 상태를 업데이트하는 메소드
        public synchronized void updateTurret(List<Turret> new_turrets) {
            this.turrets.clear();
            this.turrets.addAll(new_turrets);
        }
    }

    // 방 번호에 해당하는 게임 세션 반환하는 메소드
    public synchronized GameSession getGameRoom(long rid) {
        return games.get(rid);
    }
    
    // 게임 세션을 관리하는 내부 클래스
    public class GameSession extends Thread {
        private Player red;
        private ObjectOutputStream redObjOs;
        private Player blue;
        private ObjectOutputStream blueObjOs;
        
        private volatile boolean isRunning = false; // 스레드 실행 상태를 추적하는 플래그
        public GameSession(Player red, Player blue) {
            this.red = red;
            this.blue = blue;
            redObjOs = red.user.getObjOutputStream();
            blueObjOs = blue.user.getObjOutputStream();
        }

        /**
         * 1. 몬스터 생성, 이동, 피격 처리
         *     1. 생성 : 1~4 path중 하나 선택후 해당 경로에 생성
         *     2. 이동 : 결정된 path 를 통해 좌표를 이동시킴
         *     3. 피격 : 터렛의 데미지 계산 후, 적당히 사망처리
         *     4. 위 처리 후, 두 클라에 모두 뿌려줌
         * 2. 클라에서 포탑 업데이트 요청이 들어옴
         *     1. 해당 정보를 통해 몬스터의 피격처리 강화
         *     2. 두 클라에 포탑 변경사항 뿌려줌
         */
        @Override
        public void run() {
        	if(isRunning) {
        		// 실행중이면 새 스레드 실행 방지
        		return;
        	}
        	isRunning = true; // 스레드가 실행중임을 표시한다.
            Thread monsterUpdater = new MonsterUpdateThread();
            monsterUpdater.start(); // 몬스터 업데이트 스레드 시작

            // 게임이 종료될 때까지 무한루프
            while(!isGameEnd());
            System.out.println("game session - 승패 판정 남");
            
            isRunning = false;  // 게임 종료시 스레드가 종료되었음을 표시
            monsterUpdater.interrupt(); // 게임 끝남

            // 클라이언트 들에게 메세지 보내기
            Player winner = red.getLife() != 0 ? red : blue;
            Player losser = red == winner ? blue : red;

            ObjectOutputStream winOS = winner.user.getObjOutputStream();
            ObjectOutputStream lossOS = losser.user.getObjOutputStream();

            synchronized (winOS) {
                synchronized (lossOS) {
                    try {
                    	// 각 클라에게 승패 여부를 전송
                        winOS.writeObject(new MOD(MODE.GAME_WIN_MOD, null));
                        lossOS.writeObject(new MOD(MODE.GAME_LOSE_MOD, null));

                        winOS.flush();
                        lossOS.flush();
                    }
                    catch (IOException e) {
                        System.out.println("승패 가려졌지만 클라가 받지 못함");
                    }
                }
            }
            System.out.println("게임 끝");
        }
        public boolean isRunning() {
        	return isRunning;
        }
        public boolean isGameEnd() { // 어느 한 클라이언트의 생명력이 0이면 true
            return red.getLife() <= 0 || blue.getLife() <= 0;
        }
        public synchronized Player getPlayer(int uid) {
            return red.user.getUserID() == uid ? red : blue;
        }

        // 몬스터 업데이트를 담당하는 내부 클래스(스레드)
        private class MonsterUpdateThread extends Thread {
        	Vector<MonsterPosPair> redCurrent, blueCurrent, redTemp, blueTemp;
        	
            public MonsterUpdateThread() {
            	redCurrent = new Vector<MonsterPosPair>();
                blueCurrent = new Vector<MonsterPosPair>();
                redTemp = new Vector<MonsterPosPair>();
                blueTemp = new Vector<MonsterPosPair>();
            }
            @Override
            public void run() {
                while(isRunning) {
                    try{
                        // 데이터 전송 후 대기시간 추가
                        sleep(1000);

                        redCurrent.clear();
                    	blueCurrent.clear();
                    	redTemp.clear();
                    	blueTemp.clear();

                        redTemp = red.monsterProcess(redPath);
                        blueTemp = blue.monsterProcess(bluePath);

                        redCurrent.add(redTemp.get(0));
                        blueCurrent.add(blueTemp.get(0));

                        redTemp.remove(0);
                        blueTemp.remove(0);

                        redCurrent.addAll(redTemp);
                        redCurrent.addAll(blueTemp);

                        blueCurrent.addAll(redTemp);
                        blueCurrent.addAll(blueTemp);

                        synchronized (redObjOs) {
                            synchronized (blueObjOs) {
                                try {
                                	// 각 클라이언트에게 업데이트된 몬스터들을 그리라고 전송
                                	redObjOs.reset();
                                	blueObjOs.reset();
                                    redObjOs.writeObject(new MOD(MODE.PNT_MONSTER_MOD, new Vector<MonsterPosPair>(redCurrent)));
                                    blueObjOs.writeObject(new MOD(MODE.PNT_MONSTER_MOD, new Vector<MonsterPosPair>(blueCurrent)));

                                    redObjOs.flush();
                                    blueObjOs.flush();

                                    redObjOs.reset();
                                    blueObjOs.reset();

                                    // 생명력이 바뀐 것도 전송
                                    redObjOs.writeObject(new MOD(MODE.MODIFY_LIFE_MOD, red.life));
                                    blueObjOs.writeObject(new MOD(MODE.MODIFY_LIFE_MOD, blue.life));
                                } catch (Exception e) {
                                    System.out.println("몬스터 데이터 전송 실패");
                                }
                            }
                        }
                    }
                    catch (InterruptedException e){
                        System.out.println("승패 판정으로 인한 몹 업뎃 중단");
                        break;
                    }
                }
            }
        }
    }
}
