package edu.pitt.cs1635group3;

import java.util.ArrayList;

public class CustomList {
	private String name, lastUpdated;
	private int ID;
	protected ArrayList<Item> listItems;

	public CustomList() {
		this.name = null;
		this.ID = -1;
	}

	public CustomList(int ID, String name) {
		this.ID = ID;
		this.name = name;
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
	
	public Item getItemAfter(Item item) {
		//return the item in the list directly proceeding Item item
		int i = listItems.indexOf(item);
		return listItems.get(i+1);
	}
	
	public Item getItemBefore(Item item) {
		//like previous method
		int i = listItems.indexOf(item);
		return listItems.get(i-1); // Thoughts on this - If we're doing this lookup often, better to store item's position as item var?
	}

}
