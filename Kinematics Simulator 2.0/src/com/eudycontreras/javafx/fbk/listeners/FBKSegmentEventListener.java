package com.eudycontreras.javafx.fbk.listeners;

import com.eudycontreras.javafx.fbk.models.FBKVector;
import com.eudycontreras.javafx.fbk.models.FBKSegment;
import com.eudycontreras.javafx.fbk.models.FBKSegment.FBKinematicsType;

public interface FBKSegmentEventListener {
	
	public void onKinematicsTypeChanged(FBKSegment segment, FBKinematicsType type);

	public void onSegmentConstrained(FBKSegment segment);
	
	public void onSegmentUnconstrained(FBKSegment segment);
	
	public void onSegmentLocked(FBKSegment segment);
	
	public void onSegmentUnlocked(FBKSegment segment);
	
	public void onSegmentLengthChange(FBKSegment segment, double length);
	
	public void onPositionUpdate(FBKSegment segment, FBKVector headPoint, FBKVector tailPoint, double angle, double rotation, double length);
}

