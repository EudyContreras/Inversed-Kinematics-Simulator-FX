package com.eudycontreras.javafx.fbk.models;

import java.text.DecimalFormat;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;

/**
 * 2D Vector used for performing vector based calculations and for keeping track
 * of axes.
 * 
 * @author Eudy Contreras
 *
 */
public class FBKVector {

	private static DecimalFormat formatter = new DecimalFormat("0.000");

	private static final double DEGS_TO_RADS = Math.PI / 180.0d;
	private static final double RADS_TO_DEGS = 180.0d / Math.PI;

	public static final FBKVector ZERO = new FBKVector(0, 0);

	public static final FBKVector NULL = null;

	private double x = 0d;
	private double y = 0d;

	public FBKVector() {
		this(0, 0);
	}

	public FBKVector(FBKVector vector) {
		this.x = vector.getX();
		this.y = vector.getY();
	}

	public FBKVector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public FBKVector set(FBKVector vector) {
		this.x = vector.getX();
		this.y = vector.getY();

		return this;
	}

	public FBKVector set(double x, double y) {
		this.x = x;
		this.y = y;

		return this;
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

	public static final FBKVector copy(FBKVector point) {
		return new FBKVector(point.getX(), point.getY());
	}

	public static double heading(FBKVector point) {

		double angle = (double) Math.atan2(-point.getY(), point.getX());

		return -1 * angle;
	}

	public double heading() {

		double angle = (double) Math.atan2(-y, x);

		return -1 * angle;
	}

	public FBKVector sub(FBKVector vector) {
		x = x - vector.x;
		y = y - vector.y;

		return this;
	}

	public static FBKVector sub(FBKVector v1, FBKVector v2) {
		return sub(v1, v2, null);
	}

	public static FBKVector sub(FBKVector v1, FBKVector v2, FBKVector target) {
		if (target == null) {
			target = new FBKVector(v1.getX() - v2.getX(), v1.getY() - v2.getY());
		}
		return target;
	}

	public FBKVector add(FBKVector vector) {
		x = x + vector.x;
		y = y + vector.y;

		return this;
	}

	public static FBKVector add(FBKVector v1, FBKVector v2) {
		return add(v1, v2, null);
	}

	public static FBKVector add(FBKVector v1, FBKVector v2, FBKVector target) {
		if (target == null) {
			target = new FBKVector(v1.getX() + v2.getX(), v1.getY() + v2.getY());
		}

		return target;
	}

	public FBKVector midpoint(double x, double y) {
		return new FBKVector(x + (getX() - x) / 2.0, y + (getY() - y) / 2.0);
	}

	public FBKVector midpoint(FBKVector point) {
		return midpoint(point.getX(), point.getY());
	}

	public FBKVector normalize() {
		double m = mag();

		if (m != 0 && m != 1) {
			div(m);
		}

		return this;
	}

	public static FBKVector normalize(FBKVector point) {
		double m = mag(point);

		if (m != 0 && m != 1) {
			return div(m, point);
		}

		return point;
	}

	public FBKVector div(double value) {
		x /= value;
		y /= value;

		return this;
	}

	public static FBKVector div(double n, FBKVector point) {
		double x = point.getX();
		double y = point.getY();

		x /= n;
		y /= n;

		return new FBKVector(x, y);
	}

	public FBKVector mult(double value) {
		x *= value;
		y *= value;

		return this;
	}

	public static FBKVector mult(double n, FBKVector point) {
		double x = point.getX();
		double y = point.getY();

		x *= n;
		y *= n;

		return new FBKVector(x, y);
	}

	public FBKVector neg() {
		x = -x;
		y = -y;

		return this;
	}

	public FBKVector setMag(double len) {
		normalize();
		mult(len);

		return this;
	}

	public static FBKVector setMag(double len, FBKVector point) {
		point = normalize(point);
		point = mult(len, point);

		return point;
	}

	public double mag() {
		return Math.sqrt((x * x) + (y * y));
	}

	public static double mag(FBKVector point) {
		return Math.sqrt(point.getX() * point.getX() + point.getY() * point.getY());
	}

	public static final double mapValues(double value, double istart, double istop, double ostart, double ostop) {
		return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
	}

	public double getAngle(FBKVector other) {
		return getAngle(this,other);
	}

	public static double getAngle(FBKVector start, FBKVector end) {

		double angle = Math.toDegrees(Math.atan2(end.getY() - start.getY(), end.getX() - start.getX()));
		
	    if(angle < 0){
	        angle += 360;
	    }else if(angle > 360){
	    	 angle -= 360;
	    }
	    
		return angle;
	}

	public static double getAngle(final FBKVector pivotPoints, final FBKVector scenePoints,
			final FBKVector mousePoints) {

		final double angle1 = Math.atan2(pivotPoints.getY() - scenePoints.getY(),
				pivotPoints.getX() - scenePoints.getX());
		final double angle2 = Math.atan2(pivotPoints.getY() - mousePoints.getY(),
				pivotPoints.getX() - mousePoints.getX());

		return ((angle1 - angle2) / Math.PI * 180.0d);
	}

	public static FBKVector rotateDegs(FBKVector source, double angleDegs) {

		double angleRads = angleDegs * DEGS_TO_RADS;

		double cosTheta = Math.cos(angleRads);
		double sinTheta = Math.sin(angleRads);

		return new FBKVector(source.x * cosTheta - source.y * sinTheta, source.x * sinTheta + source.y * cosTheta);
	}

	public double distanceX(double x) {
		double a = getX() - x;

		return Math.sqrt(a * a);
	}

	public double distanceY(double y) {
		double a = getY() - y;

		return Math.sqrt(a * a);
	}

	public double distance(double x1, double y1) {
		double a = getX() - x1;
		double b = getY() - y1;
		return Math.sqrt(a * a + b * b);
	}

	public double distance(FBKVector vector) {
		return distance(vector.getX(), vector.getY());
	}

	public static FBKVector normalized(FBKVector source) {
		return new FBKVector(source).normalize();
	}

	public static FBKVector getDirectionUV(FBKVector a, FBKVector b) {
		return b.sub(a).normalize();
	}

	public static double distanceBetween(FBKVector v1, FBKVector v2) {
		return Math.sqrt((v2.x - v1.x) * (v2.x - v1.x) + (v2.y - v1.y) * (v2.y - v1.y));
	}

	public static FBKVector getCoordinates(Bounds localBounds) {
		return new FBKVector(localBounds.getMinX(), localBounds.getMaxY());
	}

	public static double dot(FBKVector v1, FBKVector v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}

	public double dot(FBKVector v) {
		return x * v.x + y * v.y;
	}

	public static double getUnsignedAngleBetweenVectorsDegs(FBKVector v1, FBKVector v2) {
		return Math.acos(FBKVector.normalized(v1).dot(FBKVector.normalized(v2))) * RADS_TO_DEGS;
	}

	public double getSignedAngleDegsTo(FBKVector otherVector) {
		FBKVector thisVectorUV = FBKVector.normalized(this);
		FBKVector otherVectorUV = FBKVector.normalized(otherVector);

		double unsignedAngleDegs = Math.acos(thisVectorUV.dot(otherVectorUV)) * RADS_TO_DEGS;

		if (FBKVector.zcross(thisVectorUV, otherVectorUV) == 1) {
			return unsignedAngleDegs;
		} else {
			return -unsignedAngleDegs;
		}
	}

	public static int zcross(FBKVector u, FBKVector v) {
		double p = u.x * v.y - v.x * u.y;

		if (p > 0.0f) {
			return 1;
		} else if (p < 0.0f) {
			return -1;
		}
		return 0;
	}

	public static FBKVector getConstrainedUV(FBKVector directionUV, FBKVector baselineUV, double clockwiseConstraintDegs, double antiClockwiseConstraintDegs) {

		double signedAngleDegs = baselineUV.getSignedAngleDegsTo(directionUV);

		if (signedAngleDegs > antiClockwiseConstraintDegs) {
			return FBKVector.rotateDegs(baselineUV, antiClockwiseConstraintDegs);
		}

		if (signedAngleDegs < -clockwiseConstraintDegs) {
			return FBKVector.rotateDegs(baselineUV, -clockwiseConstraintDegs);
		}

		return directionUV;
	}

	public FBKVector rotateRads(double angleRads) {

		double cosTheta = Math.cos(angleRads);
		double sinTheta = Math.sin(angleRads);

		FBKVector rotatedVector = new FBKVector(x * cosTheta - y * sinTheta, x * sinTheta + y * cosTheta);

		x = rotatedVector.x;
		y = rotatedVector.y;

		return this;
	}

	@Override
	public String toString() {
		return formatter.format(x) + ", " + formatter.format(y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FBKVector other = (FBKVector) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	public static FBKVector toVector(Point2D point) {
		return new FBKVector(point.getX(), point.getY());
	}

	public Point2D toPoint2D() {
		return new Point2D(getX(), getY());
	}
	
	public static Point2D toPoint2D(FBKVector vector) {
		return new Point2D(vector.getX(), vector.getY());
	}
	
	public static class FBKVectorCapsule {

		private double distance;
		private FBKVector vector;

		public FBKVectorCapsule(double distance, FBKVector vector) {
			super();
			this.distance = distance;
			this.vector = vector;
		}

		public double getDistance() {
			return distance;
		}

		public void setDistance(double distance) {
			this.distance = distance;
		}

		public FBKVector getVector() {
			return vector;
		}

		public void setVector(FBKVector vector) {
			this.vector = vector;
		}
	}

}
