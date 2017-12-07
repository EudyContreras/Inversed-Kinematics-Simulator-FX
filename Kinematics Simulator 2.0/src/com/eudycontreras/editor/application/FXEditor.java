package com.eudycontreras.editor.application;

import com.eudycontreras.editor.controls.FXZoomSlider;
import com.eudycontreras.editor.elements.FXBaseIndicator;
import com.eudycontreras.editor.elements.FXEditorGrid;
import com.eudycontreras.editor.gestures.FXEditorGestures;
import com.eudycontreras.editor.handlers.FXResizeHandler;
import com.eudycontreras.editor.handlers.FXStylesHandler;
import com.eudycontreras.editor.sections.FXEditorFooter;
import com.eudycontreras.editor.sections.FXEditorSideBoard;
import com.eudycontreras.editor.sections.FXEditorToolbar;
import com.eudycontreras.editor.sections.FXEditorViewport;
import com.eudycontreras.editor.sections.FXEditorWindow;
import com.eudycontreras.models.Size;
import com.eudycontreras.utilities.FXPaintUtility;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class FXEditor{

	public static final String IMAGE_SOURCE_DIRECTORY = "com/eudycontreras/resources/images/";
    private static final FXStylesHandler helper = new FXStylesHandler();

	private double width = 0;
	private double height = 0;

	private FXEditorGestures gestureHandler = null;

	private FXEditorWindow mainWindow = new FXEditorWindow();
	private FXEditorWindow workWindow = new FXEditorWindow();
	
	private FXArmatureManager armature;
	
	private BorderPane layoutWindow = new BorderPane();
	
	private FXEditorWindow rootWindow = new FXEditorWindow();
	
	private Scene scene = new Scene(rootWindow,Color.BLACK);
	
	private Stage stage = null;

	public FXEditor(Stage stage, double inputWidth, double inputHeight, Paint paint) {
		this.stage = stage;
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
		FXBaseIndicator indicator = new FXBaseIndicator(new Size(width,height),null,3);
		FXEditorSideBoard sideBoard = new FXEditorSideBoard(this);
		FXEditorViewport viewPort = new FXEditorViewport(this);
		FXEditorToolbar toolbar = new FXEditorToolbar(this);
		FXEditorFooter footer = new FXEditorFooter(this);
		
		zoomSlider.setTranslateX(28);
		viewPort.addElement(zoomSlider);

		gestureHandler = new FXEditorGestures(this, scene, workWindow, zoomSlider);
		gestureHandler.addPannableContent(scene, workWindow, zoomSlider);
		armature = new FXArmatureManager(stage,scene,workWindow.getWidth(), workWindow.getHeight(),gestureHandler);
		
		
		layoutWindow.setCenter(viewPort.get());
		layoutWindow.setRight(sideBoard.get());
		layoutWindow.setBottom(footer.get());
		layoutWindow.setTop(toolbar.get());
		layoutWindow.setPickOnBounds(false);
		
		layoutWindow.prefWidthProperty().bind(scene.widthProperty());
		layoutWindow.prefHeightProperty().bind(scene.heightProperty());
		
		Platform.runLater(new Runnable() {
			  @Override public void run() {
				  workWindow.getChildren().add(0,editorGrid.get());   
			  }
			});
		
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
		
		workWindow.getChildren().add(armature.getSkeleton());
		
	}
	

	public void log(Object obj) {
		System.out.println(obj);
	}


	private EventHandler<KeyEvent> keyHandler = e -> {
		armature.reportKeyEvent(e);
		if(e.isControlDown()){
			if(e.getCode() == KeyCode.ENTER){
				stage.setFullScreen(!stage.isFullScreen());
			}
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

}
