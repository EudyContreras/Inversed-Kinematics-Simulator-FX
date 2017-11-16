package com.eudycontreras.views;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class FXSeparator{

	private Rectangle separator = new Rectangle();
	
	private StackPane margin = new StackPane();
	
	public Parent Create(){
		return Create(0,10,new Insets(10,0,10,0),Color.GRAY);
	}
	
	public Parent Create(Color color){
		return Create(0,10,new Insets(10,0,10,0),color);
	}
	
	public Parent Create(double width, double height, Color color){
		return Create(width,height,Insets.EMPTY,color);
	}
	
	
	public Parent Create(double width, double height, Insets inset, Color color){
		separator.setWidth(width);
		separator.setHeight(height);
		separator.setFill(color);
		
		margin.getChildren().add(separator);
		margin.setPadding(inset);
		
		return margin;
	}
	
	
	public Parent get(){
		return margin;
	}
	
	public void setPadding(Insets insets){
		margin.setPadding(insets);
	}
	
	public void setWidth(double width){
		separator.setWidth(width);
	}
	
	public void setHeight(double heigth){
		separator.setHeight(heigth);
	}
	
	public void setFill(Paint paint){
		separator.setFill(paint);
	}
	
	public void setArcWidth(double value){
		separator.setArcWidth(value);
	}
	
	public void setArcHeight(double value){
		separator.setArcHeight(value);
	}
}
