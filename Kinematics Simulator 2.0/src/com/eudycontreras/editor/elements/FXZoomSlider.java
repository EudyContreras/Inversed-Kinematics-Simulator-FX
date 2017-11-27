package com.eudycontreras.editor.elements;

import com.eudycontreras.editor.application.FXPaintResources;
import com.eudycontreras.utilities.FXKinematicsUtility;
import com.eudycontreras.utilities.FXPaintUtility;

import javafx.animation.FadeTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class FXZoomSlider extends Group {

	
	private static final int WIDTH = 22;
	private static final int HEIGHT = 150;
	
	private Circle thumb = new Circle();
	
	private Shape slider = new Rectangle();
	private Rectangle frame = new Rectangle();
	private Rectangle track = new Rectangle();
	private Rectangle fill = new Rectangle();

	private DoubleProperty zoomProperty = new SimpleDoubleProperty(1.0);
	
	private double minZoom = 0.1;
	private double maxZoom = 2.0;
	
	private double width = WIDTH;
	private double height = HEIGHT;
	
	private boolean bound = false;
	private boolean outside = false;
	private boolean scaling = false;

	public FXZoomSlider(double zoomLevel, double maxZoom, double minZoom){
		this(WIDTH,HEIGHT, zoomLevel, maxZoom, minZoom);
	}
    
    public FXZoomSlider(double width, double height, double zoomLevel, double maxZoom, double minZoom){
    	createSlider();
    	addEventHandling();
    	
    	slider = Path.subtract(frame, track);
		
    	slider.setFill(FXPaintUtility.color(30));
    	slider.setStroke(FXPaintUtility.color(15));
    	slider.setStrokeWidth(1.4);
    	
    	DropShadow shadow = new DropShadow();
    	
    	shadow.setBlurType(BlurType.THREE_PASS_BOX);
    	shadow.setColor(Color.rgb(20, 20, 20,0.5));
    	shadow.setSpread(0.2);
    	shadow.setRadius(10);
    	shadow.setOffsetX(-6);
    	shadow.setOffsetY(-6);
    	slider.setEffect(shadow);
    	
    	getChildren().add(slider);
    	getChildren().add(track);   	
    	getChildren().add(fill);
    	getChildren().add(thumb);
    	
    	setOpacity(0.7);
    	setRotate(180);
    
    	setOnMouseEntered(e -> {
    		fadeIn();
    		
    		outside = false;
    	});
    	
    	setOnMouseExited(e -> {
    		if(!bound){
    			fadeOut();
    		}
    		outside = true;
    	});
    }
    
	private void createFrame(){
    	frame.setWidth(width);
    	frame.setHeight(height);
    	frame.setArcWidth(10);
    	frame.setArcHeight(10);    	
    }
        
    private void createTrack(){
    	track.setWidth(width*0.4);
    	track.setHeight(height*0.7);
    	track.setX((frame.getWidth()/2) - (track.getWidth()/2));
    	track.setY((frame.getHeight()/2) - (track.getHeight()/2));
    	track.setArcWidth(12);
    	track.setArcHeight(12);
    	track.setFill(Color.rgb(60,60,60));
    	track.setStroke(Color.rgb(55, 55, 55));
    	track.setStrokeWidth(1);
    	track.setOpacity(0.2);
    }
    
    private void createFill(){
    	fill.setWidth(width*0.4);
    	fill.setHeight(height*0.7);
    	fill.setX((frame.getWidth()/2) - (track.getWidth()/2));
    	fill.setY(track.getY());
    	fill.setArcWidth(12);
    	fill.setArcHeight(12);
    	fill.setFill(FXPaintResources.SECONDARY_COLOR);   	
    }
    
    private void createThumb(){	
    	thumb.setCenterX(0);
    	thumb.setCenterY(0);
    	thumb.setRadius(width*0.4);
    	thumb.setCenterX((frame.getWidth()/2));
    	thumb.setCenterY(fill.getY() + (fill.getHeight() - thumb.getRadius()) / 2);
    	thumb.setFill(FXPaintUtility.color(35));
    	thumb.setStroke(FXPaintResources.SECONDARY_COLOR);
    	thumb.setStrokeWidth(2.5);
    }

    private void createSlider(){
    	createFrame();
    	createTrack();
    	createFill();
    	createThumb();
    }
    
    private void addEventHandling() {

    	fill.setHeight(thumb.getCenterY()-thumb.getRadius()*2);
    	
		thumb.centerYProperty().addListener((osv, oldVal, newVal) -> {

			double value = ((double) newVal) - thumb.getRadius() * 2;

			double offset = thumb.getRadius() / 2;
			
			double mappedValue = FXKinematicsUtility.mapValues(value, offset, track.getHeight() + offset, minZoom, maxZoom);

			if (mappedValue > maxZoom) {
				mappedValue = maxZoom;
			}
			if (mappedValue < minZoom) {
				mappedValue = minZoom;
			}

			if(!scaling){			
				zoomProperty.set(mappedValue);
			}

			fill.setHeight(value);
		});

		thumb.setOnMouseDragged(e -> {

			scaling = false;
			
			if (bound) {
				
				Point2D point = this.sceneToLocal(thumb.getCenterX(), e.getSceneY());
				
				if (withinBounds(point)) {
					thumb.setCenterY(point.getY());
				}
			}
		});

		thumb.setOnMousePressed(e -> {
			bound = true;
		});

		thumb.setOnMouseReleased(e -> {
			bound = false;

			if (outside) {
				fadeOut();
			}
		});
	}

    private boolean withinBounds(Point2D point){
    	
    	if (withinUpperBounds(point.getY()) && withinLowerBounds(point.getY())){
    		return true;
		}
    	
    	return false;
    }
    
    private boolean withinUpperBounds(double y){
    	
    	if (y > track.getBoundsInParent().getMinY()) {
    		return true;
		}
    	
    	return false;
    }
    
    private boolean withinLowerBounds(double y){
    	
    	if (y < track.getBoundsInParent().getMaxY()) {
    		return true;
		}
    	
    	return false;
    }
	
	public DoubleProperty getZoomProperty(){
		return zoomProperty;
	}
	
	public double getZoomLevel(){
		
		double value =  thumb.getCenterY()-thumb.getRadius()*2;
		
		double offset = 0;
		
		double mappedValue = FXKinematicsUtility.mapValues(value, offset, track.getHeight() + offset, minZoom, maxZoom);

		if(mappedValue > maxZoom){
			mappedValue = maxZoom;
		}
		if(mappedValue < minZoom){
			mappedValue = minZoom;
		}
		
		return mappedValue;
	}

	public void setZoom(double scale) {
		scaling = true;
		
		double lowerBound = track.getBoundsInParent().getMinY();
		
		double upperBound = track.getBoundsInParent().getMaxY();
		
		double mappedValue = FXKinematicsUtility.mapValues(scale, minZoom, maxZoom, lowerBound, upperBound);
	
		if(!withinUpperBounds(mappedValue)){
			mappedValue = track.getBoundsInParent().getMinY();
		}
		if(!withinLowerBounds(mappedValue)){
			mappedValue = track.getBoundsInParent().getMaxY();
		}
		
		thumb.setCenterY(mappedValue);
	}
	
	private void fadeIn() {

		FadeTransition fade = new FadeTransition();

		fade.setNode(this);
		fade.setDuration(Duration.millis(250));
		fade.setFromValue(this.getOpacity());
		fade.setToValue(1);

		fade.play();
	}

	private void fadeOut() {

		FadeTransition fade = new FadeTransition();

		fade.setNode(this);
		fade.setDuration(Duration.millis(250));
		fade.setFromValue(this.getOpacity());
		fade.setToValue(0.7);

		fade.play();
	}
}
