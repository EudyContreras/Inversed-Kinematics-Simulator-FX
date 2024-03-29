
package com.eudycontreras.editor.application;

import java.util.ArrayList;
import java.util.List;

import com.eudycontreras.components.handlers.FXBone;
import com.eudycontreras.components.handlers.FXJoint;
import com.eudycontreras.components.visuals.FXBoneView;
import com.eudycontreras.components.visuals.FXBoneViewType;
import com.eudycontreras.components.visuals.FXJointView;
import com.eudycontreras.components.visuals.FXJointViewType;
import com.eudycontreras.components.visuals.IFXBoneView;
import com.eudycontreras.components.visuals.IFXJointView;
import com.eudycontreras.editor.gestures.FXEditorGestures;
import com.eudycontreras.javafx.fbk.listeners.FBKSegmentEventListener.FBKSegmentStatus;
import com.eudycontreras.javafx.fbk.models.FBKSegment;
import com.eudycontreras.javafx.fbk.models.FBKSegment.FBKConstraintPivot;
import com.eudycontreras.javafx.fbk.models.FBKSegmentChain;
import com.eudycontreras.javafx.fbk.models.FBKVector;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class FXArmatureManager {

	
	private boolean wasDragging = false;

	private boolean openingTools = false;
	private boolean closingTools = false;
	private boolean toolsClosed = false;
	private boolean toolsOpened = true;
	
	private double jointRadius = 8;
	private double boneLenght = 100;
	private double boneDensity = 16;
	
	private Stage stage;
	private Scene scene;
	
	private FXEditor editor;
	private FXJoint currentJoint = null;
	
	private FXEditorGestures gestureHandler;

	private final FBKSegmentChain skeleton = new FBKSegmentChain();
	
	private final ArrayList<FXJoint> joints = new ArrayList<>();

	
	public FXArmatureManager(FXEditor editor, Stage stage, Scene scene, double width, double height, FXEditorGestures gestureHandler){
		this.stage = stage;
		this.scene = scene;
		this.editor = editor;
		this.gestureHandler = gestureHandler;
		this.skeleton.setTranslateX(width / 2);
		this.skeleton.setTranslateY(height / 2);
		this.addEventHandling();
	}
	
	private void addEventHandling() {
		this.gestureHandler.addMousePressedAction((e, x, y)->{
			
			wasDragging = false;
		});
		
		this.gestureHandler.addMouseReleasedAction((e, x, y)->{
			
			Point2D point = skeleton.sceneToLocal(e.getSceneX(), e.getSceneY());
			
			if(e.isControlDown()){

				appendJoint(getCurrentJoint(), FBKVector.toVector(point));
				
			}else if(e.isShiftDown()){

				appendJoint(getCurrentJoint(), FBKVector.toVector(point), false);
				
			}else{
				if(!wasDragging){
					
					addJoint(FBKVector.toVector(point));
				}
			}
		});
		
		this.gestureHandler.addMouseDraggedAction((e, x, y)->{

			wasDragging = true;
		});
		
		this.gestureHandler.addMouseDraggEndedAction((e, x, y)->{
			
			wasDragging = true;
		});
		
	}

	public void reportKeyEvent(KeyEvent e) {
		switch (e.getCode()) {
		case D:
			if(e.isControlDown()){				
				if(currentJoint != null){
					currentJoint.remove();
					
				}
			}
			break;
		default:
			break;
		}
	}
	
	public FBKSegmentChain createArmature(double width, double height) {
		
		skeleton.setTranslateX(width / 2);
		skeleton.setTranslateY(height / 2);

		FBKSegment root = new FBKSegment(new FBKVector(-200,80),30);
		
		FBKSegment head1 = new FBKSegment(new FBKVector(-200,0),100);
		FBKSegment head2 = new FBKSegment(new FBKVector(-200,140),100);

		addGraphicalContent(skeleton, head1, getJoint(14),getBone(head1));
		addGraphicalContent(skeleton, head2, getJoint(14),getBone(head2));

		skeleton.setRoot(root);
		
		head1.setSkeleton(skeleton);
		head2.setSkeleton(skeleton);
		
		skeleton.setAbsoluteAncestor(head1);

		FBKSegment chain1 = createChain(skeleton, head1.getLength(), 4, FBKSegment.MAX_ANGLE, FBKSegment.MIN_ANGLE);
		FBKSegment chain2 = createChain(skeleton, head1.getLength(), 4, FBKSegment.MAX_ANGLE, FBKSegment.MIN_ANGLE);
		FBKSegment chain3 = createChain(skeleton, head2.getLength(), 3, FBKSegment.MAX_ANGLE, FBKSegment.MIN_ANGLE);
		
		head1.addChild(chain1);
		head1.addChild(chain2);
		head2.addChild(chain3);
	
		return skeleton;
	}
	
	public FBKSegmentChain getSkeleton(){
		return skeleton;
	}
	
	private final ChangeListener<Number> changeListener = (obs, oldVal, newVal)->{
//		System.out.println(newVal);
		editor.getSliderDemo().setMaxValue(newVal.doubleValue());
	};
	
	public void setCurrentJoint(FXJoint joint){
		if(currentJoint != null){
			//editor.getSliderDemo().getMinValueProperty().unbindBidirectional(currentJoint.getSegment().getMinAngleProperty());
//		//	editor.getSliderDemo().getMaxValueProperty().bind(currentJoint.getSegment().angleProperty());
//			if(currentJoint.getSegment().hasAncestor())
//			this.currentJoint.getSegment().getParent().angleProperty().removeListener(changeListener);;
			this.currentJoint = joint;
//			if(currentJoint.getSegment().hasAncestor())
//			this.currentJoint.getSegment().getParent().angleProperty().addListener(changeListener);
//			//editor.getSliderDemo().getMinValueProperty().bindBidirectional(currentJoint.getSegment().getMinAngleProperty());
//		//	editor.getSliderDemo().getMaxValueProperty().bind(currentJoint.getSegment().angleProperty());
		}else{
			this.currentJoint = joint;
//			if(currentJoint.getSegment().hasAncestor())
//			this.currentJoint.getSegment().getParent().angleProperty().addListener(changeListener);
//			//editor.getSliderDemo().getMinValueProperty().bindBidirectional(currentJoint.getSegment().getMinAngleProperty());
//			//editor.getSliderDemo().getMaxValueProperty().bind(currentJoint.getSegment().angleProperty());
		}
	}
	
	public FXJoint getCurrentJoint(){
		return currentJoint;
	}
	
	public void addJoint(FBKVector point){
		
		FBKSegment root = new FBKSegment(point,0);
		
		FXJoint joint = addGraphicalContent(skeleton, root, getJoint(jointRadius * 1.6),getBone(root));
		
		root.setEffector();
		root.setSkeleton(skeleton);
		
		setCurrentJoint(joint);
	}
	
	public void appendChain(FXJoint parentJoint, FXJoint child){
		if(parentJoint == null) {
			return;
		}

		if(parentJoint.getSegment().hasAncestor()){
			
			parentJoint.getSegment().getListener().onSegmentStatusChange(parentJoint.getSegment(), FBKSegmentStatus.ONLY_CHILD);		
			
			child.getSegment().moveHead(parentJoint.getSegment().getCurrentHead());
						
			Point2D start = parentJoint.getSegment().getSkeleton().localToScene(parentJoint.getSegment().getParent().getCurrentTail().toPoint2D());
			
			Point2D end = parentJoint.getSegment().getSkeleton().localToScene(child.getSegment().getCurrentTail().toPoint2D());
			
			double angle = getAngle(FBKVector.toVector(start),FBKVector.toVector(end));

			parentJoint.getSegment().getParent().addChild(child.getSegment());
			
			child.getSegment().moveHead(parentJoint.getSegment().getCurrentHead());
			
			FBKSegment.traverseDescendants(child.getSegment(), s-> {
				s.setLocked(true, false);
			});
			
			child.getSegment().applyRotationAngle(angle);
			
			FBKSegment.traverseDescendants(child.getSegment(), s-> {
				if(!s.getLastState().isLocked()){
					s.setLocked(false, false, false);
				}
			});
			
			child.setShowJoint(false);
			child.getSegment().computeFBK();
			
			parentJoint.changeChainingStatus();
			
//			if(FBKSegment.anySiblingsMeetCondition(parentJoint.getSegment(), c -> c.isConstrained())){
//				child.getSegment().setConstrained(true, FBKConstraintPivot.HEAD, true);
//			}
		}else{
			
//			parentJoint.getSegment().addChild(child.getSegment());
//			
//			parentJoint.changeChainingStatus();
//			
//			child.getSegment().moveHead(parentJoint.getSegment().getCurrentHead());
//			
//			child.setShowJoint(false);
		}
	}
	
	public void appendJoint(FXJoint parentJoint, FBKVector point){
		appendJoint(parentJoint, point, true);
	}
	
	public void appendJoint(FXJoint parentJoint, FBKVector point, boolean newEffector){
		
		if(parentJoint == null) {
			return;
		}
		
		final FBKSegment child = new FBKSegment(point, 0, 0, FBKSegment.MIN_ANGLE, FBKSegment.MAX_ANGLE);

		final double length = new FBKVector(point.getX(),point.getY()).distance(parentJoint.getSegment().getCurrentHead());
		
		if(parentJoint.getSegment().hasChildren()){
			
			if(parentJoint.getSegment().hasAncestor()){
				
				parentJoint.getSegment().getParent().addChild(child);
				child.moveHead(parentJoint.getSegment().getCurrentHead());
				child.setLength(length);
				child.moveTail(point);
				child.computeFBK();
				
				appendJoint(addGraphicalContent(skeleton, child, getJoint(jointRadius), getBone(child), false), child.getCurrentTail(), newEffector);
			}
			
		}else{
	
			parentJoint.getSegment().setLength(length);
			parentJoint.getSegment().moveTail(point);
			parentJoint.getSegment().addChild(child);
			
			FXJoint joint = addGraphicalContent(skeleton, child, getJoint(jointRadius), getBone(child));
			
			if(newEffector){
				setCurrentJoint(joint);
				child.setEffector();
			}	
		}
	}
	
	private void addChild(FXJoint parentJoint, FBKVector point, final FBKSegment child, final double length) {
		parentJoint.getSegment().addChild(child);
		child.moveHead(parentJoint.getSegment().getCurrentTail());
		child.setLength(length);
		child.moveTail(point);
		
		final FBKSegment descendant = new FBKSegment(point, 0, 0, FBKSegment.MIN_ANGLE, FBKSegment.MAX_ANGLE);
		
		child.addChild(descendant);
		
		addGraphicalContent(skeleton, child, getJoint(jointRadius), getBone(child), false);

		setCurrentJoint(addGraphicalContent(skeleton, descendant, getJoint(jointRadius), getBone(descendant)));
		
		descendant.setEffector();
	}

	private void addRootChild(FXJoint parentJoint, FBKVector point, final FBKSegment child, final double length) {
		parentJoint.getSegment().addChild(child);
		child.moveHead(parentJoint.getSegment().getCurrentTail());
		child.setLength(length);
		child.moveTail(point);
		
		final FBKSegment descendant = new FBKSegment(point, 0, 0, FBKSegment.MIN_ANGLE, FBKSegment.MAX_ANGLE);
		
		child.addChild(descendant);
		
		addGraphicalContent(skeleton, child, getJoint(jointRadius), getBone(child), false);

		setCurrentJoint(addGraphicalContent(skeleton, descendant, getJoint(jointRadius), getBone(descendant)));
		
		descendant.setEffector();
	}
	
	private FXJoint addGraphicalContent(FBKSegmentChain skeleton, FBKSegment segment, IFXJointView jointView, IFXBoneView boneView) {
		return addGraphicalContent(skeleton, segment, jointView, boneView, true);
	}

	private FXJoint addGraphicalContent(FBKSegmentChain skeleton, FBKSegment segment, IFXJointView jointView, IFXBoneView boneView, boolean showJoint) {

		FXBone bone = null;
		
		if(boneView != null){
			bone = new FXBone(this, segment, boneView);
		}
		
		FXJoint joint = null;
		
		if(jointView != null){
			joint = new FXJoint(this, segment, bone, jointView);joint.setShowJoint(showJoint);
		}
		
		joints.add(joint);
		
		if(bone != null){
			segment.getContent().add(bone.getBoneView().getShape());
		}
		
		if(joint != null){
			segment.getContent().add(joint.getJointView().getNode());
			
		}
		
		return joint;
	}

	private FBKSegment createChain(FBKSegmentChain skeleton, double length, int size, double maxAngle, double minAngle) {
		
		FBKSegment head = new FBKSegment(length);
	
		addGraphicalContent(skeleton, head, getJoint(jointRadius),getBone(head));

		FXJoint joint = null;
		
		for (int i = 0; i < size-1; i++) {

			final FBKSegment segment = new FBKSegment(length, 0, minAngle, maxAngle);
			
			joint = addGraphicalContent(skeleton, segment, getJoint(jointRadius), getBone(segment));

			head.addChild(segment);

			head = segment;
		}
		
		joint.setLeafJoint(true);
		
		return head.getAbsoluteAncestor();
	}
	
	private IFXJointView getJoint(double radius){
		return FXJointView.getFXJointView(FXJointViewType.TYPE_A, 0, 0, radius);		
	}
	
	private IFXJointView getRootJoint(double radius){
		return FXJointView.getFXJointView(FXJointViewType.TYPE_B, 0, 0, radius);		
	}
	
	private IFXBoneView getBone(FBKSegment segment){
		return getBone(segment, segment.getLength()/2 - jointRadius);
	}
	
	private IFXBoneView getBone(FBKSegment segment, double length) {
		double height = boneDensity + (length * 0.065d);
		return FXBoneView.getFXBoneView(FXBoneViewType.TYPE_A, jointRadius, 0, length, height);
	}

	public double getAngle(FBKVector start, FBKVector end) {

		double angle = Math.toDegrees(Math.atan2(end.getY() - start.getY(), end.getX() - start.getX()));

		return angle;
	}

	public static FBKVector getCoordinates(Bounds localBounds) {
		return new FBKVector(localBounds.getMinX(), localBounds.getMaxY());
	}

	public List<FXJoint> getJoints() {
		// TODO Auto-generated method stub
		return joints;
	}

	public double getJointRadius() {
		return jointRadius;
	}

	public void setJointRadius(double jointRadius) {
		this.jointRadius = jointRadius;
	}

	public double getBoneLenght() {
		return boneLenght;
	}

	public void setBoneLenght(double boneLenght) {
		this.boneLenght = boneLenght;
	}

	public double getBoneDensity() {
		return boneDensity;
	}

	public void setBoneDensity(double boneDensity) {
		this.boneDensity = boneDensity;
	}
	
	
}
