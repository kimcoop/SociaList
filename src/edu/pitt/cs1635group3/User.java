package edu.pitt.cs1635group3;

public class User {

	private int ID;
	private String name;
	
	public User(int i, String s) {
		this.ID = i;
		this.name = s;
	}
	
	public User() {
	}
	
	/*
	 * SETTERS
	 */
	
	public void setName(String s) {
		this.name = s;
	}
	
	public void setID(int i) {
		this.ID = i;
	}
	
	/*
	 * GETTERS
	 */
	
	public String getName() {
		return name;
	}
	
	public int getID() {
		return ID;
	}
	
	
}
