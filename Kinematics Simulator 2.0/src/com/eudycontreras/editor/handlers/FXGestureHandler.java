package com.eudycontreras.editor.handlers;

import java.util.ArrayList;
import java.util.List;

import com.eudycontreras.editor.application.FXEditor;
import com.eudycontreras.utilities.FXIterator;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public abstract class FXGestureHandler {
	
	protected static final double NaN = Double.NaN;

	protected FXEditor editor;
	
	protected DragContext dragContext = new DragContext();

	private List<GestureEvent<MouseEvent>> mousePressed = new ArrayList<>();
	private List<GestureEvent<MouseEvent>> mouseClicked = new ArrayList<>();
	private List<GestureEvent<MouseEvent>> mouseReleased = new ArrayList<>();
	private List<GestureEvent<MouseEvent>> mouseEntered = new ArrayList<>();
	private List<GestureEvent<MouseEvent>> mouseExited = new ArrayList<>();
	private List<GestureEvent<MouseEvent>> mouseDragged = new ArrayList<>();
	private List<GestureEvent<MouseEvent>> mouseDraggStarted = new ArrayList<>();
	private List<GestureEvent<MouseEvent>> mouseDraggEnded = new ArrayList<>();
	private List<GestureEvent<ScrollEvent>> mouseScrolling = new ArrayList<>();
	
	protected FXGestureHandler(FXEditor editor) {
		this.editor = editor;
	}

	public void addMousePressedAction(GestureEvent<MouseEvent> mousePressed) {
		this.mousePressed.add(mousePressed);
	}

	public void addMouseClickedAction(GestureEvent<MouseEvent> mouseClicked) {
		this.mouseClicked.add(mouseClicked);
	}
	
	public void addMouseReleasedAction(GestureEvent<MouseEvent> mouseReleased) {
		this.mouseReleased.add(mouseReleased);
	}

	public void addMouseEnteredAction(GestureEvent<MouseEvent> mouseEntered) {
		this.mouseEntered.add(mouseEntered);
	}

	public void addMouseExitedAction(GestureEvent<MouseEvent> mouseExited) {
		this.mouseExited.add(mouseExited);
	}

	public void addMouseDraggedAction(GestureEvent<MouseEvent> mouseDragged) {
		this.mouseDragged.add(mouseDragged);
	}
	
	public void addMouseDraggEndedAction(GestureEvent<MouseEvent> mouseDragged) {
		this.mouseDraggEnded.add(mouseDragged);
	}
	
	public void addMouseDraggStartedAction(GestureEvent<MouseEvent> mouseDragged) {
		this.mouseDraggStarted.add(mouseDragged);
	}

	public void addMouseScrollingAction(GestureEvent<ScrollEvent> mouseDragged) {
		this.mouseScrolling.add(mouseDragged);
	}
	
	protected EventHandler<MouseEvent> getOnMousePressedEventHandler() {
		return onMousePressedEventHandler;
	}

	protected EventHandler<MouseEvent> getOnMouseClickedEventHandler() {
		return onMouseClickedEventHandler;
	}
	
	protected EventHandler<MouseEvent> getOnMouseReleasedEventHandler() {
		return onMouseReleasedEventHandler;
	}
	
	protected EventHandler<MouseEvent> getOnMouseEnteredEventHandler() {
		return onMouseEnteredEventHandler;
	}
	
	protected EventHandler<MouseEvent> getOnMouseExitedEventHandler() {
		return onMouseExitedEventHandler;
	}
	
	protected EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
		return onMouseDraggedEventHandler;
	}

	protected EventHandler<MouseEvent> getOnMouseDraggStartedEventHandler() {
		return onMouseDraggStartedEventHandler;
	}
	
	protected EventHandler<MouseEvent> getOnMouseDraggEndedEventHandler() {
		return onMouseDraggEndedEventHandler;
	}
	
	protected EventHandler<ScrollEvent> getOnMouseScrolledEventHandler() {
		return onScrollEventHandler;
	}
	
	private EventHandler<MouseEvent> onMouseEnteredEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {
			performMouseEntered(event);
		}
	};
	
	private EventHandler<MouseEvent> onMouseExitedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {
			performMouseExited(event);
		}
	};
	
	private EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {
			performMouseExited(event);
		}
	};
	
	private EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {
			performMouseReleased(event);
		}
	};	
	
	private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {	
			performMousePressed(event);
		}
	};

	private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {			
			performMouseDragged(event);
		}
	};
		
	private EventHandler<MouseEvent> onMouseDraggEndedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {			
			performMouseDraggEnded(event);
		}
	};
	
	private EventHandler<MouseEvent> onMouseDraggStartedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {			
			performMouseDraggStarted(event);
		}
	};
	
	private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

		@Override
		public void handle(ScrollEvent event) {
			performScrolling(event);
		}
	};
	
	protected void performGesture(GestureType type, ScrollEvent e, double x, double y){
		switch(type){
		case MOUSE_SCROLLING:
			FXIterator.Iterate(mouseScrolling, (current, index)-> {
				current.performGesture(e, x, y);
			});
			break;
		default:
			break;
		}
	}
	
	protected void performGesture(GestureType type, MouseEvent e, double x, double y){
		switch(type){
		case MOUSE_PRESSED:
			for(GestureEvent<MouseEvent> current : mousePressed){
				current.performGesture(e, x, y);
			}
			break;
		case MOUSE_CLICKED:
			for(GestureEvent<MouseEvent> current : mouseClicked){
				current.performGesture(e, x, y);
			}
			break;
		case MOUSE_RELEASED:
			for(GestureEvent<MouseEvent> current : mouseReleased){
				current.performGesture(e, x, y);
			}
			break;
		case MOUSE_DRAGGED:
			for(GestureEvent<MouseEvent> current : mouseDragged){
				current.performGesture(e, x, y);
			}
			break;
		case MOUSE_ENTERED:
			for(GestureEvent<MouseEvent> current : mouseEntered){
				current.performGesture(e, x, y);
			}
			break;
		case MOUSE_EXITED:
			for(GestureEvent<MouseEvent> current : mouseExited){
				current.performGesture(e, x, y);
			}
		case MOUSE_DRAGG_STARTED:
			for(GestureEvent<MouseEvent> current : mouseDraggStarted){
				current.performGesture(e, x, y);
			}
			break;
		case MOUSE_DRAGG_ENDED:
			for(GestureEvent<MouseEvent> current : mouseDraggEnded){
				current.performGesture(e, x, y);
			}
			break;	
		default:
			break;
		}
	}

	protected abstract void performMouseEntered(MouseEvent event);

	protected abstract void performMouseExited(MouseEvent event);

	protected abstract void performMousePressed(MouseEvent event);
	
	protected abstract void performMouseClicked(MouseEvent event);

	protected abstract void performMouseReleased(MouseEvent event);

	protected abstract void performMouseDragged(MouseEvent event);

	protected abstract void performMouseDraggStarted(MouseEvent event);
	
	protected abstract void performMouseDraggEnded(MouseEvent event);
	
	protected abstract void performScrolling(ScrollEvent event);
	
	public class DragContext {

		public double mouseAnchorX;
		public double mouseAnchorY;

		public double translateAnchorX;
		public double translateAnchorY;

	}
	
	public interface GestureEvent<EventType> {

		public void performGesture(EventType e, double x, double y);
	}
	
	public enum GestureType{
		MOUSE_PRESSED, 
		MOUSE_CLICKED,
		MOUSE_RELEASED, 
		MOUSE_ENTERED, 
		MOUSE_EXITED, 
		MOUSE_DRAGGED,
		MOUSE_DRAGG_STARTED,
		MOUSE_DRAGG_ENDED,
		MOUSE_SCROLLING
	}
}