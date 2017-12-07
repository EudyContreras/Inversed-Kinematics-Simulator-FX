package com.eudycontreras.editor.controls;


import com.eudycontreras.editor.application.Styles;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;




public class Demo extends Application {


    @Override public void start(Stage stage) {
        StackPane pane = new StackPane(new FXRangeSlider(300,12,0,180).get());

        Scene scene = new Scene(pane,800,800);

        scene.getStylesheets().addAll(Styles.STYLES);
		
        stage.setTitle("TESTING");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}