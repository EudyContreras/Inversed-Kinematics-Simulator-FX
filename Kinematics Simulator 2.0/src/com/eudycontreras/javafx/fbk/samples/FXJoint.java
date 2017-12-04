package com.eudycontreras.javafx.fbk.samples;

import java.util.ArrayList;
import java.util.List;

import com.eudycontreras.components.assist.FXRotateAssist;
import com.eudycontreras.components.visuals.IFXJointView;
import com.eudycontreras.editor.application.FXPaintResources;
import com.eudycontreras.javafx.fbk.listeners.FBKSegmentEventListener;
import com.eudycontreras.javafx.fbk.models.FBKSegment;
import com.eudycontreras.javafx.fbk.models.FBKSegment.FBKConstraintPivot;
import com.eudycontreras.javafx.fbk.models.FBKSegment.FBKinematicsType;
import com.eudycontreras.javafx.fbk.models.FBKVector;
import com.eudycontreras.observers.FXObserver;
import com.eudycontreras.observers.FXSubject;

import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class FXJoint implements FXSubject<FXJoint> {

	private boolean leafJoint = false;
	private boolean selected = false;
	private boolean chainingReady = false;
	private boolean resizingSegment = false;
	private boolean applyingMinAngle = false;
	private boolean applyingMaxAngle = false;
	
	private boolean showingJointConstraintAssist = false;
	
	private IFXJointView jointView;

	private Color colorNormal = FXPaintResources.ACCENT_COLOR_LIGHT;
	private Color colorLocked = Color.rgb(190, 190, 190, 1);
	private Color colorConstrained = Color.rgb(245, 170, 0);

	private ScaleTransition scale;
	private FillTransition stroke;
	
	private FXBone fxBone;
	private FXArmatureManager armature;
	
	private FBKVector point;
	
	private FBKSegment segment;
	
	private FXRotateAssist assistMin = new FXRotateAssist(0,60);
	private FXRotateAssist assistMax = new FXRotateAssist(0,60);
	
	private List<FXObserver<FXJoint>> observers = new ArrayList<FXObserver<FXJoint>>();
	
	public FXJoint(FXArmatureManager editor, FBKSegment current,IFXJointView jointView) {
		this(editor, current, null, jointView);
	}
	
	public FXJoint(FXArmatureManager editor, FBKSegment current, FXBone bone, IFXJointView jointView) {
		this.fxBone = bone;
		this.armature = editor;
		this.jointView = jointView;
		this.segment = current;
		this.armature.unselectAll();
		this.fxBone.setJoint(this);
		this.setSelected(true);
		this.setUpView();
		this.setUpHelpers();
		this.addEventHandling();
		this.addListener();
	}
	
	private void setUpView(){
		jointView.setCenterRadius(!segment.hasAncestor() ? 8 : 6);
		jointView.setCenterColor(Color.TRANSPARENT);
		jointView.setMainColor(colorNormal);
	}
	
	private void setUpHelpers(){
		segment.getContent().add(0,assistMin.getNode());
		segment.getContent().add(0,assistMax.getNode());
		
		assistMin.getNode().setScaleX(0);
		assistMin.getNode().setScaleY(0);
		
		assistMax.getNode().setScaleX(0);
		assistMax.getNode().setScaleY(0);
		
		assistMin.getNode().setOnMouseEntered(e -> {});
		assistMin.getNode().setOnMousePressed(e -> {});
		assistMin.getNode().setOnMouseReleased(e -> {});
		assistMin.getNode().setOnMouseClicked(e -> {});
		
		assistMin.getNode().setOnMouseEntered(e -> {});
		assistMin.getNode().setOnMousePressed(e -> {});
		assistMin.getNode().setOnMouseReleased(e -> {});
		assistMin.getNode().setOnMouseClicked(e -> {});
		
		assistMin.getShape().addEventFilter(MouseEvent.MOUSE_ENTERED, e ->{
			assistMin.getNode().toFront();
			
			e.consume();
		});
		
		assistMax.getShape().addEventFilter(MouseEvent.MOUSE_ENTERED, e ->{
			assistMax.getNode().toFront();
			
			e.consume();
		});
		
		assistMax.setFill(colorNormal);
	}
	
	public void setLeafJoint(boolean leafJoint){
		this.leafJoint = leafJoint;		
		if(leafJoint){
			segment.setLength(0);
			jointView.setRadius(8);
			jointView.setCenterRadius(6);
			jointView.setMainColor(colorNormal);
			jointView.setCenterColor(colorNormal.deriveColor(1, 1, 1, 0.45));
		}else{
			segment.setLength(0);
			jointView.setRadius(10);
			jointView.setCenterRadius(10);
			jointView.setMainColor(colorNormal);
			jointView.setCenterColor(Color.TRANSPARENT);
		}		
	}
	
	public void addListener(){
		segment.setEventListener(new FBKSegmentEventListener(){
		
			@Override
			public void onKinematicsTypeChanged(FBKSegment segment, FBKinematicsType type) {
				if(type == FBKinematicsType.FORWARD){
					
				}else{
					
				}		
			}

			@Override
			public void onSegmentConstrained(FBKSegment segment) {
				if(segment.getKinematicsType() != FBKinematicsType.FORWARD){
					jointView.setMainColor(colorConstrained);
					if(isLeafJoint()){
						jointView.setCenterColor(colorConstrained.deriveColor(1, 1, 1, 0.45));
					}
				}
			}

			@Override
			public void onSegmentUnconstrained(FBKSegment segment) {
				jointView.setMainColor(colorNormal);	
				if(isLeafJoint()){
					jointView.setCenterColor(colorNormal.deriveColor(1, 1, 1, 0.45));
				}
			}

			@Override
			public void onSegmentLocked(FBKSegment segment) {
				jointView.setMainColor(colorLocked);
				if(isLeafJoint()){
					jointView.setCenterColor(colorLocked.deriveColor(1, 1, 1, 0.45));
				}
			}

			@Override
			public void onSegmentUnlocked(FBKSegment segment) {
				jointView.setMainColor(colorNormal);	
				if(isLeafJoint()){
					jointView.setCenterColor(colorNormal.deriveColor(1, 1, 1, 0.45));
				}
			}

			@Override
			public void onEffectorStatusChanged(FBKSegment segment, boolean status) {
				if(status){
						armature.setCurrentJoint(FXJoint.this);
						jointView.setSelected(status);
				}else{
					if(segment.getLastState().isEffector()){
						jointView.setSelected(status);
					}
				}
			}

			@Override
			public void onSegmentLengthChange(FBKSegment segment, double length) {
				fxBone.computeLength(length, jointView.getOuterRadius());
				
				if(!segment.isAbsoluteAncestor() && !segment.isChildless()){
					double assistLength = length * 0.25;
					
					if(assistLength > (segment.getParent().getLength() * 0.85)){
						assistLength = (segment.getParent().getLength() * 0.85);
					}
					
					assistMin.setRadius(assistLength);
					assistMax.setRadius(assistLength);
				}
	
			}

			@Override
			public void onSegmentStatusChange(FBKSegment segment, FBKSegmentStatus status) {
				switch(status){
				case ABSOLUTE_PARENT:
					leafJoint = false;
					break;
				case ONLY_CHILD:
					jointView.setRadius(10);
					jointView.setCenterRadius(10);
					jointView.setMainColor(colorNormal);
					jointView.setCenterColor(Color.TRANSPARENT);
					leafJoint = false;
					break;
				case SIBLING:
					jointView.setRadius(10);
					jointView.setCenterRadius(10);
					jointView.setMainColor(colorNormal);
					jointView.setCenterColor(Color.TRANSPARENT);
					leafJoint = false;
					break;
				case TAIL:
					jointView.setRadius(7);
					jointView.setCenterRadius(4);			
					jointView.setMainColor(colorNormal);
					jointView.setCenterColor(colorNormal.deriveColor(1, 1, 1, 0.65));
					leafJoint = true;
					break;
				}
			}

			@Override
			public void onPositionUpdate(FBKSegment segment, FBKVector headPoint, FBKVector tailPoint, double angle, double rotation, double length) {

			}

			@Override
			public void onAngleChanged(FBKSegment segment, double angle) {
				assistMin.updateAngle(0, angle-180);
				assistMax.updateAngle(0, angle+180);
			}
		});
	}
	
	public void changeChainingStatus() {			
		if(scale != null){
			scale.stop();
			stroke.stop();
		}
		
		scale = new ScaleTransition(Duration.millis(450));
		stroke = new FillTransition(Duration.millis(450));
		
		if(!chainingReady){
			chainingReady = true;
			stroke.setShape(jointView.getShape());
			stroke.setFromValue(((Color)jointView.getShape().getFill()));
			stroke.setToValue(Color.TRANSPARENT);
			stroke.setCycleCount(Transition.INDEFINITE);
			stroke.setAutoReverse(true);
			stroke.play();
			
			scale.setNode(jointView.getNode());
			scale.setCycleCount(Transition.INDEFINITE);
			scale.setAutoReverse(true);
			scale.setFromX(1);
			scale.setFromY(1);
			scale.setToX(1.5);
			scale.setToY(1.5);
			scale.play();
		}else{
			chainingReady = false;	
			stroke.setShape(jointView.getShape());
			stroke.setFromValue(((Color)jointView.getShape().getFill()));
			stroke.setToValue(colorNormal.deriveColor(1, 1, 1, 0.6));
			stroke.play();
			
			scale.setNode(jointView.getNode());
			scale.setFromX(1.5);
			scale.setFromY(1.5);
			scale.setToX(1);
			scale.setToY(1);
			scale.play();
		}	
	}
	
	public FXJoint checkCollisions(){
		for (FXJoint joint : armature.getJoints()) {

			if (joint.getSegment().getSegmentId() == getSegment().getSegmentId())
				continue;

			if (checkBounds(joint)) {
				
				return joint;
			}
		}
		
		return null;
	}

	public boolean checkBounds(FXJoint joint) {
		
		Bounds bounds1 = jointView.getNode().localToScene(jointView.getNode().getBoundsInLocal());

		Bounds bounds2 = joint.getJointView().getNode().localToScene(joint.getJointView().getNode().getBoundsInLocal());

		if (bounds2.contains(bounds1)) {

			return true;
		}
		return false;
	}


	private void addEventHandling() {

		jointView.getNode().setOnMousePressed(e -> {
		
			jointView.getNode().getScene().setCursor(Cursor.CLOSED_HAND);	
			
			applySelectEffeect();
			
			e.consume();
		});

		jointView.getNode().setOnMouseReleased(e -> {	
			
			applyUnselectEffect();
			
			if(resizingSegment){
				finishResize();
			}else{
				segment.setEffector();
				
				FXJoint collision = checkCollisions();
				
				if(collision != null){
					
					if(collision.isChainingReady()){
						
						performChaining(collision);
					}
				}
			}
			
			jointView.getNode().getScene().setCursor(Cursor.OPEN_HAND);	
			
			setMouseCoordinates(e.getSceneX(), e.getSceneY());			

			e.consume();

		});

		jointView.getNode().setOnMouseDragged(e -> {

			jointView.getNode().getScene().setCursor(Cursor.MOVE);
			
			if (e.isControlDown()) {
				
				performResize(e);
				
				resizingSegment = true;
			}
			else if(e.isAltDown()){
				

			}
			else {
				moveSegment(e);
			}
			
			e.consume();
		});

		jointView.getNode().setOnMouseClicked(e -> {
			
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 2) {
					if(e.isAltDown()){
						changeChainingStatus();
					}
					else if(e.isControlDown()){
						changeConstraintStatus();
					}
					else if(e.isShiftDown()){
						
					}
					else {
						changeLockStatus();
					}
				}
			}else{}
			
			e.consume();
		});

		jointView.getNode().setOnDragDetected(e -> {
			applyUnselectEffect();
			
			if (e.isControlDown()) {
	
			}
		});

		jointView.getNode().setOnMouseEntered(e -> {

			if (!e.isPrimaryButtonDown()) {

				jointView.getNode().getScene().setCursor(Cursor.HAND);
			}
		});

		jointView.getNode().setOnMouseExited(e -> {

			if (!e.isPrimaryButtonDown()) {

				jointView.getNode().getScene().setCursor(Cursor.DEFAULT);
			}
		});
		
		assistMin.getShape().setOnMousePressed(e -> {e.consume();});
		
		assistMin.getShape().setOnMouseReleased(e -> {e.consume();});
		
		assistMax.getShape().setOnMousePressed(e -> {e.consume();});
		
		assistMax.getShape().setOnMouseReleased(e -> {e.consume();});
		
		assistMin.getShape().setOnMouseClicked(e -> {		
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 2) {				
					applyMinAngle();
				}
			}else{}
			
			e.consume();
		});
		
		assistMax.getShape().setOnMouseClicked(e -> {		
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 2) {				
					applyMaxAngle();
				}
			}else{}
			
			e.consume();
		});
	}

	private void applyUnselectEffect() {
		if(segment.isLocked()){
			jointView.setStroke(colorLocked);
		}else if(segment.isConstrained()){
			jointView.setStroke(colorConstrained);
		}else{
			jointView.setStroke(colorNormal);
		}
	}

	private void applySelectEffeect() {
		jointView.setStroke(Color.WHITE);
	}

	public void showAngleConstraintAssist() {
		if(segment.isAbsoluteAncestor() || segment.isChildless()){
			return;
		}
		
		ScaleTransition scaleMin = new ScaleTransition();
		scaleMin.setInterpolator(Interpolator.EASE_OUT);
		scaleMin.setNode(assistMin.getNode());
		scaleMin.setDuration(Duration.millis(200));
		
		ScaleTransition scaleMax = new ScaleTransition();
		scaleMax.setInterpolator(Interpolator.EASE_OUT);
		scaleMax.setNode(assistMax.getNode());
		scaleMax.setDuration(Duration.millis(200));
		
		if(showingJointConstraintAssist){
			
			scaleMin.setFromX(1);
			scaleMin.setFromY(1);
			scaleMin.setToX(0);
			scaleMin.setToY(0);
			scaleMin.setOnFinished(e -> {
				
			});
			
			scaleMax.setFromX(1);
			scaleMax.setFromY(1);
			scaleMax.setToX(0);
			scaleMax.setToY(0);
			scaleMax.setOnFinished(e -> {

			});
			
			showingJointConstraintAssist = false;
		}else{
			
			assistMin.updateAngle(0, segment.getAngle() - 180);

			scaleMin.setFromX(0);
			scaleMin.setFromY(0);
			scaleMin.setToX(1);
			scaleMin.setToY(1);
			scaleMin.setOnFinished(e -> {
				
			});
			
			assistMax.updateAngle(0, segment.getAngle() + 180);
			
			scaleMax.setFromX(0);
			scaleMax.setFromY(0);
			scaleMax.setToX(1);
			scaleMax.setToY(1);
			scaleMax.setOnFinished(e -> {

			});
			
			showingJointConstraintAssist = true;
		}
		
		scaleMin.play();
		scaleMax.play();
	}

	private void applyMinAngle() {
		if(!applyingMinAngle){

			applyingMinAngle = true;
		}else{
			
			applyingMinAngle = false;
		}
		
		segment.setMinAngle(segment.getAngle());
		
		//assistMin.updateAngle(0, 360);
		
		ScaleTransition scale = new ScaleTransition();
		scale.setInterpolator(Interpolator.EASE_IN);
		scale.setNode(assistMin.getNode());
		scale.setDuration(Duration.millis(200));
		scale.setFromX(1);
		scale.setFromY(1);
		scale.setToX(0);
		scale.setToY(0);
		scale.play();
	}

	private void applyMaxAngle() {
		if(!applyingMaxAngle){

			applyingMaxAngle = true;
		}else{
			
			applyingMaxAngle = false;
		}
		
		segment.setMaxAngle(segment.getAngle());
		
		//assistMax.updateAngle(0, 360);
		
		ScaleTransition scale = new ScaleTransition();
		scale.setInterpolator(Interpolator.EASE_IN);
		scale.setNode(assistMax.getNode());
		scale.setDuration(Duration.millis(200));
		scale.setFromX(1);
		scale.setFromY(1);
		scale.setToX(0);
		scale.setToY(0);
		scale.play();
	}

	private void performChaining(FXJoint collision) {
		System.out.println("MERGING");
		
		armature.appendChain(collision, this);
	}

	private void changeLockStatus() {
		if(isLeafJoint()){
			return;
		}
		if (segment.isLocked()) {
			segment.setLocked(false);
		} else {
			segment.setLocked(true);
		}
	}

	private void changeConstraintStatus() {
		if (segment.isConstrained()) {
			segment.setConstrained(false, FBKConstraintPivot.NONE);						
		} else {
			segment.setConstrained(true, FBKConstraintPivot.HEAD);
		}
	}
	
	private void moveSegment(MouseEvent e) {
		
		Point2D point = segment.getSkeleton().sceneToLocal(e.getSceneX(), e.getSceneY());
		
		segment.moveHead(FBKVector.toVector(point));					
	}
	

	private void performResize(MouseEvent e) {
	
		Point2D point = segment.getSkeleton().sceneToLocal(e.getSceneX(), e.getSceneY());
	
		if(segment.hasAncestor()){		
			resizeChildSegment(point);
		}else{		
			resizeAbsoluteParent(point);
		}
		
	}

	private void resizeAbsoluteParent(Point2D point) {
		Point2D start = segment.getCurrentTail().toPoint2D();

		double distance = start.distance(point);
		
		if(!resizingSegment){		
			
			segment.setConstrained(true, FBKConstraintPivot.TAIL);
			
			for(FBKSegment child : segment.getChildren()){
				child.setLocked(true, true);
			}
			
			resizingSegment = true;
		}
		
		segment.resizeFromHead(distance, FBKVector.toVector(point));
	}


	private void resizeChildSegment(Point2D point) {
		Point2D start = segment.getParent().getCurrentHead().toPoint2D();

		double distance = start.distance(point);
		
		if(!resizingSegment){
			segment.setConstrained(false);
			for(FBKSegment child : segment.getParent().getChildren()){
				child.setLocked(true, true);
			}
			resizingSegment = true;
		}
		
		segment.getParent().resizeFromTail(distance, FBKVector.toVector(point));
	}

	private void finishResize(){
		if (segment.hasAncestor() && !segment.isChildless()) {
			for (FBKSegment child : segment.getParent().getChildren()) {
				child.setLocked(false, true);
			}
		} else if (segment.isChildless()) {
			segment.setConstrained(false);
			segment.setLocked(false, true);
		} else {
			segment.setConstrained(false, FBKConstraintPivot.TAIL);

			for (FBKSegment child : segment.getChildren()) {
				child.setLocked(false, true);
			}
		}
		resizingSegment = false;

		if (segment.getLastState().isConstrained()) {
			segment.setConstrained(true);
		}
	}
	
	public void remove() {
		if(segment.hasAncestor()){
			if(segment.hasChildren()){
				segment.clearDescendants();		
			}
		}else{
			if(segment.hasChildren()){
				segment.clearDescendants();		
			}else{
				removeAnimate(()->{
					segment.getContent().clear();
					segment.getSkeleton().getSegmentsWritable().remove(segment);
				});
			}
			
			armature.setCurrentJoint(null);		
		}
	}
	
	public void removeAnimate(Runnable script){
		ScaleTransition scale = new ScaleTransition();
		scale.setInterpolator(Interpolator.EASE_IN);
		scale.setNode(jointView.getNode());
		scale.setDuration(Duration.millis(150));
		scale.setFromX(1);
		scale.setFromY(1);
		scale.setToX(0);
		scale.setToY(0);
		scale.play();
		scale.setOnFinished(e -> {
			if(script != null){
				script.run();
			}
		});
	}


	public boolean isShowJoint() {
		return this.jointView.getNode().isVisible();
	}

	public void setShowJoint(boolean showJoint) {
		this.jointView.getNode().setVisible(showJoint);
	}
	
	public void setOpacity(double opacity){
		this.jointView.getNode().setOpacity(opacity);
	}

	public double getOpacity(){
		return this.jointView.getNode().getOpacity();
	}

	public FXBone getFXBone() {
		return fxBone;
	}

	public void setFXBone(FXBone fxBone) {
		this.fxBone = fxBone;
		this.fxBone.setJoint(this);
	}

	public IFXJointView getJointView() {
		return jointView;
	}

	public FBKSegment getSegment(){
		return segment;
	}
	
	public void setSegment(FBKSegment segment){
		this.segment = segment;
	}

	public void setSelected(boolean selected) {
		segment.setEffector();
	}
	
	public boolean isLeafJoint(){
		return leafJoint;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public boolean isChainingReady() {
		return chainingReady;
	}

	private void setMouseCoordinates(double x, double y) {
		this.point = new FBKVector(x, y);
	}
	
	public FBKVector getLastMouseCoordinates(){
		return point;
	}

	public boolean isConstrained(){
		return segment.isConstrained();
	}

	public double getAngle() {
		return segment.getAngle();
	}

	public void setAngle(double angle) {
		this.segment.setAngle(angle);
	}

	public double getMaxAngle() {
		return segment.getMaxAngle();
	}

	public void setMaxAngle(double maxAngle) {
		this.segment.setMaxAngle(maxAngle);
		
		if(segment.getAngle() > segment.getMaxAngle()){
			this.segment.setAngle(segment.getMaxAngle());
		}
	}

	public double getMinAngle() {
		return segment.getMinAngle();
	}

	public void setMinAngle(double minAngle) {	
		this.segment.setMinAngle(minAngle);
		
		if(segment.getAngle() < segment.getMinAngle()){
			this.segment.setAngle(segment.getMinAngle());
		}
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
