package com.eudycontreras.editor.sections;

import java.util.HashMap;
import java.util.Map;

import com.eudycontreras.editor.application.FXPaintResources;
import com.eudycontreras.editor.application.Styles;
import com.eudycontreras.editor.controls.FXRangeSlider;

import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Side;
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

public class FXEditorSideTools extends Pane{

	private Map<Tab, Node> tabContent = new HashMap<>();
	private BorderPane borderPane = new BorderPane();
	private TabPane tabPane = new TabPane();
	private Tab[] tab = new Tab[3];


	public FXEditorSideTools(String id, int count, double width, double height) {
		tabPane.setPrefSize(width, height);
		tabPane.setMinWidth(0);
		tabPane.setSide(Side.TOP);
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		tabPane.setId(id);

		tab[0] = new Tab();
		tab[1] = new Tab();
		tab[2] = new Tab();

		tab[0].setText("Tab 1");
		tab[1].setText("Tab 2");
		tab[2].setText("Tab 3");

		tabContent.put(tab[0], createTabContent(tab[0].getText(), width, height));
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
			TranslateTransition fadeOut = new TranslateTransition(Duration.seconds(0.25), oldContent);
			//fadeOut.setFromX(1);
			fadeOut.setFromY(0);
			//fadeOut.setToX(0);
			fadeOut.setToY(-oldContent.getLayoutBounds().getHeight());

			TranslateTransition fadeIn = new TranslateTransition(Duration.seconds(0.25), newContent);
			//fadeIn.setFromX(0);
			fadeIn.setFromY(-newContent.getLayoutBounds().getHeight());
			//fadeIn.setToX(1);
			fadeIn.setToY(0);

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
		rect.setFill(Color.rgb(30, 30, 30));
		pane.getChildren().addAll(rect);
		pane.getChildren().add(new FXRangeSlider(200,8,0,180).get());
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
