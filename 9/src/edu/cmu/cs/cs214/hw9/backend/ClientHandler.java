package edu.cmu.cs.cs214.hw9.backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler {
	
	private int serverPort;
	private String server;
	Socket cSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message;
	
	public ClientHandler(String request) {
		
		chooseServer();
		message = null;
		run();
		
	}
	
	public void run() {
		try {
			
			openConnection();
			
			while(message == null) {
				
				try {
					message = (String)in.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
			}
			
		} catch(IOException ioException){
			ioException.printStackTrace();
		}
		
		finally {
			closeConnection();	
		}


	}
	
	//Open socket and read/write connection
	public void openConnection() {
		try {
			cSocket = new Socket(server, serverPort);
			out = new ObjectOutputStream(cSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(cSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Close socket
	public void closeConnection() {
		try{
			in.close();
			out.close();
			cSocket.close();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}	
	}
	
	//Send a message to the server
	public void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	//Get a server and port from the serverList 
	public void chooseServer() {
		
		ServerConstants.getServerlist();
		int serverIndex = (int) (Math.random() * (ServerConstants.getServerlist().length));
		serverPort = ServerConstants.getServerPort() + serverIndex;
		server = ServerConstants.getServerlist()[serverIndex]+":"+serverPort;

	}

}
