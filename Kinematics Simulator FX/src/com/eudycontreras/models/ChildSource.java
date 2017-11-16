package com.eudycontreras.models;

import com.eudycontreras.components.models.Bone;

public class ChildSource implements Comparable<Bone>{

	private Bone parent;
	
	private ParentSource source;
	
	private double jointRadius;
	
	public ChildSource(ParentSource source, Bone parent, double jointRadius) {
		super();
		this.parent = parent;
		this.source = source;
		this.jointRadius = jointRadius;
	}

	public Bone getParent() {
		return parent;
	}

	public double getJointRadius() {
		return jointRadius;
	}

	public ParentSource getSource() {
		return source;
	}

	@Override
	public int compareTo(Bone other) {
		return parent.compareTo(other);
	}
	
	
	
}
