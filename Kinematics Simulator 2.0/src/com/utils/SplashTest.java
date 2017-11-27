package com.utils;

import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashTest extends Application{
 
	private int itemsToLoad = 20000;
	private TilePane root;
	private Stage mainWindow;
	private Scene scene;
	private Image splash_image = new Image("http://fxexperience.com/wp-content/uploads/2010/06/logo.png");

	public void start(Stage stage) throws Exception {
		new SplashScreen(stage,splash_image,()-> initializeGame(),()->showGame());
	}

	private void initializeGame() {
		Rectangle[] tiles = new Rectangle[itemsToLoad];
		root = new TilePane();
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = new Rectangle(20,20, randomColor(10,130,40,200,0,0));
			tiles[i].setStroke(randomColor(20,60,120,230,0,0));
			tiles[i].setStrokeWidth(2);
		}
		root.getChildren().addAll(tiles);
	}

	private void showGame() {
		scene = new Scene(root, 1280, 720, Color.BLACK);
		mainWindow = new Stage();
		mainWindow.setFullScreen(true);
		mainWindow.setScene(scene);
		mainWindow.initStyle(StageStyle.DECORATED);
		mainWindow.show();
	}

	private Color randomColor() {
		return Color.rgb(0, getRandomColor(70, 155), getRandomColor(200, 255));
	}

	private Color randomColor(int startR, int endR, int startG, int endG, int startB, int endB) {
		int rgb = getRandomColor(startR, endR);
		return Color.rgb(rgb,rgb,rgb);
	}
	
	public int getRandomColor(int minValue, int maxValue) {
		Random rand = new Random();
		int color = rand.nextInt(maxValue + 1 - minValue) + minValue;
		return color;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
