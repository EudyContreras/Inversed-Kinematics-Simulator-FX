package com.eudycontreras.models;

import com.eudycontreras.components.models.Bone;
import com.eudycontreras.components.models.Skeleton;

public class ParentSource {

	private Skeleton skeleton;
	private Bone parent;
	
	private double jointRadius;
	
	private boolean hasChildren;
	
	public ParentSource(Skeleton skeleton, Bone parent, double jointRadius) {
		super();
		this.skeleton = skeleton;
		this.parent = parent;
		this.jointRadius = jointRadius;
	}

	public Skeleton getSkeleton() {
		return skeleton;
	}

	public Bone getParent() {
		return parent;
	}

	public double getJointRadius() {
		return jointRadius;
	}
	
	public boolean hasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
}
