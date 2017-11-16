package com.utils;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Duration;

public class FilledRegion extends Region {
	private Random rand = new Random();
	private ObjectProperty<Color> externalColor = new SimpleObjectProperty<>();
	private ObjectProperty<Color> internalColor = new SimpleObjectProperty<>();
	private Color oldExternalColor;
	private Color oldInternalColor;
	private Background bg;
	private Timeline timeline;
	private int duration;

	public FilledRegion() {
		oldExternalColor = getRandomColor(AnimatedGradients.baseExternalColor, AnimatedGradients.externalDelta);
		oldInternalColor = getRandomColor(AnimatedGradients.baseInternalColor, AnimatedGradients.internalDelta);
		externalColor.set(oldExternalColor);
		internalColor.set(oldInternalColor);
		setMinWidth(75);
		setBackground();
		internalColor.addListener((obs, oldColor, newColor) -> {
			setBackground();
		});
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
		duration = getRandomDuration();

		KeyFrame kf1 = new KeyFrame(Duration.ZERO,
				new KeyValue(externalColor, oldExternalColor),
				new KeyValue(internalColor, oldInternalColor));

		oldExternalColor = getRandomColor(AnimatedGradients.baseExternalColor, AnimatedGradients.externalDelta);
		oldInternalColor = getRandomColor(AnimatedGradients.baseInternalColor, AnimatedGradients.internalDelta);

		KeyFrame kf2 = new KeyFrame(new Duration(duration),
				new KeyValue(externalColor, oldExternalColor),
				new KeyValue(internalColor, oldInternalColor));
		timeline.getKeyFrames().addAll(kf1, kf2);
	}

	private void setBackground() {
		bg = new Background(
				new BackgroundFill(
				new LinearGradient(0, 0, 0, 1, true,
				CycleMethod.NO_CYCLE, new Stop[] {
				new Stop(0, externalColor.get()),
				new Stop(1, internalColor.get()),
				new Stop(1, externalColor.get()) }),
				new CornerRadii(0),
				new Insets(0, 0, 0, 0)));
		setBackground(bg);
	}

	private Color getRandomColor(Color color, double delta) {

		int index = (int) ((color.getGreen() + getRandomCoefficient(delta)) * 255);

		int newColor = (index > 225) ? 225 : ((index < 25) ? 25 : index);

		return Color.rgb(newColor, newColor, newColor);
	}

	private double getRandomCoefficient(double delta) {
		return (rand.nextDouble() * 2 - 1) * delta;
	}

	private int getRandomDuration() {
		return (int) ((rand.nextDouble() * 2 - 1) 
				* AnimatedGradients.durationDelta 
				* AnimatedGradients.baseDuration)
				+ AnimatedGradients.baseDuration;
	}
}