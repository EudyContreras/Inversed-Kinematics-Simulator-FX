package com.eudycontreras.editor.sections;

import com.eudycontreras.editor.application.FXEditor;
import com.eudycontreras.editor.application.FXImageResources;
import com.eudycontreras.utilities.FXImageLoader;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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

	private HBox hBox = new HBox();
	private VBox vBox = new VBox(50);
	private HBox content = new HBox();
	private Pane board = new StackPane();
	private VBox layout = new VBox();
	private VBox rect = new VBox(12);

	public FXEditorSideBoard(FXEditor editor) {

		//board.getStylesheets().add(FXEditorSideBoard.class.getResource("FXEditorSideBoard.css").toExternalForm());

		vBox.setPadding(new Insets(0, 20, 0, 20));

		vBox.setAlignment(Pos.CENTER);

		hBox.getChildren().addAll(new Separator(Orientation.VERTICAL), vBox);
		hBox.getChildren().add(new Text("HELLO"));
		hBox.setPickOnBounds(false);
		vBox.setPickOnBounds(false);

		content.setId("side-board-content");
		
		content.widthProperty().addListener(dragTranslation(editor));
		content.setRotationAxis(Rotate.Y_AXIS);
		content.setRotate(180);

		rect.setMaxHeight(50);

		layout.getChildren().add(rect);
		
		Node searchBar = createSearchBar();
		FXEditorExplorer explorer = new FXEditorExplorer(editor);
		
		VBox.setVgrow(rect, Priority.ALWAYS);
		//VBox.setVgrow(searchBar, Priority.ALWAYS);
		
		rect.setPadding(new Insets(15,5,5,5));
//		rect.getChildren().add(searchBar);
//		rect.getChildren().add(explorer.get());
		
		layout.setPickOnBounds(false);

		board.setId("footer-bar");
	
		board.getChildren().add(content);
		board.getChildren().add(layout);
//		board.getChildren().add(rect);

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
