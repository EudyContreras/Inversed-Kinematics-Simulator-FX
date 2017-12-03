package com.eudycontreras.components.assist;

import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Shape;

public class FXRotateAssist {

	private Arc angleConstraintAssist = new Arc();

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
	}
	
	public Shape getShape(){
		return angleConstraintAssist;
	}

	public void setRadius(double radius) {
		angleConstraintAssist.setRadiusX(radius);
		angleConstraintAssist.setRadiusY(radius);
	}

	public void setFill(Color color) {
		angleConstraintAssist.setFill(color.deriveColor(1, 1, 1, 0.45));
		angleConstraintAssist.setStroke(color);
	}

	public void setStroke(Color color) {
		angleConstraintAssist.setStroke(color);
	}

	public void setX(double x, double y) {
		angleConstraintAssist.setCenterX(x);
	}
	
	public void setY(double x, double y) {
		angleConstraintAssist.setCenterY(y);
	}
	
	public void setPosition(double x, double y) {
		angleConstraintAssist.setCenterX(x);
		angleConstraintAssist.setCenterY(y);
	}

	public void updateAngle(double start, double angle) {
		angleConstraintAssist.setStartAngle(start);
		angleConstraintAssist.setLength(angle);
	}
}