import java.net.*;
import java.util.*;

import LoginWindow.RandomDefence;

import java.io.*;
public class Client {
	final static int ServerPort = 9999;   // 포트 번호
	DataInputStream is;
	DataOutputStream os;
	private Socket socket;
	private String name;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new RandomDefence("랜덤 디펜스 게임");
		
	}

}
