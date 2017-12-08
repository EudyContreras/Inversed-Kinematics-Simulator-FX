package com.eudycontreras.editor.sections;

import java.util.HashMap;
import java.util.Map;

import com.eudycontreras.editor.application.Styles;
import com.eudycontreras.editor.controls.FXRangeSlider;

import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class FXEditorSideTools extends Pane{

	private Map<Tab, Node> tabContent = new HashMap<>();
	private BorderPane borderPane = new BorderPane();
	private TabPane tabPane = new TabPane();
	private Tab[] tab = new Tab[3];

	private FXRangeSlider sliderDemo = new FXRangeSlider(200,8,-180,180);

	public FXEditorSideTools(String id, int count, double width, double height) {
		tabPane.setPrefSize(width, height);
		tabPane.setMinWidth(0);
		tabPane.setSide(Side.TOP);
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		tabPane.setId(id);

		tab[0] = new Tab();
		tab[1] = new Tab();
		tab[2] = new Tab();

		tab[0].setText("Joint");
		tab[1].setText("Bone");
		tab[2].setText("Skeleton");

		tabContent.put(tab[0], createTabContent(tab[0].getText(), width, height,
				sliderDemo.get(),
				new FXRangeSlider(200,8,0,180).get(),
				new FXRangeSlider(200,8,0,180).get(),
				new FXRangeSlider(200,8,0,180).get(),
				new FXRangeSlider(200,8,0,180).get(),
				new FXRangeSlider(200,8,0,180).get(),
				new FXRangeSlider(200,8,0,180).get(),
				new FXRangeSlider(200,8,0,180).get()));
		tabContent.put(tab[1], createTabContent(tab[1].getText(), width, height));
		tabContent.put(tab[2], createTabContent(tab[2].getText(), width, height));

		tab[0].setContent(tabContent.get(tab[0]));
		tab[1].setContent(tabContent.get(tab[1]));
		tab[2].setContent(tabContent.get(tab[2]));

		tabPane.getSelectionModel().select(tab[0]);
		tabPane.getTabs().setAll(tab);

		tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
			oldTab.setContent(null);
			Node oldContent = tabContent.get(oldTab);
			Node newContent = tabContent.get(newTab);

			newTab.setContent(oldContent);
			ScaleTransition fadeOut = new ScaleTransition(Duration.seconds(0.25), oldContent);
			//fadeOut.setFromX(1);
			fadeOut.setFromY(1);
			//fadeOut.setToX(0);
			fadeOut.setToY(0);

			ScaleTransition fadeIn = new ScaleTransition(Duration.seconds(0.25), newContent);
			//fadeIn.setFromX(0);
			fadeIn.setFromY(0);
			//fadeIn.setToX(1);
			fadeIn.setToY(1);

			fadeOut.setOnFinished(event -> {
				newTab.setContent(newContent);
			});

			SequentialTransition crossFade = new SequentialTransition(fadeOut, fadeIn);
			crossFade.play();
		});
		borderPane.setCenter(tabPane);
		getChildren().add(borderPane);
		getStylesheets().add(Styles.styledToolBarCss);
		setId(id);
	}

	public FXRangeSlider getSlider(){
		return sliderDemo;
	}

	private Node createTabContent(String index, double width, double height, Node... node) {
		Pane pane = new Pane();
		Rectangle rect = new Rectangle(0, 0, width, height);
		VBox linearLayout = new VBox(2);
		linearLayout.setPadding(new Insets(10,0,10,0));
		rect.setFill(Color.rgb(30, 30, 30));
		pane.getChildren().addAll(rect);
		pane.getChildren().add(linearLayout);
		if(node != null){
			linearLayout.getChildren().addAll(node);
		}
		pane.setId(Styles.WINDOW);
		ScrollPane scroller = new ScrollPane();
		scroller.setPrefHeight(400);
		scroller.setMinWidth(0);

        scroller.setContent(pane);
        scroller.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
        scroller.getStylesheets().add(Styles.styledToolBarCss);
        scroller.setId(Styles.SCROLLER);

        scroller.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
            	pane.setMinSize(newValue.getWidth(), newValue.getHeight());
            }
        });

		return scroller;
	}

}
