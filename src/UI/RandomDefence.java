package UI;

import UI.LoginFormPanel;
import util.TransitionDisplayCommand;

import javax.swing.*;
import java.awt.*;

public class RandomDefence extends JFrame{

    private LoginFormPanel loginFormPanel;

    public RandomDefence(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initWindow();

        setVisible(true);
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

    public void transition(JPanel displayTarget, TransitionDisplayCommand displayCommand) {
        getContentPane().removeAll();
        getContentPane().add(displayTarget);
        setContentPane(displayTarget);
        getContentPane().revalidate();
        getContentPane().repaint();
        if (displayCommand != null) displayCommand.execute();
    }

    public void transition(JPanel displayTarget) {
        transition(displayTarget, null);
    }
}
