package edu.cmu.cs.cs214.hw9.backend;

public class ServerConstants {
	//Array of server IPs. The servers ID is the index in this list.
	private static final String[] serverList = {"localhost", "localhost"};
	public static final int servePort = 1470;

	public static String[] getServerlist() {
		return serverList;
	}
	
	public static final int getServerPort() {
		return servePort;
	}

}
