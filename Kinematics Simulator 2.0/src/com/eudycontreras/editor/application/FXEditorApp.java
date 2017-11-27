package com.eudycontreras.editor.application;
	
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

 
public class FXEditorApp extends Application {

    @Override

    public void start(Stage stage) {
    	new FXEditor(stage, FXEditorSettings.EDITOR_WIDTH, FXEditorSettings.EDITOR_HEIGHT, Color.BLACK);
    }

    public static void main(String[] args) {
        launch(args);
    }


}
