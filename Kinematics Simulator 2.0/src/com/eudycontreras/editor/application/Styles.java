package com.eudycontreras.editor.application;

import java.util.ArrayList;
import java.util.List;

public class Styles {

	public static final List<String> STYLES = getStyles();
	
	public static final String MainWindow = "-fx-background-color: transparent; -fx-border-width: 5; -fx-border-color: black; "+ "-fx-border-width: 2;";
	public static final String tileWindow = "-fx-background-color: gray; -fx-border-width: 5; -fx-border-color: black; "+ "-fx-border-width: 2;";
	public static final String TILE_SELECTED = "-fx-effect: dropshadow(one-pass-box, white, " + 25 + ", 0.8, 0, 0);-fx-border-color: white;";
	public static final String SIMPLE_TILE_SELECTED = "-fx-effect: dropshadow(one-pass-box, white, " + 15 + ", 0.7, 0, 0);-fx-border-color: white;";
	public static final String TILE_BORDER ="-fx-border-color: red;";
	public static final String DARK_PANE = "-fx-background-color: #292929;";

	public static String styledToolBarCss = GUIElements.class.getResource("Application.css").toExternalForm();

	public static final String SCROLLER = ".scroll-bar";
	public static final String WINDOW = "window";
	public static final String TOLLBAR = "toolbar";
	public static final String MENUBAR = "menubar";
	public static final String BUTTON = ".buttons";
	public static final String LABEL = "label";
	public static final String SLIDER = ".slider";
	public static final String UTILS_BUTTONS = null;
	public static final String DRAG_DROP = "-fx-border-color: lightblue;" + "-fx-border-width: 5;" + "-fx-background-color: transparent;" + "-fx-border-style: solid;";

	
	private static final List<String> getStyles(){
		
		List<String> styles = new ArrayList<>();
		
		styles.add(FXEditorApp.class.getResource("buttons.css").toExternalForm());
		styles.add(FXEditorApp.class.getResource("application.css").toExternalForm());
	
		return styles;

	}
}
