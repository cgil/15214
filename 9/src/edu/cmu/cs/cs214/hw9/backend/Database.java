package edu.cmu.cs.cs214.hw9.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Database implements Backend{
	
	private HashSet<User> userStorage= new HashSet<User>();
	private HashMap<User, ArrayList<Status>> statusStorage = new HashMap<User, ArrayList<Status>>();
	private HashMap<User, ArrayList<User>> friendStorage = new HashMap<User, ArrayList<User>>();
	private HashMap<User, ArrayList<User>> requestStorage = new HashMap<User, ArrayList<User>>();
	private class StatusCompare implements Comparator<Status>{

		@Override
		public int compare(Status a, Status b) {
			return -a.getTimestamp().compareTo(b.getTimestamp());
		}
		
	}
	public Database(){}
	
	public boolean register (User u) {
		if(getUser(u.getEmail()) != null){
			return false;
		}
		userStorage.add(u);
		statusStorage.put(u, new ArrayList<Status>());
		friendStorage.put(u, new ArrayList<User>());
		requestStorage.put(u, new ArrayList<User>());
		return true;
	}

	public boolean removeUser(User u) {
		if(getUser(u.getEmail()) == null){
			return false;
		}
		userStorage.remove(u);
		statusStorage.remove(u);
		for(User u1 : friendStorage.get(u)){
			removeFriend(u,u1);
		}
		friendStorage.remove(u);
		requestStorage.remove(u);
		return true;
	}

	public User getUser(String email) {
		for(User u: userStorage){
			if(u.getEmail().equals(email)){
				return u;
			}
		}
		return null;
	}

	public boolean storeStatus(Status s) {
		if (!statusStorage.containsKey(s.getPoster())){
			return false;
		}
		statusStorage.get(s.getPoster()).add(s);
		return true;
	}

	public boolean removeMostRecentStatus(User u) {
		if (!statusStorage.containsKey(u)){
			return false;
		}
		ArrayList<Status> s = statusStorage.get(u);
		s.remove(s.size()-1);
		return true;
	}
	
	public List<Status> getStatuses(User u) {
		return statusStorage.get(u);
	}

	public boolean storeFriend(User u1, User u2) {
		if (friendStorage.get(u1).contains(u2)){
			return false;
		}
		friendStorage.get(u1).add(u2);
		friendStorage.get(u2).add(u1);
		return true;
	}

	public boolean removeFriend(User u1, User u2) {
		if (!friendStorage.get(u1).contains(u2)){
			return false;
		}
		friendStorage.get(u1).remove(u2);
		friendStorage.get(u1).remove(u2);
		return true;
	}

	public List<User> getFriends(User u) {
		return friendStorage.get(u);
	}

	@Override
	public List<Status> getMostRecentFriendUpdates(User u, int n) {
		List<User> friends = getFriends(u);
		ArrayList<Status> allStatus = new ArrayList<Status>();
		for(User f: friends){
			List<Status> currStatus = getStatuses(f);
			for(int i = 0; i < n && i< currStatus.size(); i++){
				allStatus.add(currStatus.get(i));
			}
		}
		Collections.sort(allStatus, new StatusCompare());
		if(allStatus.size() < n){
			allStatus.trimToSize();
			return allStatus;
		}
		else{
			List<Status> statArray = new ArrayList<Status>();
			for(int i = 0; i < n; i ++){
				statArray.add(allStatus.get(i));
			}
			return statArray;
		}
	}

	@Override
	public boolean requestFriend(User requester, User requestee) {
		if(getUser(requester.getEmail()) == null
				|| getUser(requestee.getEmail()) == null){
			return false;
		}
		if(requestStorage.get(requestee).contains(requester) 
				|| friendStorage.get(requester).contains(requestee)){
			return false;
		}
		if(requestStorage.get(requester).contains(requestee)){
			requestStorage.get(requester).remove(requestee);
			storeFriend(requester, requestee);
			return true;
		}
		else{
			requestStorage.get(requestee).add(requester);
			return true;
		}
	}
	
	@Override
	public boolean login(String email, String pwd){
		User u = getUser(email);
		if(u == null) return false;
		return pwd.equals(u.getPassword());
	}
	

	@Override
	public List<User> getFriendRequests(User user) {
		if(getUser(user.getEmail()) == null){
			throw new IllegalArgumentException("user not found.");
		}
		return requestStorage.get(user);
	}

	@Override
	public boolean areFriends(User u1, User u2) {
		if(getUser(u1.getEmail()) == null || getUser(u2.getEmail()) == null){
			throw new IllegalArgumentException("users must be in the backend");
		}
		return u1.equals(u2) || friendStorage.get(u1).contains(u2);
	}

	@Override
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		for(User u: userStorage){
			users.add(u);
		}
		return users;
	}	
}
