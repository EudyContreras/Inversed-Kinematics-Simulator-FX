/*
 * Copyright 2013 Michael Heinrichs, http://netopyr.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eudycontreras.components.models;

import com.eudycontreras.components.utilities.BoneUtility;
import com.eudycontreras.models.GraphicData;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

/**
 * The class {@code Bone} is the basic building block for inverse kinematics.
 * <p>
 * {@code Bone} objects need to be assembled in a tree-structure using the field
 * {@link #children}.
 * <p>
 * A {@code Bone} has a head, which is a point in space around which it can be
 * rotated, and a {@link #length}. The current value of the head and rotation is
 * stored in {@link #currentHead} and {@link #rotate}. Head, rotation, and
 * length define a second point in space, the tail. The current position of the
 * tail is stored in {@link #currentTail}.
 * <p>
 * The rotation can be limited using the fields {@link #minAngle} and
 * {@link #maxAngle}. The angle is measured in regard to the parent-bone. It can
 * be read from {@link #angle}.
 * <p>
 * The {@code Node} objects which need to be translated and rotated according to
 * the {@code Bone} can be set using {@link #content}. All {@code Bone} objects
 * need to be assigned to a {@link Skeleton}, the {@code Node} which needs to be
 * added to the scenegraph. All coordinates of a {@code Bone} are based on the
 * local coordinate-system of its {@link Skeleton}.
 */
public class Bone implements Comparable<Bone>{

	public enum PointType { START, END }
	public enum KinematicsType { FORWARD, INVERSED }
	
	private static final Point2D ZERO = new Point2D(0, 0);
	
	private static final double DEFAULT_ANGLE = 0.0;
	private static final double MIN_ANGLE = -180.0;
	private static final double MAX_ANGLE = 180.0;
	
	private boolean userMoved = false;
	private boolean locked = false;
	private boolean fixed = false;
	
	private boolean endPointFixed = false;
	private boolean startPointFixed = false;

	private int boneId = -1;

	private GraphicData GraphicData;
	
	private Point2D fixedEndPoint;
	private Point2D fixedStartPoint;	
	
	private Bone lastChild;
	
	private RotationConstraint rotationConstraint = new RotationConstraint(MAX_ANGLE, MIN_ANGLE);
	
	public class RotationConstraint{
		
		public double maxAngle;
		public double minAngle;		
		
		public RotationConstraint(double maxAngle, double minAngle) {
			super();
			this.maxAngle = maxAngle;
			this.minAngle = minAngle;
		}
		
		protected double getMaxAngle() {
			return maxAngle;
		}
		
		protected void setMaxAngle(double maxAngle) {
			this.maxAngle = maxAngle;
		}
		
		protected double getMinAngle() {
			return minAngle;
		}
		
		protected void setMinAngle(double minAngle) {
			this.minAngle = minAngle;
		}
	}
	
	public void setData(GraphicData data){
		this.GraphicData = data;
	}
	
	public GraphicData getData(){
		return GraphicData;
	}
	
	public void setBoneId(int id){
		this.boneId = id;
	}
	
	public int getBoneId(){
		return boneId;
	}

	public boolean isUserMoved() {
		return userMoved;
	}

	public void setUserMoved(boolean userMoved) {
		this.userMoved = userMoved;
	}

	public boolean isFixed() {
		return fixed;
	}

	public void setFixed(boolean fixed) {
		if(fixed){
			this.fixed = fixed;
			this.fixedStartPoint = new Point2D(getStartPoint().getX(),getStartPoint().getY());
			this.fixedEndPoint = new Point2D(getEndPoint().getX(),getEndPoint().getY());	
		}else{
			this.fixed = fixed;
		}	
	}	

	public void setFixed(boolean fixed, boolean fixSiblings) {
		if(fixed){
			this.fixed = fixed;
			this.fixedStartPoint = new Point2D(getStartPoint().getX(),getStartPoint().getY());
			this.fixedEndPoint = new Point2D(getEndPoint().getX(),getEndPoint().getY());	
		}else{
			this.fixed = fixed;
		}	
		
		if(fixSiblings){
			if(getParent() != null){
				setSiblingsFixed(fixed);
			}
		}
	}
	
	public void setFixedEndPoint(boolean fixed){
		this.endPointFixed = fixed;
		this.fixedEndPoint = new Point2D(getEndPoint().getX(),getEndPoint().getY());	
	}
	
