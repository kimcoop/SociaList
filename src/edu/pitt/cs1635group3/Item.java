package edu.pitt.cs1635group3;


public class Item {

	private String name, assignee;
	private boolean selected;

	public Item(String name, String a) {
		this.name = name;
		this.assignee = a;
		selected = false;
	}

	public String getName() {
		return name;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
