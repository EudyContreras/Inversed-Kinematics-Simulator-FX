package com.utils;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.PathTransitionBuilder;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathBuilder;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ArcToDemo extends Application {

	public static final double speed = 4;
	public static final double rotationRadius = 10;

    @Override
    public void start(Stage primaryStage) throws Exception {

        StackPane root = new StackPane();
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(root,500,500));

        ObservableList<Circle> list = FXCollections.observableArrayList();

        for(int i = 0; i<5; i++){
        	  Circle circle = new Circle(10);
              circle.setCenterX(i+12);
              circle.setTranslateY(i*12);
              circle.setFill(Color.GREEN);
              root.getChildren().add(circle);
        }

        final long startNanoTime = System.nanoTime();

		primaryStage.show();
		new AnimationTimer() {

			public void handle(long currentNanoTime) {

				double time = (currentNanoTime - startNanoTime) / 1000000000.0;

				double multiplier = 1.0;
				
				time = time * speed;

				for(Node node: root.getChildren()){
					
					Circle circle = (Circle)node;
					
					double x = (circle.getRadius() + circle.getCenterX()) * Math.cos(time * multiplier) * (rotationRadius);
					double y = (circle.getRadius() + circle.getCenterY()) * Math.sin(time * multiplier) * (rotationRadius);

					circle.setTranslateX(x);
					circle.setTranslateY(y);
					
				}

			}
		}.start();

    }

    public static void main(String[] args) {
        launch(args);
    }
}