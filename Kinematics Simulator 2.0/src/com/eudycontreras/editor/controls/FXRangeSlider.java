package com.eudycontreras.editor.controls;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.eudycontreras.editor.application.FXPaintResources;
import com.eudycontreras.utilities.FXKinematicsUtility;
import com.eudycontreras.utilities.FXPaintUtility;

import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class FXRangeSlider{
	
	private static DecimalFormat formatter = new DecimalFormat("0");
	
	public static final String  DEGREE  = "\u00b0";

	private HBox component = new HBox();
	
	private StackPane wrapper = new StackPane();
	
	private Group sliderWrapper = new Group();
	
	private Shape slider = new Rectangle();

	private Circle thumbLeft = new Circle();
	private Circle thumbRight = new Circle();
	
	private Rectangle sliderFrame = new Rectangle();
	private Rectangle sliderTrack = new Rectangle();
	private Rectangle rangeIndicator = new Rectangle();
	
	private Color frameColor = FXPaintResources.SECONDARY_COLOR; 
	private Color trackColor = FXPaintUtility.color(22);
	private Color thumbColor = FXPaintUtility.color(22);
	private Color rangeIndicatorColor = FXPaintResources.ACCENT_COLOR;
	private Color rangeIndicatorColorActive = FXPaintResources.ACCENT_COLOR_LIGHT;
	
	private Label minValueText = new Label ();
	private Label maxValueText = new Label ();
	
	private DoubleProperty minValue = new SimpleDoubleProperty( -1);
	private DoubleProperty maxValue = new SimpleDoubleProperty( -1);
	
	private final Delta thumbRightDelta = new Delta();
	private final Delta thumbLeftDelta = new Delta();
	
	private double minRangeValue = 0.0;
	private double maxRangeValue = 0.0;
	
	private double width = 0;
	private double height = 0;
	
	private boolean directMapping = true;
	private boolean active = false;
	
	private FXRangeSliderType sliderType = FXRangeSliderType.HORIZONTAL;

	private List<FXRangeSliderChangeListener> changeListeners = new ArrayList<>();

	public enum FXRangeSliderType {
		VERTICAL, HORIZONTAL
	}
	
	public interface FXRangeSliderChangeListener{	
		public void onChange(double minValue, double maxValue);
	}

	public FXRangeSlider() {
		this(0, 0);
	}

	public FXRangeSlider(double minRange, double maxRange) {
		this(0, 0, minRange, maxRange);
	}

	public FXRangeSlider(double width, double height, double minRange, double maxRange) {
		this(0, 0, width, height, minRange, maxRange, minRange, maxRange);
	}

	public FXRangeSlider(double width, double height, double minValue, double maxValue, double minRange, double maxRange) {
		this(0, 0, width, height, minValue, maxValue, minRange, maxRange);
	}

	public FXRangeSlider(double x, double y, double width, double height, double minValue, double maxValue,double minRange, double maxRange) {
		this.sliderWrapper.setTranslateX(x);
		this.sliderWrapper.setTranslateY(y);

		this.sliderTrack.setWidth(width);
		this.sliderTrack.setHeight(height);

		this.minRangeValue = minRange;
		this.maxRangeValue = maxRange;
		
		this.addConverters();
		this.createSlider();
		this.addEventHandling();
	}

	private void createSlider(){
		
		this.sliderFrame.setWidth(sliderTrack.getWidth() * 1.15);
		this.sliderFrame.setHeight(sliderTrack.getHeight() * 2.55);
		
		this.sliderFrame.setArcWidth(sliderFrame.getHeight()*0.8);
	  	this.sliderFrame.setArcHeight(sliderFrame.getHeight()*0.8);
		
		this.sliderFrame.setFill(getFrameColor());
		this.sliderTrack.setFill(getTrackColor());
		this.sliderTrack.setHeight(sliderTrack.getHeight() * 1.1);
		
		this.sliderTrack.setX(sliderFrame.getWidth()/2 - sliderTrack.getWidth()/2);
		this.sliderTrack.setY(sliderFrame.getHeight()/2 - sliderTrack.getHeight()/2);
		
		this.sliderTrack.setArcWidth(sliderTrack.getHeight()*0.8);
	  	this.sliderTrack.setArcHeight(sliderTrack.getHeight()*0.8);
	  	this.sliderTrack.setStroke(FXPaintUtility.color(15));
	  	this.sliderTrack.setStrokeWidth(1.5);
	  	
	  	this.slider = Path.subtract(sliderFrame, sliderTrack);
		
	  	this.slider.setFill(FXPaintUtility.color(30));
	  	//this.slider.setStroke(FXPaintUtility.color(15));
	  	//this.slider.setStrokeWidth(1.4);
		
		this.thumbLeft.setRadius(sliderTrack.getHeight());
		this.thumbRight.setRadius(sliderTrack.getHeight());
		
		this.thumbLeft.setCenterX(sliderFrame.getWidth()/2 - thumbLeft.getRadius()/2);
		this.thumbLeft.setCenterY(sliderFrame.getHeight()/2 + thumbLeft.getRadius()/12);
			
		this.thumbRight.setCenterX(sliderFrame.getWidth()/2 - thumbRight.getRadius()/2);
		this.thumbRight.setCenterY(sliderFrame.getHeight()/2 + thumbRight.getRadius()/12);
		
		this.thumbLeft.setTranslateX(-30);
		this.thumbRight.setTranslateX(30 + thumbRight.getRadius());
		
		this.rangeIndicator.setY(sliderTrack.getY());
		this.rangeIndicator.setX(thumbLeft.getCenterX());

		this.rangeIndicator.setFill(rangeIndicatorColor);
		
		this.thumbLeft.setFill(getThumbColorInactive());
		this.thumbRight.setFill(getThumbColorInactive());
		
		this.thumbLeft.setStroke(rangeIndicatorColor);
		this.thumbRight.setStroke(rangeIndicatorColor);
		
		this.thumbLeft.setStrokeWidth(thumbLeft.getRadius()*0.30);
		this.thumbRight.setStrokeWidth(thumbRight.getRadius()*0.30);
		
		this.setMinValue(minRangeValue);
		this.setMaxValue(maxRangeValue);
		
		this.rangeIndicator.setWidth(distanceBetween(thumbLeft.getTranslateX(),thumbRight.getTranslateX()));
		this.rangeIndicator.setHeight(sliderTrack.getHeight());
		
		this.rangeIndicator.setTranslateX(thumbLeft.getTranslateX());
		
		this.minValueText.setId("range-slider-label");
		this.minValueText.setPrefWidth(50);
		this.minValueText.setAlignment(Pos.CENTER);

		this.maxValueText.setId("range-slider-label");
		this.maxValueText.setPrefWidth(50);
		this.maxValueText.setAlignment(Pos.CENTER);
		this.component.setAlignment(Pos.CENTER);
		
		this.sliderWrapper.getChildren().add(slider);
		this.sliderWrapper.getChildren().add(rangeIndicator);
		this.sliderWrapper.getChildren().add(thumbLeft);
		this.sliderWrapper.getChildren().add(thumbRight);
		
		//this.component.setId("range-slider");
		this.component.getChildren().add(minValueText);
		this.component.getChildren().add(sliderWrapper);
		this.component.getChildren().add(maxValueText);
		this.component.setPadding(new Insets(0,0,2,0));
		
		this.sliderFrame.setArcWidth(sliderFrame.getHeight()*0.2);
	  	this.sliderFrame.setArcHeight(sliderFrame.getHeight()*0.2);
		this.sliderFrame.setHeight(sliderFrame.getHeight()*2.85);
		this.sliderFrame.setWidth(sliderFrame.getWidth()*1.6);
		this.sliderFrame.setFill(FXPaintUtility.color(27));
	  	this.sliderFrame.setStroke(FXPaintUtility.color(22));
	  	this.sliderFrame.setStrokeWidth(1);
	  	
		this.wrapper.getChildren().add(sliderFrame);
		this.wrapper.getChildren().add(sliderTrack);
		this.wrapper.getChildren().add(component);
		
		this.wrapper.setPadding(new Insets(20,8,20,8));
	}
	
	private void addEventHandling() {

		thumbRight.setOnMousePressed(e -> {		
			addSelectAnimation(thumbRight);
			directMapping = false;
			active = true;
			thumbRightDelta.x = thumbRight.getTranslateX() - e.getSceneX();
			thumbRight.toFront();
			thumbRight.setStroke(rangeIndicatorColorActive);
			thumbLeft.setStroke(rangeIndicatorColorActive);
		});
		
		thumbLeft.setOnMousePressed(e -> {
			addSelectAnimation(thumbLeft);
			directMapping = false;
			active = true;
			thumbLeftDelta.x = thumbLeft.getTranslateX() - e.getSceneX();
			thumbLeft.toFront();
			thumbRight.setStroke(rangeIndicatorColorActive);
			thumbLeft.setStroke(rangeIndicatorColorActive);
		});
		
		thumbRight.setOnMouseReleased(e -> {
			removeSelectAnimation(thumbRight);
			directMapping = true;
			active = false;
			thumbRightDelta.x = thumbRight.getTranslateX() - e.getSceneX();
			thumbRight.toFront();
			thumbRight.setStroke(rangeIndicatorColor);
			thumbLeft.setStroke(rangeIndicatorColor);	
		});
		
		thumbLeft.setOnMouseReleased(e -> {
			removeSelectAnimation(thumbLeft);
			directMapping = true;
			active = false;
			thumbLeftDelta.x = thumbLeft.getTranslateX() - e.getSceneX();
			thumbLeft.toFront();
			thumbRight.setStroke(rangeIndicatorColor);
			thumbLeft.setStroke(rangeIndicatorColor);
		});

		thumbRight.setOnMouseDragged(e -> {
			double newX = e.getSceneX() + thumbRightDelta.x;
			
			Bounds childBounds = thumbRight.getBoundsInLocal();

			Bounds parentBounds = sliderTrack.getLayoutBounds();
			
			double maxBounds = (newX + childBounds.getMaxX());		
			double minBounds = (thumbLeft.getTranslateX());
			
	        if(maxBounds >= parentBounds.getMaxX() + 12){
	        	newX = (parentBounds.getWidth() / 2) + 5;
	        }else if(newX <= minBounds){
	        	newX = minBounds;
	        }
	        
			thumbRight.setTranslateX(newX);
			rangeIndicator.setTranslateX(thumbLeft.getTranslateX());
			rangeIndicator.setWidth(distanceBetween(thumbLeft.getTranslateX(),thumbRight.getTranslateX()));
		});
		
		thumbLeft.setOnMouseDragged(e -> {
			double newX = e.getSceneX() + thumbLeftDelta.x;
			
			Bounds childBounds = thumbLeft.getBoundsInLocal();

			Bounds parentBounds = sliderTrack.getLayoutBounds();
			
			double minBounds = (newX + childBounds.getMinX());
			double maxBounds = (thumbRight.getTranslateX());
			
	        if(minBounds <= parentBounds.getMinX() - 10) {
	            newX = -(parentBounds.getWidth() / 2) + 4;
	        }else if(newX >= maxBounds){
	        	newX = maxBounds;
	        }

			thumbLeft.setTranslateX(newX);
			rangeIndicator.setTranslateX(thumbLeft.getTranslateX());
			rangeIndicator.setWidth(distanceBetween(thumbLeft.getTranslateX(),thumbRight.getTranslateX()));
		});
	}

	private void addConverters() {
		thumbLeft.translateXProperty().addListener((osv, oldVal, newVal) -> {

			if(directMapping) return;
			
			double value = newVal.doubleValue();

			double minBounds = -(sliderTrack.getLayoutBounds().getWidth() / 2) + 4;
			
			double maxBounds = (sliderTrack.getLayoutBounds().getWidth() / 2) + 4;
			
			double mappedValue = FXKinematicsUtility.mapValues(value, minBounds, maxBounds, minRangeValue, maxRangeValue);

			if (mappedValue > maxRangeValue) {
				mappedValue = maxRangeValue;
			}
			if (mappedValue < minRangeValue) {
				mappedValue = minRangeValue;
			}

			minValue.set(mappedValue);;
			
			for(FXRangeSliderChangeListener listener : changeListeners){
				listener.onChange(minValue.get(), maxValue.get());
			}
					
			minValueText.setText(formatter.format(mappedValue)+""+DEGREE);
		});
		
		thumbRight.translateXProperty().addListener((osv, oldVal, newVal) -> {

			if(directMapping) return;
			
			double value = newVal.doubleValue();

			double minBounds = -(sliderTrack.getLayoutBounds().getWidth() / 2) + 4;
			
			double maxBounds = (sliderTrack.getLayoutBounds().getWidth() / 2) + 4;
			
			double mappedValue = FXKinematicsUtility.mapValues(value, minBounds, maxBounds, minRangeValue, maxRangeValue);

			if (mappedValue > maxRangeValue) {
				mappedValue = maxRangeValue;
			}
			if (mappedValue < minRangeValue) {
				mappedValue = minRangeValue;
			}
			
			maxValue.set(mappedValue);;
	
			for(FXRangeSliderChangeListener listener : changeListeners){
				listener.onChange(minValue.get(), maxValue.get());
			}
			
			maxValueText.setText(formatter.format(mappedValue)+""+DEGREE);
		
		});
		
		minValue.addListener((obs, oldVal, newVal)-> {
			//System.out.println(newVal.doubleValue());
			
			if(directMapping){
				setMinValue(newVal.doubleValue());
			}
		});
		
		maxValue.addListener((obs, oldVal, newVal)-> {
			//System.out.println(newVal.doubleValue());
			
			if(directMapping){
				setMaxValue(newVal.doubleValue());
			}
		});
	}
	
	public void setMinValue(double minValue) {

		if(outOfRange(minValue)){
			try {
				throw new ValueOutOfRangeException(minValue,minRangeValue,maxRangeValue);
			} catch (ValueOutOfRangeException e) {
				e.printStackTrace();
				System.exit(4);
			}
		}

		double value = minValue;

		double minBounds = -(sliderTrack.getLayoutBounds().getWidth() / 2) + 4;
		
		double maxBounds = (sliderTrack.getLayoutBounds().getWidth() / 2) + 4;
		
		double mappedValue = FXKinematicsUtility.mapValues(value, minRangeValue, maxRangeValue, minBounds, maxBounds);

		if (mappedValue > maxBounds) {
			mappedValue = maxBounds;
		}
		if (mappedValue < minBounds) {
			mappedValue = minBounds;
		}
		
		thumbLeft.setTranslateX(mappedValue);
		
		if(thumbLeft.getTranslateX() > thumbRight.getTranslateX()){
			thumbRight.setTranslateX(thumbLeft.getTranslateX());
		}
		
		minValueText.setText(formatter.format(minValue)+""+DEGREE);
	}

	public void setMaxValue(double maxValue) {

		if(outOfRange(maxValue)){
			try {
				throw new ValueOutOfRangeException(maxValue,minRangeValue,maxRangeValue);
			} catch (ValueOutOfRangeException e) {
				e.printStackTrace();
				System.exit(4);
			}
		}

		double value = maxValue;

		double minBounds = -(sliderTrack.getLayoutBounds().getWidth() / 2) + 4;
		
		double maxBounds = (sliderTrack.getLayoutBounds().getWidth() / 2) + 4;
		
		double mappedValue = FXKinematicsUtility.mapValues(value, minRangeValue, maxRangeValue, minBounds, maxBounds);

		if (mappedValue > maxBounds) {
			mappedValue = maxBounds;
		}
		if (mappedValue < minBounds) {
			mappedValue = minBounds;
		}
		
		thumbRight.setTranslateX(mappedValue);
		
		if(thumbRight.getTranslateX() < thumbLeft.getTranslateX()){
			thumbLeft.setTranslateX(thumbRight.getTranslateX());
		}
		
		maxValueText.setText(formatter.format(maxValue)+""+DEGREE);
		rangeIndicator.setTranslateX(thumbLeft.getTranslateX());
		rangeIndicator.setWidth(distanceBetween(thumbLeft.getTranslateX(),thumbRight.getTranslateX()));
	}
	
	private void addSelectAnimation(Circle circle)	{
		
		FillTransition fill = new FillTransition(Duration.millis(150),rangeIndicator);
		ScaleTransition scale = new ScaleTransition(Duration.millis(150),circle);
		
		scale.setFromX(circle.getScaleX());
		scale.setFromY(circle.getScaleY());
		scale.setToX(1.2);
		scale.setToY(1.2);
		scale.play();
		
		fill.setFromValue(rangeIndicatorColor);
		fill.setToValue(rangeIndicatorColorActive);
		fill.play();
	}
		
	private void removeSelectAnimation(Circle circle)	{

		FillTransition fill = new FillTransition(Duration.millis(150),rangeIndicator);
		ScaleTransition scale = new ScaleTransition(Duration.millis(150),circle);
		
		scale.setFromX(circle.getScaleX());
		scale.setFromY(circle.getScaleY());
		scale.setToX(1);
		scale.setToY(1);
		scale.play();
		
		fill.setFromValue(rangeIndicatorColorActive);
		fill.setToValue(rangeIndicatorColor);
		fill.play();
	}
	
	public void addChangeListener(FXRangeSliderChangeListener changeListener) {
		if(changeListener == null) return;
		
		this.changeListeners.add(changeListener);
	}
	
	public void removeChangeListener(FXRangeSliderChangeListener changeListener) {
		if(changeListener == null) return;
		
		this.changeListeners.remove(changeListener);
	}

	private boolean outOfRange(double value) {
		return value > maxRangeValue || value < minRangeValue;
	}

	public static double distanceBetween(double x1, double x2) {
		return Math.sqrt((x2 - x1) * (x2 - x1));
	}
	
	public double getMinRangeValue() {
		return minRangeValue;
	}

	public void setMinRangeValue(double minRangeValue) {
		this.minRangeValue = minRangeValue;
	}

	public double getMaxRangeValue() {
		return maxRangeValue;
	}

	public void setMaxRangeValue(double maxRangeValue) {
		this.maxRangeValue = maxRangeValue;
	}

	public Node get(){
		return wrapper;
	}

	public Color getFrameColor() {
		return frameColor;
	}

	public void setFrameColor(Color frameColor) {
		this.frameColor = frameColor;
	}

	public Color getTrackColor() {
		return trackColor;
	}

	public void setTrackColor(Color trackColor) {
		this.trackColor = trackColor;
	}

	public Color getThumbColorInactive() {
		return thumbColor;
	}

	public void setThumbColorInactive(Color thumbColorInactive) {
		this.thumbColor = thumbColorInactive;
	}

	public double getMinValue() {
		return minValue.doubleValue();
	}

	public double getMaxValue() {
		return maxValue.doubleValue();
	}
	
	public DoubleProperty getMinValueProperty(){
		return minValue;
	}
	
	public DoubleProperty getMaxValueProperty(){
		return maxValue;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	
	public FXRangeSliderType getSliderType() {
		return sliderType;
	}

	public void setSliderType(FXRangeSliderType sliderType) {
		this.sliderType = sliderType;
	}

	private class Delta {
	double x;
	private double y;

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	} }
	
	private class ValueOutOfRangeException extends Exception{

		private static final long serialVersionUID = 1L;
		
		private static final String message = "Given value is out of range!";
		
		public ValueOutOfRangeException(double value, double minRange, double maxRange){
			super(message + " Value : "+value + " Max allowed value : " + maxRange + " Min allowed value: " + minRange);
		}
	}
	
class IntField extends TextField {
		
		final private IntegerProperty value;

	    private int minValue;
		
	    private int maxValue;
	    
	    private String filler = "";

		public int getValue() {
			return value.getValue();
		}

		public void setDegreeValue(boolean value) {
			if(value){
				this.filler = DEGREE;
			}else{
				this.filler = "";
			}			

			setText(this.value.get()  + filler);
		}

		public void setValue(int newValue) {
			value.setValue(newValue);
		}

		public void setValue(double newValue) {
			value.setValue((double)newValue);
		}
		
		public IntegerProperty valueProperty() {
			return value;
		}
		
		public int getMinValue() {
			return minValue;
		}

		public void setMinValue(int minValue) {
			this.minValue = minValue;
		}

		public void setMinValue(double minValue) {
			this.minValue = (int)minValue;
		}
		
		public int getMaxValue() {
			return maxValue;
		}

		public void setMaxValue(int maxValue) {
			this.maxValue = maxValue;
		}
		
		public void setMaxValue(double maxValue) {
			this.maxValue = (int)maxValue;
		}

		public IntField() {
			this(0,0,0,false);
		}

		public IntField(double minValue, double maxValue, double initialValue, boolean degrees) {
			this((int)minValue, (int)maxValue, (int)initialValue,degrees);
		}
		
		public IntField(int minValue, int maxValue, int initialValue, boolean degrees) {
			if (minValue > maxValue)
				throw new IllegalArgumentException(
						"IntField min value " + minValue + " greater than max value " + maxValue);
			if (maxValue < minValue)
				throw new IllegalArgumentException(
						"IntField max value " + minValue + " less than min value " + maxValue);
			if (!((minValue <= initialValue) && (initialValue <= maxValue)))
				throw new IllegalArgumentException(
						"IntField initialValue " + initialValue + " not between " + minValue + " and " + maxValue);

			this.minValue = minValue;
			this.maxValue = maxValue;
			
			this.value = new SimpleIntegerProperty(initialValue);
			
			this.filler = degrees ? DEGREE : "";

			setText(initialValue  + filler);
			setAlignment(Pos.CENTER_RIGHT);
			setWidth(55);

			final IntField intField = this;

			// make sure the value property is clamped to the required range
			// and update the field's text to be in sync with the value.
			value.addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observableValue, Number oldValue,
						Number newValue) {
					if (newValue == null) {
						intField.setText("");
					} else {
						if (newValue.intValue() < intField.minValue) {
							value.setValue(intField.minValue);
							return;
						}

						if (newValue.intValue() > intField.maxValue) {
							value.setValue(intField.maxValue);
							return;
						}

						if (newValue.intValue() == 0
								&& (textProperty().get() == null || "".equals(textProperty().get()))) {
							// no action required, text property is already
							// blank, we don't need to set it to 0.
						} else {
							intField.setText(newValue.toString() + filler);
						}
					}
				}
			});

			// restrict key input to numerals.
			this.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent keyEvent) {
					if (!"0123456789".contains(keyEvent.getCharacter())) {
						keyEvent.consume();
					}
				}
			});

			// ensure any entered values lie inside the required range.
			this.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observableValue, String oldValue,
						String newValue) {
					if (newValue == null || "".equals(newValue)) {
						value.setValue(0);
						return;
					}
					
					String numberValue = newValue;

					if(newValue.contains(DEGREE)){
						numberValue = newValue.substring(0, newValue.length()-1);
					}
					
					if(newValue.contentEquals("") || newValue.contentEquals(" ")){
						numberValue = "0";
					
					}
					
					final int intValue = Integer.parseInt(numberValue);

					if (intField.minValue > intValue || intValue > intField.maxValue) {
						textProperty().setValue(oldValue + filler);
					}

					numberValue = textProperty().get();

					if(newValue.contains(DEGREE)){
						numberValue = newValue.substring(0, newValue.length()-1);
					}
					
					if(newValue.contentEquals("")){
						numberValue = "0";
					}

					value.set(Integer.parseInt(numberValue));
				}
			});
		}
	}
}
