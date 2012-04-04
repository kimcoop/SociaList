package edu.pitt.cs1635group3;

public class User {

	private int ID;
	private String email, first, last;
	private String deviceToken, deviceID;

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

	public void setDeviceToken(String s) {
		this.deviceToken = s;
	}
	
	public void setDeviceID(String s) {
		this.deviceID = s;
	}
	
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
	
	
	public String getDeviceToken() {
		return deviceToken;
	}
	
	public String getDeviceID() {
		return deviceID;
	}
	
	
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