package com.eudycontreras.components.controls;

import java.text.DecimalFormat;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class FXProgressIndicator {

	private Arc[] arcs;
	private double radius;
	private double width;
	private boolean unknown;
	private Color color;
	private Text percentage;
	private Pane container;
	private EventHandler<ActionEvent> onComplete;
	private DecimalFormat format = new DecimalFormat("##.00");
	private StackPane parentContainer;
	private DoubleProperty progress;
	private Timeline timeline;
	private FadeTransition transition;


	public FXProgressIndicator(){
		this(50,Color.DODGERBLUE);
	}

	public FXProgressIndicator(double radius, Color color){
		this.container = new Pane();
		this.timeline = new Timeline();
		this.timeline.setCycleCount( Timeline.INDEFINITE );
		this.transition = new FadeTransition();
		this.progress = new SimpleDoubleProperty();
		this.percentage = new Text();
		this.color = color;
		this.radius = radius;
		this.width = radius/6;
		this.transition.setNode(container);
		this.container.setVisible(false);
		this.percentage.setFill(Color.BLACK);
		this.parentContainer = new StackPane(container,percentage);
		this.percentage.opacityProperty().bind(container.opacityProperty());
		this.progress.addListener((obs,oldValue,newValue)->{
			if(unknown)
				return;

			updateProgress(newValue.doubleValue());

			if(newValue.doubleValue()>=0.99){
				progress.unbind();

				this.percentage.setText("100.00%");
				this.transition.setFromValue(1);
				this.transition.setToValue(0);
				this.transition.setDuration(Duration.millis(300));
				this.transition.setOnFinished(e->{
					reset();
					if(onComplete!=null){
						onComplete.handle(e);
					}
				});
				this.transition.play();
			}
		});
		this.parentContainer.setScaleX(0);
		this.parentContainer.setScaleY(0);
	}

	private void createGraphic(int main, int stroke){
		arcs = new Arc[stroke];

		for(int i = 0; i<arcs.length; i++){
			arcs[i] = new Arc();
			Arc arc = arcs[i];

			arc.setFill(null);

		    arc.setCenterX(((radius+width)*2)/2);
		    arc.setCenterY(((radius+width)*2)/2);

		    arc.setStartAngle(270);
		    arc.setLength(0);

		    if(i==main){
			    arc.setRadiusX(radius);
			    arc.setRadiusY(radius);
			    arc.setStrokeWidth(width);
			    arc.setStroke(color);
		    }else if(i<main){
			    arc.setStrokeWidth(width*.75);
			    arc.setRadiusX(radius*1.0005 + arc.getStrokeWidth());
			    arc.setRadiusY(radius*1.0005 + arc.getStrokeWidth());
			    arc.setStroke(color.deriveColor(1, 0.7, 1, 1));
		    }else if(i>main){
			    arc.setStrokeWidth(width*.6);
			    arc.setRadiusX(radius*0.78 + arc.getStrokeWidth());
			    arc.setRadiusY(radius*0.78 + arc.getStrokeWidth());
			    arc.setStroke(color.deriveColor(0, 0.7, 1, 1));
		    }

		    container.getChildren().add(arc);
		}
	}

	private void createIndeterminateIndicator(int main, int strokes){
		arcs = new Arc[strokes];

		for(int i = 0; i<arcs.length; i++){
			arcs[i] = new Arc();
			Arc arc = arcs[i];

			arc.setFill(null);

		    arc.setCenterX(((radius+width)*2)/2);
		    arc.setCenterY(((radius+width)*2)/2);

		    arc.setStartAngle(270);

		    arc.setStrokeWidth(width);

		    container.getChildren().add(arc);
		    
			}
			    arcs[1].setRadiusX(radius);
			    arcs[1].setRadiusY(radius);
			    arcs[1].setStroke(color);
			    arcs[1].setLength(280);

			    arcs[0].setRadiusX(radius*1.06 + arcs[0].getStrokeWidth());
			    arcs[0].setRadiusY(radius*1.06 + arcs[0].getStrokeWidth());
			    arcs[0].setStroke(color.deriveColor(0, 0.7, 1, 1));
			    arcs[0].setLength(300);

			    arcs[2].setRadiusX(radius*0.60 + arcs[0].getStrokeWidth());
			    arcs[2].setRadiusY(radius*0.60 + arcs[0].getStrokeWidth());
			    arcs[2].setStroke(color.deriveColor(0, 0.7, 1, 1));
			    arcs[2].setLength(300);

			    arcs[3].setRadiusX(radius*0.38 + arcs[0].getStrokeWidth());
			    arcs[3].setRadiusY(radius*0.38 + arcs[0].getStrokeWidth());
			    arcs[3].setStroke(color);
			    arcs[3].setLength(300);

	}

	private void reset(){
		for(Arc arc : arcs){
			arc.setLength(0);
		}
	}

	private void updateProgress(double value){
		for(Arc arc : arcs){
			arc.setLength(-360*value);
		}
		percentage.setText(format.format(value*100) + "%");
	}

	public void setVisible(boolean state){
		if (state) {
			this.width = radius / 6;
			this.unknown = false;

			this.container.setPrefSize((radius+width) * 2, (radius+width) * 2);
			this.container.setMinSize((radius+width) * 2, (radius+width) * 2);
			this.container.setMaxSize((radius+width) * 2, (radius+width) * 2);

			DropShadow dropShadow = new DropShadow();

			dropShadow.setOffsetX(-5);
			dropShadow.setOffsetY(10);
			dropShadow.setRadius(45);
			dropShadow.setSpread(0.4);

			this.createGraphic(1, 3);

			this.percentage.setStroke(Color.WHITE);
			this.percentage.setFill(Color.WHITE);
			this.percentage.setFont(Font.font(null,FontWeight.BOLD,radius*.4));
			this.percentage.setTextAlignment(TextAlignment.CENTER);

			this.arcs[0].setEffect(dropShadow);
//			this.arcs[1].setEffect(new DropShadow(BlurType.TWO_PASS_BOX, color.deriveColor(0, 1,0, 0.7), 5, 0.1, 0, 0));
			this.arcs[arcs.length-1].setEffect(new DropShadow(BlurType.TWO_PASS_BOX, color.deriveColor(0, 1,0, 0.7), 5, 0.1, 0, 0));

			this.container.setVisible(true);

		} else {
			this.container.setVisible(false);
		}
	}

	public Region get(){
		return parentContainer;
	}

	public void setIndeterminate(boolean state){

		if (state) {
			this.width = radius / 6;
			this.unknown = true;

			this.container.setPrefSize((radius+width) * 2, (radius+width) * 2);
			this.container.setMinSize((radius+width) * 2, (radius+width) * 2);
			this.container.setMaxSize((radius+width) * 2, (radius+width) * 2);

			this.createIndeterminateIndicator(1, 4);

			this.container.setVisible(true);

			DropShadow dropShadow = new DropShadow();

			dropShadow.setColor(color);
			dropShadow.setOffsetX(0);
			dropShadow.setOffsetY(0);
			dropShadow.setRadius(20);
			dropShadow.setWidth(70);
			dropShadow.setHeight(70);
			dropShadow.setSpread(0.2);
			dropShadow.setInput(new DropShadow(BlurType.TWO_PASS_BOX,  color.deriveColor(0, 1,0, 0.7), 8, 0.1, 0, 0));

			this.arcs[0].setEffect(dropShadow);

			progress.unbind();

			KeyFrame kf = new KeyFrame(Duration.seconds(0.015), new EventHandler<ActionEvent>() {

				double[] rotations = {40,240,169,240};

				public void handle(ActionEvent ae) {

					rotations[0]++;
					rotations[1]--;
					rotations[2]++;
					rotations[3]--;

					for(int i = 0; i<arcs.length; i++){

						Arc arc = arcs[i];

						if(rotations[i]<0){
							rotations[i]=360;
						}
						if(rotations[i]>360){
							rotations[i]=0;
						}

						arc.setRotate(rotations[i]);
					}
				}
			});

			timeline.getKeyFrames().clear();
	        timeline.getKeyFrames().add( kf );
	        timeline.play();

		} else {

			this.transition.setFromValue(1);
			this.transition.setToValue(0);
			this.transition.setDuration(Duration.millis(400));
			this.transition.setOnFinished(e->{
				reset();

				if (onComplete != null) {
					onComplete.handle(e);
				}
				this.container.setVisible(false);
			});
			this.transition.play();
		}
	}

	public void show(){
		
		ScaleTransition scale = new ScaleTransition();
		scale.setInterpolator(Interpolator.EASE_IN);
		scale.setNode(parentContainer);
		scale.setDuration(Duration.millis(250));
		scale.setFromX(0);
		scale.setFromY(0);
		scale.setToX(1);
		scale.setToY(1);
		scale.play();
	}
	

	public void hide(){
		this.hide(null,false);
	}
	
	public void hide(Pane parent, boolean remove){
		ScaleTransition scale = new ScaleTransition();
		scale.setInterpolator(Interpolator.EASE_OUT);
		scale.setNode(parentContainer);
		scale.setDuration(Duration.millis(150));
		scale.setFromX(1);
		scale.setFromY(1);
		scale.setToX(0);
		scale.setToY(0);
		scale.setOnFinished(e -> {
			if(remove){
				parent.getChildrenUnmodifiable().remove(parentContainer);
			}
		});
		scale.play();
	}
	
	public void setColor(Color color){
		this.color = color;
	}

	public void setRadius(double radius){
		this.radius = radius;
		this.width = radius/6;
	}

	public void setValue(double value){
		if(value>1.0){
			this.progress.set(1.0);
		}else if(value<0){
			this.progress.set(0);
		}else{
			this.progress.set(value);
		}
	}


	public DoubleProperty progressProperty(){
		return progress;
	}

	public void setOnProgressComplete(EventHandler<ActionEvent> event) {
		this.onComplete = event;
	}

}

