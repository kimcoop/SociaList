package edu.pitt.cs1635group3;

public class User {

	private int ID;
	private String email, first, last;

	public User(int i, String f, String l, String e) {
		this.ID = i;
		this.first = f;
		this.last = l;
		this.email = e;
	}

	public User() {
	}

	/*
	 * SETTERS
	 */

	public void setFirstName(String s) {
		this.first = s;
	}

	public void setLastName(String s) {
		this.last = s;
	}
	
	public void setEmail(String s) {
		this.email = s;
	}

	public void setID(int i) {
		this.ID = i;
	}

	/*
	 * GETTERS
	 */
	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return first;
	}

	public String getLastName() {
		return last;
	}

	public String getName() {
		return first + " " + last;
	}

	public int getID() {
		return ID;
	}

}