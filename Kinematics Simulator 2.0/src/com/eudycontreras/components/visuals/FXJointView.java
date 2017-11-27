package com.eudycontreras.components.visuals;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;

public abstract class FXJointView implements IFXJointView{	

	public static final IFXJointView FLESH_A = getFXJointView(FXJointViewType.TYPE_A);
	
	public static final IFXJointView FLESH_B = getFXJointView(FXJointViewType.TYPE_B);

	protected double centerX = 0;
	protected double centerY = 0;
	
	protected double sectionDistance = 10;
	
	protected double outerRadius = -1;
	protected double innerRadius = -1;
	protected double centerRadius = -1;
	
	protected double strokeWidth = 2;

	protected Color mainColor = Color.DODGERBLUE;
	protected Color centerColor = Color.BLACK;
	protected Color strokeColor = Color.DODGERBLUE.deriveColor(1, 1, 1, 0.3);
	
	public static IFXJointView getFXJointView(FXJointViewType type) {
		switch(type){
		case TYPE_A:
			return new FXJointViewA(0);
		case TYPE_B:
			return new FXJointViewB(0);
		}
		return null;
	}
	
	public static IFXJointView getFXJointView(FXJointViewType type, double radius) {
		switch (type) {
		case TYPE_A:
			return new FXJointViewA(radius);
		case TYPE_B:
			return new FXJointViewB(radius);
		}
		return null;
	}

	public static IFXJointView getFXJointView(FXJointViewType type, double radius, Color mainColor) {
		switch (type) {
		case TYPE_A:
			return new FXJointViewA(radius, mainColor);
		case TYPE_B:
			return new FXJointViewB(radius, mainColor);
		}
		return null;
	}
	
	
	public static IFXJointView getFXJointView(FXJointViewType type, double radius, Color mainColor, Color centerColor) {
		switch (type) {
		case TYPE_A:
			return new FXJointViewA(radius, mainColor, centerColor);
		case TYPE_B:
			return new FXJointViewB(radius, mainColor);
		}
		return null;
	}

	public static IFXJointView getFXJointView(FXJointViewType type, double centerX, double centerY, double radius) {
		switch (type) {
		case TYPE_A:
			return new FXJointViewA(centerX, centerY, radius, Color.DODGERBLUE);
		case TYPE_B:
			return new FXJointViewB(centerX, centerY, radius, Color.DODGERBLUE);
		}
		return null;
	}
	
	public static IFXJointView getFXJointView(FXJointViewType type, double centerX, double centerY, double radius, Color mainColor) {
		switch (type) {
		case TYPE_A:
			return new FXJointViewA(centerX, centerY, radius, mainColor);
		case TYPE_B:
			return new FXJointViewB(centerX, centerY, radius, mainColor);
		}
		return null;
	}
	
	public double getCenterX() {
		return centerX;
	}

	public double getCenterY() {
		return centerY;
	}

	public double getOuterRadius() {
		return outerRadius;
	}

	public double getInnerRadius() {
		return innerRadius;
	}

	public double getSectionDistance() {
		return sectionDistance;
	}

	public Color getMainColor() {
		return mainColor;
	}

	public Color getCenterColor() {
		return centerColor;
	}

	private static class FXJointViewA extends FXJointView {

		private Shape shape = null;

		private Group merger = new Group();

		private Ellipse bigCircle = null;
		private Ellipse smallCircle = null;
		private Ellipse centerCircle = null;

		public FXJointViewA(double radius) {
			this(radius, Color.DODGERBLUE);
		}

		public FXJointViewA(double radius, Color mainColor) {
			this(radius, mainColor, Color.BLACK);
		}

		public FXJointViewA(double radius, Color mainColor, Color centerColor) {
			this(0, 0, radius, radius * 0.65, radius * 0.30, mainColor, centerColor);
		}

		public FXJointViewA(double centerX, double centerY, double radius, Color mainColor) {
			this(centerX, centerY, radius, radius * 0.65, radius * 0.30, mainColor, Color.BLACK);
		}
		
		public FXJointViewA(double centerX, double centerY, double radiusOuter, double radiusInner, double radiusCenter, Color mainColor, Color centerColor) {
			this.centerX = centerX;
			this.centerY = centerY;
			this.outerRadius = radiusOuter;
			this.innerRadius = radiusInner;
			this.centerRadius = radiusCenter;
			this.mainColor = mainColor;
			this.centerColor = centerColor;

			bigCircle = new Ellipse();
			bigCircle.setCenterX(0);
			bigCircle.setCenterY(0);
			bigCircle.setRadiusX(radiusOuter);
			bigCircle.setRadiusY(radiusOuter);

			smallCircle = new Ellipse();
			smallCircle.setCenterX(0);
			smallCircle.setCenterY(0);
			smallCircle.setRadiusX(radiusInner);
			smallCircle.setRadiusY(radiusInner);

			centerCircle = new Ellipse();
			centerCircle.setCenterX(0);
			centerCircle.setCenterY(0);
			centerCircle.setRadiusX(radiusCenter);
			centerCircle.setRadiusY(radiusCenter);
			centerCircle.setStrokeWidth(radiusCenter / 5);
			centerCircle.setStroke(mainColor);
			centerCircle.setFill(Color.WHITE);

			shape = Path.subtract(bigCircle, smallCircle);

			shape.setFill(mainColor.deriveColor(1, 1, 1, 0.3));
			shape.setStroke(mainColor);
			shape.setStrokeWidth(1);

			merger.getChildren().addAll(centerCircle, shape);

			merger.setTranslateX(centerX);
			merger.setTranslateY(centerY);
			
			centerCircle.strokeProperty().bind(shape.strokeProperty());
		}

		@Override
		public Node getNode() {
			return merger;
		}


