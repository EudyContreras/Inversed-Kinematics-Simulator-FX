package com.eudycontreras.components.handlers;

import com.eudycontreras.components.models.Bone;
import com.eudycontreras.components.visuals.IFXBoneView;

public class FXBone{

	private boolean showBones;
	
	private IFXBoneView boneView;
	
	private FXFlesh flesh;
	private Bone bone;
	
	public FXBone(IFXBoneView boneView, Bone bone){
		this.boneView = boneView;
		this.bone = bone;	
	}
	
	public IFXBoneView getBone(){
		return boneView;
	}
	
	public void showBones(boolean showBones){
		this.showBones = showBones;
	}
	
	public boolean showingBones(){
		return showBones;
	}

	public void mapFlesh(FXFlesh flesh){
		this.flesh = flesh;
//		startJoint.getPivot().centerXProperty().addListener((osv, oldX, newX) -> {
//
//			boneView.setStartX((double) newX);
//
//			computeMuscleVolume(muscleMass);
//		});
//
//		startJoint.getPivot().centerYProperty().addListener((osv, oldY, newY) -> {
//
//			boneView.setStartY((double) newY);
//
//			computeMuscleVolume(muscleMass);
//		});
//		
//		endJoint.getPivot().centerXProperty().addListener((osv, oldX, newX) -> {
//
//			boneView.setEndX((double) newX);
//
//			computeMuscleVolume(muscleMass);
//		});
//
//		endJoint.getPivot().centerYProperty().addListener((osv, oldY, newY) -> {
//
//			boneView.setEndY((double) newY);
//
//			computeMuscleVolume(muscleMass);
//		});
		
	}

	public void computeLength(double length, double radius){
		boneView.setWidth(length - radius);	
		boneView.setX(-(length - (radius * 2)));
		if(flesh != null){
			flesh.mapLength(boneView, radius);
		}
	}
}
