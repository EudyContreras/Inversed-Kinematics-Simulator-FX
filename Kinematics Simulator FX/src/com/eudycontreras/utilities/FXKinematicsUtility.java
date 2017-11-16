package com.eudycontreras.utilities;

import javafx.geometry.Point2D;

public class FXKinematicsUtility {

	public static final Point2D copy(Point2D point) {
		return new Point2D(point.getX(), point.getY());
	}

	public static double heading(Point2D point) {

		double angle = (float) Math.atan2(-point.getY(), point.getX());

		return -1 * angle;
	}

	public static Point2D sub(Point2D v1, Point2D v2) {
		return sub(v1, v2, null);
	}

	public static Point2D sub(Point2D v1, Point2D v2, Point2D target) {
		if (target == null) {
			target = new Point2D(v1.getX() - v2.getX(), v1.getY() - v2.getY());
		}
		return target;
	}

	public static Point2D add(Point2D v1, Point2D v2) {
		return add(v1, v2, null);
	}

	/**
	 * Add two vectors into a target vector
	 * 
	 * @param target
	 *            the target vector (if null, a new vector will be created)
	 */
	public static Point2D add(Point2D v1, Point2D v2, Point2D target) {
		if (target == null) {
			target = new Point2D(v1.getX() + v2.getX(), v1.getY() + v2.getY());
		} 
		
		return target;
	}

	public static Point2D normalize(Point2D point) {
		float m = mag(point);
		
		if (m != 0 && m != 1) {
			return div(m, point);
		}
		
		return point;
	}
	
	public static Point2D div(float n, Point2D point) {
		double x = point.getX();
		double y = point.getY();

		x /= n;
		y /= n;

		return new Point2D(x,y);
	}
	
	public static Point2D mult(float n, Point2D point) {
		double x = point.getX();
		double y = point.getY();

	    x *= n;
	    y *= n;
	    
	    return new Point2D(x,y);
	  }

	public static Point2D setMag(float len, Point2D point) {
		point = normalize(point);
		point = mult(len, point);
		
		return point;
	}
	 
	public static float mag(Point2D point) {
	    return (float) Math.sqrt(point.getX()*point.getX() + point.getY()*point.getY());
	  }

	public static final double mapValues(double value, double istart, double istop, double ostart, double ostop) {
		return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
	}
	
	public static float getAngle(Point2D current, Point2D target) {
		
	    float angle = (float) Math.toDegrees(Math.atan2(target.getY() - current.getY(), target.getX() - current.getX()));

	    if(angle < 0){
	        angle += 360;
	    }

	    return angle;
	}

	public static double computeAngle(final Point2D pivotPoints, final Point2D scenePoints, final Point2D mousePoints) {

		final double angle1 = Math.atan2(pivotPoints.getY() - scenePoints.getY(),
				pivotPoints.getX() - scenePoints.getX());
		final double angle2 = Math.atan2(pivotPoints.getY() - mousePoints.getY(),
				pivotPoints.getX() - mousePoints.getX());

		return (angle1 - angle2) / Math.PI * 180;
	}
}
