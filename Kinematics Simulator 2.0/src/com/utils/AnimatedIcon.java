package com.utils;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AnimatedIcon extends WritableImage {
private ObjectProperty<Color> topColor = new SimpleObjectProperty<>();
private ObjectProperty<Color> bottomColor = new SimpleObjectProperty<>();
private Color oldTopColor;
private Color oldBottomColor;
private Timeline timeline;
private static final int DURATION = 5000;
private static final Random rand = new Random();
private static final int ICON_WIDTH = 32;
private static final int ICON_HEIGHT = 32;

private Stage primaryStage;

public AnimatedIcon(Stage primaryStage) {
    super(ICON_WIDTH, ICON_HEIGHT);
    this.primaryStage = primaryStage;
    oldTopColor = Color.rgb(0, 45, 0);
    oldBottomColor = Color.rgb(12, 128, 12);
    topColor.set(oldTopColor);
    bottomColor.set(oldBottomColor);
    createGraphics();
    bottomColor.addListener((obs, oldColor, newColor) -> {
        createGraphics();
    });
}

private void createGraphics() {
    PixelWriter pixelWriter = this.getPixelWriter();
    for (int y = 0; y < ICON_HEIGHT; y++) {
        for (int x = 0; x < ICON_WIDTH; x++) {
            double position = (double)x/(double)ICON_WIDTH;
            int r = (int)( ( topColor.get().getRed() - (topColor.get().getRed() - bottomColor.get().getRed())*position ) * 255 );
            int g = (int)( ( topColor.get().getGreen() - (topColor.get().getGreen() - bottomColor.get().getGreen())*position ) * 255 );
            int b = (int)( ( topColor.get().getBlue() - (topColor.get().getBlue() - bottomColor.get().getBlue())*position ) * 255 );
            double o = topColor.get().getOpacity() - (topColor.get().getOpacity() - bottomColor.get().getOpacity())*position;
            pixelWriter.setColor(x,y,Color.rgb(r,g,b,o));
        }
    }
    int index = primaryStage.getIcons().indexOf(this);
    if (index == 0) {
        primaryStage.getIcons().set(index, this);
    } else {
        primaryStage.getIcons().add(this);
    }
}

public void startAnimation() {
    timeline = new Timeline();
    createTimelineContent();
    timeline.setOnFinished(ActionEvent -> {
        createTimelineContent();
        timeline.play();
    });
    timeline.play();
}

private void createTimelineContent() {
    timeline.getKeyFrames().clear();
    KeyFrame kf1 =
        new KeyFrame(Duration.ZERO,
            new KeyValue( topColor, oldTopColor ),
            new KeyValue( bottomColor, oldBottomColor ));
    oldTopColor = Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), rand.nextDouble());
    oldBottomColor = Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 1);
    KeyFrame kf2 =
        new KeyFrame(new Duration(DURATION),
            new KeyValue( topColor, oldTopColor ),
            new KeyValue( bottomColor, oldBottomColor ));
    timeline.getKeyFrames().addAll( kf1, kf2 );
}}