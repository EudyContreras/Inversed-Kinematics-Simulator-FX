package com.eudycontreras.editor.sections;

import com.eudycontreras.editor.application.FXEditor;
import com.eudycontreras.editor.application.FXImageResources;
import com.eudycontreras.editor.application.Styles;
import com.eudycontreras.editor.controls.FXRangeSlider;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

public class FXEditorSideBoard {


	private HBox content = new HBox();
	private Pane board = new StackPane();
	private VBox layout = new VBox();
	private EditingArea rect = new EditingArea(Styles.WINDOW, 410, 1200);

	public FXEditorSideBoard(FXEditor editor) {

		content.setId("side-board-content");
		
		content.setPrefWidth(70);
		content.widthProperty().addListener(dragTranslation(editor));
		content.setRotationAxis(Rotate.Y_AXIS);
		content.setRotate(180);
		content.setOpacity(0.35);

		layout.setPickOnBounds(false);
		layout.setPadding(new Insets(40,10,0,20));

		board.setId("side-board");
		board.setPadding(new Insets(0,50,50,0));
		
		board.getChildren().add(content);
		board.getChildren().add(layout);
		layout.getChildren().add(rect);
	
	}
	
	public FXRangeSlider getSlider(){
		return rect.getSliderDemo();
	}

	private ChangeListener<Number> dragTranslation(FXEditor editor) {

		ChangeListener<Number> listener = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number ov, Number nv) {
				
				double delta = 0;

				double oldValue = (double) ov;
				double newValue = (double) nv;

				double x = editor.getEditingWindow().getTranslateX();

				if (oldValue > newValue) {
					delta = x + Math.abs(oldValue - newValue) * 0.45;
				} 
				else if (newValue > oldValue) {
					delta = x - Math.abs(oldValue - newValue) * 0.45;
				}

				editor.getEditingWindow().setTranslateX(delta);
				
				//rect.setPrefWidth(content.getWidth());
			}

		};

		return listener;
	}

	public class EditingArea extends VBox{
		
		private FXEditorSideTools sideEditorTools;
		
		public EditingArea(String id) {
			
		}
		
		public EditingArea(String id, double width, double height) {

			sideEditorTools = new FXEditorSideTools(id, 10, width, height);

			VBox.setVgrow(sideEditorTools, Priority.ALWAYS);
//
//			setAlignment(Pos.CENTER);
			getChildren().add(sideEditorTools);

//			setPrefSize(width, height);
			getStylesheets().add(Styles.styledToolBarCss);
			setId(id);
		}
		
		public FXRangeSlider getSliderDemo(){
			return sideEditorTools.getSlider();
		}
	}
	


	private Node createSearchBar(){
        //Controls to be added to the HBox

        ImageView label = new ImageView();
        
        
        label.setImage(FXImageResources.tree_node_logo.getImage());
        label.setPreserveRatio(true);
        label.setFitWidth(30);
        label.setFitHeight(30);

        TextField textField = new TextField();
        textField.setId("side-board-header");
        textField.maxHeight(30);

        Button button = new Button("Button");
        button.setId("search-button");
        HBox hbox = new HBox(10);

        HBox.setHgrow(textField, Priority.ALWAYS);
        hbox.getChildren().addAll(label, textField, button);

        hbox.setAlignment(Pos.CENTER);

        return hbox;
	}
	
	public Region get() {
		return board;
	}

	public Region getContent() {
		return content;
	}
}
