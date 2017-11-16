package com.eudycontreras.Editor;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FXEditorGrid{

	private Canvas grid = new Canvas();

	public FXEditorGrid(double width, double height, double lineWidth, Color background, Color lines){
  
		grid.setWidth(width);
		grid.setHeight(height);
        grid.setMouseTransparent(true);

        GraphicsContext gc = grid.getGraphicsContext2D();

        gc.setFill(background);
        gc.setStroke(lines);
        gc.fillRect(0, 0, width, height);
        gc.setLineWidth(lineWidth);

        double offset = 30;
        
        for( double i=offset; i < width; i+=offset) {
            gc.strokeLine( i, 0, i, height);
            gc.strokeLine( 0, i, width, i);
        }
	}
	
	public Node get(){
		return grid;
	}
}
