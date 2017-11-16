package com.eudycontreras.components.visuals;

import javafx.scene.Node;
import javafx.scene.paint.Color;

public interface IFXFleshView {
    
	public Node getShape();

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