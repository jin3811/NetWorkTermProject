package Main;

import LoginWindow.LoginFormPanel;
import util.TransitionDisplayCommand;

import javax.swing.*;
import java.awt.*;

public class RandomDefence extends JFrame{

    private LoginFormPanel loginFormPanel;
    private ImageIcon icon = new ImageIcon("background.jpg");
    private Image img = icon.getImage();


    public RandomDefence(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setSize(1000, 800);

        initWindow();

        setVisible(true);

//        loginFormPanel.setFocusable(true);
//        loginFormPanel.requestFocus();
    }

    private void initWindow() {
        loginFormPanel = new LoginFormPanel(this);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 50, 50, 50); // 내부 여백 설정
        add(loginFormPanel, gbc);
    }

    public void transition(JPanel removeTarget, JPanel displayTarget, TransitionDisplayCommand displayCommand) {
        remove(removeTarget);
        add(displayTarget);
        revalidate();
        repaint();
        if (displayCommand != null) displayCommand.execute();
    }

    public void transition(JPanel removeTarget, JPanel displayTarget) {
        transition(removeTarget, displayTarget, null);
    }
}
