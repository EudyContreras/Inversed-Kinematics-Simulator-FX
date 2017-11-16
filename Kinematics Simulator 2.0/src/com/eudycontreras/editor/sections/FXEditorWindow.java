package com.eudycontreras.editor.sections;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class FXEditorWindow extends Pane {

    private DoubleProperty zoomProperty = new SimpleDoubleProperty(1.0);
    
    private double pivotX = 0;
    private double pivotY = 0;
    
    public FXEditorWindow(Node... node) {
    	getChildren().addAll(node);
        scaleXProperty().bind(zoomProperty);
        scaleYProperty().bind(zoomProperty);
    }
   
    public FXEditorWindow(double width, double heigth, Node... node) {
    	getChildren().addAll(node);
        setPrefSize(width, heigth);
        scaleXProperty().bind(zoomProperty);
        scaleYProperty().bind(zoomProperty);
    }
    
	public DoubleProperty getScaleProperty() {
		return zoomProperty;
	}
   
    public double getScale() {
        return zoomProperty.get();
    }

    public void setScale( double scale) {
        zoomProperty.set(scale);
    }

    public void setPivot( double x, double y) {
    	pivotX = getTranslateX()-x;
    	pivotY = getTranslateY()-y;
    	
        setTranslateX(pivotX);
        setTranslateY(pivotY);
    }

	public double getPivotX() {
		return pivotX;
	}

	public double getPivotY() {
		return pivotY;
	}

    
}
