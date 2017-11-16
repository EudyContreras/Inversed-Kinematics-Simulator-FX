package com.eudycontreras.models;

public class Size {

	public double width;
	public double height;
	
	public double Width;
	public double Height;

	
	public Size(double width, double height) {
		super();
		this.width = width;
		this.height = height;
		this.Width = width;
		this.Height = height;
	}

	public double getWidth() {
		return width;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	
}
