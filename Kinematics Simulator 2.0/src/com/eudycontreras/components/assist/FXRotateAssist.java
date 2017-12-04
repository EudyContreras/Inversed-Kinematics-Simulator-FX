package com.eudycontreras.components.assist;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class FXRotateAssist {

	private Arc angleConstraintAssist = new Arc();
	
	private Circle padder = new Circle();
	
	private Group wrapper = new Group();

	public FXRotateAssist(double angleStart, double angleLength){
		createAssistant(angleStart, angleLength);
	}
	
	private void createAssistant(double angleStart, double angleLength){
		angleConstraintAssist.setFill(Color.rgb(245, 170, 0).deriveColor(1, 1, 1, 0.4));
		angleConstraintAssist.setStroke(Color.rgb(245, 170, 0));
		angleConstraintAssist.setStrokeWidth(1.5);
		angleConstraintAssist.setCenterX(0);
		angleConstraintAssist.setCenterY(0);
		angleConstraintAssist.setRadiusX(60);
		angleConstraintAssist.setRadiusY(60);
		angleConstraintAssist.setStartAngle(angleStart);
		angleConstraintAssist.setLength(angleLength);
		angleConstraintAssist.setType(ArcType.ROUND);
		
		padder.setRadius(60);
		padder.setRadius(60);
		padder.setFill(Color.TRANSPARENT);
		
		wrapper.getChildren().addAll(padder,angleConstraintAssist);
	}
	
	public Node getNode(){
		return wrapper;
	}
	
	public Shape getShape(){
		return angleConstraintAssist;
	}

	public void setRadius(double radius) {
		angleConstraintAssist.setRadiusX(radius);
		angleConstraintAssist.setRadiusY(radius);
		
		padder.setRadius(radius);
	}

	public void setFill(Color color) {
		angleConstraintAssist.setFill(color.deriveColor(1, 1, 1, 0.45));
		angleConstraintAssist.setStroke(color);
	}

	public void setStroke(Color color) {
		angleConstraintAssist.setStroke(color);
	}

	public void setX(double x) {
		wrapper.setTranslateX(x);
	}
	
	public void setY(double y) {
		wrapper.setTranslateY(y);
	}
	
	public void setPosition(double x, double y) {
		wrapper.setTranslateX(x);
		wrapper.setTranslateY(y);
	}

	public void updateAngle(double start, double angle) {
		angleConstraintAssist.setStartAngle(start);
		angleConstraintAssist.setLength(angle);
	}
}