package edu.pitt.cs1635group3;

import java.util.ArrayList;

public class List {
	private String name, lastUpdated;
	private int ID;
	protected ArrayList<Item> listItems;
	
	public List() {
		this.name = null;
		this.ID = -1;
	}
	
	public List(String name, int ID) {
		this.name = name;
		this.ID = ID;
	}
	
	/*
	 * SETTERS
	 */
	
	public void setLastUpdated(String date) {
		this.lastUpdated = date;
	}
	
	
	/*
	 * GETTERS
	 */

	
	public String getName() {
		return name;
	}
	
	public String getLastUpdated() {
		return lastUpdated;
	}
	
	
	
	
}
