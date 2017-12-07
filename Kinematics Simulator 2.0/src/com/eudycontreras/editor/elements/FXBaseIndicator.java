package com.eudycontreras.editor.elements;

import com.eudycontreras.editor.application.FXPaintResources;
import com.eudycontreras.models.Size;
import com.eudycontreras.views.FXTriangle;
import com.eudycontreras.views.FXTriangle.Direction;

import javafx.animation.FadeTransition;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class FXBaseIndicator extends Group {

	private int colorValue = 0;
	
	private Color lineColor = Color.rgb(colorValue,colorValue,colorValue);	
	
	private Color light = Color.rgb(120, 120, 120);
	private Color dark = Color.rgb(30, 30, 30);
	
	private FXTriangle[] triangle = new FXTriangle[4];
	
	private Line horizontalLine;
	private Line verticalLine;

	private Rectangle borderLines;
	
	private Circle dragger;
	
	private Group markers;
	
	private Size size;
	
	private boolean allowDrag;

	private double lineWidth;

	private double radius;
	
	private double centerX;
	private double centerY;
	
	private double markerSize = radius*0.65;
	
	private double orgSceneX, orgSceneY;
	private double orgTranslateX, orgTranslateY;
	

	public FXBaseIndicator(Size size, Point2D center, double lineWidth) {
		this.size = size;
		this.lineWidth = lineWidth;

		this.horizontalLine = new Line();
		this.verticalLine = new Line();
		this.borderLines = new Rectangle();

		this.borderLines.setWidth(size.width);
		this.borderLines.setHeight(size.height);
		this.borderLines.setFill(Color.TRANSPARENT);
		this.borderLines.setStroke(FXPaintResources.SECONDARY_COLOR_DARK);
		this.borderLines.setStrokeWidth(this.lineWidth*3);

		this.horizontalLine.setStartX(borderLines.getTranslateX());
		this.horizontalLine.setStartY(borderLines.getTranslateY() + borderLines.getHeight()*0.55);

		this.horizontalLine.setEndX(borderLines.getWidth());
		this.horizontalLine.setEndY(borderLines.getTranslateY() + borderLines.getHeight()*0.55);

		this.horizontalLine.setStroke(lineColor.deriveColor(1, 1, 1, 0.55));
		this.horizontalLine.setStrokeWidth(this.lineWidth);

		this.verticalLine.setStartX(borderLines.getTranslateX() + borderLines.getWidth()/2);
		this.verticalLine.setStartY(borderLines.getTranslateY());

		this.verticalLine.setEndX(borderLines.getTranslateX() + borderLines.getWidth()/2);
		this.verticalLine.setEndY(borderLines.getHeight());

		this.verticalLine.setStroke(lineColor.deriveColor(1, 1, 1, 0.55));
		this.verticalLine.setStrokeWidth(lineWidth);
		
		this.createDragger(20);

		this.getChildren().add(verticalLine);
		this.getChildren().add(horizontalLine);
		this.getChildren().add(borderLines);
		this.getChildren().addAll(markers);
		this.getChildren().add(dragger);

		this.addEventHandling();
		this.setAllowDrag(false);
		this.setPickOnBounds(false);
	}
	
	public boolean isAllowDrag() {
		return allowDrag;
	}

	public void setAllowDrag(boolean allowDrag) {
		this.allowDrag = allowDrag;
	}

	public void  createDragger(double radius){
		this.centerX = borderLines.getTranslateX() + borderLines.getWidth()/2;
		this.centerY = borderLines.getTranslateY() + borderLines.getHeight()*0.55;
		this.radius = radius;
		
		this.dragger = new Circle(centerX, centerY, radius*0.4);
		this.dragger.setFill(light);
		this.dragger.setStroke(dark);
		this.dragger.setStrokeWidth(2);
		
		this.markerSize = radius*0.65;
		
		this.triangle[0] = new FXTriangle(markerSize,markerSize,FXPaintResources.ACCENT_COLOR,dark,2,Direction.UP);
		this.triangle[1] = new FXTriangle(markerSize,markerSize,FXPaintResources.ACCENT_COLOR,dark,2,Direction.DOWN);
		this.triangle[2] = new FXTriangle(markerSize,markerSize,FXPaintResources.ACCENT_COLOR,dark,2,Direction.LEFT);
		this.triangle[3] = new FXTriangle(markerSize,markerSize,FXPaintResources.ACCENT_COLOR,dark,2,Direction.RIGHT);
		
		this.triangle[0].setTranslateX(centerX - markerSize/2);
		this.triangle[0].setTranslateY(centerY - (radius + markerSize/2));

		this.triangle[1].setTranslateX(centerX - markerSize/2);
		this.triangle[1].setTranslateY(centerY + (radius - markerSize/2));
		
		this.triangle[2].setTranslateX(centerX - (radius + markerSize/2));
		this.triangle[2].setTranslateY(centerY - markerSize/2 );
	
		this.triangle[3].setTranslateX(centerX + (radius - markerSize/2));
		this.triangle[3].setTranslateY(centerY - markerSize/2 );
		
		this.markers = new Group(triangle);
	}
	
	private void addEventHandling() {

		fadeOut();
		
		dragger.setOnMouseEntered(e -> {
			dragger.getScene().setCursor(Cursor.CROSSHAIR);
			fadeIn();
		});
		
		dragger.setOnMouseExited(e -> {
			dragger.getScene().setCursor(Cursor.DEFAULT);
			fadeOut();
		});
		
		dragger.setOnMousePressed(e -> {
			
			if(!allowDrag) return;
			
			Point2D point = this.sceneToLocal(e.getSceneX(), e.getSceneY());

			orgSceneX = point.getX();
			orgSceneY = point.getY();
			
			orgTranslateX = dragger.getCenterX();
			orgTranslateY = dragger.getCenterY();

			e.consume();
		});
		
		dragger.setOnMouseReleased(e -> {
			e.consume();
		});
		
		dragger.setOnMouseDragged(e -> {
			
			if(!allowDrag) return;
			
			dragger.getScene().setCursor(Cursor.MOVE);
			
			Point2D point = this.sceneToLocal(e.getSceneX(), e.getSceneY());

			double offsetX = point.getX() - orgSceneX;
			double offsetY = point.getY() - orgSceneY;

			double newTranslateX = orgTranslateX + offsetX;
			double newTranslateY = orgTranslateY + offsetY;
			
			moveCenterPoint(newTranslateX, newTranslateY);
			
			e.consume();
		});
		
		dragger.setOnMouseDragReleased(e -> {
			
			if(!allowDrag) return;
			
			dragger.getScene().setCursor(Cursor.CROSSHAIR);
			
			e.consume();
		});	
	}

	public void moveCenterPoint(double x, double y) {

		x = clampX(x);
		y = clampY(y);
		
		centerX = x;
		centerY = y;

		dragger.setCenterX(centerX);
		dragger.setCenterY(centerY);

		triangle[0].setTranslateX(centerX - markerSize / 2);
		triangle[0].setTranslateY(centerY - (radius + markerSize / 2));

		triangle[1].setTranslateX(centerX - markerSize / 2);
		triangle[1].setTranslateY(centerY + (radius - markerSize / 2));

		triangle[2].setTranslateX(centerX - (radius + markerSize / 2));
		triangle[2].setTranslateY(centerY - markerSize / 2);

		triangle[3].setTranslateX(centerX + (radius - markerSize / 2));
		triangle[3].setTranslateY(centerY - markerSize / 2);

		horizontalLine.setStartY(y);

		horizontalLine.setEndY(y);

		verticalLine.setStartX(x);

		verticalLine.setEndX(x);
	}

	private double clampX(double x) {
		if (x < -size.getWidth() / 4) {
			x = (-size.getWidth() / 4) + 1;
		}
		if (x > size.getWidth() - size.getWidth() / 4) {
			x = (size.getWidth() - size.getWidth() / 4) - 1;
		}

		return x;
	}

	private double clampY(double y) {
		if (y < -size.getHeight() / 4) {
			y = (-size.getHeight() / 4) + 1;
		}
		if (y > size.getHeight() - size.getHeight() / 4) {
			y = (size.getHeight() - size.getHeight() / 4) - 1;
		}

		return y;
	}

	private void fadeIn(){
		
		FadeTransition fade = new FadeTransition();
		
		fade.setNode(markers);
		fade.setDuration(Duration.millis(250));
		fade.setFromValue(markers.getOpacity());
		fade.setToValue(1);
		
		fade.play();
	}
	
	private void fadeOut(){
	
		FadeTransition fade = new FadeTransition();
		
		fade.setNode(markers);
		fade.setDuration(Duration.millis(250));
		fade.setFromValue(markers.getOpacity());
		fade.setToValue(0);
		
		fade.play();
	}
	
}
