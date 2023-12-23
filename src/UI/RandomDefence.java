package UI;

import UI.LoginFormPanel;
import util.TransitionDisplayCommand;

import javax.swing.*;
import java.awt.*;

/*
 * RandomDefence: 게임의 메인 윈도우를 나타내는 JFrame을 확장하는 클래스
 * 이 클래스는 게임의 메인 프레임을 초기화하고 로그인 패널을 표시하는 역할
 * */
public class RandomDefence extends JFrame{

    private LoginFormPanel loginFormPanel; // 로그인 폼 패널 객체 
    /*
     * 생성자: 윈도우의 타이틀을 설정하고 초기화 메소드를 호출
     * title은 프레임의 타이틀 바에 표시될 문자열
     */
    public RandomDefence(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initWindow(); // 윈도우 초기화

        setVisible(true);
    }
    /*
     * initWindow 메소드: 윈도우 내부를 초기화
     * 로그인 폼 패널을 생성하고, 윈도우에 추가
     */
    private void initWindow() {
        loginFormPanel = new LoginFormPanel(this); // 로그인 폼 패널 인스턴스 생성

        setLayout(new GridBagLayout()); // 레이아웃 설정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 50, 50, 50); // 내부 여백 설정
        add(loginFormPanel, gbc); // 로그인 패널을 윈도우에 추가
    }
    /*
     * transition 메소드: 새로운 패널로 윈도우의 내용을 전환하는 역할
     * displayTarget: 표시할 새로운 패널
     * displayCommand: 전환 후 실행할 추가 명령이 있을 경우 실행될 콜백
     */
    public void transition(JPanel displayTarget, TransitionDisplayCommand displayCommand) {
        getContentPane().removeAll(); // 현재 컨텐트 팬의 모든 컴포넌트를 제거
        getContentPane().add(displayTarget); // 새로운 패널을 컨텐트 팬에 추가
        setContentPane(displayTarget); // 새로운 패널을 컨텐트 팬으로 설정
        getContentPane().revalidate(); // 컨텐트 팬을 다시 레이아웃하도록 요청
        getContentPane().repaint(); // 컨텐트 팬을 다시 그리도록 요청
        if (displayCommand != null) displayCommand.execute();
    }
    /*
     * 오버로딩된 transition 메소드: 추가 명령 없이 패널만 전환할 때 사용
     * displayTarget: 표시할 새로운 패널
     */
    public void transition(JPanel displayTarget) {
        transition(displayTarget, null);
    }
}
