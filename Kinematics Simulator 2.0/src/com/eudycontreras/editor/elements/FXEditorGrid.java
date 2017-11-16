package com.eudycontreras.editor.elements;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FXEditorGrid{

	private Canvas grid = new Canvas();
	
	private Color light = Color.rgb(90, 90, 90);
	private Color dark = Color.rgb(60, 60, 60);
	
	private double blockSize;
	
	private double width;
	private double height;

	public FXEditorGrid(double inputWidth, double inputHeight, double blockSize, Color dark, Color light){
		this.blockSize = blockSize;
		this.width = inputWidth;
		this.height = inputHeight;
		this.dark = dark;
		this.light = light;
		
		this.grid.setWidth(width);
		this.grid.setHeight(height);
		
		this.grid.setMouseTransparent(true);
    
        drawGrid(grid);
	}
	
	public void drawGrid(Canvas grid){

		GraphicsContext gc = grid.getGraphicsContext2D();
		
		gc.clearRect(0, 0, width, height);
		
        gc.setFill(light);  
        
        gc.fillRect(0, 0, width, height);

        gc.setFill(dark);

        for(double i = 0; i <= width; i+=blockSize*2){
            for(double j = 0; j <= height; j+=blockSize*2){
                gc.fillRect(i, j, blockSize, blockSize);
            }
        }

        for(double i = blockSize; i <= width; i+=blockSize*2){
            for(double j = blockSize; j <= height; j+=blockSize*2){
                gc.fillRect(i, j, blockSize, blockSize);
            }
        }
		
	}
	
	public Node get(){
		return grid;
	}
}
