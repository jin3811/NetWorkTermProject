package UI;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class MultiRoomJPanel extends JPanel {
    protected Socket socket;
    protected ObjectOutputStream objOs;
    protected ObjectInputStream objIs;

    protected String nickname;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ObjectOutputStream getObjOs() {
        return objOs;
    }

    public void setObjOs(ObjectOutputStream objOs) {
        this.objOs = objOs;
    }

    public ObjectInputStream getObjIs() {
        return objIs;
    }

    public void setObjIs(ObjectInputStream objIs) {
        this.objIs = objIs;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public abstract void initCommunicate();
}