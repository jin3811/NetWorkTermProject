package Server;

import java.io.Serializable;
import java.util.Set;
import java.util.Vector;

public class Room implements Serializable {
    private String name;

    public Room(String name, int managerId) {
        this.name = name;
    }
    public Room(Room n){
        this.name = n.getRoomName();
    }
    public String getRoomName() {
    	return name;
    }
    public void participate(UserService user) {


    }
}
