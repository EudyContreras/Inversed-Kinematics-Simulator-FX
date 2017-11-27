package com.utils;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AnimatedGradients extends Application {
	
protected static int baseDuration = 1000;

protected static double durationDelta = 0.0;
protected static double externalDelta = 0.1;
protected static double internalDelta = 1;

protected static Color baseExternalColor = Color.rgb(200, 0, 0);
protected static Color baseInternalColor = Color.rgb(200, 0, 0);
 
public static void main(String[] args) {
    launch(args);
}

@Override
public void start(Stage primaryStage) {
 
    FilledRegion gr = new FilledRegion();
    Scene scene = new Scene(gr, 761, 500, Color.BLACK);
    primaryStage.setScene(scene);
    gr.startAnimation();
    primaryStage.show();

}}
