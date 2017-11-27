package com.eudycontreras.javafx.fbk.models;

import com.eudycontreras.javafx.fbk.models.FBKSegmentChain.FBKAttachPivot;

public class FBKAttachment {

	private FBKSegmentChain chain;
	private FBKAttachPivot pivot;
	
	public FBKAttachment(FBKSegmentChain chain, FBKAttachPivot pivot) {
		super();
		this.chain = chain;
		this.pivot = pivot;
	}
	
	public FBKSegmentChain getChain() {
		return chain;
	}
	public FBKAttachPivot getPivot() {
		return pivot;
	}
	
	
}
