package com.eudycontreras.components.gestures;

import com.eudycontreras.components.handlers.FXJoint;
import com.eudycontreras.editor.application.FXEditor;
import com.eudycontreras.editor.handlers.FXGestureHandler;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;


public class FXFleshGestureHandler extends FXGestureHandler {

	private FXJoint joint;
	
	public FXFleshGestureHandler(FXEditor editor, FXJoint joint) {
		super(editor);
		this.joint = joint;
		this.joint.getJoint().getNode().setOnMouseEntered(getOnMouseEnteredEventHandler());
		this.joint.getJoint().getNode().setOnMouseExited(getOnMouseExitedEventHandler());
		this.joint.getJoint().getNode().setOnMousePressed(getOnMousePressedEventHandler());
		this.joint.getJoint().getNode().setOnMouseReleased(getOnMouseReleasedEventHandler());
		this.joint.getJoint().getNode().setOnMouseDragged(getOnMouseDraggedEventHandler());
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
	protected void performMousePressed(MouseEvent event) {
		performGesture(GestureType.MOUSE_PRESSED,event, NaN, NaN);
	}

	@Override
	protected void performMouseReleased(MouseEvent event) {		
		
		dragContext.mouseAnchorX = event.getSceneX();
		dragContext.mouseAnchorY = event.getSceneY();

		dragContext.translateAnchorX = joint.getJoint().getCenterX();
		dragContext.translateAnchorY = joint.getJoint().getCenterY();

		performGesture(GestureType.MOUSE_RELEASED,event, NaN, NaN);
	}

	@Override
	protected void performMouseDragged(MouseEvent event) {
		
		double scale = editor.getEditingWindow().getScale();

		double x = dragContext.translateAnchorX + ((event.getSceneX() - dragContext.mouseAnchorX) / scale);
		double y = dragContext.translateAnchorY + ((event.getSceneY() - dragContext.mouseAnchorY) / scale);

		performGesture(GestureType.MOUSE_DRAGGED,event, x, y);
			
	}

	@Override
	protected void performMouseDraggStarted(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void performMouseDraggEnded(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void performScrolling(ScrollEvent event) {
		// TODO Auto-generated method stub
		
	}	
}