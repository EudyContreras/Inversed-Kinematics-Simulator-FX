package com.eudycontreras.editor.dialogs;


import com.eudycontreras.components.handlers.FXJoint;
import com.eudycontreras.editor.application.FXEditorApp;
import com.eudycontreras.editor.controls.FXSlider;
import com.eudycontreras.editor.controls.FXToggles;
import com.eudycontreras.models.ToggleInfo;
import com.eudycontreras.views.FXSeparator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class FXEditorJointDialog extends StackPane implements FXEditorDialog{

	private Rectangle background = new Rectangle();
	
	private FXSeparator separator = new FXSeparator();

	private VBox vLayout = new VBox(5);
	
	private FXSlider[] sliders = new FXSlider[3];

	private ToggleInfo[] toggles = new ToggleInfo[3];
	
	private FXToggles togglePane = new FXToggles();
	
	private Button button = new Button();
	
	private Text label = new Text("Joint options");
	
	private FXJoint currentJoint = null;
	
	private Runnable closeAction = null;
	
	public FXEditorJointDialog(int x, int y, double width, double height){	
		
		background.setX(x);
		background.setY(y);
		
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.DODGERBLUE);
        dropShadow.setRadius(15);
        dropShadow.setSpread(0.15);
        dropShadow.setBlurType(BlurType.GAUSSIAN);
        
        label.setEffect(dropShadow);
        label.setFont(Font.font(null, FontWeight.BOLD, 22));
        label.setId("FX2");

		background.setFill(Color.rgb(55, 55, 55));
		background.setArcHeight(20);
		background.setArcWidth(20);
		background.setStroke(Color.rgb(35,35,35));
		background.setStrokeWidth(8);
		background.setStrokeType(StrokeType.OUTSIDE);
		
		separator.Create();
		separator.setHeight(5);
		separator.setArcHeight(8);
		separator.setArcWidth(8);
		separator.setFill(Color.rgb(90, 90, 90));

		separator.setPadding(new Insets(10,0,20,0));		
		
		button.setText("Close");
		button.setId("dark");
		button.setOnAction(e -> {
			if(closeAction != null){
				closeAction.run();
			}
		});
	
     	sliders[0] = new FXSlider("Angle", -180, 180, 0, 300, true);
     	sliders[1] = new FXSlider("Min angle", -180, 180, 0, 300, true);
    	sliders[2] = new FXSlider("Max angle", -180, 180, 0, 300, true);
		
    	toggles[0] = new ToggleInfo("Active", ()-> System.out.println("active"));
     	toggles[1] = new ToggleInfo("Open", ()-> System.out.println("active"));
     	toggles[2] = new ToggleInfo("Lock", ()-> System.out.println("active"));
    	
     	vLayout.setSpacing(12);
     	vLayout.setPadding(new Insets(10,10,20,10));
     	vLayout.getChildren().add(label);
     	vLayout.getChildren().add(separator.get());
        vLayout.getChildren().addAll(sliders);      
        vLayout.getChildren().add(togglePane.addToggleInfo(toggles));
        vLayout.getChildren().add(button);
        vLayout.setAlignment(Pos.CENTER);
		
        this.setWidth(width);
        this.setHeight(height);
        this.setShape(background);
	   // this.getStylesheets().add(FXEditorApp.class.getResource("StyledSliders.css").toExternalForm());
		this.getChildren().add(background);
	    this.getChildren().add(vLayout);
	}
	
	public FXEditorJointDialog show(int x, int y){
		
		return this;
	}
	
	public void computeSize(){
		background.setWidth(this.getWidth());
		background.setHeight(this.getHeight());
		separator.setWidth(this.getWidth() *0.9);
		button.setPrefWidth(this.getWidth()*0.9);
	}

	public void addCloseAction(Runnable action){
		this.closeAction = action;
	}
	
	public FXEditorJointDialog hide(){
		
		return this;
	}

	public void setJoint(FXJoint subject) {
		
		if(this.currentJoint != null){

			sliders[0].removeValueListener(currentJoint.getAngleChangeListener());
			sliders[1].removeValueListener(currentJoint.getMinAngleChangeListener());
			sliders[2].removeValueListener(currentJoint.getMaxAngleChangeListener());

		}
		this.currentJoint = subject;
		
		if(currentJoint != null){
			
			sliders[0].setValue(subject.getAngle());
			sliders[1].setValue(subject.getMinAngle());
			sliders[2].setValue(subject.getMaxAngle());
			
			sliders[0].addValueListener(subject.getAngleChangeListener());
			sliders[1].addValueListener(subject.getMinAngleChangeListener());
			sliders[2].addValueListener(subject.getMaxAngleChangeListener());
	
		}
	}
	
}
