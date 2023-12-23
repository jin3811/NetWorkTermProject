package Server.Manager;

import Server.Server;
import Server.UserService;

import java.util.Vector;
/*
 * UserManager: 서버에 연결된 모든 사용자를 관리하는 클래스
 * 서버에 연결된 모든 UserService 인스턴스를 저장하고 관리
 */
public class UserManager {
	// 서버에 연결된 모든 사용자 저장하는 벡터
    private Vector<UserService> allUsers = new Vector<>();
    private Server server;

    // 생성자: 서버 인스턴스 초기화
    public UserManager(Server server) {
        this.server = server;
    }

    // 새 사용자 allUsers 벡터에 추가
    public void addUser(UserService user) {
        allUsers.add(user);
    }
    
    // 서버에 연결된 모든 사용자 목록 반환
    public Vector<UserService> getAllUsers() {
        return allUsers;
    }

    // 특정 id 가진 사용자 반환
    public UserService getUserbyId(int id) {
        for (UserService user : allUsers) {
            if(user.getUserID() == id) {
                return user;
            }
        }
        return null;
    }
}
