package com.eudycontreras.editor.sections;

import com.eudycontreras.editor.application.FXPaintResources;
import com.eudycontreras.editor.application.FXEditor;
import com.eudycontreras.editor.application.FXImageResources;
import com.eudycontreras.utilities.FXPaintUtility;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class FXEditorToolbar {

	private ToolBar toolBar = new ToolBar();
	private Region spacer = new Region();
	private HBox buttonBar = new HBox(10);

	private Button sampleButton1 = new Button("Tasks");
	private Button sampleButton2 = new Button("Administrator");
	private Button sampleButton3 = new Button("Search");
	private Button sampleButton4 = new Button("Line");
	private Button sampleButton5 = new Button("Process");
	
	private Image image = FXImageResources.create_new_project_icon.getImage();

	public FXEditorToolbar(FXEditor editor) {

		editor.getLayoutWindow().setId("background");
		
		spacer.getStyleClass().setAll("spacer");

		buttonBar.getStyleClass().setAll("segmented-button-bar");

		sampleButton1.getStyleClass().addAll("first");

		sampleButton5.getStyleClass().addAll("last", "capsule");
		
		buttonBar.setAlignment(Pos.CENTER);

		buttonBar.getChildren().addAll(
				createButton("Open",image ), 
				createButtonDivider(30),
				createButton("Save",image ),
				createButtonDivider(30),
				createButton("Save",image ),
				createButtonDivider(30),
				createButton("Save",image ),
				createButtonDivider(30),
				createButton("Save",image ));

		toolBar.getItems().addAll(spacer, buttonBar);

	}
	
	
	public void addButtons(Button...buttons){
		
	}
	
	private Node createButton(String text, Image image){
    	
		VBox button = new VBox(1);
		Text label = new Text(text);
		ImageView graphic = new ImageView(image);

		InnerShadow shadow = new InnerShadow();
		
		shadow.setChoke(1);
		shadow.setColor(Color.rgb(140, 140, 140));
		
		InnerShadow highlight = new InnerShadow();
		
		highlight.setChoke(1);
		highlight.setColor(FXPaintResources.SECONDARY_COLOR);

		graphic.setEffect(highlight);
		graphic.setCache(true);
		graphic.setCacheHint(CacheHint.SPEED);
		
//		graphic.effectProperty().bind(
//	                Bindings
//	                    .when(graphic.hoverProperty())
//	                        .then((Effect) highlight)
//	                        .otherwise((Effect) shadow)
//	        );


		label.setFill(Color.WHITE);

		button.setPadding(new Insets(4, 4, 4, 4));
		button.setAlignment(Pos.CENTER);
		button.getChildren().add(graphic);
    	
    	return button;
    }
	
	private Node createButtonDivider(double height){
		return createButtonDivider(height, FXPaintUtility.color(5), FXPaintUtility.color(70));
	}
			
	private Node createButtonDivider(double height, Color main, Color highlight){
		
		Rectangle separator = new Rectangle(1,height);
		separator.setFill(main);
		separator.setStroke(highlight);
		separator.setStrokeWidth(0.5);
		
		
		return separator;
	}
	
	public Node get(){
		return toolBar;
	}
}
