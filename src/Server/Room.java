package Server;

import java.io.Serializable;
import java.util.Set;

public class Room implements Serializable {
    private String name;
    private Set<UserService> users;
}
