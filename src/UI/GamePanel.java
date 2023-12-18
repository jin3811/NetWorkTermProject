package UI;

import javax.swing.*;

// 실제 게임을 진행할 panel
public class GamePanel extends JPanel {
    private RandomDefence context;

    public GamePanel(RandomDefence context, String nickname, Socket socket) {
        this.context = context;
        
        context.setSize(1000, 800);
        
    }
}
