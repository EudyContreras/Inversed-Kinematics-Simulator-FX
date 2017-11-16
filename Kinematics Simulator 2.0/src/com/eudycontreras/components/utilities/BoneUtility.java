package com.eudycontreras.components.utilities;

import com.eudycontreras.components.models.Bone;

import javafx.geometry.Point2D;

public class BoneUtility {

	public static Bone findDescendant(Bone bone, int boneId) {

		if (bone.getBoneId() == boneId)
			return bone;

		if (!bone.getChildren().isEmpty()) {
			for (Bone child : bone.getChildren()) {
				if (child.getBoneId() == boneId) {
					return child;
				}
			}

			for (Bone child : bone.getChildren()) {
				if (!child.getChildren().isEmpty()) {
					return findDescendant(child, boneId);
				}
			}
		}
		return null;
	}
	
	public static boolean isChildTo(Bone thisBone, Bone bone){
		Bone parent = thisBone.getParent();
		
		while(parent != null){
			
			if(parent.compareTo(bone) == 0){
				
				return true;
			}
			
			parent = thisBone.getParent().getParent();
		}
		
		return false;
	}
	
	public static Point2D createPoint2D(Point2D origin, double angle, double length) {
		return new Point2D(origin.getX() + Math.cos(angle) * length, origin.getY() + Math.sin(angle) * length);
	}

	public static double getAngle(Point2D p1, Point2D p2) {
		return Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());
	}
}
