package edu.cmu.cs.cs214.hw9.backend;

import java.util.List;

public interface Backend {

	/**
	 * Returns a list of registered Users
	 * @return a list of registered Users
	 */
	List<User> getUsers();
	
	/**
	 * Attempts to store the user object in the
	 * backend abstraction.
	 * @return true if the store was successful
	 *          false if otherwise
	 */
	boolean register (User u);
	
	/**
	 * Attempts to remove the user object in the
	 * backend abstraction.
	 * @return true if the remove was successful
	 *          false if otherwise
	 */
	boolean removeUser(User u);
	
	/**
	 * Returns the unique user with the given email
	 * @return a User object from the backend 
	 * 			that has the associated email.
	 * 			null if no user has this email
	 */
	User getUser(String email);
	
	/**
	 * Attempts to store the Status object in the
	 * backend abstraction.
	 * @return true if the store was successful
	 *          false if otherwise
	 */
	boolean storeStatus(Status s);
	 
	/**
	 * Attempts to remove the Status object in the
	 * backend abstraction.
	 * @return true if the remove was successful
	 *          false if otherwise
	 */
	boolean removeMostRecentStatus(User u);
	
	/**
	 * Returns the list of statuses posted by the given
	 * User.
	 * @return a list of Statuses that have been posted
	 * 			by the given user.
	 * 			Empty list if the user has posted no
	 * 	 		statuses.
	 */
	List<Status> getStatuses(User u);
	
	/**
	 * Adds friend link between u1 and u2
	 * Should only be used for testing purposes,
	 * in practice requestFriend should be used both ways.
	 * @return true if the store was successful
	 *          false if otherwise
	 */
	boolean storeFriend(User u1, User u2);
	
	/**
	 * Stores a friend request from requester to requestee
	 * @param requester the requesting User
	 * @param requestee the requested User
	 * @return true if it is not a repeat and you are not friends
	 * 			false otherwise.
	 */
	boolean requestFriend(User requester, User requestee);
	
	/**
	 * Removes friend link between u1 and u2
	 * @return true if the remove was successful
	 *          false if otherwise
	 */
	boolean removeFriend(User u1, User u2);
	
	/**
	 * Returns a list of users that make up the
	 * given user's friends list
	 * @return a User list from the backend 
	 * 			that represents the given User's
	 * 			friends list.
	 * 			Empty list if use is friendless.
	 */
	List<User> getFriends(User u);
	
	/**
	 * Attempt to login with the provided information
	 * @param email email address of user
	 * @param pwd password of the user
	 * @return true if login successful, false otherwise
	 */
	boolean login(String email, String pwd);
	
	
	/**
	 * Returns a list of the latest n
	 * status updates posted by friends
	 * of user u
	 * @return a Status list of size <= n
	 * 			of updates by friends of the
	 * 			given User.
	 */
	List<Status> getMostRecentFriendUpdates(User u, int n);

	/**
	 * Returns a list of the User's requesting the
	 * given User as a friend
	 * @param user The user whose requests we will be getting
	 * @return a User list of the User's requesting user.
	 */
	List<User> getFriendRequests(User user);

	/**
	 * Check if the two given users are friends
	 * @param u1 first user
	 * @param u2 second user
	 * @return true if the two users are friends, false otherwise
	 */
	boolean areFriends(User u1, User u2);
}
