
package com.eudycontreras.javafx.fbk.samples;

import com.eudycontreras.components.visuals.FXBoneView;
import com.eudycontreras.components.visuals.FXBoneViewType;
import com.eudycontreras.components.visuals.FXJointView;
import com.eudycontreras.components.visuals.FXJointViewType;
import com.eudycontreras.components.visuals.IFXBoneView;
import com.eudycontreras.components.visuals.IFXJointView;
import com.eudycontreras.editor.application.FXPaintResources;
import com.eudycontreras.javafx.fbk.models.FBKSegment;
import com.eudycontreras.javafx.fbk.models.FBKSegment.FBKConstraintPivot;
import com.eudycontreras.javafx.fbk.models.FBKSegment.FBKKinematicsType;
import com.eudycontreras.javafx.fbk.models.FBKSegmentChain;
import com.eudycontreras.javafx.fbk.models.FBKVector;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
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

		addContent(skeleton, head1, true);
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

	private void addContent(FBKSegmentChain skeleton, FBKSegment segment, boolean root) {

		IFXBoneView bone = FXBoneView.getFXBoneView(FXBoneViewType.TYPE_A, 10, 0, segment.getLength()/2 - 10, segment.getLength()/5);
		bone.setStrokeWidth(1);
		bone.setFill(Color.rgb(170, 170, 170,0.65));
		bone.setStroke(Color.rgb(170, 170, 170, 1));
		
		IFXJointView joint = FXJointView.getFXJointView(FXJointViewType.TYPE_A, 0, 0, root ? 14 : 10);
		joint.setCenterRadius(root ? 8 : 6);
	
		joint.setCenterColor(Color.TRANSPARENT);
		joint.setMainColor(FXPaintResources.ACCENT_COLOR_LIGHT);

		bone.getShape().setOnMousePressed(e -> {
			bone.setFill(FXPaintResources.ACCENT_COLOR_LIGHT.deriveColor(1,1,1,0.5));
			bone.setStroke(FXPaintResources.ACCENT_COLOR_LIGHT);
			if(skeleton.getAbsoluteAncestor().getKinematicsType() != FBKKinematicsType.FORWARD){
				skeleton.getAbsoluteAncestor().setKinematicsType(FBKKinematicsType.FORWARD);
			}
		});
		
		bone.getShape().setOnMouseReleased(e -> {
			bone.setFill(Color.rgb(170, 170, 170,0.5));
			bone.setStroke(Color.rgb(170, 170, 170, 1));
			if(skeleton.getAbsoluteAncestor().getKinematicsType() == FBKKinematicsType.FORWARD){
				skeleton.getAbsoluteAncestor().setKinematicsType(FBKKinematicsType.INVERSED);
				skeleton.getAbsoluteAncestor().removeAllConstraints();
			}
			if(segment.isConstrained()){
				segment.setConstrained(false);
			}
			
			if(segment.isLocked()){
				segment.computateAngleConstraints();
			}
		});
		
		bone.getShape().setOnMouseDragged(e -> {
			FBKVector point = FBKVector.toVector(skeleton.sceneToLocal(e.getSceneX(), e.getSceneY()));

			if(!segment.isConstrained()){
				segment.setConstrained(true);
			}
			segment.moveTail(point);
			
			e.consume();
		});
		
		joint.getNode().setOnMouseDragged(e -> {
			Point2D point = skeleton.sceneToLocal(e.getSceneX(), e.getSceneY());

			segment.moveHead(FBKVector.toVector(point));
			
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

	private void addTailContent(FBKSegmentChain skeleton, FBKSegment segment) {
		IFXJointView joint = FXJointView.getFXJointView(FXJointViewType.TYPE_A, segment.getLength(), 0, 4);
		joint.setCenterColor(Color.rgb(170, 170, 170, 1));
		joint.setMainColor(Color.rgb(170, 170, 170, 1));
		joint.setCenterRadius(6);

		joint.getNode().setOnMouseDragged(e -> {
			Point2D point = skeleton.sceneToLocal(e.getSceneX(), e.getSceneY());

			segment.moveTail(FBKVector.toVector(point));
			
			e.consume();
		});


		segment.getContent().addAll(joint.getNode());
	}

	private FBKSegment createChain(FBKSegmentChain skeleton, double length, int size, double maxAngle, double minAngle) {
		FBKSegment head = new FBKSegment(length);

		addContent(skeleton, head, false);

		for (int i = 0; i < size-1; i++) {

			final FBKSegment FBKSegment = new FBKSegment(length, 0, minAngle, maxAngle);

			addContent(skeleton, FBKSegment, false);

			head.addChild(FBKSegment);

			head = FBKSegment;
		}

		addTailContent(skeleton, head);

		return head.getAbsoluteAncestor();
	}

}
