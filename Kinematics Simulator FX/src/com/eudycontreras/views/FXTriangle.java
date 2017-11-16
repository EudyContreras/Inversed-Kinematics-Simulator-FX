package com.eudycontreras.views;

import com.eudycontreras.enums.Direction;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineJoin;

public class FXTriangle extends Group {
    
    private MoveTo open = new MoveTo();
    private LineTo lineTo1 = new LineTo();
    private LineTo lineTo2 = new LineTo();
    private ClosePath close = new ClosePath();
    private Path path = new Path();


    public FXTriangle(){
    	
    	getChildren().add(path);
    }
    
    public FXTriangle(double width, double height, Color fill, Color stroke, double strokeWidth, Direction direction){
    	

        getChildren().addAll(Create(width, height, fill, stroke, strokeWidth, direction));
    }
    
    
    public Node Create(double width, double height, Color fill, Color stroke, double strokeWidth, Direction direction) {
      	switch(direction){
    		case DOWN:
    		    open.setX(0);             
    			open.setY(0);
    	        
    	        lineTo1.setX(width / 2);
    	        lineTo1.setY(height);

    	        lineTo2.setX(width);
    	        lineTo2.setY(0);
    			break;
    		case LEFT:
    		    open.setX(width);             
    			open.setY(0);

    			lineTo1.setX(0);
    			lineTo1.setY(height/2);

    			lineTo2.setX(width);
    			lineTo2.setY(height);
    			break;
    		case RIGHT:
    			open.setX(0);     
    			open.setY(0);
    	        
    	        lineTo1.setX(width);
    	        lineTo1.setY(height / 2);

    	        lineTo2.setX(0);
    	        lineTo2.setY(height);
    			break;
    		case UP:
    			open.setX(0);
    			open.setY(height);

    			lineTo1.setX(width / 2);
    			lineTo1.setY(0);

    			lineTo2.setX(width);
    			lineTo2.setY(height);
    			break;
    		default:
    			break;	 
        	}     	   	   
      	
      	path.getElements().addAll(open, lineTo1, lineTo2, close);
        path.setStrokeWidth(strokeWidth);
        path.setFill(fill);
        path.setStroke(stroke);
        path.setStrokeLineJoin(StrokeLineJoin.ROUND);
        
    	
      	return path;
	}
}