package com.eudycontreras.javafx.fbk.samples;

import com.eudycontreras.components.visuals.IFXBoneView;
import com.eudycontreras.editor.application.FXPaintResources;
import com.eudycontreras.javafx.fbk.models.FBKSegment;
import com.eudycontreras.javafx.fbk.models.FBKVector;
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
		boneView.setFill(boneColorNormal.deriveColor(1,1,1, 0.55));
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
			boneView.setFill(boneColorSelected.deriveColor(1,1,1,0.55));
			boneView.setStroke(boneColorSelected);
			
		}else{
			boneView.setFill(boneColorNormal.deriveColor(1,1,1,0.55));
			boneView.setStroke(boneColorNormal);		
		}
	}
	
	private void addEventHandling() {
		boneView.getShape().setOnMousePressed(e -> {	
			setSelected(true);
			applyForwardKinematics();
			
			e.consume();
		});
		
		boneView.getShape().setOnMouseReleased(e -> {
			setSelected(false);
			applyInverseKinematics();
			
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

	private void applyForwardKinematics() {
		segment.setKinematicsType(FBKinematicsType.FORWARD);
		segment.setConstrained(true);
	}

	private void applyInverseKinematics() {
		
		segment.setKinematicsType(FBKinematicsType.INVERSED);
		
		if(!segment.getLastState().isConstrained()){
			segment.setConstrained(false);
		}
		
		if(segment.isLocked()){
			segment.computateAngleConstraints();
		}
	}

	private void performSegmentMove(MouseEvent e) {
		FBKVector point = FBKVector.toVector(segment.getSkeleton().sceneToLocal(e.getSceneX(), e.getSceneY()));

		segment.moveTail(point);
	}

	public void computeLength(double length, double radius){
		double offset = radius / 2d;
		double height = armature.getBoneDensity() + (length*0.065d);
		
		boneView.setHeight(height - (height * 0.01d));
		boneView.setWidth(length - ((offset * 2d) + (offset * 2d)));
		boneView.setX(-(length - ((offset * 2d) + (offset * 4d))));
		
	}
}
