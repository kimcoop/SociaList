package edu.pitt.cs1635group3;


public class Item {

	//item(name, assigner, assignee, creation_date, notes, quantity, creator, completion_date, complete)
	private String name, assigner, assignee, creation_date, notes, creator, completion_date;
	private int id, quantity;
	private boolean selected, completed;

	public Item(int id, String name, String a1, String a2, String c1, String n, int q, String c2, String c_date, boolean complete) {
		this.id = id;
		this.name = name;
		this.assigner = a1;
		this.assignee = a2;
		this.creation_date = c1;
		this.notes = n;
		this.creator = c2;
		this.completion_date = c_date;
		this.quantity = q;
		this.completed = complete;
		selected = false;
	}

	
	/*
	 * GETTERS
	 */
	
	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAssignee() {
		return assignee;
	}

	public boolean isSelected() {
		return selected;
	}
	
	/*
	 * SETTERS
	 */

	public void setName(String name) {
		this.name = name;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
