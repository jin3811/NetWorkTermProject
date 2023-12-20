package Server.Manager;

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
    private ConcurrentHashMap<Long, GameSession> games;
    private final BluePath bluePath = BluePath.getInstance();
    private final RedPath redPath = RedPath.getInstance();
    private final BlueArea blueArea = BlueArea.getInstance();
    private final RedArea redArea = RedArea.getInstance();
    private final Random randGenerator = new Random();

    public GameManager(Server server) {
        this.server = server;
        games = new ConcurrentHashMap<>();
    }

    public synchronized void addGame(Room r, long roomNum) {
        UserService manager = this.server.userManager.getUserbyId(r.getManagerId());
        UserService player = this.server.userManager.getUserbyId(r.getPlayerId());

        games.put(roomNum, new GameSession(
                new Player(manager),
                new Player(player)
        ));
    }

    public void startGame(long roomNum) {
        games.get(roomNum).start();
    }

    private class Player {
        protected UserService user;
        protected int gold;
        protected int life;
        protected List<MonsterPosPair> monsters = new ArrayList<>();
        protected List<Turret> turrets = new CopyOnWriteArrayList<>();
        private static final double TURRET_RANGE = 300; // 이 값은 게임의 실제 단위에 맞춰 조정해야 함

        public Player(UserService user) {
            this.user = user;
            this.gold = 500;
            this.life = 5;
        }

        public void decreaseLife() {
            life -= 1;
        }
        private Point getPoint(int idx, Point point, Object path){
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

        public Vector<MonsterPosPair> monsterProcess(Object path) {
            // 기존의 몹들을 한칸씩 이동시킨다
            for (MonsterPosPair m : monsters) {
                m.monster.setPoint(getPoint(m.idx, m.monster.getPoint(), path));
            }

            // 새로운 몹을 생성한 후, 몹 리스트에 넣는다
            int newMonsterPathIdx = randGenerator.nextInt(4) + 1;
            monsters.add(new MonsterPosPair(
                            newMonsterPathIdx,
                            new Monster(getPoint(newMonsterPathIdx, null, path))));

            // 터렛에 의한 피격처리
            int plusGold = turretAttackProcess();

            // 깊은복사한 Vector를 최종 전달
            // 0번째 요소는 처리된 몬스터를 바탕으로 몇골드를 얻게 되는지를 추가한다.
            Vector<MonsterPosPair> result = new Vector<>(monsters);
            result.insertElementAt(new MonsterPosPair(plusGold, null), 0);
            return result;
        }

        public int turretAttackProcess() {
            int earnedGold = 0;

            // 각 터렛에 대해 실행
            for (Turret turret : turrets) {
                if (turret.getLevel() > 0) { // 0 레벨은 공격 불가
                    MonsterPosPair target = findClosestMonster(turret.getPoint());
                    if (target != null) {
                        // 몬스터 공격 로직
                        turret.attack(target.monster);

                        // 몬스터가 죽었는지 체크
                        if (target.monster.getHP() <= 0) {
                            monsters.remove(target);
                            earnedGold += 10; // 가정한 금액, 게임 규칙에 따라 조정 필요
                        }
                    }
                }
            }

            return earnedGold;
        }

        private MonsterPosPair findClosestMonster(Point turretPosition) {
            MonsterPosPair closestMonster = null;
            double closestDistance = Double.MAX_VALUE;

            // 몬스터 리스트를 순회하며 가장 가까운 몬스터를 찾음
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
    }

    private class GameSession extends Thread {
        private Player red;
        private ObjectOutputStream redObjOs;
        private ObjectInputStream redObjIs;
        private Player blue;
        private ObjectOutputStream blueObjOs;
        private ObjectInputStream blueObjIs;

        public GameSession(Player red, Player blue) {
            this.red = red;
            this.blue = blue;
            redObjOs = red.user.getObjOutputStream();
            redObjIs = red.user.getObjectInputStream();
            blueObjOs = blue.user.getObjOutputStream();
            blueObjIs = blue.user.getObjectInputStream();
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
            Vector<MonsterPosPair> current;
            Thread red2blue = new TurretUpdateThread(red, blueObjIs, redObjOs);
            Thread blue2red = new TurretUpdateThread(blue, redObjIs, blueObjOs);
            red2blue.start();
            blue2red.start();

            while(true){
                current = new Vector<>();
                current.addAll(red.monsterProcess(redPath));
                current.addAll(blue.monsterProcess(bluePath));

                sendDataToPlayer(MODE.PNT_MONSTER_MOD, current);
            }
        }

        private void sendDataToPlayer(MODE mode, Object payload) {
            MOD mod = new MOD(mode, payload);
            try{
                redObjOs.writeObject(mod);
                blueObjOs.writeObject(mod);

                redObjOs.flush();
                blueObjOs.flush();
            }
            catch (Exception e) {
                System.out.println("데이터 전송 실패");
            }
        }

        // red, blue 클라에서 날라오는 터렛 업데이트 요청을 처리함
        private class TurretUpdateThread extends Thread {
            private Player updateTarget; // 업데이트한 객체
            private ObjectInputStream is; // 한테서 받을 is
            private ObjectOutputStream os; // 업데이트 받을 객체의 os
            public TurretUpdateThread(Player updateTarget, ObjectInputStream is, ObjectOutputStream os) {
                this.updateTarget = updateTarget;
                this.is = is;
                this.os = os;
            }

            @Override
            public void run() {
                while(true) {
                    try {
                        MOD mod = (MOD)is.readObject();
                        if (mod.getMode() == MODE.TURRET_UPDATE_MOD) {
                            // 자기 터렛 정보를 업데이트한다.
                            updateTarget.turrets.clear();
                            updateTarget.turrets.addAll((List<Turret>)mod.getPayload());

                            // 상대한테 알려줘서 그리도록 한다.
                            mod.setMode(MODE.PNT_TURRET_MOD);
                            os.writeObject(mod);
                            os.flush();
                        }
                    } catch (Exception e) {
                        System.out.println("------- 터렛 받아오기 실패");
                    }
                }
            }
        }
    }
}
