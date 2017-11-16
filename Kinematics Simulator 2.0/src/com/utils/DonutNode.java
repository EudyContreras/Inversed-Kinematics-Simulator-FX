package com.utils;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class DonutNode extends Region {

	private static final double RADIUS = 35.0f;
	private static final double CENTER = 40.0f;
	private final double START_ANGLE = 90.0f;
	private final int STROKE_WIDTH = 10;

	private Arc arc;
	private Label label;
	private Circle circle;

	public enum Motion {
		REVERSE, FORWARD
	}

	public DonutNode(Paint arc_stroke, Paint label_fill, Paint circle_fill) {
		init(arc_stroke, label_fill, circle_fill);
	}

	private void init(Paint arc_stroke, Paint label_fill, Paint circle_fill) {

		arc = new Arc();
		arc.setCenterX(CENTER);
		arc.setCenterY(CENTER);
		arc.setRadiusX(RADIUS);
		arc.setRadiusY(RADIUS);
		arc.setStartAngle(START_ANGLE);
		arc.setStroke(arc_stroke);
		arc.setFill(Color.TRANSPARENT);
		arc.setStrokeWidth(STROKE_WIDTH);
		arc.setType(ArcType.OPEN);

		label = new Label();
		label.setText("0");
		label.setFont(Font.font(16));
		label.setLayoutX(CENTER - 12);
		label.setLayoutY(CENTER - 16);
		label.setContentDisplay(ContentDisplay.CENTER);
		label.setTextFill(label_fill);
		label.setLabelFor(arc);

		circle = new Circle();
		circle.setRadius(30);
		circle.setFill(circle_fill);
		circle.setLayoutX(arc.getCenterX());
		circle.setLayoutY(arc.getCenterY());


		this.setStyle("-fx-background-color:black;");
		this.setPrefSize(80, 80);
		this.setMinSize(80, 80);
		this.setMaxSize(80, 80);
		this.getChildren().addAll(arc, circle, label);
	}

	public void setPercentage(double percent) {
		double angle = (18 * percent) / 5;
		arc.setLength(angle);
		label.setText(String.valueOf(percent));
	}

	private double getLength() {
		return arc.getLength();
	}

	public void playAnimation(Duration duration, Motion motion) {

		if (Motion.FORWARD == motion) {
			playForwardAnimation(duration);
		} else if (Motion.REVERSE == motion) {
			playReverseAnimation(duration);
		}

	}

	private void playForwardAnimation(Duration duration) {

		final Animation animation = new Transition() {
			double i = 0;
			double lenght = getLength();
			{
				setCycleDuration(duration);
			}

			@Override
			protected void interpolate(double frac) {

				if (i == lenght + 1) {
					stop();
				} else {
					arc.setLength(i);
					label.setText(String.valueOf((5 * i) / 18).split("\\.")[0]);
					i++;
				}
			}
		};
//		animation.setRate(0.5);
		animation.play();

	}

	private void playReverseAnimation(Duration duration) {

		final Animation animation = new Transition() {
			double i = getLength();
			{
				setCycleDuration(duration);
			}

			@Override
			protected void interpolate(double frac) {

				if (i < 0) {
					stop();
				} else {
					arc.setLength(i);
					label.setText(String.valueOf((5 * i) / 18).split("\\.")[0]);
					i--;
				}
			}
		};
//		animation.setRate(0.5);
		animation.play();
	}

}
