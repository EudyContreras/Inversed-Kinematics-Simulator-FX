package com.utils;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

public class SkySimulator extends Application {

    private final static double TOTAL_HEIGHT = 4000 ;
 

    @Override
    public void start(Stage stage) {
        VBox vbox = new VBox();

        Scene scene = new Scene(vbox, 1900, 1020);
        stage.setScene(scene);

        Canvas canvas = new Canvas(1900, 980);
        ScrollBar scrollBar = new ScrollBar();
        scrollBar.setMin(0);
        scrollBar.setMax(TOTAL_HEIGHT - canvas.getWidth());
        

        scrollBar.setVisibleAmount(scrollBar.getMax() * canvas.getWidth() / TOTAL_HEIGHT);

        vbox.getChildren().addAll(canvas, scrollBar);
        draw(canvas, scrollBar.getValue());

        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                draw(canvas, scrollBar.getValue());
            }
        });


        stage.show();
    }

    private void draw(Canvas canvas, double scrollAmount)
    {
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Stop[] stops = new Stop[] { new Stop(0, Color.rgb(0, 0,10)),new Stop(0.5, Color.rgb(0, 0, 80)), new Stop(1, Color.SKYBLUE)};
        LinearGradient lg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        graphics.setFill(lg);
        graphics.fillRect(0, -100-scrollAmount, 1920, TOTAL_HEIGHT);
    }

    public static void main(String[] args) {
        launch(args);
    }
}