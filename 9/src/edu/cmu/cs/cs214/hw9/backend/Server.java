package edu.cmu.cs.cs214.hw9.backend;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server extends Thread {

	Backend localStorage = new Database();
	boolean serverOn = true;
	Map<String, Integer> userTable;
	ServerSocket sSock;
	final int id;
	
	public Server(int id) {
		this.id = id;
		userTable = new HashMap<String, Integer>();
		try {
			sSock = new ServerSocket(ServerConstants.servePort + id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (serverOn) {
			try {
				Socket sock = sSock.accept();
				ServeThread t = new ServeThread(sock.getInputStream(), sock.getOutputStream(), localStorage, userTable, id);
				t.run();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	

}
