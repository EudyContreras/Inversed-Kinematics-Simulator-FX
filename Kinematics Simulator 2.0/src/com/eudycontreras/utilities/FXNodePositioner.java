package com.eudycontreras.utilities;

import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class FXNodePositioner {

	public final static void CENTER_SHAPE(Shape shape, Node node, Dimension2D dimension){
		CENTER_SHAPE(shape,Center.CENTER_X_Y, node, dimension);
	}

	public final static void CENTER_SHAPE(Shape shape, Center center, Node node, Dimension2D dimension){
		double width = dimension.getWidth();
		double height = dimension.getHeight();
		double shapeWidth = 0;
		double shapeHeight = 0;

		switch(shape){
		case CIRCLE:
			shapeWidth = ((Circle)node).getRadius();
			shapeHeight = ((Circle)node).getRadius();
			break;
		case RECTANGLE:
			shapeWidth = ((Rectangle)node).getWidth();
			shapeHeight = ((Rectangle)node).getHeight();
			break;
		}

		switch(center){
		case CENTER_X:
			node.setTranslateX((width/2)-(shapeWidth/2));
			break;
		case CENTER_Y:
			node.setTranslateY((height/2)-(shapeHeight/2));
			break;
		case CENTER_X_Y:
			node.setTranslateX((width/2)-(shapeWidth/2));
			node.setTranslateY((height/2)-(shapeHeight/2));
			break;
		}
	}

	public static enum Center{
		CENTER_X, CENTER_Y, CENTER_X_Y
	}
	public static enum Shape{
		RECTANGLE, CIRCLE
	}
}
