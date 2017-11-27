package com.eudycontreras.javafx.fbk.samples;

import java.util.ArrayList;

import com.eudycontreras.javafx.fbk.models.FBKSegment.FBKConstraintPivot;
import com.eudycontreras.javafx.fbk.models.FBKSegmentChain;
import com.eudycontreras.javafx.fbk.models.FBKVector;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class NetRing extends Application {

	ArrayList<FBKSegmentChain> chains;


	double TWO_PI = 6.2831855;
	
	@Override
	public void start(Stage primaryStage) {

		Pane pane = new Pane();
		Scene scene = new Scene(pane, Color.BLACK);
		
		primaryStage.setFullScreen(true);
		primaryStage.setScene(scene);
		
		primaryStage.show();
		
		setup(primaryStage, pane);
		
		Circle circle = new Circle(primaryStage.getWidth()/2,primaryStage.getHeight()/2,300d);
		circle.setStroke(Color.rgb(0, 180, 220));
		circle.setStrokeWidth(12);
		circle.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(0, 180, 220),30,0.2,0,0));
		circle.setFill(Color.TRANSPARENT);
		pane.getChildren().add(circle);
		
		scene.setOnMouseMoved(e -> {
			if(e.isControlDown()){
				FBKVector pos = new FBKVector(e.getSceneX(), e.getSceneY());
				
				draw(primaryStage, pos);
			}
		});
	}

	private void setup(Stage primaryStage, Pane pane) {
		chains = new ArrayList<FBKSegmentChain>();

		double da = TWO_PI / (double) 150.0d;
		
		for (double a = 0.0; a < TWO_PI ; a += da) {
			float x = (float) ((primaryStage.getWidth()/2d) + (Math.cos(a) * 300d));
			float y = (float) ((primaryStage.getHeight()/2d) + (Math.sin(a) * 300d));
			FBKSegmentChain chain = new FBKSegmentChain(pane, 40, x, y);
			chain.constrain(FBKConstraintPivot.NONE);
			chain.draw();
			chains.add(chain);
		}
	}

	void draw(Stage primaryStage, FBKVector pos) {
		for (FBKSegmentChain t : chains) {
			t.update(pos);
			t.draw();
			
		//	System.out.println("DSDF");
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