	public void setFixedStartPoint(boolean fixed){
		this.startPointFixed = fixed;
		this.fixedStartPoint = new Point2D(getStartPoint().getX(),getStartPoint().getY());
	}
	
	public boolean isEndPointFixed(){
		return this.endPointFixed;
	}
	
	public boolean isStartPointFixed(){
		return this.startPointFixed;
	}
	
	public Point2D getFixedPoint(PointType type){
		if(type == PointType.START){
			return fixedStartPoint;
		}else{
			return fixedEndPoint;
		}
	}
	
	public void setSiblingsFixed(boolean fixed){
		if(getSiblings().stream().anyMatch(b -> !b.isFixed())){
			
			for(int i = 0; i<getSiblings().size(); i++){
				
				Bone sibling = getSiblings().get(i);
				
				if(Bone.this.compareTo(sibling) != 0){
					sibling.setFixed(fixed);
				}
			}
		}
	}
	
	public Bone getLastDescendant(){
		return lastChild;
	}
	
	public void setLastDecendant(Bone descendant){
		this.lastChild = descendant;
	}
	
	public void addChild(Bone child){
		this.getSkeleton().getPrimaryChild().setLastDecendant(child);
		this.getSkeleton().allChildren.add(child);
		this.getChildren().add(child);
	}

	public void setKinematicsType(KinematicsType type, Bone child){
		switch(type){
		case FORWARD:
			rotationConstraint = new RotationConstraint(maxAngle,minAngle);
			minAngle = angle.get();
			maxAngle = angle.get();
			break;
		case INVERSED:
			if(rotationConstraint != null){
				minAngle = rotationConstraint.getMinAngle();
				maxAngle = rotationConstraint.getMaxAngle();
			}
			break;	
		}
	}
	
