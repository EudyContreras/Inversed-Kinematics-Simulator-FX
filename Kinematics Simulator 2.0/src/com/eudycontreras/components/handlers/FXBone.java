package com.eudycontreras.components.handlers;

import com.eudycontreras.components.visuals.IFXBoneView;
import com.eudycontreras.editor.application.FXArmatureManager;
import com.eudycontreras.editor.application.FXPaintResources;
import com.eudycontreras.javafx.fbk.models.FBKSegment;
import com.eudycontreras.javafx.fbk.models.FBKVector;
import com.eudycontreras.javafx.fbk.models.FBKSegment.FBKConstraintPivot;
import com.eudycontreras.javafx.fbk.models.FBKSegment.FBKinematicsType;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class FXBone{

	private IFXBoneView boneView;
	
	private FXJoint fxJoint;
	
	private FBKSegment segment;
	
	private FXArmatureManager armature;
	
	private Color boneColorSelected = FXPaintResources.ACCENT_COLOR_LIGHT;
	private Color boneColorNormal = Color.rgb(170, 170, 170);
	
	public FXBone(FXArmatureManager armature, FBKSegment bone, IFXBoneView boneView){
		this.armature = armature;
		this.boneView = boneView;
		this.segment = bone;	
		this.setUpView();
		this.addEventHandling();
	}
	
	private void setUpView(){
		boneView.setStrokeWidth(1);
		boneView.setFill(boneColorNormal.deriveColor(1,1,1, 0.35));
		boneView.setStroke(boneColorNormal);
	}

	
	public void setJoint(FXJoint joint) {
		this.fxJoint = joint;
	}

	public IFXBoneView getBoneView(){
		return boneView;
	}
	
	public void showBones(boolean showBones){
		this.boneView.getShape().setVisible(showBones);
	}
	
	public boolean showingBones(){
		return this.boneView.getShape().isVisible();
	}
	
	public void setSelected(boolean selected){
		if(selected){
			boneView.setFill(boneColorSelected.deriveColor(1,1,1,0.45));
			boneView.setStroke(boneColorSelected);
			
		}else{
			boneView.setFill(boneColorNormal.deriveColor(1,1,1,0.35));
			boneView.setStroke(boneColorNormal);		
		}
	}
	
	private void addEventHandling() {
		boneView.getShape().setOnMousePressed(e -> {	
			setSelected(true);
			applyForwardKinematics(e);
			
			e.consume();
		});
		
		boneView.getShape().setOnMouseReleased(e -> {
			setSelected(false);
			applyInverseKinematics(e);
			
			e.consume();
		});
		
		boneView.getShape().setOnMouseDragged(e -> {
			performSegmentMove(e);
			
			e.consume();
		});
		
		boneView.getShape().setOnMouseClicked(e -> {
			
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 2) {
					if(e.isAltDown()){

					}
					else if(e.isControlDown()){

					}
					else if(e.isShiftDown()){
						
					}
					else {
						fxJoint.showAngleConstraintAssist();
					}
				}
			}else{}
			
			e.consume();
		
		});
	}

	private void applyForwardKinematics(MouseEvent e) {
		if(e.isShiftDown()){
			if(segment.getAbsoluteAncestor().isConstrained()){
				return;
			}
			segment.setChildrenAttached(false);
			segment.setKinematicsType(FBKinematicsType.FORWARD, true, true);
			segment.setConstrained(true,FBKConstraintPivot.TAIL);
		}else{
			if(segment.getAllDescendants().stream().anyMatch(s -> s.isConstrained())){
				return;
			}
			segment.setKinematicsType(FBKinematicsType.FORWARD);
			segment.setConstrained(true);
		}
	}

	private void applyInverseKinematics(MouseEvent e) {
		
		if(e.isShiftDown()){
			if(segment.getAbsoluteAncestor().isConstrained() && !segment.isAbsoluteAncestor()){
				return;
			}
			segment.setChildrenAttached(true);
			segment.setKinematicsType(FBKinematicsType.INVERSED, true, true);
			if(!segment.getLastState().isConstrained()){
				segment.setConstrained(false);
			}else{
				segment.setConstrained(false);
			}
		}else{
			
			if(segment.getAllDescendants().stream().anyMatch(s -> s.isConstrained())){
				return;
			}
			if(!segment.getLastState().isConstrained()){
				segment.setConstrained(false);
			}else{
		
			}
			segment.setKinematicsType(FBKinematicsType.INVERSED);
			
		}
		if(segment.isLocked()){
			segment.computateAngleConstraints();
		}
	}

	private void performSegmentMove(MouseEvent e) {
		FBKVector point = FBKVector.toVector(segment.getSkeleton().sceneToLocal(e.getSceneX(), e.getSceneY()));

		if(e.isShiftDown()){
			if(segment.getAbsoluteAncestor().isConstrained() && !segment.isAbsoluteAncestor()){
				return;
			}
			
			segment.moveHead(point);
		}else{
			
			if(segment.getAllDescendants().stream().anyMatch(s -> s.isConstrained())){
				return;
			}
			
			segment.moveTail(point);
		}
		
	}

	public void computeLength(double length, double radius){
		double offset = radius / 2d;
		double height = armature.getBoneDensity() + (length * 0.035d);
		
		boneView.setHeight(height - (height * 0.01d));
		boneView.setWidth(length - ((offset * 2d) + (offset * 2d)));
		boneView.setX(-(length - ((offset * 2d) + (offset * 4d))));
		
	}
}
