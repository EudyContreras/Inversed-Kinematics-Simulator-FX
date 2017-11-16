package com.eudycontreras.examples;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class ShapeSubstract extends Application {
    
    public static void main(String[] args) {
        
        Application.launch(args);
    }
    
    public void start(Stage primaryStage) {
        
        primaryStage.setTitle("Shape");
        
        Group root = new Group();
        Scene scene = new Scene(root, 700, 200, Color.WHITE);
        
        Rectangle rec1 = new Rectangle(100,100);
        rec1.setFill(Color.LIGHTGREEN);
        rec1.setX(30);
        rec1.setY(30);
      
        Rectangle rec2 = new Rectangle(50,50);
        rec2.setFill(Color.TRANSPARENT);
        rec2.setX(55);
        rec2.setY(55);

        Shape shp1 = Path.subtract(rec1, rec2);
        shp1.setStrokeWidth(3);
        shp1.setStroke(Color.AQUA);
        shp1.setFill(Color.DARKGRAY);
      
        Shape shp2 = Path.intersect(rec1, rec2);
        shp2.setLayoutX(100);
        shp2.setStrokeWidth(3);
        shp2.setStroke(Color.AQUA);
        shp2.setFill(Color.DARKGRAY);

        root.getChildren().addAll(shp1,shp2);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}