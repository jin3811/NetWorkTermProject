package Server.Manager;

import Server.Server;
import Server.UserService;

import java.util.Vector;

public class UserManager {
    private Vector<UserService> allUsers = new Vector<>();
    private Server server;

    public UserManager(Server server) {
        this.server = server;
    }

    public void addUser(UserService user) {
        allUsers.add(user);
    }

    public Vector<UserService> getAllUsers() {
        return allUsers;
    }

    public UserService getUserbyId(int id) {
        for (UserService user : allUsers) {
            if(user.getUserID() == id) {
                return user;
            }
        }
        return null;
    }
}
