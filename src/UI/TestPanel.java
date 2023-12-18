package UI;

import javax.swing.*;

public class TestPanel extends JPanel{
    private JFrame context;
    public TestPanel(JFrame context, String clientUserName) {
        this.context = context;

        add(new JLabel(clientUserName + "님, 안녕하세요."));
    }
}
