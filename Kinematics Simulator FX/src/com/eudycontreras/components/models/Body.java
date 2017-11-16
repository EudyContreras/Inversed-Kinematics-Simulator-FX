package com.eudycontreras.components.models;

public abstract class Body {
    protected double x, y; // position
    protected double w, h; // dimensions for drawing
    protected double vx = 0, vy = 0; // velocities along x, y axes
    protected double ax = 0, ay = 0; // accelerations along x, y axes
    protected double mass = 1; // default = 1
    protected double vertical_speed = 0;
    protected boolean stationary = false;
    
    protected static final double  GRAVITY = 0.8;
    protected static final double  TERMINAL_VELOCITY = 300;

    protected double applyGravity (double positionY)
    {
        
        positionY = positionY + GRAVITY;
        
        return positionY;
    }
    
	protected double getX() {
		return x;
	}
	protected void setX(double x) {
		this.x = x;
	}
	protected double getY() {
		return y;
	}
	protected void setY(double y) {
		this.y = y;
	}
	protected double getW() {
		return w;
	}
	protected void setW(double w) {
		this.w = w;
	}
	protected double getH() {
		return h;
	}
	protected void setH(double h) {
		this.h = h;
	}
	protected double getVx() {
		return vx;
	}
	protected void setVx(double vx) {
		this.vx = vx;
	}
	protected double getVy() {
		return vy;
	}
	protected void setVy(double vy) {
		this.vy = vy;
	}
	protected double getAx() {
		return ax;
	}
	protected void setAx(double ax) {
		this.ax = ax;
	}
	protected double getAy() {
		return ay;
	}
	protected void setAy(double ay) {
		this.ay = ay;
	}
	protected double getMass() {
		return mass;
	}
	protected void setMass(double mass) {
		this.mass = mass;
	}
	protected boolean isStationary() {
		return stationary;
	}
	protected void setStationary(boolean stationary) {
		this.stationary = stationary;
	}
    
    
    
    
}