		@Override
		public Shape getShape() {
			return shape;
		}
		
		@Override
		public void setRadius(double radius) {
			setOuterRadius(radius);
			setInnerRadius(radius * 0.7);
			setCenterRadius(radius * 0.3);
		}

		@Override
		public void setOuterRadius(double outerRadius) {
			this.outerRadius = outerRadius;
			this.bigCircle.setRadiusX(outerRadius);
			this.bigCircle.setRadiusY(outerRadius);
		}

		@Override
		public void setInnerRadius(double innerRadius) {
			this.innerRadius = innerRadius;
			this.smallCircle.setRadiusX(innerRadius);
			this.smallCircle.setRadiusY(innerRadius);
		}

		@Override
		public void setCenterRadius(double centerRadius) {
			this.centerRadius = centerRadius;
			this.centerCircle.setRadiusX(centerRadius);
			this.centerCircle.setRadiusY(centerRadius);
		}

		@Override
		public void setCenterX(double centerX) {
			this.centerX = centerX;
			this.merger.setTranslateX(centerX);
		}

		@Override
		public void setCenterY(double centerY) {
			this.centerY = centerY;
			this.merger.setTranslateX(centerY);
		}

		@Override
		public void setMainColor(Color mainColor) {
			this.mainColor = mainColor;
			this.shape.setFill(mainColor.deriveColor(1, 1, 1, 1));
			this.shape.setStroke(mainColor);
		}

		@Override
		public void setCenterColor(Color centerColor) {
			this.centerColor = centerColor;
			this.centerCircle.setFill(centerColor);
		}

		@Override
		public void setSectionDistance(double sectionDistance) {}

		@Override
		public double getCenterRadius() { return 0;}

	}
	
	private static class FXJointViewB extends FXJointView{
		
		private Shape shape = null;
		
		private Circle outerCircle;
	    private Circle innerCircle;
	    
	    private Line topLeftBottomRightLine;
	    private Line bottomLeftTopRightLine;
	    
	    public FXJointViewB(double outerRadius){
	    	this(outerRadius, outerRadius * 0.5);
	    }
	    
	    public FXJointViewB(double outerRadius, Color mainColor){
	    	this(outerRadius, outerRadius * 0.5, mainColor);
	    }
	    
	    public FXJointViewB(double outerRadius, double innerRadius){
	    	this(outerRadius, innerRadius, Color.WHITE);
	    }
	    	    
	    public FXJointViewB(double outerRadius, double innerRadius, Color mainColor){
	    	this(0, 0, outerRadius, innerRadius, mainColor);
	    }
	    
	    public FXJointViewB(double centerX, double centerY, double outerRadius, Color mainColor){
	    	this(centerX, centerY, outerRadius, outerRadius * 0.5, mainColor);
	    }

	    public FXJointViewB(double centerX, double centerY, double outerRadius, double innerRadius, Color mainColor){
	    	this.centerX = centerX;
	    	this.centerY = centerY;
	    	this.outerRadius = outerRadius;
	    	this.innerRadius = innerRadius;
	    	this.sectionDistance = outerRadius/2;
	    	this.mainColor = mainColor;
	    	
			outerCircle = new Circle(outerRadius);
			innerCircle = new Circle(innerRadius);

			topLeftBottomRightLine = new Line(-outerRadius, -outerRadius, outerRadius, outerRadius);
			topLeftBottomRightLine.setStrokeWidth(sectionDistance);

			bottomLeftTopRightLine = new Line(-outerRadius, outerRadius, outerRadius, -outerRadius);
			bottomLeftTopRightLine.setStrokeWidth(sectionDistance);

			shape = Shape.subtract(outerCircle, innerCircle);

			shape = Shape.subtract(shape, topLeftBottomRightLine);
			shape = Shape.subtract(shape, bottomLeftTopRightLine);

			shape.setStrokeWidth(strokeWidth);
			shape.setStroke(mainColor);
			shape.setFill(strokeColor);

			shape.setTranslateX(centerX);
			shape.setTranslateY(centerY);
			
			innerCircle.strokeProperty().bind(shape.strokeProperty());
		}
	    
	    @Override
	    public Shape getNode(){
	    	return shape;
	    }

		@Override
		public Shape getShape() {
			return shape;
		}
		
	    @Override
	    public void setCenterX(double centerX) {
			this.centerX = centerX;
			this.shape.setTranslateX(centerX);
		}

	    @Override
	    public void setCenterY(double centerY) {
			this.centerY = centerY;
			this.shape.setTranslateY(centerY);
		}

	    @Override
	    public void setSectionDistance(double sectionDistance) {
			this.sectionDistance = sectionDistance;
			this.topLeftBottomRightLine.setStrokeWidth(sectionDistance);
			this.bottomLeftTopRightLine.setStrokeWidth(sectionDistance);
		}

	    @Override
	    public void setOuterRadius(double outerRadius) {
			this.outerRadius = outerRadius;
			this.outerCircle.setRadius(outerRadius);
		}

	    @Override
	    public void setInnerRadius(double innerRadius) {
			this.innerRadius = innerRadius;
			this.innerCircle.setRadius(innerRadius);
		}

		@Override
		public void setRadius(double radius) {
			this.setOuterRadius(radius);
			this.setInnerRadius(radius * 0.5);
		}

		@Override
		public void setCenterRadius(double centerRadius) {}

		@Override
		public void setMainColor(Color mainColor) {
			this.shape.setFill(mainColor.deriveColor(1, 1, 1, 0.45));
			this.shape.setStroke(mainColor);		
		}

		@Override
		public void setCenterColor(Color centerColor) {
		}

		@Override
		public double getCenterRadius() {
			return 0;
		}
	}
}
