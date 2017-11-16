package com.eudycontreras.components.assist;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.paint.Color;

public class FXRotateAssist extends Application {

    public static void main(String[] args) {

        Application.launch(args);
    }

    public void start(Stage primaryStage) {

        primaryStage.setTitle("Arc");
        
        Group root = new Group();
        Scene scene = new Scene(root, 700, 200, Color.BLACK);
 
        Arc arc = new Arc();
        arc.setFill(Color.rgb(240, 190, 0).deriveColor(1, 1, 1, 0.5));
        arc.setStroke(Color.rgb(255, 150, 0));
        arc.setStrokeWidth(4);
        arc.setCenterX(100);
        arc.setCenterY(100);
        arc.setRadiusX(60);
        arc.setRadiusY(60);
        arc.setStartAngle(0);
        arc.setLength(90);
        arc.setType(ArcType.ROUND);
         
        root.getChildren().add(arc);
        primaryStage.setScene(scene);
        primaryStage.show();       
    }
}