package edu.cmu.cs.cs214.hw9.backend;

public class User {

	private final String email;
	private final String fullname;
	private final String password;
	
	/**
	 * The constructor for the User object. 
	 * @param mail the User's email
	 * @param name the User's full name
	 * @param pass the User's pass
	 */
	public User(String mail, String name, String pass){
		email = mail;
		fullname = name;
		password = pass;
	}
	
	/**
	 * Constructs a minimal user object, for use on servers that don't
	 * hold this users data.
	 * 
	 * @param mail the email to the User
	 */
	public User(String mail) {
		email = mail;
		fullname = "";
		password = "";
	}
	
	public String getEmail() {
		return email;
	}

	public String getFullname() {
		return fullname;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public boolean equals(Object other){
		if(!(other instanceof User)) return false;
		return this.email.equals(((User)other).email);
	}
	
	@Override
	public int hashCode(){
		return this.email.hashCode();
	}
	
}
