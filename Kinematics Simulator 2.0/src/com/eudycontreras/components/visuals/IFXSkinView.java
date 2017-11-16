package com.eudycontreras.components.visuals;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public interface IFXSkinView {
    
	public Shape getShape();

	public void setX(double x);
	
	public void setY(double y);

	public void setWidth(double width);

	public void setHeight(double height);

	public void setFill(Color fill);

	public void setStroke(Color stroke);
	
	public void setStrokeWidth(double width);

	public double getX();

	public double getY();

	public double getWidth();

	public double getHeight();

	public Color getFill();

	public Color getStroke();
	
  
}