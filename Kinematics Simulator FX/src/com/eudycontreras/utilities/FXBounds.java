package com.eudycontreras.utilities;

import javafx.geometry.Bounds;

public class FXBounds{
	
	public static double getCenterX(Bounds bounds){
		return bounds.getMinX() + (bounds.getWidth()/2);
	}
	
	public static double getCenterY(Bounds bounds){
		return bounds.getMinY() + (bounds.getHeight()/2);
	}
	
}
