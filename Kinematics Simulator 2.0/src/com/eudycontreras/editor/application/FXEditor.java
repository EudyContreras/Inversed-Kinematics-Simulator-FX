package com.eudycontreras.editor.application;

import com.eudycontreras.components.handlers.FXJoint;
import com.eudycontreras.editor.elements.FXBaseIndicator;
import com.eudycontreras.editor.elements.FXEditorGrid;
import com.eudycontreras.editor.elements.FXZoomSlider;
import com.eudycontreras.editor.gestures.FXEditorGestures;
import com.eudycontreras.editor.handlers.FXResizeHandler;
import com.eudycontreras.editor.handlers.FXStylesHandler;
import com.eudycontreras.editor.sections.*;
import com.eudycontreras.models.Size;
import com.eudycontreras.observers.FXObserver;
import com.eudycontreras.utilities.FXPaintUtility;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class FXEditor implements FXObserver<FXJoint> {

	public static final String IMAGE_SOURCE_DIRECTORY = "com/eudycontreras/resources/images/";
    private static final FXStylesHandler helper = new FXStylesHandler();
	 
	private int jointId = 0;
	private int linkCount = 0;

	private double width = 0;
	private double height = 0;
	
	private boolean wasDragging = false;
	private boolean useCurrent = false;

	private boolean openingTools = false;
	private boolean closingTools = false;
	private boolean toolsClosed = false;
	private boolean toolsOpened = true;

	private FXEditorGestures panner = null;

	private FXEditorWindow mainWindow = new FXEditorWindow();
	private FXEditorWindow workWindow = new FXEditorWindow();
	
	private BorderPane layoutWindow = new BorderPane();
	
	private FXEditorWindow rootWindow = new FXEditorWindow();
	
	private Scene scene = new Scene(rootWindow,Color.BLACK);

	public FXEditor(Stage stage, double inputWidth, double inputHeight, Paint paint) {
		this.width = inputWidth*4;
		this.height = inputHeight*4;
			
		scene.getStylesheets().addAll(Styles.STYLES);
		
		mainWindow.setPickOnBounds(false);
		mainWindow.setPrefWidth(inputWidth);
		mainWindow.setPrefHeight(inputHeight);	
		
		workWindow.setPrefSize(width,height);
		workWindow.setTranslateX(-(inputWidth+(inputWidth/2)));
		workWindow.setTranslateY(-(inputHeight+(inputHeight/2)));

		FXZoomSlider zoomSlider = new FXZoomSlider(1, FXEditorGestures.MAX_SCALE, FXEditorGestures.MIN_SCALE);
		FXEditorGrid editorGrid = new FXEditorGrid(width, height, 20, Color.rgb(50, 50, 50), Color.rgb(80, 80, 80));
		FXBaseIndicator indicator = new FXBaseIndicator(new Size(width,height),null,2);
		FXEditorSideBoard sideBoard = new FXEditorSideBoard(this);
		FXEditorViewport viewPort = new FXEditorViewport(this);
		FXEditorToolbar toolbar = new FXEditorToolbar(this);
		FXEditorFooter footer = new FXEditorFooter(this);
		
		zoomSlider.setTranslateX(28);
		viewPort.addElement(zoomSlider);
		
		panner = new FXEditorGestures(this, scene, workWindow, zoomSlider);
		panner.addPannableContent(scene, workWindow, zoomSlider);
		//splitPane.setDividerPositions(0.22);
		
		layoutWindow.setCenter(viewPort.get());
		layoutWindow.setRight(sideBoard.get());
		layoutWindow.setBottom(footer.get());
		layoutWindow.setTop(toolbar.get());
		layoutWindow.setPickOnBounds(false);
		
		layoutWindow.prefWidthProperty().bind(scene.widthProperty());
		layoutWindow.prefHeightProperty().bind(scene.heightProperty());
		
		workWindow.getChildren().add(editorGrid.get());
		workWindow.getChildren().add(indicator);
		
		rootWindow.getChildren().add(workWindow);
		rootWindow.getChildren().add(mainWindow);
		rootWindow.getChildren().add(layoutWindow);
		
		FXResizeHandler.makeResizable(sideBoard.getContent());

		rootWindow.setBackground(FXPaintUtility.background(Color.rgb(40, 40, 41)));
		
		scene.setOnKeyPressed(keyHandler);
		
		stage.setScene(scene);
		stage.setWidth(inputWidth);
		stage.setHeight(inputHeight);
		stage.show();

	}

	public void log(Object obj) {
		System.out.println(obj);
	}


	EventHandler<KeyEvent> keyHandler = e -> {

		switch (e.getCode()) {

		case D:
			if (e.isControlDown()) {

			}
			break;
		default:
			break;

		}
	};

	public static Color getNamedColor(String name) {
		helper.setStyle("-named-color: " + name + ";");
		
		helper.applyCss();

		return helper.getNamedColor();
	}
	
	public static String loadResource(String image) {
		String url = IMAGE_SOURCE_DIRECTORY + image;
		return url;
	}

	public FXEditorWindow getEditingWindow(){
		return workWindow;
	}
	
	public BorderPane getLayoutWindow(){
		return layoutWindow;
	}
	
	public Scene getScene(){
		return scene;
	}

	@Override
	public void update() {

	}

	@Override
	public void setSubject(FXJoint subject) {

	}

	@Override
	public void notify(FXJoint subject) {
		// dialog.setJoint(subject);
	}

}
