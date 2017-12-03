package com.eudycontreras.components.assist;

import com.eudycontreras.javafx.fbk.models.FBKVector;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class FXSelectionAssist {

	public static PulseMarker createMarker(Group parent, Color color, FBKVector vector) {
		return createMarker(parent,color,vector.getX(),vector.getY());
	}
	
	public static PulseMarker createMarker(Group parent, Color color, double x, double y) {
		
		PulseMarker marker = new PulseMarker(parent, color, x, y);
		
		return marker;
	}
	
	public static class PulseMarker extends Circle{
		
		private ParallelTransition pack;
		
		private Group parent;
		
		public PulseMarker(Group parent, Color color, double x, double y) {
			this.parent = parent;
			
			setFill(color.deriveColor(1, 1, 1, 3.5));
			setStroke(color);
			setStrokeWidth(2);
			setRadius(0);
			setCenterX(x);
			setCenterY(y);
			
			parent.getChildren().add(0,this);
			Duration duration = Duration.millis(500);

			ScaleTransition scale = new ScaleTransition(duration, this);
			FadeTransition fade = new FadeTransition(duration, this);
			
			pack = new ParallelTransition(this, scale, fade);
			
			scale.setFromX(0);
			scale.setFromY(0);
			scale.setToX(26);
			scale.setToY(26);
			fade.setFromValue(0.8);
			fade.setToValue(0);
		}
		
		public void startPulsing(){
			setVisible(true);
			pack.play();
		}
		
		public void stopPulsing(){
			pack.stop();
			setVisible(false);
		}
		
		public void remove(){
			pack.setOnFinished(e2 -> {
				parent.getChildren().remove(this);
			});
			pack.setCycleCount(2);
		}

		public void setColor(Color color) {
			setFill(color.deriveColor(1, 1, 1, 3.5));
			setStroke(color);
		}
	}

}
