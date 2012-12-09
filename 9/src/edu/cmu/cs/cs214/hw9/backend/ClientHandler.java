package edu.cmu.cs.cs214.hw9.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class abstracts the client and server interaction
 * @author Carlos Gil
 * @author Nick Zukoski
 *
 */
public class ClientHandler {
	
	private int serverPort;
	private String server;
	Socket cSocket;
	private BufferedReader reader;
	private PrintWriter socketWriter;
	
	public ClientHandler() {
		InputStream in = null;
		OutputStream out = null;
		chooseServer();
	}
	
	/**
	 * Register a new user
	 * @param email : user email
	 * @param name : user name
	 * @param password : user password
	 * @return boolean is registered
	 */
	public boolean register(String email, String name, String password) {
		openConnection();
		String requestType = "REGISTER";
		String request = requestType + "____" + email + "____" + name + "____" + password; 
		String response = handleSimpleMessages(request);	
		closeConnection();
		System.out.println("reg response:" +response);
		if (!response.equals("OK")) {
			return false;
		}
		else {
			return true;
		}
		
	}
	
	/**
	 * Get update statuses
	 * @param email : email of user
	 * @param status : status to update
	 * @return updating status was successful 
	 */
	public boolean updateStatus(String email, String status) {
		openConnection();
		String requestType = "UPDATE_STATUS";
		String request = requestType + "____" + email + "____" + status;
		String response = handleSimpleMessages(request);
		closeConnection();
		
		if (!response.equals("OK")) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * Request a new friend
	 * @param email1: first users email
	 * @param email2: second users email
	 * @return if the two users were made friends
	 */
	public boolean requestFriend(String email1, String email2) {
		openConnection();
		String requestType = "REQUEST_FRIEND";
		String request = requestType + "____" + email1 + "____" + email2;
		String response = handleSimpleMessages(request);
		closeConnection();
		
		if (!response.equals("OK")) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * Add a new friend
	 * @param requesterEmail: person requesting the email
	 * @param requesteeEmail: person to friend
	 * @return if they were friended
	 */
	public boolean addFriend(String requesterEmail, String requesteeEmail) {
		openConnection();
		String requestType = "ADD_FRIEND";
		String request = requestType + "____" + requesterEmail + "____" + requesteeEmail + "____" + "CLIENT";
		String response = handleSimpleMessages(request);
		closeConnection();
		
		if (!response.equals("OK")) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * Remove a friend
	 * @param requesterEmail: user requesting to remove a friend
	 * @param requesteeEmail: user to be removed
	 * @return if they were made friends
	 */
	public boolean removeFriend(String requesterEmail, String requesteeEmail) {
		openConnection();
		String requestType = "REMOVE_FRIEND";
		String request = requestType + "____" + requesterEmail + "____" + requesteeEmail + "____" + "CLIENT";
		String response = handleSimpleMessages(request);
		closeConnection();
		
		if (!response.equals("OK")) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * Are two users friends
	 * @param email1: first users email
	 * @param email2: second users email
	 * @return if they are friends
	 */
	public boolean areFriends(String email1, String email2) {
		openConnection();
		String requestType = "ARE_FRIENDS";
		String request = requestType + "____" + email1 + "____" + email2;
		String response = handleSimpleMessages(request);
		closeConnection();
		
		if (response.equals("YES")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Log a user in
	 * @param email: users email
	 * @param password: users password
	 * @return if the users can be logged in
	 */
	public boolean login(String email, String password) {
		openConnection();
		String requestType = "LOGIN";
		String request = requestType + "____" + email + "____" + password;
		String response = handleSimpleMessages(request);
		closeConnection();
		
		if (!response.equals("OK")) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * Get list of pending friend requests
	 * @param email1: first user asking for requests
	 * @param email2: request from second user
	 * @return if there is a pending friend request
	 */
	public boolean pendingFriendRequests(String email1, String email2) {
		openConnection();
		String requestType = "PENDING_FRIEND_REQUEST";
		String request = requestType + "____" + email1 + "____" + email2;
		String response = handleSimpleMessages(request);
		closeConnection();
		
		if (response.equals("YES")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Get the users information
	 * @param email: user to get information's email
	 * @return full name of user
	 */
	public String getUserInfo(String email) {
		openConnection();
		String requestType = "GET_USER_INFO";
		String request = requestType + "____" + email;
		
		String responseLine;
		String fullName = "";
		try {
			sendMessage(request);
			responseLine = reader.readLine();
			if (responseLine == null) {
				fullName = null;
			}
			else {
				String[] args = parseMessage(responseLine);
				System.out.println(responseLine);
				if (args[0].equals("ERROR")) {
					fullName = null;
				}
				else {
					fullName = args[2];
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			fullName = null;
			e.printStackTrace();
		} 

		closeConnection();
		
		return fullName;
	}
	
	/**
	 * Get the users statuses
	 * @param email: the users email
	 * @return the list of statuses for this user
	 */
	public ArrayList<Status> getStatuses(String email) {
		openConnection();
		String requestType = "GET_STATUSES";
		String request = requestType + "____" + email;
		ArrayList<Status>statusList = new ArrayList<Status>();
		
		try {
			String responseLine;
			sendMessage(request);
			while ((responseLine = reader.readLine() ) != null) {
				String[] args = parseMessage(responseLine);
				String userEmail = args[0];
				String message = args[1];
				User u = new User(userEmail, getUserInfo(userEmail), "");
				Date d = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(args[2]);
				Status status = new Status(message, u, d);
				statusList.add(status);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		closeConnection();
		
		return statusList;
	}
	
	
	/**
	 * Get all the friend requests for this user
	 * @param email: email of user to get friend requests
	 * @return list of friend requests
	 */
	public ArrayList<User> getFriendRequests(String email) {
		openConnection();
		String requestType = "GET_FRIEND_REQUESTS";
		String request = requestType + "____" + email;
		
		ArrayList<User>userList = new ArrayList<User>();
		
		try {
			String responseLine;
			sendMessage(request);
			while ((responseLine = reader.readLine() ) != null) {
				String[] args = parseMessage(responseLine);
				String userEmail = args[0];
				User u = new User(userEmail, getUserInfo(userEmail), "");
				
				userList.add(u);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		closeConnection();
		
		return userList;
	}
	
	/**
	 * get a users friends
	 * @param email: email of user to get friends
	 * @return a list of the users friends
	 */
	public ArrayList<User> getFriends(String email) {
		openConnection();
		String requestType = "GET_FRIENDS";
		String request = requestType + "____" + email;
		
		ArrayList<User>userList = new ArrayList<User>();
		
		try {
			String responseLine;
			sendMessage(request);
			while ((responseLine = reader.readLine() ) != null) {
				String[] args = parseMessage(responseLine);
				String userEmail = args[0];
				User u = new User(userEmail, getUserInfo(userEmail), "");
				
				userList.add(u);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		closeConnection();
		
		return userList;
	}
	
	/**
	 * Get friends updates
	 * @param email: email of user to get friends updates
	 * @return list of status updates for user
	 */
	public ArrayList<Status> getFriendUpdates(String email) {
		openConnection();
		String requestType = "GET_FRIEND_UPDATES";
		String request = requestType + "____" + email;
		
		
		ArrayList<Status>statusList = new ArrayList<Status>();
		
		try {
			String responseLine;
			sendMessage(request);
			while ((responseLine = reader.readLine() ) != null) {
				String[] args = parseMessage(responseLine);
				if (args.length != 3) continue;
				String userEmail = args[0];
				String message = args[1];
				User u = new User(userEmail, getUserInfo(userEmail), "");
				Date d = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(args[2]);
				Status status = new Status(message, u, d);
				statusList.add(status);
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		closeConnection();
		
		return statusList;
	}

	/**
	 * Parse the message passed in
	 * @param message: message to parse
	 * @return a string array of message fields
	 */
	public String[] parseMessage(String message) {
		String myDelimiter = "____";
		String[] result = message.split(myDelimiter);  
		return result;
	}
	
	/**
	 * Handle a simple message between servers where there is only a simple response passed back
	 * @param request: the request to handle
	 * @return A response from the server
	 */
	public String handleSimpleMessages(String request) {
		String responseLine = "NO RESPONSE";
		String dummyString;
		try {
			sendMessage(request);
			while ((dummyString = reader.readLine() ) != null) {
				responseLine = dummyString;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseLine;
		
	}
	
	/**
	 * Open a socket connection for read and write
	 */
	public void openConnection() {
		try {
			cSocket = new Socket(InetAddress.getLocalHost(), serverPort);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		InputStream in = null;
		OutputStream out = null;
		try {
			in = cSocket.getInputStream();
			out = cSocket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		reader = new BufferedReader(new InputStreamReader(in));
		socketWriter = new PrintWriter(out, true);

	}
	
	/**
	 * Close a socket connection for read and write
	 */
	public void closeConnection() {
		try{
			reader.close();
			socketWriter.close();
			cSocket.close();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}	
	}
	
	/**
	 * Send a message to the server
	 * @param msg: message to send
	 */
	public void sendMessage(String msg)
	{
		socketWriter.println(msg);
		socketWriter.flush();
	}
	
	/**
	 * Choose a server randomly from a list of given servers
	 */
	public void chooseServer() {
		int serverIndex = (int) (Math.random() * (ServerConstants.serverList.length));
		serverPort = ServerConstants.servePort + serverIndex;
		server = ServerConstants.serverList[serverIndex]+":"+serverPort;

	}

}
