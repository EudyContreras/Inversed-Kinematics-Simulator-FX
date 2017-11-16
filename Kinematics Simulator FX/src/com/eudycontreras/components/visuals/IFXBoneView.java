package com.eudycontreras.components.visuals;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public interface IFXBoneView {

	public Shape getShape();

	public void setX(double x);

	public void setY(double y);

	public void setWidth(double width);

	public void setHeight(double height);

	public void setStrokeWidth(double strokeWidth);

	public void setFill(Color fill);

	public void setStroke(Color stroke);

	public double getX();

	public double getY();

	public double getWidth();

	public double getHeight();

	public double getStrokeWidth();

	public Color getFill();

	public Color getStroke();
}
