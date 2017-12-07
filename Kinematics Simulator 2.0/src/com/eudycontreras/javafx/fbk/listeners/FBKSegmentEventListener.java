package com.eudycontreras.javafx.fbk.listeners;

import com.eudycontreras.javafx.fbk.models.FBKVector;
import com.eudycontreras.javafx.fbk.models.FBKSegment;
import com.eudycontreras.javafx.fbk.models.FBKSegment.FBKinematicsType;

public interface FBKSegmentEventListener {
	
	public enum FBKSegmentStatus{
		ABSOLUTE_PARENT, ONLY_CHILD, SIBLING, TAIL
	}
	
	public void onKinematicsTypeChanged(FBKSegment segment, FBKinematicsType type);

	public void onSegmentConstrained(FBKSegment segment);
	
	public void onSegmentUnconstrained(FBKSegment segment);
	
	public void onSegmentLocked(FBKSegment segment);
	
	public void onSegmentUnlocked(FBKSegment segment);
	
	public void onEffectorStatusChanged(FBKSegment segment,boolean status);
	
	public void onSegmentLengthChange(FBKSegment segment, double length);
	
	public void onSegmentStatusChange(FBKSegment segment, FBKSegmentStatus status);
	
	public void onSegmentChildAdded(FBKSegment segment, FBKSegment child);
	
	public void onSegmentChildRemoved(FBKSegment segment, FBKSegment child);
	
	public void onAngleChanged(FBKSegment segment, double angle);

	public void onPositionUpdate(FBKSegment segment, FBKVector headPoint, FBKVector tailPoint, double angle, double rotation, double length);
}