	public void setKinematicsType(KinematicsType type){
		switch(type){
		case FORWARD:
			rotationConstraint = new RotationConstraint(maxAngle,minAngle);
			minAngle = angle.get();
			maxAngle = angle.get();
			break;
		case INVERSED:
			if(rotationConstraint != null){
				minAngle = rotationConstraint.getMinAngle();
				maxAngle = rotationConstraint.getMaxAngle();
			}
			break;	
		}
		if(getParent() != null){
			getParent().setKinematicsType(type, this);
		}
		if(getChildren().get(0) != null){
			getChildren().get(0).setKinematicsType(type, this);
		}
	}
	
	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked, boolean lockChildren) {
		
		if(this.locked){
			if(!locked){
				
				if(rotationConstraint != null){
					minAngle = rotationConstraint.getMinAngle();
					maxAngle = rotationConstraint.getMaxAngle();
				}
				
				if(lockChildren){
					for(Bone child : getChildren()){
						child.setLocked(false, true);;
					}
				}
				
				this.locked = locked;
			}
		}else{
			if(locked){
				if(rotationConstraint == null){
					rotationConstraint = new RotationConstraint(maxAngle,minAngle);
				}
				minAngle = angle.get();
				maxAngle = angle.get();
				
				if(lockChildren){
					for(Bone child : getChildren()){
						child.setLocked(true, true);;
					}
				}
				
				this.locked = locked;
			}
		}
	}

	public Bone getFirstFixedBone(){
		for(int i = 0; i<getSkeleton().getChildren().size(); i++){
			
			Node content = getSkeleton().getChildren().get(i);
			
			Bone bone = (Bone)content.getUserData();
			
			if(bone.isFixed()){
				return bone;
			}
		}
		
		return null;

	}
	
	public List<Bone> getFixedBones(){
		
		List<Bone> bones = new ArrayList<Bone>();
		
		for(int i = 0; i<getSkeleton().getChildren().size(); i++){
			
			Node content = getSkeleton().getChildren().get(i);
			
			Bone bone = (Bone)content.getUserData();
			
			if(bone.isFixed()){
				bones.add(bone);
			}
		}
		
		return bones;
	}
	
	public void attachFixedChildren(){	
		if(!isFixed()){
			for(int i = 0; i<getChildren().size(); i++){
				
				Bone bone = getChildren().get(i);
				
				if(bone.isFixed()){
					
					bone.setFixed(false);
				}
				
				if(!bone.getChildren().isEmpty()){
					
					bone.attachFixedChildren();				
				}
			}
		}
	}
	/**
	 * The length of this {@code Bone}.
	 */
	private double length;

	public final double getLength() {
		return length;
	}
	
	public void setLength(double length){
		this.length = length;
		this.setup();
	}
	
	public void resize(double newAngle, double newLength, Point2D newPosition){
		setLength(newLength);
		moveEndpoint(newPosition);
		
		for(Bone child : getChildren()){
			child.setLocked(true, true);
		}
	}
	
	public void finishResize(){

	}
	

	/**
	 * The lower bound of the {@link #angle} of this {@code Bone}.
	 */
	private double minAngle;

	public final double getMinAngle() {
		return minAngle;
	}

	public void setMinAngle(double minAngle){
		this.minAngle = minAngle;
	}
	/**
	 * The upper bound of the {@link #angle} of this {@code Bone}.
	 */
	private double maxAngle;

	public final double getMaxAngle() {
		return maxAngle;
	}

	public void setMaxAngle(double maxAngle){
		this.maxAngle = maxAngle;
	}
	/**
	 * The current position of the currentHead of this {@code Bone}.
	 * <p>
	 * The position of the currentHead is calculated in local coordinates of the
	 * {@link Skeleton}.
	 * <p>
	 * The currentHead's position can be altered using
	 * {@link #moveStartPoint(double, double)} or
	 * {@link #moveStartPoint(javafx.geometry.Point2D)}.
	 */
	private final PrivatePoint2DProperty currentHead = new PrivatePoint2DProperty("currentHead");

	public final Point2D getStartPoint() {
		return currentHead.get();
	}

	public final ReadOnlyObjectProperty<Point2D> currentHeadProperty() {
		return currentHead;
	}
	
	private final PrivatePoint2DProperty lastHead = new PrivatePoint2DProperty("currentHead");

	public final Point2D getLastHead() {
		return lastHead.get();
	}

	/**
	 * The current position of the currentTail of this {@code Bone}.
	 * <p>
	 * The currentTail is calculated in local coordinates of the
	 * {@link Skeleton}.
	 * <p>
	 * The currentTail's position can be altered using
	 * {@link #moveEndPoint(double, double)} or
	 * {@link #moveEndpoint(javafx.geometry.Point2D)}.
	 */
	private final PrivatePoint2DProperty currentTail = new PrivatePoint2DProperty("currentTail");

	public final Point2D getEndPoint() {
		return currentTail.get();
	}

	public final ReadOnlyObjectProperty<Point2D> currentTailProperty() {
		return currentTail;
	}

	private final PrivatePoint2DProperty lastTail = new PrivatePoint2DProperty("currentTail");

	public final Point2D getLastTail() {
		return lastTail.get();
	}

	/**
	 * The current angle of this {@code Bone}.
	 * <p>
	 * The angle is measured in degrees between this {@code Bone} and the
	 * extension of its parent, an angle of null degrees results in a straight
	 * line. An {@code angle} has a value in the range [-180..180].
	 * <p>
	 * The angle can be narrowed down using {@link #minAngle} and
	 * {@link #maxAngle}.
	 */
	private final PrivateDoubleProperty angle = new PrivateDoubleProperty("angle");

	public final double getAngle() {
		return angle.get();
	}

	public ReadOnlyDoubleProperty angleProperty() {
		return angle;
	}

	public void setAngle(double angle) {

		if (angle > minAngle && angle < maxAngle) {
			this.angle.set(angle);
			this.setRotate(getAngle());
		}
	}

	/**
	 * The current rotation of this {@code Bone} in relation to the
	 * {@link Skeleton}.
	 */
	private final PrivateDoubleProperty rotate = new PrivateDoubleProperty("rotate");

	public final double getRotate() {
		return rotate.get();
	}

	public ReadOnlyDoubleProperty rotateProperty() {
		return rotate;
	}

	public void setRotate(double rotateValue) {
		setRotate(rotateValue, null);
	}

	public void setRotate(double rotateValue, Bone initiator) {

		rotate.set(borderAngle(getParent().getRotate() + getAngle()));
	}

	public void translateRotation(PointType type, double value) {

		double angle = borderAngle(getParent().getRotate() + getAngle()) + value;

		if (angle <= (Math.abs(minAngle) - 10) && angle >= -(maxAngle + 10)) {

			double x, y;

			double distance = length;

			x = Math.round(getStartPoint().getX() + (Math.cos(Math.toRadians(angle)) * distance));
			y = Math.round(getStartPoint().getY() + (Math.sin(Math.toRadians(angle)) * distance));

			switch(type){
			case START:
				moveStartPoint(x, y);
				break;
			case END:
				moveEndPoint(x, y);
			break;
			}
		}
	}

	public void setRotation(double value) {

		double angle = value;

		if (angle <= (Math.abs(minAngle) - 10) && angle >= -(maxAngle + 10)) {

			double x, y;

			double distance = length;

			x = Math.round(getStartPoint().getX() + (Math.cos(Math.toRadians(angle)) * distance));
			y = Math.round(getStartPoint().getY() + (Math.sin(Math.toRadians(angle)) * distance));

			moveEndPoint(x, y);
		}
	}
	
	/**
	 * The {@code Node} objects which are controlled by this {@code Bone}.
	 * <p>
	 * If this {@code Bone} is translated or rotated, the {@code Node} objects
	 * in {@code content} are updated accordingly.
	 */
	private ListProperty<Node> content = new SimpleListProperty<>(this, "content", FXCollections.<Node> observableArrayList());

	public final ObservableList<Node> getContent() {
		return content.get();
	}

	public final void setContent(ObservableList<Node> list) {
		this.content.set(list);
	}

	/**
	 * The {@link Skeleton} is the {@code Node} in the scenegraph, that
	 * determines how {@code Bone} objects are rendered.
	 * <p>
	 * All connected {@code Bone} objects in a tree belong to one
	 * {@link Skeleton}. (This is taken care of automatically. If the
	 * {@code Skeleton} of one {@code Bone} is changed, the {@code Skeleton}
	 * -values for all connected {@code Bone} objects are updated.)
	 * <p>
	 * All coordinates of this {@code Bone} are based on the local
	 * coordinate-system of this {@code Skeleton}.
	 */
	private ObjectProperty<Skeleton> skeleton = new ObjectPropertyBase<Skeleton>() {
		private Skeleton oldSkeleton = null;

		@Override
		protected void invalidated() {
			final Skeleton newSkeleton = get();
			if ((newSkeleton == null) ? oldSkeleton != null : !newSkeleton.equals(oldSkeleton)) {
				if (oldSkeleton != null) {
					oldSkeleton.getBonesWritable().remove(Bone.this);
				}
				if (newSkeleton != null) {
					newSkeleton.getBonesWritable().add(Bone.this);
				}
				final Bone parentBone = getParent();
				if (parentBone != null) {
					parentBone.setSkeleton(newSkeleton);
				}
				for (final Bone child : children) {
					if (child != null) {
						child.setSkeleton(newSkeleton);
					}
				}
				oldSkeleton = newSkeleton;
			}
		}

		@Override
		public Object getBean() {
			return Bone.this;
		}

		@Override
		public String getName() {
			return "skeleton";
		}
	};

	public final Skeleton getSkeleton() {
		return skeleton.get();
	}

	public final void setSkeleton(Skeleton skeleton) {
		this.skeleton.set(skeleton);
	}

	public final void setSourceSkeleton(Skeleton skeleton) {
		this.skeleton.set(skeleton);
		this.skeleton.get().setPrimaryChild(this);
	}
	
	public final ObjectProperty<Skeleton> skeletonProperty() {
		return skeleton;
	}

	/**
	 * The parent-{@code Bone} of this {@code Bone}.
	 */
	private final PrivateParentProperty parent = new PrivateParentProperty();

	public final Bone getParent() {
		if(!isFixed()){
			return parent.get();
		}else{
			return parent.get();
		}
	}

	public final ReadOnlyObjectProperty<Bone> parentProperty() {
		return parent;
	}
	
	/**
	 * The children of this {@code Bone}.
	 */
	private final ListProperty<Bone> children = new SimpleListProperty<>(this, "children",FXCollections.<Bone> observableArrayList());

	public final ObservableList<Bone> getChildren() {
		return children.get();
	}

	public final void setChildren(ObservableList<Bone> children) {
		this.children.set(children);
	}

	public final ListProperty<Bone> childrenProperty() {
		return children;
	}
	
	public final ObservableList<Bone> getSiblings(){
		if(getParent() != null){
			return getParent().getChildren();
		}
		
		return null;
	}

	public final boolean hasAncestor(Bone bone){
		Bone parent = getParent();
		
		while(parent != null){
			
			if(parent.compareTo(bone) == 0){
				return true;
			}
			
			parent = parent.getParent();
		}
		
		return false;
	}
	
	private final Group group = new Group();

	Node getGroup() {
		return group;
	}

	public Bone(double length, double angle, double minAngle, double maxAngle) {
		this.length = length;
		this.minAngle = minAngle;
		this.maxAngle = maxAngle;
		this.boneId = Skeleton.BONE_ID;
		this.angle.set(angle);

		Skeleton.BONE_ID = Skeleton.BONE_ID + 1;
		
		Rotate rotate = new Rotate(getRotate(), 0.0, 0.0);
		
		rotate.angleProperty().bind(rotateProperty());
		
		group.getTransforms().setAll(rotate);

		group.setUserData(this);
		group.translateXProperty().bind(new DoubleBinding() {
			{
				super.bind(currentHeadProperty());
			}

			@Override
			protected double computeValue() {
				
				return getStartPoint().getX();
			}
		});
		group.translateYProperty().bind(new DoubleBinding() {
			{
				super.bind(currentHeadProperty());
			}

			@Override
			protected double computeValue() {
	
				return getStartPoint().getY();
			}
		});
		content.addListener(new ChangeListener<ObservableList<? extends Node>>() {
			@Override
			public void changed(ObservableValue<? extends ObservableList<? extends Node>> observableValue,ObservableList<? extends Node> oldList, ObservableList<? extends Node> newList) {
				if (oldList != newList) {
					Bindings.unbindContent(group.getChildren(), oldList);
					Bindings.bindContent(group.getChildren(), newList);
				}
			}
		});
		Bindings.bindContent(group.getChildren(), content.get());
		
		children.addListener(new ListChangeListener<Bone>() {
			@Override
			public void onChanged(Change<? extends Bone> change) {
				while (change.next()) {
					if (!change.wasPermutated()) {
						for (final Bone child : change.getRemoved()) {
							if (Bone.this.equals(child.getParent())) {
								child.parent.set(null);
								
							}
						}
						for (final Bone child : change.getAddedSubList()) {
							if (!Bone.this.equals(child.getParent())) {
								child.parent.set(Bone.this);
							}
						}
					}
				}
			}
		});
		setup();
	}

	public Bone(double length, double minAngle, double maxAngle) {
		this(length, DEFAULT_ANGLE, minAngle, maxAngle);
	}

	public Bone(double length, double angle) {
		this(length, angle, MIN_ANGLE, MAX_ANGLE);
	}

	public Bone(double length) {
		this(length, DEFAULT_ANGLE, MIN_ANGLE, MAX_ANGLE);
	}

	public void moveStartPoint(Point2D p) {

		//attachFixedChildren();
		
		moveStartPoint(p, null);
		
		if(getParent() == null){
			System.out.println("HEH");
			for(Bone bone : getFixedBones()){
				bone.moveEndPoint(bone.fixedEndPoint,null);
			}
		}else{
			for(Bone bone : getFixedBones()){
				bone.moveStartPoint(bone.fixedStartPoint,null);
			}
		}
	}

	public void moveStartPoint(double x, double y) {
		
		//attachFixedChildren();
		
		moveStartPoint(new Point2D(x, y), null);
		
		if(getParent() == null){
			for(Bone bone : getFixedBones()){
				bone.moveEndPoint(bone.fixedStartPoint,null);
			}
		}else{
			for(Bone bone : getFixedBones()){
				bone.moveStartPoint(bone.fixedStartPoint,null);
			}
		}
	}

	public void moveEndpoint(Point2D p) {

		moveEndPoint(p, null);
		
		if(getParent() == null){
			for(Bone bone : getFixedBones()){
				bone.moveEndPoint(bone.fixedStartPoint,null);
			}
		}else{
			for(Bone bone : getFixedBones()){
				bone.moveStartPoint(bone.fixedStartPoint,null);
			}
		}
	}
	

	public void moveEndPoint(double x, double y) {
		
		moveEndPoint(new Point2D(x, y), null);
		
		if(getParent() == null){
			for(Bone bone : getFixedBones()){
				bone.moveEndPoint(bone.fixedStartPoint,null);
			}
		}else{
			for(Bone bone : getFixedBones()){
				bone.moveStartPoint(bone.fixedStartPoint,null);
			}
		}
	}

	private void moveStartPoint(Point2D point, Bone initiator) {
		final Bone parent = getParent();
		
		assert initiator == null || initiator.equals(parent);

		if (!getStartPoint().equals(point)) {
			

			if(getParent() != null){
				if(getDistance(point,getParent().getEndPoint()) > 1){
					
					point = new Point2D(getParent().getEndPoint().getX(),getParent().getEndPoint().getY());
				}
				
			}
			currentHead.set(point);
			
			final double alpha = BoneUtility.getAngle(point, getEndPoint());
			
			final double rotateValue = 180 * alpha / Math.PI;
			
			final double minAngle = getMinAngle();
			final double maxAngle = getMaxAngle();
			
			if ((initiator != null) && ((minAngle > -180) || (maxAngle < 180))) {
				
				final double initiatorRotate = initiator.getRotate();
				
				final double angleValue = Math.max(minAngle, Math.min(borderAngle(rotateValue - initiatorRotate), maxAngle));
				
				angle.set(angleValue);
				rotate.set(borderAngle(initiatorRotate + angleValue));
				currentTail.set(BoneUtility.createPoint2D(point, getRotate() * Math.PI / 180.0, getLength()));
				
			} else {
				
				rotate.set(rotateValue);
				
				currentTail.set(BoneUtility.createPoint2D(point, alpha, getLength()));
				
				if ((initiator == null) && (parent != null)) {
					
					angle.set(borderAngle(rotateValue - getParent().getRotate()));
					
					parent.moveEndPoint(point, this);
				}
			}
			updateChildren(null);
		}
	}

	private void moveEndPoint(Point2D point, Bone initiator) {
		final Bone parent = getParent();
		
		assert initiator == null || !initiator.equals(parent);

		
		if (!getEndPoint().equals(point)) {

			currentTail.set(point);
			
			double alpha = BoneUtility.getAngle(point, getStartPoint());
			
			double rotateValue = borderAngle(180.0 + 180 * alpha / Math.PI);
			
			if (initiator != null) {
				
				final double minAngle = initiator.getMinAngle();
				final double maxAngle = initiator.getMaxAngle();
				
				if ((minAngle > -180) || (maxAngle < 180)) {
					
					final double initiatorRotate = initiator.getRotate();
					
					final double childAngle = Math.max(minAngle, Math.min(borderAngle(initiatorRotate - rotateValue), maxAngle));
					
					rotateValue = borderAngle(initiatorRotate - childAngle);
					
					alpha = (rotateValue - 180) * Math.PI / 180.0;
				}
			}
			rotate.set(rotateValue);
			
			currentHead.set(BoneUtility.createPoint2D(point, alpha, getLength()));
			
			if (parent != null) {
				angle.set(borderAngle(rotateValue - getParent().getRotate()));
				
				parent.moveEndPoint(getStartPoint(), this);
			}
			
			updateChildren(initiator);
		}
		
		lastTail.set(currentTail.get());
		lastHead.set(currentHead.get());
		
	}

	private void resetFromParent() {
		final Bone parent = getParent();
		
		setSkeleton(getParent().getSkeleton());
		
		currentHead.set(getParent().getEndPoint());
		
		setup();
	}

	private void setup() {
		final Bone parent = getParent();
		
		final double rotateValue = (parent == null) ? getAngle() : borderAngle(getParent().getRotate() + getAngle());
		
		rotate.set(rotateValue);
		
		currentTail.set(BoneUtility.createPoint2D(getStartPoint(), Math.PI * rotateValue / 180.0, getLength()));
		
		for (final Bone child : children) {
			child.resetFromParent();
		}
	}

	private void updateChildren(Bone initiator) {
		final Point2D currentTail = getEndPoint();
		for (final Bone bone : getChildren()) {

			if (!bone.equals(initiator)) {
				bone.moveStartPoint(currentTail, this);
			}
		}
	}

	private double borderAngle(double value) {
		if (value <= MIN_ANGLE) {
			return value + 360;
		}
		if (value > MAX_ANGLE) {
			return value - 360;
		}
		return value;
	}

	private class PrivatePoint2DProperty extends ReadOnlyObjectPropertyBase<Point2D> {

		private final String name;
		private Point2D value = ZERO;

		@Override
		public Point2D get() {
			return value;
		}

		protected void set(Point2D value) {
			this.value = value;
			fireValueChangedEvent();
		}

		@Override
		public Object getBean() {
			return Bone.this;
		}

		@Override
		public String getName() {
			return name;
		}

		private PrivatePoint2DProperty(String name) {
			this.name = name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PrivatePoint2DProperty other = (PrivatePoint2DProperty) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

		private Bone getOuterType() {
			return Bone.this;
		}		
	}

	private class PrivateDoubleProperty extends ReadOnlyDoublePropertyBase {

		private final String name;
		private double value;

		@Override
		public double get() {
			return value;
		}

		protected void set(double value) {
			this.value = value;
			fireValueChangedEvent();
		}

		@Override
		public Object getBean() {
			return Bone.this;
		}

		@Override
		public String getName() {
			return name;
		}

		private PrivateDoubleProperty(String name) {
			this.name = name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			long temp;
			temp = Double.doubleToLongBits(value);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PrivateDoubleProperty other = (PrivateDoubleProperty) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
				return false;
			return true;
		}

		private Bone getOuterType() {
			return Bone.this;
		}
		
		
	}

	private class PrivateParentProperty extends ReadOnlyObjectPropertyBase<Bone> {

		private Bone value;

		@Override
		public Bone get() {
			return value;
		}

		private void set(Bone newValue) {
			final Bone oldValue = value;
			if ((newValue == null) ? oldValue != null : !newValue.equals(oldValue)) {
				value = newValue;
				if (oldValue != null) {
					oldValue.getChildren().remove(Bone.this);
				}
				if (newValue != null) {
					final List<Bone> children = value.getChildren();
					if (!children.contains(Bone.this)) {
						children.add(Bone.this);
					}
					resetFromParent();
				}
				fireValueChangedEvent();
			}
		}

		@Override
		public Object getBean() {
			return Bone.this;
		}

		@Override
		public String getName() {
			return "parent";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PrivateParentProperty other = (PrivateParentProperty) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

		private Bone getOuterType() {
			return Bone.this;
		}
			
	}
	
	public double getDistance(Point2D point1, Point2D point2){
		return getDistance(point1.getX(),point1.getY(),point2.getX(),point2.getY());
	}
	
	public double getDistance(double x1, double y1, double x2, double y2){
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((angle == null) ? 0 : angle.hashCode());
		result = prime * result + ((currentHead == null) ? 0 : currentHead.hashCode());
		result = prime * result + ((currentTail == null) ? 0 : currentTail.hashCode());
		long temp;
		temp = Double.doubleToLongBits(length);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(maxAngle);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minAngle);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((rotate == null) ? 0 : rotate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Bone))
			return false;
		Bone other = (Bone) obj;
		if (angle == null) {
			if (other.angle != null)
				return false;
		} else if (!angle.equals(other.angle))
			return false;
		if (currentHead == null) {
			if (other.currentHead != null)
				return false;
		} else if (!currentHead.equals(other.currentHead))
			return false;
		if (currentTail == null) {
			if (other.currentTail != null)
				return false;
		} else if (!currentTail.equals(other.currentTail))
			return false;
		if (Double.doubleToLongBits(length) != Double.doubleToLongBits(other.length))
			return false;
		if (Double.doubleToLongBits(maxAngle) != Double.doubleToLongBits(other.maxAngle))
			return false;
		if (Double.doubleToLongBits(minAngle) != Double.doubleToLongBits(other.minAngle))
			return false;
		if (rotate == null) {
			if (other.rotate != null)
				return false;
		} else if (!rotate.equals(other.rotate))
			return false;
		return true;
	}

	@Override
	public int compareTo(Bone other) {
		return Integer.compare(getBoneId(), other.getBoneId());
	}

	public void clearChildren() {
		for(int i = 0; i<getChildren().size(); i++){
			
			Bone child = getChildren().get(i);
			
			child.getContent().clear();
			
			if(!child.getChildren().isEmpty()){
				child.clearChildren();
			}
		}
		
		getChildren().clear();
	}

	
	public void resetState() {
		this.setLength(0);
	}

	
}
