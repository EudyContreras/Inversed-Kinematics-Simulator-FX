package com.eudycontreras.components.handlers;

import java.util.ArrayList;
import java.util.List;

import com.eudycontreras.Editor.FXEditor;
import com.eudycontreras.components.models.Bone;
import com.eudycontreras.components.visuals.FXJointView;
import com.eudycontreras.components.visuals.FXJointViewType;
import com.eudycontreras.components.visuals.IFXJointView;
import com.eudycontreras.models.Point;
import com.eudycontreras.observers.FXObserver;
import com.eudycontreras.observers.FXSubject;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.Transition;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class FXJoint implements FXSubject<FXJoint> {

	private int jointId;

	private boolean fixed = false;
	private boolean locked = false;
	private boolean selected = false;
	private boolean resizing = false;
	private boolean readyMerge = false;
	private boolean isMaster = false;

	private IFXJointView pivot;

	private Color colorNormal = Color.rgb(0, 150, 255);
	private Color colorSelected = Color.rgb(255, 190, 0);
	private Color colorLocked = Color.WHITE;

	private Point point;

	private FXBone parent;
	private FXEditor editor;
	
	private Bone currentBone;
	private Bone parentBone;

	private List<FXObserver<FXJoint>> observers = new ArrayList<FXObserver<FXJoint>>();
	
	public FXJoint(FXEditor editor, IFXJointView jointView, Bone current, Bone parent, int jointId) {
		this.editor = editor;
		this.jointId = jointId;
		this.pivot = jointView;
		this.currentBone = current;
		this.parentBone = parent;
		this.colorNormal = jointView.getMainColor();
		
		addEventHandling();
	}

	public IFXJointView getJoint() {
		return pivot;
	}

	public int getJointId() {
		return jointId;
	}
	
	public Bone getCurrentBone(){
		return currentBone;
	}
	
	public void setCurrentBone(Bone bone){
		this.currentBone = bone;
		this.parentBone = bone.getParent();
	}
	
	public void setLocked(double status){
		
	}

	
	public void setLeafJoint(double status){
		
	}

	public void setSelected(boolean selected) {
		if(!selected && readyMerge)return;
		
		this.selected = selected;

		ScaleTransition scale = new ScaleTransition(Duration.millis(250), pivot.getNode());
		FillTransition fill = new FillTransition(Duration.millis(250),pivot.getShape());
		StrokeTransition stroke = new StrokeTransition(Duration.millis(250),pivot.getShape());
		ParallelTransition parallel = new ParallelTransition(pivot.getNode(), fill, stroke, scale);

		if (selected) {
			
			if(!locked){
				scale.setFromX(1);
				scale.setFromY(1);
				scale.setToX(1.3);
				scale.setToY(1.3);
				fill.setFromValue(colorNormal.deriveColor(1, 1, 1, 0.6));
				fill.setToValue(colorSelected.deriveColor(1, 1, 1, 0.6));
				stroke.setFromValue(colorNormal);
				stroke.setToValue(colorSelected);
			} else {
				fill.setFromValue(((Color) pivot.getShape().getFill()));
				fill.setToValue(Color.TRANSPARENT);
				stroke.setFromValue(((Color) pivot.getShape().getStroke()));
				stroke.setToValue(colorSelected);
			}

			editor.setJointAdjust(this);
		} else {
			if(!locked){
				scale.setFromX(1.3);
				scale.setFromY(1.3);
				scale.setToX(1);
				scale.setToY(1);
				fill.setToValue(colorNormal.deriveColor(1, 1, 1, 0.6));
				fill.setFromValue(colorSelected.deriveColor(1, 1, 1, 0.6));
				stroke.setToValue(colorNormal);
				stroke.setFromValue(colorSelected);
			
			}else{
				fill.setFromValue(((Color) pivot.getShape().getFill()));
				fill.setToValue(Color.TRANSPARENT);
				
				stroke.setFromValue(((Color) pivot.getShape().getStroke()));
				stroke.setToValue(Color.WHITE);
			}
		}
		parallel.play();
	}
	
	public boolean isSelected() {
		return selected;
	}

	public boolean isMergeReady() {
		return readyMerge;
	}
	
	public void secureChildren(boolean state){
		locked = state;
		
		FillTransition fill = new FillTransition(Duration.millis(250), pivot.getShape());
		StrokeTransition stroke = new StrokeTransition(Duration.millis(250), pivot.getShape());
		ScaleTransition scale = new ScaleTransition(Duration.millis(250), pivot.getNode());
		ParallelTransition parallel = new ParallelTransition(pivot.getNode(),scale, fill, stroke);
		
		if(locked){
			fill.setFromValue(((Color) pivot.getShape().getFill()));
			fill.setToValue(Color.TRANSPARENT);
			
			stroke.setFromValue(((Color) pivot.getShape().getStroke()));
			stroke.setToValue(Color.WHITE);
			
			scale.setFromX(pivot.getNode().getScaleX());
			scale.setFromY(pivot.getNode().getScaleY());
			
			scale.setToX(1);
			scale.setToY(1);
			
			currentBone.setFixed(true, true);
			
			fixed = true;

		}else{
			fill.setFromValue(((Color) pivot.getShape().getFill()));
			fill.setToValue(colorNormal.deriveColor(1, 1, 1, 0.5));
			
			stroke.setFromValue(((Color) pivot.getShape().getStroke()));
			stroke.setToValue(colorNormal);			
			
			currentBone.setFixed(false, true);
			
			fixed = false;
		}
		
		parallel.play();
	}

	public void unponCollision(Runnable action) {

		for (FXJoint joint : editor.getJoints()) {

			if (joint.getJointId() == jointId)
				continue;

			if (checkBounds(joint)) {

				if (action != null) {
					action.run();
				}
			}
		}
	}

	public boolean checkBounds(FXJoint joint) {

		Bounds bounds1 = pivot.getNode().localToScene(pivot.getNode().getBoundsInLocal());

		Bounds bounds2 = joint.getJoint().getNode().localToScene(joint.getJoint().getNode().getBoundsInLocal());

		if (bounds2.contains(bounds1)) {

			return true;
		}
		return false;
	}


	private void addEventHandling() {

		pivot.getNode().setOnMousePressed(e -> {
			
			if(!e.isAltDown()){
				if(!selected){
					
					editor.unselectRest(getJointId());
					
					setSelected(true);
				}
			}

			pivot.getNode().getScene().setCursor(Cursor.CLOSED_HAND);
			
			setMouse(e.getSceneX(), e.getSceneY());			

			e.consume();
		});

		pivot.getNode().setOnMouseReleased(e -> {	
			
			if(resizing){
				finishResize();
			}
			
			pivot.getNode().getScene().setCursor(Cursor.OPEN_HAND);	
			
			setMouse(e.getSceneX(), e.getSceneY());			

			e.consume();

		});

		pivot.getNode().setOnMouseDragged(e -> {

			pivot.getNode().getScene().setCursor(Cursor.MOVE);
			
			if (e.isControlDown()) {
				
				performResize(e);
				
				resizing = true;
			}
			else if(e.isAltDown()){
				

			}
			else {

				moveAttachedBones(e);
			}
		});

		pivot.getNode().setOnMouseClicked(e -> {
			
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 2) {
					if(e.isAltDown()){
						if (!locked){
							secureChildren(true);
						}else{
							secureChildren(false);
						}
					}
					else if(e.isControlDown()){
						if(!editor.IsJointDialogOpen()){
							editor.openJointDialog(this);
							editor.setJointAdjust(this);
							setSelected(true);
						}
					}
				}
			}else{
				
			}
		});

		pivot.getNode().setOnDragDetected(e -> {
			if (e.isControlDown()) {
	
			}
		});

		pivot.getNode().setOnMouseEntered(e -> {

			if (!e.isPrimaryButtonDown()) {

				pivot.getNode().getScene().setCursor(Cursor.HAND);
			}
		});

		pivot.getNode().setOnMouseExited(e -> {

			if (!e.isPrimaryButtonDown()) {

				pivot.getNode().getScene().setCursor(Cursor.DEFAULT);
			}
		});
	}

	private void moveAttachedBones(MouseEvent e) {
		if(parentBone != null){
			
			Point2D point = parentBone.getSkeleton().sceneToLocal(e.getSceneX(), e.getSceneY());
			
			if(!currentBone.isFixed()){
				parentBone.moveEndpoint(point);
			}
		}else{
			Point2D point = currentBone.getSkeleton().sceneToLocal(e.getSceneX(), e.getSceneY());
			
			currentBone.moveStartPoint(point);
			
			//unfixChildren(currentBone);
		}
	}
	
	
	private void unfixChildren(Bone bone){
		for(FXJoint joint : editor.getJoints()){
			
			if(joint.getCurrentBone().hasAncestor(bone)){
				
				if(joint.isFixed()){
					joint.secureChildren(false);
				}
			}
		}
	}

	private void performResize(MouseEvent e) {
		if(parentBone == null) return;
		
		Point2D point = parentBone.getSkeleton().sceneToLocal(e.getSceneX(), e.getSceneY());
		
		Point2D start = parentBone.getSkeleton().getParent().localToScene(parentBone.getStartPoint());
		
		double angle = editor.getAngle(start, point);

		double distance = start.distance(point);
		
		parentBone.resize(angle, distance, point);

		currentBone.setLocked(true, true);
		
		if(parent != null){
			parent.computeLength(distance,pivot.getOuterRadius());
		}
		
		resizing = true;
	}

	private void finishResize(){
		if(parentBone == null) return;
		
		resizing = false;
		
		parentBone.finishResize();
		
		currentBone.setLocked(false, true);
	}
	
	private void setMouse(double x, double y) {
		this.point = new Point(x, y);
	}

	private double getMouseX() {
		return point.getX();
	}

	private double getMouseY() {
		return point.getY();
	}

	public void setParentBone(FXBone bone) {
		this.parent = bone;
	}

	public FXBone getParentBone() {
		return parent;
	}
	
	public boolean isFixed(){
		return fixed;
	}

	public double getAngle() {
		return currentBone.getAngle();
	}

	public void setAngle(double angle) {
		this.currentBone.setAngle(angle);
	}

	public double getMaxAngle() {
		return currentBone.getMaxAngle();
	}

	public void setMaxAngle(double maxAngle) {
		this.currentBone.setMaxAngle(maxAngle);
		
		if(currentBone.getAngle() > currentBone.getMaxAngle()){
			this.currentBone.setAngle(currentBone.getMaxAngle());
		}
	}

	public double getMinAngle() {
		return currentBone.getMinAngle();
	}

	public void setMinAngle(double minAngle) {	
		this.currentBone.setMinAngle(minAngle);
		
		if(currentBone.getAngle() < currentBone.getMinAngle()){
			this.currentBone.setAngle(currentBone.getMinAngle());
		}
	}

	public boolean isMaster() {
		return isMaster;
	}

	@Override
	public void register(FXObserver<FXJoint> observer) {
		observers.add(observer);
	}

	@Override
	public void unregister(FXObserver<FXJoint> observer) {
		observers.remove(observer);
	}

	@Override
	public FXJoint getUpdate(FXObserver<FXJoint> observer) {
		return this;
	}

	@Override
	public void notifyObservers() {
		observers.stream().forEach(o -> o.notify(this));
		
	}

	private ChangeListener<? super  Number> AngleChangeListener = (osv, oldValue, newValue)->{
		setAngle((double)newValue);
	};
	
	private ChangeListener<? super  Number> MinAngleChangeListener = (osv, oldValue, newValue)->{
		setMinAngle((double)newValue);
	};
	
	private ChangeListener<? super  Number> MaxAngleChangeListener = (osv, oldValue, newValue)->{
		setMaxAngle((double)newValue);
	};
	
	public ChangeListener<? super  Number> getAngleChangeListener() {
		return AngleChangeListener;
	}

	public ChangeListener<? super  Number> getMinAngleChangeListener() {
		return MinAngleChangeListener;
	}
	
	public ChangeListener<? super  Number> getMaxAngleChangeListener() {
		return MaxAngleChangeListener;
	}

}
