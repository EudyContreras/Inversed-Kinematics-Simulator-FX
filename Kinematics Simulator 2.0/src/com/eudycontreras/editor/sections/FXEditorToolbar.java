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
	
	private Image[] image = new Image[5];
	

	public FXEditorToolbar(FXEditor editor) {
		
		image[0] = FXImageResources.create_new_project_icon.getImage();
		image[1] = FXImageResources.settings_icon.getImage();
		image[3] = FXImageResources.undo_icon.getImage();
		image[4] = FXImageResources.redo_icon.getImage();
		
		editor.getLayoutWindow().setId("background");
		
//		spacer.getStyleClass().setAll("spacer");
//
//		buttonBar.getStyleClass().setAll("segmented-button-bar");
//
//		sampleButton1.getStyleClass().addAll("first");
//
//		sampleButton5.getStyleClass().addAll("last", "capsule");
		
		buttonBar.setAlignment(Pos.CENTER);

		buttonBar.getChildren().addAll(
				createButtonSpace(5),
				createButton("Open",image[0] ), 
				createButtonDivider(25),
				createButton("Save",image[1] ),
				createButtonDivider(25),
				createButton("Save",image[3] ),
				createButtonDivider(25),
				createButton("Save",image[4] )
				);

		toolBar.getItems().addAll(spacer, buttonBar);

	}
	
	
	public void addButtons(Button...buttons){
		
	}
	
	private Node createButton(String text, Image image){
    	
		VBox button = new VBox(1);
		Text label = new Text(text);
		ImageView graphic = new ImageView(image);
		graphic.setFitWidth(30);
		graphic.setFitHeight(30);
		InnerShadow shadow = new InnerShadow();
		
		shadow.setChoke(1);
		shadow.setColor(Color.rgb(140, 140, 140));
		
		InnerShadow highlight = new InnerShadow();
		
		highlight.setChoke(1);
		highlight.setColor(FXPaintResources.ACCENT_COLOR);

		graphic.setEffect(highlight);
		graphic.setCache(true);
		graphic.setCacheHint(CacheHint.SPEED);
		
		button.setOnMousePressed(e -> {
			highlight.setColor(FXPaintResources.ACCENT_COLOR_BRIGHT);
		});
		
		button.setOnMouseEntered(e -> {
			highlight.setColor(FXPaintResources.ACCENT_COLOR);
		});
		
		button.setOnMouseExited(e -> {
			highlight.setColor(Color.rgb(45, 45, 45));
		});
		
		button.setOnMouseDragExited(e -> {
			highlight.setColor(Color.rgb(150, 150, 150));
		});
		
		button.setOnMouseReleased(e -> {
			highlight.setColor(FXPaintResources.ACCENT_COLOR);
		});
//		graphic.effectProperty().bind(
//	                Bindings
//	                    .when(graphic.hoverProperty())
//	                        .then((Effect) highlight)
//	                        .otherwise((Effect) shadow)
//	        );


		label.setFill(Color.WHITE);

		button.setPadding(new Insets(6, 1, 6, 1));
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
	
	private Node createButtonSpace(){
		return createButtonSpace(0);
	}
			
	private Node createButtonSpace(double width){
		
		Rectangle separator = new Rectangle(width,0);
		separator.setFill(Color.TRANSPARENT);
		separator.setVisible(false);
		
		return separator;
	}
	
	public Node get(){
		return toolBar;
	}
}
