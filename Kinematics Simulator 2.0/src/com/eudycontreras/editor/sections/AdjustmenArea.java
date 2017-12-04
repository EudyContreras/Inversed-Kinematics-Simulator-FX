package com.eudycontreras.editor.sections;

import java.util.HashMap;
import java.util.Map;

import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class AdjustmenArea extends Group{

	private Map<Tab, Node> tabContent = new HashMap<>();
	private ScrollPane scroller;
	private BorderPane borderPane = new BorderPane();
	private TabPane tabPane = new TabPane();
	private Tab[] tab = new Tab[3];


	public AdjustmenArea(String id, int count, double width, double height) {
	tabPane.setPrefSize(width, height*.4);
	tabPane.setSide(Side.TOP);
	tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
	tabPane.setId(id);

	tab[0] = new Tab();
	tab[1] = new Tab();
	tab[2] = new Tab();

	tab[0].setText("Tab 1");
	tab[1].setText("Tab 2");
	tab[2].setText("Tab 3");

	tabContent.put(tab[0], createTabContent(tab[0].getText(), width, height*.4));
	tabContent.put(tab[1], createTabContent(tab[1].getText(), width, height*.4));
	tabContent.put(tab[2], createTabContent(tab[2].getText(), width, height*.4));

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
		fadeOut.setFromX(1);
		fadeOut.setFromY(1);
		fadeOut.setToX(0);
		fadeOut.setToY(0);

		ScaleTransition fadeIn = new ScaleTransition(Duration.seconds(0.25), newContent);
		fadeIn.setFromX(0);
		fadeIn.setFromY(0);
		fadeIn.setToX(1);
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


	private Node createTabContent(String index, double width, double height) {
		Pane pane = new Pane();
		Rectangle rect = new Rectangle(0, 0, width, height);
		rect.setFill(Color.GRAY);
		pane.getChildren().addAll(rect);
		pane.setId(Styles.WINDOW);
		scroller = new ScrollPane();
		scroller.setPrefHeight(400);

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

        scroller.setPrefViewportWidth(256);
        scroller.setPrefViewportHeight(256);

		return scroller;
	}

}
