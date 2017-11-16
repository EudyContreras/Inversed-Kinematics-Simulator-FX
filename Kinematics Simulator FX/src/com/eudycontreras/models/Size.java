package com.eudycontreras.models;

public class Size {

	public int width;
	public int height;
	
	public int Width;
	public int Height;

	
	public Size(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		this.Width = width;
		this.Height = height;
	}

	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	
}
