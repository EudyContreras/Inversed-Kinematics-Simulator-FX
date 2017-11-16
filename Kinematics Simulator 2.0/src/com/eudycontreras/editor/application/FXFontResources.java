package com.eudycontreras.editor.application;

import javafx.scene.text.Font;

public class FXFontResources {

	public static final Font ROBOTO_FONT_REGULAR = loadFont("RobotoCondensed-Regular.ttf");
	public static final Font ROBOTO_FONT_BOLD = loadFont("RobotoCondensed-Bold.ttf");
	
	public static Font loadFont(String string) {
		return loadFont(string, 12);
	}
	
	public static Font loadFont(String string, int size) {
		return Font.loadFont("com/eudycontreras/resources/fonts/", size);
	}
}
