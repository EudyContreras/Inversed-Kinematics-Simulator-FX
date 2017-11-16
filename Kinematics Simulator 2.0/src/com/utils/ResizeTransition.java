package com.utils;

import javafx.animation.Transition;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class ResizeTransition extends Transition {

	public static final int RESIZE_WIDTH = 0;
	public static final int RESIZE_HEIGHT = 1;
	public static final int RESIZE_WIDTH_HEIGHT = 2;

	protected Region region;
	protected double fromHeight;
	protected double fromWidth;
	protected double toHeight;
	protected double toWidth;
	protected double heightDiff;
	protected double widthDiff;
	protected int resize;


	public ResizeTransition(int resize) {
		super();
		this.resize = resize;
	}

	public void setDuration(Duration duration) {
		setCycleDuration(duration);
	}

	private void computeDifference() {
		this.heightDiff = toHeight - fromHeight;
		this.widthDiff = toWidth - fromWidth;
	}

	public void fromWidth(double width) {
		this.fromWidth = width;
	}

	public void fromHeight(double height) {
		this.fromHeight = height;
	}

	public void toWidth(double width) {
		this.toWidth = width;
	}

	public void toHeight(double height) {
		this.toHeight = height;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	private void resizeRegion(double fraction){
		switch(resize){
		case RESIZE_HEIGHT:
			region.setPrefHeight(fromHeight + (heightDiff * fraction));
			region.setMinHeight(fromHeight + (heightDiff * fraction));
			region.setMaxHeight(fromHeight + (heightDiff * fraction));
			break;
		case RESIZE_WIDTH:
			region.setPrefWidth(fromWidth + (widthDiff * fraction));
			region.setMinWidth(fromWidth + (widthDiff * fraction));
			region.setMaxWidth(fromWidth + (widthDiff * fraction));
			break;
		case RESIZE_WIDTH_HEIGHT:
			region.setPrefWidth(fromWidth + (widthDiff * fraction));
			region.setMinWidth(fromWidth + (widthDiff * fraction));
			region.setMaxWidth(fromWidth + (widthDiff * fraction));
			region.setPrefHeight(fromHeight + (heightDiff * fraction));
			region.setMinHeight(fromHeight + (heightDiff * fraction));
			region.setMaxHeight(fromHeight + (heightDiff * fraction));
			break;
		}
	}

	@Override
	protected void interpolate(double fraction) {
		resizeRegion(fraction);
	}

	@Override
	public void play(){
		computeDifference();
		super.play();
	}
}