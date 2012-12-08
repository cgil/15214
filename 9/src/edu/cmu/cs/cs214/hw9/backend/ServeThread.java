package edu.cmu.cs.cs214.hw9.backend;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

public class ServeThread extends Thread {
	private final BufferedReader reader;
	private final PrintWriter writer;
	private final Backend db;
	private final Map<String, Integer> userTable;
	private final int serverID;
	/**
	 * Creates a serverThread to handle requests to register or unregister
	 * @param in The stream from the client
	 * @param out The stream back to the client
	 * @param b The database 
	 */
	public ServeThread(InputStream in, OutputStream out, Backend b, Map<String, Integer> userTable, int id) {
		reader = new BufferedReader(new InputStreamReader(in));
		writer = new PrintWriter(out, true);
		db = b;
		this.userTable = userTable;
		serverID = id;
	}
	
	/** Reads one line at a time and transfers to the writer */
	public void run() {
		try {
			String line = reader.readLine();
			
			String[] args = line.split(" ");
			String requestType = args[0];
			String email = args[1];
			
			//First handle requests that deal with adding new users to the userTable
			if (requestType.equals("REGISTER")) {
				//REGISTER (email) (username) (password)
				if (args.length != 4) {
					returnError("BAD_ARGUMENTS");
					return;
				}
				if (userTable.get(args[1]) != null) {
					returnError("USER_ALREADY_REGISTERED");
					return;
				}
				
				String name = args[2];
				String password = args[3];
				//register the user on this server
				db.register(new User(email, name, password));
				userTable.put(email, serverID);
				updateUserTables(email, true);
				return;
			}
			else if (requestType.equals("NEW_USER")) {
				//NEW_USER (email) (serverID)
				
				int serverID = Integer.parseInt(args[2]);
				userTable.put(email, serverID);
				return;
			}
			
			//Now check if the data requested is stored locally
			if (userTable.get(email) == null) {
				returnError("UNKNOWN_USER");
				return;
			}
			
			int locationID = userTable.get(email);
			if (locationID != serverID) {
				forwardRequestToServer(locationID, line);
				return;
			}
			
			//The data must be local at this point
			if (requestType.equals("UNREGISTER")) {
				
			}
			else if (requestType.equals("UPDATE_STATUS")) {
				
			}
			else if (requestType.equals("ADD_FRIEND")) {
				
			}
			else if (requestType.equals("REMOVE_FRIEND")) {
				
			}
			else if (requestType.equals("LOGIN")) {
				//LOGIN (email) (password)
				String pwd = args[2];
				if (db.login(email, pwd)) {
					writer.write("OK");
				}
				else {
					returnError("BAD_LOGIN");
				}
			}
			else if (requestType.equals("GET_USER")) {
				
			}
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the given error message to the output stream
	 * @param error The error string to return
	 */
	private void returnError(String error) {
		writer.println("ERROR " + error);
	}
	
	/**
	 * Send a notification to all the other servers that a new user has been registered
	 * @param email the users email
	 * @param justRegistered True if the email needs to be added, False if it needs to be removed
	 */
	private void updateUserTables(String email, boolean justRegistered) {
		
	}

	/**
	 * Forwards a request to a server that can handle it and writes what is returned
	 * to the output stream of this thread.
	 * 
	 * @param destID The serverID that can handle the request
	 * @param request The request string
	 */
	private void forwardRequestToServer(int destID, String request) {
		try {
			Socket requestSock = new Socket(ServerConstants.serverList[destID], ServerConstants.servePort + destID);
			PrintWriter w = new PrintWriter(requestSock.getOutputStream(), true);
			BufferedReader r = new BufferedReader(new InputStreamReader(requestSock.getInputStream()));
			w.println(request);
			String responseLine;
			while ((responseLine = r.readLine()) != null) {
				w.println(responseLine);
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
