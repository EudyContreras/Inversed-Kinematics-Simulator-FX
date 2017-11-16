package com.eudycontreras.models;

public class GraphicData{

	public Object bone;
	
	public Object flesh;
	
	public Object joint;
	
	public Object mesh;
	
	public Object texture;

	public <T> void setBone(T bone) {
		this.bone = bone;
	}

	public <T> void setFlesh(T flesh) {
		this.flesh = flesh;
	}

	public <T> void setJoint(T joint) {
		this.joint = joint;
	}

	public <T> void setMesh(T mesh) {
		this.mesh = mesh;
	}

	public <T> void setTexture(T texture) {
		this.texture = texture;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBone() {
		return (T) bone;
	}

	@SuppressWarnings("unchecked")
	public <T> T  getFlesh() {
		return (T) flesh;
	}

	@SuppressWarnings("unchecked")
	public <T> T  getJoint() {
		return (T) joint;
	}

	@SuppressWarnings("unchecked")
	public <T> T  getMesh() {
		return (T) mesh;
	}

	@SuppressWarnings("unchecked")
	public <T> T  getTexture() {
		return (T) texture;
	}
	
	
}
