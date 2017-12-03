package com.eudycontreras.javafx.fbk.utilities;

import com.eudycontreras.javafx.fbk.models.FBKSegment;

public class FBKSegmentUtility {

	public static FBKSegment findDescendant(FBKSegment bone, int boneId) {

		if (bone.getSegmentId() == boneId)
			return bone;

		if (!bone.getChildren().isEmpty()) {
			for (FBKSegment child : bone.getChildren()) {
				if (child.getSegmentId() == boneId) {
					return child;
				}
			}

			for (FBKSegment child : bone.getChildren()) {
				if (!child.getChildren().isEmpty()) {
					return findDescendant(child, boneId);
				}
			}
		}
		return null;
	}
	
	public static boolean isChildTo(FBKSegment thisBone, FBKSegment bone){
		FBKSegment parent = thisBone.getParent();
		
		while(parent != null){
			
			if(parent.compareTo(bone) == 0){
				
				return true;
			}
			
			parent = thisBone.getParent().getParent();
		}
		
		return false;
	}
}
