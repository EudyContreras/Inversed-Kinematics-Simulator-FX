package com.utils;

import java.util.Random;

import com.eudycontreras.multithreading.managers.ThreadManager;
import com.eudycontreras.utilities.FXPaintUtility;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GradientAnimator {

	private int minValue;
	private int maxValue;
	private int[] offset;
	private int[] value;
	private volatile boolean running;
	private Rectangle rect;
	private Circle circle;
	private Random rand;


	public GradientAnimator() {
		this(null,null);
	}

	public GradientAnimator(Node node, Duration duration){
		this.rand = new Random();
		this.value = new int[3];
		this.offset = new int[3];
		this.setNode(node);
		this.resetAnim();
	}

	public void setNode(Node node){

		Color[] colors = new Color[value.length];

		for(int i = 0; i<colors.length; i++){
			colors[i] = getColor(value[i]);
		}

		if(node instanceof Circle){
			circle = ((Circle)(node));
			circle.setFill(FXPaintUtility.linearGradient(colors));
		}
		if(node instanceof Rectangle){
			rect = ((Rectangle)(node));
			rect.setFill(FXPaintUtility.linearGradient(colors));
		}
	}

	private void colorAnimation(){
		clampColors();

		Color[] colors = new Color[value.length];

		for(int i = 0; i<colors.length; i++){
			colors[i] = getColor(value[i]);
		}

		if(rect!=null){
			rect.setFill(FXPaintUtility.linearGradient(colors));
		}
		else{
			circle.setFill(FXPaintUtility.linearGradient(colors));
		}
	}

	private int getRandomRange(int minValue, int maxValue){
		return rand.nextInt(maxValue + 1 - minValue) + minValue;
	}

	private Color getColor(int color){
		return Color.rgb(color, color, color);
	}

	private void clampColors(){
		for(int i = 0; i<value.length; i++){
			value[i]+=offset[i];
			if(value[i]>maxValue){
				value[i] = maxValue;
				offset[i] = -1;
			}if(value[i]<minValue){
				value[i] = minValue;
				offset[i] = 1;
			}
		}
	}

	private void resetAnim(){
		this.minValue = 50;
		this.maxValue = 215;
		for(int i = 0; i<value.length; i++){
			value[i] = getRandomRange(minValue,maxValue);
			offset[i] = 1;
		}
	}

	public void play(){
		if (!running) {
			running = true;
			ThreadManager.performScript(new Runnable() {
				@Override
				public void run() {

					while (running) {
						Platform.runLater(() -> {
							colorAnimation();
						});
						try {
							Thread.sleep(80);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
	}

	public void stop(){
		running = false;
		resetAnim();
	}

	public enum AnimatedColor{
		SILVER, GOLD, RED, BLUE, CYAN, ORANGE, YELLOW
	}
}
