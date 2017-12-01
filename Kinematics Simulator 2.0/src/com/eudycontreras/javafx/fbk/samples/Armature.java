
package com.eudycontreras.javafx.fbk.samples;

import com.eudycontreras.components.models.Bone;
import com.eudycontreras.components.visuals.FXBoneView;
import com.eudycontreras.components.visuals.FXBoneViewType;
import com.eudycontreras.components.visuals.FXJointView;
import com.eudycontreras.components.visuals.FXJointViewType;
import com.eudycontreras.components.visuals.IFXBoneView;
import com.eudycontreras.components.visuals.IFXJointView;
import com.eudycontreras.editor.application.FXPaintResources;
import com.eudycontreras.javafx.fbk.listeners.FBKSegmentEventListener;
import com.eudycontreras.javafx.fbk.models.FBKSegment;
import com.eudycontreras.javafx.fbk.models.FBKSegment.FBKConstraintPivot;
import com.eudycontreras.javafx.fbk.models.FBKSegment.FBKinematicsType;
import com.eudycontreras.javafx.fbk.models.FBKSegmentChain;
import com.eudycontreras.javafx.fbk.models.FBKVector;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Armature {

	public FBKSegmentChain createArmature(Stage stage, Scene scene, double width, double height) {
		final FBKSegmentChain skeleton = new FBKSegmentChain();

		skeleton.setTranslateX(width / 2);
		skeleton.setTranslateY(height / 2);

		FBKSegment root = new FBKSegment(new FBKVector(-200,80),30);
		
		FBKSegment head1 = new FBKSegment(new FBKVector(-200,0),100);
		//FBKSegment head2 = new FBKSegment(new FBKVector(-200,140),100);

		FBKSegmentListener listener = new FBKSegmentListener();
		head1.setEventListener(listener);
		addContent(skeleton, head1, listener, true);
		//addContent(skeleton, head2);
		
		//createRoot(skeleton, root);

		skeleton.setRoot(root);
		
		head1.setSkeleton(skeleton);
		//head2.setSkeleton(skeleton);
		
		skeleton.setAbsoluteAncestor(head1);

		FBKSegment chain1 = createChain(skeleton, head1.getLength(), 4, FBKSegment.MAX_ANGLE, FBKSegment.MIN_ANGLE);
		FBKSegment chain2 = createChain(skeleton, head1.getLength(), 4, FBKSegment.MAX_ANGLE, FBKSegment.MIN_ANGLE);
		//FBKSegment chain3 = createChain(skeleton, head2.getLength(), 3, FBKSegment.MAX_ANGLE, FBKSegment.MIN_ANGLE);
		
		head1.addChild(chain1);
		head1.addChild(chain2);
		//head2.addChild(chain2);
		
		scene.setOnKeyPressed(e -> {
			if(e.isControlDown()){
				if(e.getCode() == KeyCode.ENTER){
					stage.setFullScreen(!stage.isFullScreen());
				}
			}
		});

		return skeleton;
	}

	private double lastX;
	private double lastY;
	
	private void createRoot(FBKSegmentChain skeleton, FBKSegment root) {
		IFXJointView joint = FXJointView.getFXJointView(FXJointViewType.TYPE_B, 0, 0, 10);
		joint.setCenterColor(Color.rgb(170, 170, 170,0.1));
		joint.setMainColor(Color.rgb(220, 170, 0,1));
		joint.setSectionDistance(2);
		joint.setCenterRadius(7);
		
//		joint.getNode().setOnMousePressed(e -> {
//			lastX = skeleton.getTranslateX();
//			lastY = skeleton.getTranslateY();
//		});
//		joint.getNode().setOnMouseDragged(e -> {
//			Point2D point = skeleton.sceneToLocal(e.getSceneX(), e.getSceneY());
//
//			skeleton.setTranslateX(point.getX() - lastX);
//			skeleton.setTranslateY(point.getY() - lastY);
//			
//			System.out.println(skeleton.getLayoutBounds().getWidth());
//			
//			e.consume();
//		});
		root.getContent().addAll(joint.getNode());
	}

	
	private void addContent(FBKSegmentChain skeleton, FBKSegment segment, FBKSegmentListener listener, boolean root) {

		IFXBoneView bone = FXBoneView.getFXBoneView(FXBoneViewType.TYPE_A, 10, 0, segment.getLength()/2 - 10, segment.getLength()/5);
		bone.setStrokeWidth(1);
		bone.setFill(Color.rgb(170, 170, 170,0.65));
		bone.setStroke(Color.rgb(170, 170, 170, 1));
		
		IFXJointView joint = FXJointView.getFXJointView(FXJointViewType.TYPE_A, 0, 0, root ? 14 : 10);
		joint.setCenterRadius(root ? 8 : 6);

		listener.setBoneView(bone);
		listener.setJointView(joint);
		
		joint.setCenterColor(Color.TRANSPARENT);
		joint.setMainColor(FXPaintResources.ACCENT_COLOR_LIGHT);

		bone.getShape().setOnMousePressed(e -> {
			bone.setFill(FXPaintResources.ACCENT_COLOR_LIGHT.deriveColor(1,1,1,0.5));
			bone.setStroke(FXPaintResources.ACCENT_COLOR_LIGHT);
			
			segment.setKinematicsType(FBKinematicsType.FORWARD);
			segment.setConstrained(true);
		});
		
		bone.getShape().setOnMouseReleased(e -> {
			bone.setFill(Color.rgb(170, 170, 170,0.5));
			bone.setStroke(Color.rgb(170, 170, 170, 1));
			
			segment.setKinematicsType(FBKinematicsType.INVERSED);
			if(!segment.getLastState().isConstrained()){
				segment.setConstrained(false);
			}
			
			if(segment.isLocked()){
				segment.computateAngleConstraints();
			}
		});
		
		bone.getShape().setOnMouseDragged(e -> {
			FBKVector point = FBKVector.toVector(skeleton.sceneToLocal(e.getSceneX(), e.getSceneY()));

			segment.moveTail(point);
			
			e.consume();
		});
		
		joint.getNode().setOnMousePressed(e -> {
			Point2D point = skeleton.sceneToLocal(e.getSceneX(), e.getSceneY());

			lastX = point.getX();
			lastY = point.getY();
			
		});
		joint.getNode().setOnMouseReleased(e -> {	
			
			if(resizingBone){
				finishResize(segment);
			}
			
			e.consume();

		});
		joint.getNode().setOnMouseDragged(e -> {
			Point2D point = skeleton.sceneToLocal(e.getSceneX(), e.getSceneY());

			if(e.isControlDown()){
								
				performResize(segment, point);
			}else{
				segment.moveHead(FBKVector.toVector(point));			
			}
			e.consume();
		});
		joint.getNode().setOnMouseClicked(e -> {

			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 2) {
					if(e.isControlDown()){

						if (segment.isConstrained()) {
							segment.setConstrained(false, FBKConstraintPivot.NONE);
							joint.setMainColor(FXPaintResources.ACCENT_COLOR_LIGHT);
						} else {
							segment.setConstrained(true, FBKConstraintPivot.HEAD);
							joint.setMainColor(Color.rgb(230, 150, 0));
						}
					}else{
						if (segment.isLocked()) {
							segment.setLocked(false);
							joint.setMainColor(FXPaintResources.ACCENT_COLOR_LIGHT);
						} else {
							segment.setLocked(true);
							joint.setMainColor(Color.rgb(170, 170, 170, 1));
						}
					}
				}
			} 
		});

		segment.getContent().addAll(bone.getShape(), joint.getNode());
	}

	private void addTailContent(FBKSegmentChain skeleton, FBKSegment segment, FBKSegmentListener listener) {
		IFXJointView joint = FXJointView.getFXJointView(FXJointViewType.TYPE_A, segment.getLength(), 0, 4);
		joint.setCenterColor(Color.rgb(170, 170, 170, 1));
		joint.setMainColor(Color.rgb(170, 170, 170, 1));
		joint.setCenterRadius(6);

		listener.setJointView(joint);
		
		joint.getNode().setOnMouseDragged(e -> {
			Point2D point = skeleton.sceneToLocal(e.getSceneX(), e.getSceneY());

			if(e.isControlDown()){
				
				performResize(segment, point);
			}else{
				segment.moveTail(FBKVector.toVector(point));					
			}
			
			e.consume();
		});


		segment.getContent().addAll(joint.getNode());
	}

	private FBKSegment createChain(FBKSegmentChain skeleton, double length, int size, double maxAngle, double minAngle) {
		FBKSegment head = new FBKSegment(length);
		FBKSegmentListener hListener = new FBKSegmentListener();
		head.setEventListener(hListener);
		
		addContent(skeleton, head, hListener, false);

		for (int i = 0; i < size-1; i++) {

			final FBKSegment FBKSegment = new FBKSegment(length, 0, minAngle, maxAngle);
			FBKSegmentListener listener = new FBKSegmentListener();
			FBKSegment.setEventListener(listener);
			addContent(skeleton, FBKSegment,listener, false);

			head.addChild(FBKSegment);

			head = FBKSegment;
		}

		FBKSegmentListener tListener = new FBKSegmentListener();
		head.setEventListener(tListener);
		
		addTailContent(skeleton, head,tListener);

		return head.getAbsoluteAncestor();
	}

	
	private boolean resizingBone = false;

	private void performResize(FBKSegment segment, Point2D point) {
		if(segment.getParent() != null && !segment.isChildless()){		
			resizeChildSegment(segment, point);
		}else if(segment.isChildless()){
			resizeChildessSegment(segment, point);
		}else{		
			resizeAbsoluteParent(segment, point);
		}
		
	}

	private void resizeAbsoluteParent(FBKSegment segment, Point2D point) {
		Point2D start = segment.getCurrentTail().toPoint2D();

		double distance = start.distance(point);
		
		if(!resizingBone){			
			segment.setConstrained(true, FBKConstraintPivot.TAIL);
			
			for(FBKSegment child : segment.getChildren()){
				child.setLocked(true, true);
			}
			
			resizingBone = true;
		}
		
		segment.resizeFromHead(distance, FBKVector.toVector(point));
	}


	private void resizeChildSegment(FBKSegment segment, Point2D point) {
		Point2D start = segment.getParent().getCurrentHead().toPoint2D();

		double distance = start.distance(point);
		
		if(!resizingBone){
			for(FBKSegment child : segment.getParent().getChildren()){
				child.setLocked(true, true);
			}
			resizingBone = true;
		}
		
		segment.getParent().resizeFromTail(distance, FBKVector.toVector(point));
	}
	

	private void resizeChildessSegment(FBKSegment segment, Point2D point) {
		Point2D start = segment.getCurrentHead().toPoint2D();

		double distance = start.distance(point);
		
		if(!resizingBone){
			segment.setConstrained(true);
			resizingBone = true;
		}
		
		segment.resizeFromTail(distance, FBKVector.toVector(point));
	}

	
	private void finishResize(FBKSegment segment){
		if(segment.getParent() != null && !segment.isChildless()){
			for(FBKSegment child : segment.getParent().getChildren()){
				child.setLocked(false, true);
			}
		}else if(segment.isChildless()){
			segment.setConstrained(false);
		}else{
			segment.setConstrained(false, FBKConstraintPivot.TAIL);
			
			for(FBKSegment child : segment.getChildren()){
				child.setLocked(false, true);
			}
		}

		resizingBone = false;
	}
	
	private class FBKSegmentListener implements FBKSegmentEventListener{

		private IFXBoneView boneView;
		private IFXJointView jointView;
		
		
		public void setBoneView(IFXBoneView boneView) {
			this.boneView = boneView;
		}


		public void setJointView(IFXJointView jointView) {
			this.jointView = jointView;
		}


		@Override
		public void onKinematicsTypeChanged(FBKSegment segment, FBKinematicsType type) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSegmentConstrained(FBKSegment segment) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSegmentUnconstrained(FBKSegment segment) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSegmentLocked(FBKSegment segment) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSegmentUnlocked(FBKSegment segment) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSegmentLengthChange(FBKSegment segment, double length) {
			if(boneView != null && jointView != null){
				double offset = jointView.getOuterRadius()/2;
				boneView.setWidth(length - ((offset * 2) + (offset*2)));
				boneView.setX(-(length - ((offset * 2) + (offset*4))));
			}

		}

		@Override
		public void onPositionUpdate(FBKSegment segment, FBKVector headPoint, FBKVector tailPoint, double angle, double rotation, double length) {

		}
		
	}
	
}
