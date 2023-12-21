package UI;

import UI.LoginFormPanel;
import util.TransitionDisplayCommand;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RandomDefence extends JFrame{

    private JPanel rootPanel;
    private Socket socket;
    private ObjectInputStream objIs;
    private ObjectOutputStream objOs;
    private String nickname;

    public RandomDefence(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initWindow();

        setVisible(true);
    }

    private void initWindow() {
        rootPanel = new LoginFormPanel(this);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 50, 50, 50); // 내부 여백 설정
        add(rootPanel, gbc);
    }

    public void transition(MultiRoomJPanel displayTarget, TransitionDisplayCommand displayCommand) {
        if (displayCommand != null) displayCommand.execute();

        rootPanel = displayTarget;
        displayTarget.setSocket(socket);
        displayTarget.setObjOs(objOs);
        displayTarget.setObjIs(objIs);
        displayTarget.setNickname(nickname);

        getContentPane().removeAll();
        getContentPane().add(displayTarget);
        setContentPane(displayTarget);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void transition(MultiRoomJPanel displayTarget) {
        transition(displayTarget, null);
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ObjectInputStream getObjIs() {
        return objIs;
    }

    public void setObjIs(ObjectInputStream objIs) {
        this.objIs = objIs;
    }

    public ObjectOutputStream getObjOs() {
        return objOs;
    }

    public void setObjOs(ObjectOutputStream objOs) {
        this.objOs = objOs;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
