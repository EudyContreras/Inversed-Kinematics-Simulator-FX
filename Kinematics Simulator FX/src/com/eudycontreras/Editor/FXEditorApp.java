package com.eudycontreras.Editor;
	
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FXEditorApp extends Application {

    @Override

    public void start(Stage stage) {
    	
    	new FXEditor(stage,1700, 1000, Color.BLACK);
    }

    public static void main(String[] args) {
        launch(args);
    }


}
