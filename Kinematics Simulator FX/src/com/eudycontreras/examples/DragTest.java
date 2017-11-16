package com.eudycontreras.examples;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * @web http://java-buddy.blogspot.com/
 */
public class DragTest extends Application {
    
    Circle circle_Red, circle_Green, circle_Blue;
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    
    @Override
    public void start(Stage primaryStage) {
        
        //Create Circles
    	
    	Circle holder = new Circle(30, Color.ORANGE);
        holder.setTranslateX(50);
        holder.setTranslateY(200);
        
        circle_Red = new Circle(50.0f, Color.RED);
        circle_Red.setCursor(Cursor.HAND);
        circle_Red.setTranslateX(100);
        circle_Red.setTranslateY(200);
        circle_Red.setOnMousePressed(circleOnMousePressedEventHandler);
        circle_Red.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        
        circle_Green = new Circle(50.0f, Color.GREEN);
        circle_Green.setCursor(Cursor.MOVE);
        circle_Green.setTranslateX(150);
        circle_Green.setTranslateY(200);
        circle_Green.setOnMousePressed(circleOnMousePressedEventHandler);
        circle_Green.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        
        circle_Blue = new Circle(50.0f, Color.BLUE);
        circle_Blue.setCursor(Cursor.CROSSHAIR);
        circle_Blue.setTranslateX(200);
        circle_Blue.setTranslateY(200);
        circle_Blue.setOnMousePressed(circleOnMousePressedEventHandler);
        circle_Blue.setOnMouseDragged(circleOnMouseDraggedEventHandler);
         
        holder.translateXProperty().bind(circle_Red.translateXProperty().subtract(50));
        circle_Red.translateXProperty().bind(circle_Green.translateXProperty().subtract(50));
        circle_Green.translateXProperty().bind(circle_Blue.translateXProperty().subtract(50));
        

        
        Group root = new Group();
        root.getChildren().addAll(holder,circle_Red, circle_Green, circle_Blue);
        
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 700,700));
        
        primaryStage.setTitle("java-buddy");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    EventHandler<MouseEvent> circleOnMousePressedEventHandler = 
        new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
            orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
            orgTranslateY = ((Circle)(t.getSource())).getTranslateY();
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
            
            ((Circle)(t.getSource())).setTranslateX(newTranslateX);
            ((Circle)(t.getSource())).setTranslateY(newTranslateY);
        }
    };
}