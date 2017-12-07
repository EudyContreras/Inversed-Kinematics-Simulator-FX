package com.eudycontreras.editor.gestures;

import com.eudycontreras.editor.application.FXEditor;
import com.eudycontreras.editor.controls.FXZoomSlider;
import com.eudycontreras.editor.handlers.FXGestureHandler;
import com.eudycontreras.editor.sections.FXEditorWindow;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

public class FXEditorGestures extends FXGestureHandler {


	public static final double MAX_SCALE = 2.0d;
	public static final double MIN_SCALE = 0.1d;
	
	@SuppressWarnings("unused")
	private double lastX = -1;
	@SuppressWarnings("unused")
	private double lastY = -1;
	
	private FXEditorWindow pane;
	
	public FXEditorGestures(FXEditor editor) {
		super(editor);
	}
	
	public FXEditorGestures(FXEditor editor, Scene scene, FXEditorWindow pane, FXZoomSlider slider) {
		super(editor);
		addPannableContent(scene,pane,slider);
	}
	
	public void addPannableContent(Scene scene, FXEditorWindow pane, FXZoomSlider slider){	
		this.pane = pane;
		this.pane.setOnMouseEntered(getOnMouseEnteredEventHandler());
		this.pane.setOnMouseExited(getOnMouseExitedEventHandler());
		this.pane.setOnMousePressed(getOnMousePressedEventHandler());
		this.pane.setOnMouseReleased(getOnMouseReleasedEventHandler());
		this.pane.setOnMouseDragged(getOnMouseDraggedEventHandler());	
		this.pane.setOnScroll(getOnMouseScrolledEventHandler());
		
		pane.setScale(slider.getZoomLevel());
		
		slider.getZoomProperty().addListener((osv, oldVal, newVal) -> {

			double scale = (double)newVal;
			
			pane.setScale(scale);


		});
		
		pane.getScaleProperty().addListener((osv, oldVal, newVal) -> {
			
			double scale = (double)newVal; 
			
			slider.setZoom(scale);
			
		});
	}
	@Override
	protected void performMouseEntered(MouseEvent event) {
		performGesture(GestureType.MOUSE_ENTERED, event, NaN, NaN);
	}

	@Override
	protected void performMouseExited(MouseEvent event) {
		performGesture(GestureType.MOUSE_EXITED,event, NaN, NaN);
	}

	@Override
	protected void performMouseClicked(MouseEvent event) {
		performGesture(GestureType.MOUSE_CLICKED,event, event.getSceneX(), event.getSceneY());
	}
	
	@Override
	protected void performMousePressed(MouseEvent event) {
	
		dragContext.mouseAnchorX = event.getSceneX();
		dragContext.mouseAnchorY = event.getSceneY();

		dragContext.translateAnchorX = pane.getTranslateX();
		dragContext.translateAnchorY = pane.getTranslateY();
		
		lastX = dragContext.translateAnchorX + (event.getSceneX() - dragContext.mouseAnchorX);
		lastY = dragContext.translateAnchorY + (event.getSceneY() - dragContext.mouseAnchorY);
	
		performGesture(GestureType.MOUSE_PRESSED,event, event.getSceneX(), event.getSceneY());
	}
	
	@Override
	protected void performMouseDraggStarted(MouseEvent event) {
		performGesture(GestureType.MOUSE_DRAGG_STARTED,event, NaN, NaN);
	}

	@Override
	protected void performMouseDraggEnded(MouseEvent event) {
		performGesture(GestureType.MOUSE_DRAGG_ENDED,event, NaN, NaN);
	}

	@Override
	protected void performMouseReleased(MouseEvent event) {		
		performGesture(GestureType.MOUSE_RELEASED,event, NaN, NaN);
	}

	@Override
	protected void performMouseDragged(MouseEvent event) {
		
		if(event.isConsumed() || event.isControlDown()) return;
		
		Point2D point = new Point2D(event.getSceneX(), event.getSceneY());
		
		double newX = dragContext.translateAnchorX + (point.getX() - dragContext.mouseAnchorX);
		double newY = dragContext.translateAnchorY + (point.getY() - dragContext.mouseAnchorY);

		pane.setTranslateX(newX);
		pane.setTranslateY(newY);

		performGesture(GestureType.MOUSE_DRAGGED, event, newX, newY);			
	}

	@Override
	protected void performScrolling(ScrollEvent event) {

		Point2D point = new Point2D(event.getSceneX(), event.getSceneY());
		
		double delta = 1.02;

		double scale = pane.getScale(); 
		
		double oldScale = scale;

		if (event.getDeltaY() < 0){
			scale /= delta;
		}else{
			scale *= delta;
		}
		
		scale = clamp(scale, MIN_SCALE, MAX_SCALE);

		double f = (scale / oldScale) - 1;

		double dx = (point.getX() - (pane.getBoundsInParent().getWidth() / 2 + pane.getBoundsInParent().getMinX()));
		double dy = (point.getY() - (pane.getBoundsInParent().getHeight() / 2 + pane.getBoundsInParent().getMinY()));

		pane.setScale(scale);

		pane.setPivot(f * dx, f * dy);

		event.consume();
		
		performGesture(GestureType.MOUSE_SCROLLING,event, point.getX(), point.getY());
	}	
	
	@SuppressWarnings("unused")
	private boolean outSideParentBounds(Pane source){
		
		if(outSideHorizontalBounds(source) || outSideVerticalBounds(source)){
			return true;
		}
		return false;
	}
	
	private boolean outSideHorizontalBounds(Pane source){
		
		double minX = source.getBoundsInParent().getMinX();
		
		double maxX = source.getBoundsInParent().getMaxX();
			
		if(minX > -5 ){
			return true;
		}
		
		if(maxX  < source.getPrefWidth() - 10){
			return true;
		}
		
		return false;
	}
	
	private boolean outSideVerticalBounds(Pane source){
		
		double minY = source.getBoundsInParent().getMinY();
		 
		double maxY = source.getBoundsInParent().getMaxY();		

		if(minY > -5 ){
			return true;
		}
		
		if(maxY < source.getPrefHeight() - 10){
			return true;
		}
		
		return false;
	}

	
	public static double clamp(double value, double min, double max) {

		if (Double.compare(value, min) < 0)
			return min;

		if (Double.compare(value, max) > 0)
			return max;

		return value;
	}
}