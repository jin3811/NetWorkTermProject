package Server.Manager;

import Server.*;
import util.*;

import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    private Server server;
    private ConcurrentHashMap<Long, GameSession> games;
    private final BluePath bluePath = BluePath.getInstance();
    private final RedPath redPath = RedPath.getInstance();

    public GameManager(Server server) {
        this.server = server;
        games = new ConcurrentHashMap<>();
    }

    public synchronized void addGame(Room r, long roomNum) {
        UserService manager = this.server.userManager.getUserbyId(r.getManagerId());
        UserService player = this.server.userManager.getUserbyId(r.getPlayerId());

        games.put(roomNum, new GameSession(
                new RedPlayer(manager),
                new BluePlayer(player)
        ));
    }

    public void startGame(long roomNum) {
        games.get(roomNum).start();
    }

    private abstract class Player {
        protected UserService user;
        protected int gold;
        protected int life;

        public Player(UserService user) {
            this.user = user;
        }
    }

    private class RedPlayer extends Player {

        public RedPlayer(UserService user) {
            super(user);
        }
    }

    private class BluePlayer extends Player {

        public BluePlayer(UserService user) {
            super(user);
        }
    }
    private class GameSession extends Thread {
        private RedPlayer red;
        private BluePlayer blue;

        public GameSession(RedPlayer red, BluePlayer blue) {
            this.red = red;
            this.blue = blue;
        }

        @Override
        public void run() {
            super.run();
        }
    }
}
