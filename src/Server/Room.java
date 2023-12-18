package Server;

import java.io.Serializable;
import java.util.Set;
import java.util.Vector;

public class Room implements Serializable {
    private String name;
//    private Vector<UserService> users = new Vector<>();

    public Room(String name, int managerId) {
        this.name = name;
//        users.add(manager);
    }
    public String getRoomName() {
    	return name;
    }
    public void participate(UserService user) {


    }
}
