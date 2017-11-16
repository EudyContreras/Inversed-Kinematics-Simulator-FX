package com.eudycontreras.examples;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

public class HalfRing extends Application {

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();
        root.getChildren().add(drawSemiRing(120, 120, 100, 50, Color.LIGHTGREEN, Color.DARKGREEN));
       // root.getChildren().add(drawSemiRing(350, 350, 200, 30, Color.LIGHTSKYBLUE, Color.DARKBLUE));

        Scene scene = new Scene(root, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Path drawSemiRing(double centerX, double centerY, double radius, double innerRadius, Color bgColor, Color strkColor) {
        Path path = new Path();
        path.setFill(bgColor);
        path.setStroke(strkColor);
        path.setStrokeWidth(8);
        path.setFillRule(FillRule.EVEN_ODD);

        MoveTo moveTo = new MoveTo();
        moveTo.setX(centerX + innerRadius);
        moveTo.setY(centerY);

        ArcTo arcToInner = new ArcTo();
        arcToInner.setX(centerX - innerRadius);
        arcToInner.setY(centerY);
        arcToInner.setRadiusX(innerRadius);
        arcToInner.setRadiusY(innerRadius);

        MoveTo moveTo2 = new MoveTo();
        moveTo2.setX(centerX + innerRadius);
        moveTo2.setY(centerY);

        HLineTo hLineToRightLeg = new HLineTo();
        hLineToRightLeg.setX(centerX + radius);

        ArcTo arcTo = new ArcTo();
        arcTo.setX(centerX - radius);
        arcTo.setY(centerY);
        arcTo.setRadiusX(radius);
        arcTo.setRadiusY(radius);

        path.getElements().add(moveTo);
        path.getElements().add(arcToInner);
        path.getElements().add(moveTo2);
        path.getElements().add(hLineToRightLeg);
        path.getElements().add(arcTo);
     

        return path;
    }

    public static void main(String[] args) {
        launch(args);
    }
}