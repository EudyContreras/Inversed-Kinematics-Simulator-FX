package com.eudycontreras.examples;

import java.util.LinkedList;
import java.util.List;

import com.eudycontreras.components.models.Bone;
import com.eudycontreras.components.models.Bone.PointType;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * @web http://java-buddy.blogspot.com/
 */
public class LineTest extends Application {
    

    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    
    private LinkedList<Line> lines = new LinkedList<>();
    
    @Override
    public void start(Stage primaryStage) {
        
        //Create Circles
    	
    	
        
        Pane root = new Pane();
        
        root.setOnMouseClicked(e -> {
        	if(e.isControlDown()){
        		if(lines.isEmpty()){
        			Line line = new Line(20,20,e.getSceneX(), e.getSceneY());
        			line.setStroke(Color.RED);
        			line.setStrokeWidth(10);
        			lines.add(line);
        			
        			Circle circle = new Circle(20, Color.BLUE);
        			circle.setCenterX(e.getSceneX());
        			circle.setCenterY(e.getSceneY());
        			root.getChildren().addAll(line, circle);
        		}else{
        			Line line = new Line(lines.getLast().getEndX(),lines.getLast().getEndY(),e.getSceneX(), e.getSceneY());
        			line.setStroke(Color.RED);
        			line.setStrokeWidth(10);
        			lines.add(line);      			
          			
        			Circle circle = new Circle(20, Color.BLUE);
        			circle.setCenterX(e.getSceneX());
        			circle.setCenterY(e.getSceneY());
        			root.getChildren().add(0,line);
        			root.getChildren().add(circle);
        			
        			circle.setOnMousePressed(circleOnMousePressedEventHandler);
        			circle.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        			
        			line.endXProperty().bindBidirectional(circle.centerXProperty());
        			line.endYProperty().bindBidirectional(circle.centerYProperty());
        	        
        		}
        	}
        });

        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 700,700));
        
        primaryStage.setTitle("java-buddy");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    

	
	private  static double computeAngle(final Point2D pivotPoints, final Point2D scenePoints, final Point2D mousePoints) {
		
		final double angle1 = Math.atan2(pivotPoints.getY() - scenePoints.getY(), pivotPoints.getX() - scenePoints.getX());
		final double angle2 = Math.atan2(pivotPoints.getY() - mousePoints.getY(), pivotPoints.getX() - mousePoints.getX());

		return (angle1 - angle2) / Math.PI * 180;
	}
	
    
    EventHandler<MouseEvent> circleOnMousePressedEventHandler = 
        new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
            orgTranslateX = ((Circle)(t.getSource())).getCenterX();
            orgTranslateY = ((Circle)(t.getSource())).getCenterY();
        }
    };
    
    EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = 
        new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;
            
            ((Circle)(t.getSource())).setCenterX(newTranslateX);
            ((Circle)(t.getSource())).setCenterY(newTranslateY);
        }
    };
}