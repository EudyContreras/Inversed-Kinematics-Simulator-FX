package com.eudycontreras.editor.areas.bezier;

import java.io.IOException;

import javax.tools.Tool;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SplineEditorDemo extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override public void start(Stage primaryStage) throws IOException {
        SplineEditor splineEditor = new SplineEditor();
        
        Scene scene = new Scene(splineEditor, 800, 600);
        scene.getStylesheets().add(SplineEditorDemo.class.getResource("Tools.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}