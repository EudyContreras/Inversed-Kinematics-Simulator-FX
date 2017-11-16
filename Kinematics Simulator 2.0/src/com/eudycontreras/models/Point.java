package com.eudycontreras.models;

public class Point {

	public double x;
	public double y;
			
	public double X;
	public double Y;
	
	public Point() {
		super();
	}
	
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
		this.X = x;
		this.Y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	
}
