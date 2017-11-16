package com.eudycontreras.editor.application;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;

public class GUIElements extends ToolBar {

	public static Slider createSlider(String id) {

		Slider slider = new Slider();
		slider.setId(id);

		return slider;
	}

	public static ToolBar createToolBar(String id, int amount) {
		Button[] button = new Button[amount];
		ToolBar toolBar = new ToolBar();

		toolBar.getStylesheets().add(Styles.styledToolBarCss);
		toolBar.setId(id);

		toolBar.getItems().add(createSlider(Styles.SLIDER));

		return toolBar;
	}


	public static Label createLabel(String id, String text) {

		Label label = new Label();
		label.getStylesheets().add(Styles.styledToolBarCss);
		label.setId(id);
		label.setText(text);

		return label;
	}

}
