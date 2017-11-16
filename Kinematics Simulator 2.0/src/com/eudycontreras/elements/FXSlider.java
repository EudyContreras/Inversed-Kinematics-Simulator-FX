package com.eudycontreras.elements;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class FXSlider extends HBox{


	public static final String  DEGREE  = "\u00b0";

	private IntField field = new IntField();
	
	private Slider slider = new Slider();
	
	private Label label = new Label();
	
	
	public FXSlider(String text, double min, double max, double value, double width,boolean degrees){

		label.setText(text);
		label.setFont(Font.font(12));
		label.setMinWidth(65);
		label.setAlignment(Pos.CENTER_LEFT);
		label.setPadding(new Insets(4,8,0,0));
		label.setId("dark");

		slider.setMin(min);
		slider.setMax(max);
		slider.setBlockIncrement(1);
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
		slider.setMajorTickUnit(60);
		slider.setPrefWidth(width);
		slider.setId("dark");
		
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				label.setContentDisplay(ContentDisplay.TEXT_ONLY);
			}
		});

		field.setMinValue(min);
		field.setMaxValue(max);
		field.setValue(value);
		field.setDegreeValue(true);
		field.valueProperty().bindBidirectional(slider.valueProperty());
		field.setPrefWidth(55);
		field.setId("dark");

		setSpacing(15);
		
		setAlignment(Pos.TOP_CENTER);
		getChildren().addAll(label, slider, field);
	}

	public void setValue(double value){
		slider.setValue(value);
	}
	
	public void removeValueListener(ChangeListener<? super Number> listener){
		slider.valueProperty().removeListener(listener);
	}
	
	public void addValueListener(ChangeListener<? super Number> listener){
		slider.valueProperty().addListener(listener);
	}
	
	public void setBinding(Property<Number> other){
		slider.valueProperty().bind(other);
	}

	public void removeBinding(Property<Number> other){
		slider.valueProperty().unbind();
	}
	
	// helper text field subclass which restricts text input to a given range of
	// natural int numbers
	// and exposes the current numeric int value of the edit box as a value
	// property.
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