package com.eudycontreras.models;

public class ToggleInfo{
	
	private String name;
	
	private Runnable action;

	public ToggleInfo(String name, Runnable action) {
		super();
		this.name = name;
		this.action = action;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Runnable getAction() {
		return action;
	}

	public void setAction(Runnable action) {
		this.action = action;
	}
	
	
}
