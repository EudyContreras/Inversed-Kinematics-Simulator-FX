
package com.eudycontreras.javafx.fbk.models;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import com.eudycontreras.javafx.fbk.listeners.FBKSegmentEventListener;
import com.eudycontreras.javafx.fbk.listeners.FBKSegmentEventListener.FBKSegmentStatus;
import com.eudycontreras.javafx.fbk.models.FBKIterator.FXIteration;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoublePropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

public class FBKSegment  implements Comparable<FBKSegment>{

	private static int ID = 0;

	public enum PointType {
		START, END
	}
	public enum FBKReach {		
		FORWARD, BACKWARDS, BOTH
	}
	
	public enum FBKinematicsType {
		FORWARD, INVERSED
	}

	public enum FBKConstraintPivot {		
		HEAD, TAIL, NONE;
		
		public boolean isHeadConstrained(){
			return this.compareTo(FBKConstraintPivot.HEAD) == 0;
		}
		
		public boolean isTailConstrained(){
			return this.compareTo(FBKConstraintPivot.TAIL) == 0;
		}
	}
	
	private static final String[] BEAN_NAMES = { "head", "tail", "angle", "rotation", "neighbors", "content" };

	public static final double MATH_PI = Math.PI;

	public static final double DEFAULT_ANGLE = 0.0d;
	
	public static final double DEFAULT_LENGHT = 0.0d;
	
	public static final double ROTATION_RANGE = 360d;

	public static final double MIN_ANGLE = -180.0d;
	public static final double MAX_ANGLE = 180.0d;

	private double length = DEFAULT_LENGHT;

	private final int segmentId;

	private boolean locked = false;
	private boolean constrained = false;
	private boolean nodeBinding = false;
	private boolean isEffector = false;
	private boolean attachedChildren = true;

	private FBKSegmentEventListener listener = null;

	private FBKSegmentStatus segmentStatus = FBKSegmentStatus.ABSOLUTE_PARENT;
	
	private FBKConstraintPivot constraintPivot = FBKConstraintPivot.NONE;
	
	private FBKinematicsType kinematicsType = FBKinematicsType.INVERSED;
	
	private final PrivateDoubleProperty minAngle = new PrivateDoubleProperty(MIN_ANGLE,"min-angle-prop");
	private final PrivateDoubleProperty maxAngle = new PrivateDoubleProperty(MAX_ANGLE,"max-angle-prop");
	
	private final FBKRotationConstraint angleConstraint = new FBKRotationConstraint(maxAngle.get(),minAngle.get());
	private final FBKRotationConstraint angleConstraintMain = new FBKRotationConstraint(maxAngle.get(),minAngle.get());
	private final FBKRotationConstraint angleConstraintMemory = new FBKRotationConstraint(maxAngle.get(),minAngle.get());
	
	private final FBKSegmentState lastState = new FBKSegmentState();

	private final PrivateParentProperty parent = new PrivateParentProperty();
	
	private final PrivateAncestorProperty absoluteAncestor = new PrivateAncestorProperty();
	
	private final PrivateVectorProperty currentHead = new PrivateVectorProperty(BEAN_NAMES[0]);
	private final PrivateVectorProperty currentTail = new PrivateVectorProperty(BEAN_NAMES[1]);

	private final PrivateDoubleProperty angle = new PrivateDoubleProperty(BEAN_NAMES[2]);
	private final PrivateDoubleProperty rotation = new PrivateDoubleProperty(BEAN_NAMES[3]);

	private final ListProperty<FBKSegment> children = new SimpleListProperty<>(this, BEAN_NAMES[4],FXCollections.<FBKSegment>observableArrayList());

	private final ListProperty<Node> content = new SimpleListProperty<>(this, BEAN_NAMES[5],FXCollections.<Node>observableArrayList());

	private final Group group = new Group();

	private Pane pane;

	private Line line;

	private Circle circle;

	public FBKSegment(double length) {
		this(length, DEFAULT_ANGLE, MIN_ANGLE, MAX_ANGLE);
	}

	public FBKSegment(double length, double angle) {
		this(length, angle, MIN_ANGLE, MAX_ANGLE);
	}

	public FBKSegment(double length, double minAngle, double maxAngle) {
		this(length, DEFAULT_ANGLE, minAngle, maxAngle);
	}

	public FBKSegment(FBKVector position, double length) {
		this(position, length, DEFAULT_ANGLE, MIN_ANGLE, MAX_ANGLE);
	}

	public FBKSegment(double length, double angle, double minAngle, double maxAngle) {
		this(FBKVector.ZERO, length, angle, minAngle, maxAngle);
	}

	public FBKSegment(FBKVector position, double length, double angle, double minAngle, double maxAngle) {
		this.segmentId = ID++;
		this.length = length;
		this.minAngle.set(minAngle);
		this.maxAngle.set(maxAngle);
		this.angleConstraintMain.setAngle(angle);
		this.angleConstraintMain.setMinAngle(minAngle);
		this.angleConstraintMain.setMaxAngle(maxAngle);
		this.setAngle(angle);
		this.setCurrentHead(position);
		this.initNodeBindings();
		this.performSetup();
	}
	
	public void setEventListener(FBKSegmentEventListener listener) {
		this.listener = listener;
		if(listener != null){
			if(!isAbsoluteAncestor()){
				if(hasChildren()){
					if(hasSiblings()){
						setSegmentStatus(FBKSegmentStatus.SIBLING);
					}else{
						setSegmentStatus(FBKSegmentStatus.ONLY_CHILD);
					}
				}else{
					setSegmentStatus(FBKSegmentStatus.TAIL);
				}
			}
			
			angle.addListener((ov,oldValue,newValue)->{
				listener.onAngleChanged(FBKSegment.this, newValue.doubleValue());

			});
		}
	}
	
	public boolean areChildrenAttached() {
		return attachedChildren;
	}

	public void setChildrenAttached(boolean attachedChildren) {
		this.attachedChildren = attachedChildren;
	}

	public int getSegmentId() {
		return segmentId;
	}
	
	public FBKSegmentState getLastState() {
		return lastState;
	}

	public final double getLength() {
		return length;
	}

	public void setLength(double length){
		this.length = length;
		if(listener != null){
			listener.onSegmentLengthChange(this, length);
		}
		this.performSetup();
	}
	
	public void resizeFromHead(double newLength, FBKVector newPosition){
		setLength(newLength);
		moveHead(newPosition);
	}
	
	public void resizeFromTail(double newLength, FBKVector newPosition){
		setLength(newLength);
		moveTail(newPosition);
	}
	
	public final double getMinAngle() {
		return minAngle.get();
	}
	
	public final PrivateDoubleProperty getMinAngleProperty() {
		return minAngle;
	}
	
	public final double getMinAngle(FBKSegment initiator) {
		if(initiator == null){
			return angleConstraintMemory.getMinAngle();
		}
		return minAngle.get();
	}
	
	public void setMinAngle(double angle){
		this.minAngle.set(angle);;
		//this.angleConstraintMemory.setMinAngle(minAngle);
	}

	public final double getMaxAngle() {
		return maxAngle.get();
	}
	
	public final PrivateDoubleProperty getMaxAngleProperty() {
		return maxAngle;
	}
	
	public final double getMaxAngle(FBKSegment initiator) {
		if(initiator == null){
			return angleConstraintMemory.getMaxAngle();
		}
		return maxAngle.get();
	}
	
	public void setMaxAngle(double angle){
		this.maxAngle.set(angle);;
		//this.angleConstraintMemory.setMaxAngle(maxAngle);	
	}
	
	public FBKinematicsType getKinematicsType() {
		return kinematicsType;
	}
	
	public FBKConstraintPivot getConstraintPivot(){
		return constraintPivot;
	}

	public FBKSegmentStatus getSegmentStatus() {
		return segmentStatus;
	}

	public void setSegmentStatus(FBKSegmentStatus segmentStatus) {
		if(this.segmentStatus != segmentStatus){
			this.segmentStatus = segmentStatus;
			if(listener != null){
				listener.onSegmentStatusChange(this, segmentStatus);
			}
		}
	}

	public final Node getGroup() {
		return group;
	}

	public final FBKSegment getParent() {
		return parent.get();
	}
	
	public final void setParent(FBKSegment segment){
		this.parent.set(segment);
	}
	
	public final FBKSegment getAbsoluteAncestor() {
		return hasAncestor() ? absoluteAncestor.get() : this;
	}
	
	public final void setAbsoluteAncestor(FBKSegment segment){
		this.absoluteAncestor.set(segment);
	}

	public final ReadOnlyObjectProperty<FBKSegment> parentProperty() {
		return parent;
	}
	
	public final FBKVector getLastHeadPoint(){
		return lastState.getHeadPoint();
	}

	public final FBKVector getLastTailPoint(){
		return lastState.getTailPoint();
	}
	
	public final FBKVector getCurrentHead() {
		return currentHead.get();
	}

	public void setCurrentHead(FBKVector vector) {
		this.currentHead.set(vector);
	}

	public final ReadOnlyObjectProperty<FBKVector> currentHeadProperty() {
		return currentHead;
	}

	public final FBKVector getCurrentTail() {
		return currentTail.get();
	}

	public void setCurrentTail(FBKVector vector) {
		this.currentTail.set(vector);
	}

	public final ReadOnlyObjectProperty<FBKVector> currentTailProperty() {
		return currentTail;
	}

	public final FBKSegmentChain getSkeleton() {
		return skeleton.get();
	}

	public final void setSkeleton(FBKSegmentChain skeleton) {
		this.skeleton.set(skeleton);
	}

	public final ObjectProperty<FBKSegmentChain> skeletonProperty() {
		return skeleton;
	}

	public final ObservableList<Node> getContent() {
		return content.get();
	}

	public final void setContent(ObservableList<Node> list) {
		this.content.set(list);
	}

	public boolean isLocked() {
		return locked;
	}
	
	public final double getAngle() {
		return angle.get();
	}

	public void setAngle(double angle) {
		this.lastState.setAngle(this.angle.get());
		this.angle.set(angle);
	}

	public PrivateDoubleProperty angleProperty() {
		return angle;
	}

	public final double getRotation() {
		return rotation.get();
	}

	public void setRotation(double rotation) {
		this.rotation.set(rotation);
	}
	
	public void applyRotation(double rotateValue) {
		setRotation(borderAngle(getParent().getRotation() + getAngle()));
	}

	public boolean isEffector() {
		return isEffector;
	}

	public void setEffector() {
		
		FBKSegment.traverseTree(this, segment-> {
			if(!segment.equals(this)){
				segment.setEffector(false);
			}
		});
		setEffector(true);	
	}
	
	public void setEffector(boolean isEffector) {
		this.lastState.setEffector(this.isEffector);
		this.isEffector = isEffector;		
		if(listener != null){
			listener.onEffectorStatusChanged(this,isEffector);
		}		
	}

	public PrivateDoubleProperty getRotationProperty() {
		return rotation;
	}

	public final ObservableList<FBKSegment> getChildren() {
		return children.get();
	}

	public final void setChildren(ObservableList<FBKSegment> children) {
		this.children.set(children);
	}

	public final ListProperty<FBKSegment> childrenProperty() {
		return children;
	}
	
	public final boolean isConstrained() {
		return constrained;
	}

	public final boolean hasDecendants(){
		return !children.isEmpty();
	}
	
	public final boolean hasAncestor(){
		return parent.get() != null;
	}
	
	public final boolean isChildless(){
		return children.isEmpty();
	}
	
	public final boolean hasMultipleChildren(){
		return children.size() >= 2;
	}
	
	public final boolean hasChildren(){
		return !children.isEmpty();
	}
	
	public final boolean isAbsoluteAncestor(){
		return !hasAncestor();
	}
	
	private boolean hasSiblings() {
		if(hasAncestor()){			
			if(getParent().getChildren().size() >= 2){
				return true;
			}
		}
		return false;
	}
	
	public List<FBKSegment> getAllDescendants(){
		List<FBKSegment> descendants = new LinkedList<>();
		
		FBKSegment.traverseDescendants(this, descendant -> {
			descendants.add(descendant);
		});
		
		return descendants;
	}
	
	public List<FBKSegment> getAllSiblings(){
		List<FBKSegment> siblings = new LinkedList<>();
		
		FBKSegment.traverseSiblings(this, sibling -> {
			siblings.add(sibling);
		});
		
		return siblings;
	}
	
	public void clearDescendants() {
		if(hasChildren()){			
			FBKSegment.traverseDescendants(this, (s) -> {
				s.getContent().clear();
				s.clearDescendants();
			});

			children.clear();		
			
			if(hasSiblings()){
				FBKSegment.traverseSiblings(this, (s) -> {
					if(s.hasChildren()){
						s.getContent().clear();
						s.clearDescendants();
					}
				});							
			}
			
			if(listener != null){
				if(!isAbsoluteAncestor() && !hasSiblings()){			
					if(!hasChildren()){
						setSegmentStatus(FBKSegmentStatus.TAIL);						
					}
				}
			}
		}
		setLength(0);
	}
	
	public void addChild(FBKSegment child) {
		
		if(child == null) return;
		
		if(!children.contains(child)){
			
			children.add(0,child);
			
			if (listener != null) {
				if (!isAbsoluteAncestor()) {
					if (!hasSiblings()) {
						setSegmentStatus(FBKSegmentStatus.ONLY_CHILD);
					
					}else{
						setSegmentStatus(FBKSegmentStatus.SIBLING);
					}
				} 			
			}
		
			if(!nodeBinding)
			child.setParent(this);
			
			child.setAbsoluteAncestor(null);
			
			if(isAbsoluteAncestor()){
				traverseDescendants(this, segment-> {
					segment.setAbsoluteAncestor(this);
				});
			}else{
				traverseDescendants(this, segment-> {
					segment.setAbsoluteAncestor(this.getAbsoluteAncestor());
				});
			}
			
//			if (children.stream().anyMatch(c -> c.isConstrained())){
//				
//				children.get(0).setConstrained(false);
//				children.get(0).setConstrained(true);
//				System.out.println("SHIT");
//			}
		}
	}

	public void addChildren(FBKSegment... segments) {
		for(FBKSegment segment : segments){
			addChild(segment);
		}
	}

	public void removeChild(FBKSegment segment) {
		if(segment == null) return;
		
		if(children.contains(segment)){
			children.remove(segment);
			
			if(listener != null){
				if(!isAbsoluteAncestor() && !hasSiblings()){			
					if(!hasChildren()){
						setSegmentStatus(FBKSegmentStatus.TAIL);						
					}
				}
			}

			if(!nodeBinding)
			segment.setParent(null);
		}
	}

	public void removeChildren(FBKSegment... segments) {
		for(FBKSegment segment : segments){
			removeChild(segment);
		}
	}
	
	public static void traverseTree(FBKSegment segment, FBKSegmentAction action){
		
		FBKSegment parent = segment.getAbsoluteAncestor();
		
		action.performAction(parent);
		
		FBKSegment.traverseDescendants(parent, action);		
	}

	public static void traverseDescendants(FBKSegment segment, FBKSegmentAction action){
		for(FBKSegment descendant : segment.getChildren()){
			action.performAction(descendant);
			traverseDescendants(descendant, action);
		}
	}
	
	public static void traverseSiblings(FBKSegment segment, FBKSegmentAction action){
		if(segment.hasAncestor()){
			
			if(segment.hasSiblings()){
				
				for(FBKSegment descendant : segment.getParent().getChildren()){
					
					if(descendant.equals(segment)) continue;
					
					action.performAction(descendant);
				}
			}
		}
	}

	public static boolean allSiblingsMeetCondition(FBKSegment segment, Predicate<FBKSegment> predicate){
		
		return segment.isAbsoluteAncestor() ? true : segment.getParent().getChildren().stream().allMatch(predicate);
	}
	
	public static boolean anySiblingsMeetCondition(FBKSegment segment, Predicate<FBKSegment> predicate){
		
		return segment.isAbsoluteAncestor() ? true : segment.getParent().getChildren().stream().anyMatch(predicate);
	}

	public static FBKSegment traverseAncestor(FBKSegment segment, FBKSegmentAction action){
		FBKSegment parent = segment.getParent();
		
		if(parent != null){
			
			if(action != null){
				action.performAction(parent);
			}
			
			while(parent.getParent() != null){
				
				parent = parent.getParent();
				
				if(action != null){
					action.performAction(parent);
				}	
			}
			
			return parent;
		}
		
		return segment;
	}
	
	public void switchKinematics(){
		if(getKinematicsType() == FBKinematicsType.FORWARD){
			setKinematicsType(FBKinematicsType.INVERSED);
			
		}else{
			setKinematicsType(FBKinematicsType.FORWARD);
		}
	}
	
	public void setKinematicsType(FBKinematicsType type){
		setKinematicsType(type, true);
	}
	
	public void setKinematicsType(FBKinematicsType type, boolean propagate){
		this.setKinematicsType(type, propagate, false);
	}
	
	public void setKinematicsType(FBKinematicsType type, boolean propagate, boolean affectAncestors){
				
		if(!propagate) {
			applyKinematicsType(type,this, false);
			return;
		}
			
		if(!affectAncestors){
			
			applyKinematicsType(type,this, false);
			
			FBKSegment.traverseDescendants(this, segment->{
				segment.applyKinematicsType(type,this, false);
			});
			
			removeChildrenConstraints();
		}else{
			FBKSegment.traverseTree(this, segment->{
				segment.applyKinematicsType(type,this, false);
			});
		}
	}
	
	private void applyKinematicsType(FBKinematicsType type, FBKSegment initiator){
		applyKinematicsType(type, initiator, false);
	}

	private void applyKinematicsType(FBKinematicsType type, FBKSegment initiator, boolean propagate){
		this.lastState.setKinematicsType(this.kinematicsType);
		this.kinematicsType = type;
		
		if(listener != null){
			listener.onKinematicsTypeChanged(this,type);
		}
		switch(type){
		case FORWARD:
			angleConstraint.setMinAngle(getMinAngle());
			angleConstraint.setMaxAngle(getMaxAngle());
			
			computateAngleConstraints();
			
			break;
		case INVERSED:
			
			setMinAngle(angleConstraint.getMinAngle());
			setMaxAngle(angleConstraint.getMaxAngle());
			
			break;	
		}
		
		if(!propagate) return;
				
		if(hasAncestor()){
			if(!getParent().equals(initiator)){
				getParent().applyKinematicsType(type, this);
			}
		}
		
		if(isChildless()) return;
		
		FBKSegment.traverseDescendants(this, segment-> {
			segment.applyKinematicsType(type, this, true);
		});
	}
	
	public void setLocked(boolean locked) {
		setLocked(locked, false);
	}
	
	public void setLocked(boolean locked, boolean lockChildren) {
		
		this.lastState.setLocked(this.locked);
		
		if(isLocked()){
			if(!locked){ 	
				
				minAngle.set(angleConstraintMemory.getMinAngle());;
				maxAngle.set(angleConstraintMemory.getMaxAngle());;
				
				this.locked = false;
				
				if(listener != null){
					listener.onSegmentUnlocked(this);
				}
				
				traverseSiblings(this, segment->{
					segment.setLocked(locked, lockChildren);
				});
				
				if(lockChildren){
					for(FBKSegment child : getChildren()){
						child.setLocked(locked, true);;
					}
				}
			}
		}else{
			if(locked){
				
				computateAngleConstraints();
				
				this.locked = true;
				
				if(listener != null){
					listener.onSegmentLocked(this);
				}
				
				traverseSiblings(this, segment->{
					segment.setLocked(locked, lockChildren);
				});
				
				if(lockChildren){
					for(FBKSegment child : getChildren()){
						child.setLocked(locked, true);;
					}
				}
			}
		}
	}

	public void computateAngleConstraints(){
		this.setMinAngle(getAngle());
		this.setMaxAngle(getAngle());
	}
	
	private void saveCoordinates(){
		this.lastState.setHeadPoint(currentHead.get());
		this.lastState.setTailPoint(currentTail.get());
	}
	
	public void constraintHead(FBKVector base) {
		moveHead(base);
	}

	public void constraintTail(FBKVector base) {
		moveTail(base);
	}

	public void enforseConstraint(FBKVector base) {
		enforseConstraint(base, getConstraintPivot());
	}
	
	public void enforseConstraint(FBKVector base, FBKConstraintPivot pivot) {
		switch(pivot){
		case HEAD:
			constraintHead(base);
			break;
		case TAIL:
			constraintTail(base);
			break;	
		case NONE:
			break;	
		}
	}
	
	public void setConstrained(boolean constrained) {
		if(constrained){
			this.setConstrained(constrained, FBKConstraintPivot.HEAD);
		}else{
			this.setConstrained(constrained, FBKConstraintPivot.NONE);
		}
	}
	
	public void setConstrained(boolean constrained, FBKConstraintPivot constraintPivot) {
		setConstrained(constrained, constraintPivot, true);
	}
	
	public void setConstrained(boolean constrained, FBKConstraintPivot constraintPivot, boolean constraintSiblings) {
		this.lastState.setConstrained(this.constrained);
		this.lastState.setConstraintPivot(this.constraintPivot);
		this.constraintPivot = constraintPivot;
		this.constrained = constrained;
		
		if(listener != null){
			if(constrained){
				listener.onSegmentConstrained(this);
			}else{
				listener.onSegmentUnconstrained(this);
			}
		}	
		
		saveCoordinates();
		
		if(!constraintSiblings) return;
		
		traverseSiblings(this, segment->{
			segment.lastState.setConstrained(segment.constrained);
			segment.lastState.setConstraintPivot(segment.constraintPivot);
			segment.constrained = constrained;
			segment.constraintPivot = constraintPivot;		
			if(segment.listener != null){
				if(constrained){
					segment.listener.onSegmentConstrained(segment);
				}else{
					segment.listener.onSegmentUnconstrained(segment);
				}
			}
			segment.saveCoordinates();
		});
	}

	private void enforseDescendantConstraint(FBKSegment initiator) {

		if(this.equals(initiator))
			return;
		
		if(!isChildless()){
			performHeadMove(getLastHeadPoint(), null);
			
			if(getParent() != null && !getParent().isConstrained()){
				getParent().performTailMove(getLastHeadPoint(), this);
			}	
		}else{
			if(hasAncestor()){
				performHeadMove(getParent().getCurrentTail(), initiator);
				getParent().performTailMove(getCurrentHead(), null);
			}
			
			if(getConstraintPivot() == FBKConstraintPivot.HEAD){
				performHeadMove(getLastHeadPoint(), null);
				performTailMove(getLastTailPoint(), null);
			}else{
				performTailMove(getLastTailPoint(), null);
			}
		}	
	}
	
	private void enforseAncestorConstraint(FBKVector point, FBKSegment initiator) {

		if(initiator.equals(this))
			return;
		
		if(!isChildless()){
			
			performTailMove(point, this);
			
			performHeadMove(getLastHeadPoint(), null);
		}else{
			
			if(getConstraintPivot() == FBKConstraintPivot.HEAD){
				performTailMove(point, this);
				
				performHeadMove(getLastHeadPoint(), null);
			}else{
				performTailMove(getLastTailPoint(), this);
				
				performHeadMove(point, null);
			}
		}
	}

	public void removeAllConstraints() {
		FBKSegment.traverseTree(this, (segment)->{
			segment.setConstrained(false);
		});
	}
	
	public void removeChildrenConstraints() {
		FBKSegment.traverseDescendants(this, (segment)->{
			segment.setConstrained(false);
		});
	}

	public void moveHead(double x, double y) {		
		moveHead(new FBKVector(x, y));
	}

	public void moveHead(FBKVector vector) {
		if(getKinematicsType() == FBKinematicsType.FORWARD){
			if(hasAncestor()){
				if(!getParent().isConstrained()){
					if(getConstraintPivot() == FBKConstraintPivot.TAIL){
						
						performHeadMove(vector, this);
						performTailMove(getLastTailPoint(),null);
						
						return;
					}else{
						removeChildrenConstraints();
						getParent().setConstrained(true);
						getParent().saveCoordinates();	
						
					}
				}else{
					if(getConstraintPivot() == FBKConstraintPivot.TAIL){
						
						performHeadMove(vector, this);
						performTailMove(getLastTailPoint(),null);
						
						return;
					}
				}
			}else{
				
				if(getConstraintPivot() == FBKConstraintPivot.TAIL){
					
					performHeadMove(vector, this);
					performTailMove(getLastTailPoint(),null);
					
					saveCoordinates();	
					return;
				}else{
					if(isConstrained()){
						removeChildrenConstraints();
						setConstrained(false);
						saveCoordinates();	
					}
				}
			}
			
			performHeadMove(vector, null);
			
			saveCoordinates();
		}
		else{
			if(isConstrained()){
				
//				setConstrained(false);
//				
				if(isAbsoluteAncestor()){
					
					if(getConstraintPivot() == FBKConstraintPivot.TAIL){
//						
						performHeadMove(vector, null);
						performTailMove(getLastTailPoint(),null);
						
						saveCoordinates();	
						return;
					}
				}else{
					
					saveCoordinates();	

					setConstrained(false);
//					
//					if(getConstraintPivot() == FBKConstraintPivot.TAIL){
//						
//						performHeadMove(vector, this);
//						performTailMove(getLastTailPoint(),null);
//						
//						return;
//					}
					
					return;

				}
			}
			
			performHeadMove(vector, null);
		}
	}
	
	public void moveTail(double x, double y) {		
		moveTail(new FBKVector(x, y));
	}

	public void moveTail(FBKVector vector) {
		if(getKinematicsType() == FBKinematicsType.FORWARD){
			if(!isConstrained()){
				removeChildrenConstraints();
				setConstrained(true);
				setMaxAngle(MAX_ANGLE);
				setMinAngle(MIN_ANGLE);		
			}	
			if(getConstraintPivot() == FBKConstraintPivot.HEAD){
				
				performTailMove(vector, this); //IF null creates double pivot rotation OH YEAH!
		
				performHeadMove(getLastHeadPoint(), null);
			}else{
				
				performHeadMove(vector, null);
			}
		
			saveCoordinates();
		}else{
			if(isConstrained()){

				if(getConstraintPivot() == FBKConstraintPivot.HEAD){
					performTailMove(vector, this);
					
					performHeadMove(getLastHeadPoint(), null);
				}else{
					performHeadMove(vector, null);
				}
			
				saveCoordinates();
				return;
			}
			
			performTailMove(vector, null);
		}			
	}

	private void moveTail(FBKVector point, FBKSegment initiator) {
		if(isConstrained()){
			
			enforseAncestorConstraint(point, initiator);
			
			return;
		}
		
		performTailMove(point,initiator);
	}

	private void performHeadMove(FBKVector point, FBKSegment initiator) {

		final FBKSegment parent = getParent();

		assert initiator == null || initiator.equals(parent);

		if (!getCurrentHead().equals(point)) {

			this.setCurrentHead(point);

			final double alpha = getAngle(point, getCurrentTail());
			final double rotateValue = MAX_ANGLE * (alpha / MATH_PI);

			final double minAngle = getMinAngle(initiator);
			final double maxAngle = getMaxAngle(initiator);

			if ((initiator != null) && (withinRange(minAngle, maxAngle))) {

				final double initiatorRotate = initiator.getRotation();

				final double minAngleAlpha = Math.min(borderAngle(rotateValue - initiatorRotate), maxAngle);

				final double angle = Math.max(minAngle, minAngleAlpha);

				this.setAngle(angle);

				this.setRotation(borderAngle(initiatorRotate + getAngle()));

				final double rotation = getRotation() * (MATH_PI / MAX_ANGLE);

				this.setCurrentTail(createVector(point, rotation));

			} else {

				this.setRotation(rotateValue);

				this.setCurrentTail(createVector(point, alpha));
				
				if ((initiator == null) && (parent != null)) {

					setAngle(borderAngle(rotateValue - parent.getRotation()));

					if(isConstrained()){
						if(parent.isConstrained()){
							parent.performTailMove(point, this);
						}else{
							parent.moveTail(point, this);
						}
					}else{
						parent.moveTail(point, this);
					}
				}else{
				
					if(parent != null){
						final double angle = borderAngle(rotateValue - parent.getRotation());
						
						setAngle(angle);	
					}
				}
			}
			
			if(getKinematicsType() == FBKinematicsType.FORWARD){
				computateAngleConstraints();
			}
			
			if(areChildrenAttached()){
				updateChildren(initiator);
			}
			
			if (listener != null) {
				listener.onPositionUpdate(this, getCurrentHead(), getCurrentTail(), getAngle(), getRotation(), getLength());
			}
		}
	}

	private void performTailMove(FBKVector point, FBKSegment initiator) {

		final FBKSegment parent = getParent();

		assert initiator == null || initiator.equals(parent);

		if (!getCurrentTail().equals(point)) {

			this.setCurrentTail(point);

			double alpha = getAngle(point, getCurrentHead());

			double rotateValue = borderAngle(MAX_ANGLE + (MAX_ANGLE * alpha) / MATH_PI);

			if (initiator != null) {

				final double minAngle = initiator.getMinAngle();
				final double maxAngle = initiator.getMaxAngle();

				if (withinRange(minAngle, maxAngle)) {

					final double initiatorRotate = initiator.getRotation();

					final double minAngleAlpha = Math.min(borderAngle(initiatorRotate - rotateValue), maxAngle);

					final double childAngle = Math.max(minAngle, minAngleAlpha);

					rotateValue = borderAngle(initiatorRotate - childAngle);

					alpha = (rotateValue - MIN_ANGLE) * (MATH_PI / MAX_ANGLE);
				}
			}

			this.setRotation(rotateValue);

			this.setCurrentHead(createVector(point, alpha));

			if (parent != null) {

				final double angle = borderAngle(rotateValue - parent.getRotation());
				
				setAngle(angle);	
				
				if(initiator != null){
					if(!initiator.isConstrained()){
						parent.moveTail(getCurrentHead(), this);
					}else{
						
						if(!isConstrained()){
							parent.performTailMove(getCurrentHead(), this);
						}
					}
				}else{
					parent.moveTail(getCurrentHead(), this);
				}
			}

			if(areChildrenAttached()){
				updateChildren(initiator);
			}
			
			if (listener != null) {
				listener.onPositionUpdate(this, getCurrentHead(), getCurrentTail(), getAngle(), getRotation(), getLength());
			}
		}
	}
	
//	private void preventStretch(FBKVector vector){
//		InfoCapsule<Double, FBKSegment> data = computeDistanceToConstraint(this);
//		
//		double distance = vector.distance(data.getArgB().getConstrainedVector());
//		
//		System.out.println("Mouse to Constraint : " +distance);
//		System.out.println("Effector to Constraint : " +data.getArgA());
//	}
//	
//	private FBKVector getConstrainedVector() {
//		if(isChildless()){
//			return getCurrentTail();
//		}else{
//			return getCurrentHead();
//		}
//	}
//
//	private static InfoCapsule<Double, FBKSegment> computeDistanceToConstraint(FBKSegment effector){
//		
//		InfoCapsule<Boolean, FBKSegment> info = isConstraintChild(effector);
//		
//		InfoCapsule<Double, FBKSegment> data =  new InfoCapsule<>(0d,info.getArgB());
//		 
//		double accumulatedLength = effector.getLength();
//		
//		if(info.getArgA() == true){
//			
//			FBKSegment parent = info.getArgB().getParent();
//			
//			accumulatedLength += info.getArgB().getLength();
//			
//			while(parent != null && !parent.equals(effector)){
//				
//				accumulatedLength += parent.getLength();
//				
//				parent = parent.getParent();		
//			}
//			
//			accumulatedLength += parent.getLength();
//		
//		}else{
//			FBKSegment parent = effector.getParent();
//			
//			while(parent != null && !parent.isConstrained()){
//				
//				accumulatedLength += parent.getLength();
//				
//				parent = parent.getParent();		
//			}
//			
//			if(parent!=null){
//				accumulatedLength += parent.getLength();	
//				
//				data.setArgA(accumulatedLength);
//				data.setArgB(parent);
//			}else{
//				accumulatedLength = Integer.MAX_VALUE;
//			}
//		
//		}
//		
//		return data;
//	}
//	
//	private static InfoCapsule<Boolean, FBKSegment> isConstraintChild(FBKSegment effector) {
//		InfoCapsule<Boolean, FBKSegment> info = new InfoCapsule<>(false,null);
//		
//		traverseDescendants(effector, (s)-> {
//			if(s.isConstrained()){
//				info.setArgA(true);
//				info.setArgB(s);
//			}
//		});
//		
//		return info;
//	}

	private void resetFromParent() {
		final FBKSegment parent = getParent();
		setSkeleton(parent.getSkeleton());
		setCurrentHead(parent.getCurrentTail());
		performSetup();
	}

	private void performSetup() {
		final FBKSegment parent = getParent();

		final double rotateValue = (getParent() == null) ? getAngle() : borderAngle(parent.getRotation() + getAngle());

		final double angle = MATH_PI * (rotateValue / MAX_ANGLE);

		setRotation(rotateValue);

		setCurrentTail(createVector(getCurrentHead(), angle));

		if(areChildrenAttached()){
			FBKIterator.Iterate(getChildren(), childSetupIterator, this);
		}
	}
	

	public void computeFBK() {
		FBKSegment parent = getParent();
		
		final double alpha = getAngle(getCurrentHead(), getCurrentTail());
		
		final double rotateValue = MAX_ANGLE * (alpha / MATH_PI);
		
		final double angle = borderAngle(rotateValue - parent.getRotation());
		
		setAngle(angle);
		
		setLocked(getParent().getChildren().stream().anyMatch(s -> s.isLocked()));		
	}

	private final FXIteration<FBKSegment> childSetupIterator = (child, index, action, initiator) -> {
		child.resetFromParent();
	};

	private final FXIteration<FBKSegment> childUpdateIterator = (child, index, action, initiator) -> {
		if (!child.equals(initiator)) {
			if(!child.isConstrained()){
				child.performHeadMove(FBKSegment.this.getCurrentTail(), FBKSegment.this);
			}else{
				child.enforseDescendantConstraint(initiator);
			}
		}
	};
	
	public void applyRotationAngle(double angle) {

		if (angle <= (Math.abs(getMinAngle()) - 10) && angle >= -(getMaxAngle() + 10)) {

			double x, y;

			double distance = getLength();

			x = Math.round(getCurrentHead().getX() + (Math.cos(Math.toRadians(angle)) * distance));
			y = Math.round(getCurrentHead().getY() + (Math.sin(Math.toRadians(angle)) * distance));

			moveTail(x, y);
		}
	}

	public void applyRotationAngle(PointType type, double value) {

		double angle = borderAngle(getParent().getRotation() + getAngle()) + value;

		if (angle <= (Math.abs(getMinAngle()) - 10) && angle >= -(getMaxAngle() + 10)) {

			double x, y;

			double distance = length;

			x = Math.round(getCurrentHead().getX() + (Math.cos(Math.toRadians(angle)) * distance));
			y = Math.round(getCurrentHead().getY() + (Math.sin(Math.toRadians(angle)) * distance));

			switch(type){
			case START:
				moveHead(x, y);
				break;
			case END:
				moveTail(x, y);
			break;
			}
		}
	}

	private void updateChildren(FBKSegment initiator) {
		FBKIterator.Iterate(getChildren(), childUpdateIterator, initiator);
	}

	@SuppressWarnings("unused")
	private boolean withinRange(double angle) {
		return (angle >= minAngle.get()) && (angle <= maxAngle.get());
	}
	
	private boolean withinRange(double minAngle, double maxAngle) {
		return (minAngle > MIN_ANGLE-1) || (maxAngle < MAX_ANGLE-1);
	}

	private double borderAngle(double value) {
		if (value < MIN_ANGLE) {
			return value + ROTATION_RANGE;
		}
		if (value > MAX_ANGLE) {
			return value - ROTATION_RANGE;
		}
		return value;
	}

	private FBKVector createVector(FBKVector origin, double angle) {
		return new FBKVector(origin.getX() + Math.cos(angle) * getLength(), origin.getY() + Math.sin(angle) * getLength());
	}

	private static double getAngle(FBKVector vectorOne, FBKVector vectorTwo) {
		return Math.atan2(vectorTwo.getY() - vectorOne.getY(), vectorTwo.getX() - vectorOne.getX());
	}

	public interface FBKSegmentAction {

		void performAction(FBKSegment segment);
	}


	public interface FBKSegmentPredicate {

		boolean checkCondition(FBKSegment segment);
	}
	
	private ObjectProperty<FBKSegmentChain> skeleton = new ObjectPropertyBase<FBKSegmentChain>() {

		private FBKSegmentChain oldSkeleton = null;

		@Override
		protected void invalidated() {
			final FBKSegmentChain newSkeleton = get();
			if ((newSkeleton == null) ? oldSkeleton != null : !newSkeleton.equals(oldSkeleton)) {
				if (oldSkeleton != null) {
					oldSkeleton.getSegmentsWritable().remove(FBKSegment.this);
				}
				if (newSkeleton != null) {
					newSkeleton.getSegmentsWritable().add(0,FBKSegment.this);
				}
				final FBKSegment parentBone = getParent();
				if (parentBone != null) {
					parentBone.setSkeleton(newSkeleton);
				}
				for (final FBKSegment child : children.get()) {
					if (child != null) {
						child.setSkeleton(newSkeleton);
					}
				}
				oldSkeleton = newSkeleton;
			}
		}

		@Override
		public Object getBean() {
			return FBKSegment.this;
		}

		@Override
		public String getName() {
			return "skeleton";
		}
	};

	private class PrivateParentProperty extends ReadOnlyObjectPropertyBase<FBKSegment> {

		private FBKSegment value;

		@Override
		public FBKSegment get() {
			return value;
		}

		private void set(FBKSegment newValue) {
			final FBKSegment oldValue = value;
			if ((newValue == null) ? oldValue != null : !newValue.equals(oldValue)) {
				value = newValue;
				if (oldValue != null) {
					oldValue.getChildren().remove(FBKSegment.this);
				}
				if (newValue != null) {
					
					final List<FBKSegment> children = value.getChildren();
					
					if (!children.contains(FBKSegment.this)) {
						children.add(0,FBKSegment.this);
					}
					resetFromParent();
				}
				fireValueChangedEvent();
			}
		}

		@Override
		public Object getBean() {
			return FBKSegment.this;
		}

		@Override
		public String getName() {
			return "parent";
		}
	}
	
	private class PrivateAncestorProperty extends ReadOnlyObjectPropertyBase<FBKSegment> {

		private FBKSegment value;

		@Override
		public FBKSegment get() {
			return value;
		}

		private void set(FBKSegment newValue) {
			this.value = newValue;
		}

		@Override
		public Object getBean() {
			return FBKSegment.this;
		}

		@Override
		public String getName() {
			return "absolute ancestor";
		}
	}
	
	public static class InfoCapsule<T,A>{
		
		private T argA;
		private A argB;

		public InfoCapsule(T argA, A argB) {
			super();
			this.argA = argA;
			this.argB = argB;
		}

		public T getArgA() {
			return argA;
		}

		public void setArgA(T argA) {
			this.argA = argA;
		}

		public A getArgB() {
			return argB;
		}

		public void setArgB(A argB) {
			this.argB = argB;
		}		
	}

	public static class FBKSegmentState {
		
		private boolean locked = false;
		private boolean constrained = false;
		private boolean effector = false;
		
		private double angle = 0;
		
		private FBKVector headPoint = FBKVector.ZERO;
		private FBKVector tailPoint = FBKVector.ZERO;
		
		private FBKConstraintPivot constraintPivot = FBKConstraintPivot.NONE;
		
		private FBKinematicsType kinematicsType = FBKinematicsType.INVERSED;

		public double getAngle() {
			return angle;
		}

		private void setAngle(double angle) {
			this.angle = angle;
		}

		public boolean isLocked() {
			return locked;
		}

		private void setLocked(boolean locked) {
			this.locked = locked;
		}
		
		public boolean isEffector() {
			return effector;
		}

		private void setEffector(boolean effector) {
			this.effector = effector;
		}

		public boolean isConstrained() {
			return constrained;
		}

		private void setConstrained(boolean constrained) {
			this.constrained = constrained;
		}

		public FBKVector getHeadPoint() {
			return headPoint;
		}

		private void setHeadPoint(FBKVector headPoint) {
			this.headPoint = headPoint;
		}

		public FBKVector getTailPoint() {
			return tailPoint;
		}

		private void setTailPoint(FBKVector tailPoint) {
			this.tailPoint = tailPoint;
		}

		public FBKConstraintPivot getConstraintPivot() {
			return constraintPivot;
		}

		private void setConstraintPivot(FBKConstraintPivot constraintPivot) {
			this.constraintPivot = constraintPivot;
		}

		public FBKinematicsType getKinematicsType() {
			return kinematicsType;
		}

		private void setKinematicsType(FBKinematicsType kinematicsType) {
			this.kinematicsType = kinematicsType;
		}
	}
	
	public static class FBKRotationConstraint {

		public double maxAngle;
		public double minAngle;
		
		public double angle;
		
		public static FBKRotationConstraint get(double maxAngle, double minAngle) {
			return new FBKRotationConstraint(maxAngle, minAngle);
		}

		public FBKRotationConstraint(double maxAngle, double minAngle) {
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

		public double getAngle() {
			return angle;
		}

		public void setAngle(double angle) {
			this.angle = angle;
		}
	}

	private class PrivateVectorProperty extends ReadOnlyObjectPropertyBase<FBKVector> {

		private final String name;
		private FBKVector value = FBKVector.ZERO;

		private PrivateVectorProperty(String name) {
			this.name = name;
		}

		@Override
		public FBKVector get() {
			return value;
		}

		protected void set(FBKVector value) {
			this.value = value;
			fireValueChangedEvent();
		}

		@Override
		public Object getBean() {
			return FBKSegment.this;
		}

		@Override
		public String getName() {
			return name;
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
			PrivateVectorProperty other = (PrivateVectorProperty) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
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

		private FBKSegment getOuterType() {
			return FBKSegment.this;
		}	
	}

	public class PrivateDoubleProperty extends ReadOnlyDoublePropertyBase {

		private String name;
		private double value;

		private PrivateDoubleProperty() {
			this(0.0d,"DEFAULT_DOUBLE_PROPERTY");
		}
		
		private PrivateDoubleProperty(String name) {
			this(0.0d, name);
		}
		
		private PrivateDoubleProperty(double value) {
			this(value,"DEFAULT_DOUBLE_PROPERTY");
		}
		
		private PrivateDoubleProperty(double value, String name) {
			this.name = name;
			this.set(value);
		}

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
			return FBKSegment.this;
		}

		@Override
		public String getName() {
			return name;
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
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
				return false;
			return true;
		}

		private FBKSegment getOuterType() {
			return FBKSegment.this;
		}
	
	}
	
	@Override
	public int compareTo(FBKSegment other) {
		return Integer.compare(this.getSegmentId(), other.getSegmentId());
	}
	
	private void initNodeBindings() {
		nodeBinding = true;
		final Rotate rotate = new Rotate(getRotation(), 0.0, 0.0);
		rotate.angleProperty().bind(getRotationProperty());
		
		group.setUserData(this);
		group.getTransforms().setAll(rotate);

		group.translateXProperty().bind(new DoubleBinding() {
			{
				super.bind(currentHeadProperty());
			}

			@Override
			protected double computeValue() {
				return getCurrentHead().getX();
			}
		});
		group.translateYProperty().bind(new DoubleBinding() {
			{
				super.bind(currentHeadProperty());
			}

			@Override
			protected double computeValue() {
				return getCurrentHead().getY();
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
		children.addListener(new ListChangeListener<FBKSegment>() {
			@Override
			public void onChanged(Change<? extends FBKSegment> change) {
				while (change.next()) {
					if (!change.wasPermutated()) {
						for (final FBKSegment child : change.getRemoved()) {
							if (FBKSegment.this.equals(child.getParent())) {
								child.parent.set(null);
							}
						}
						for (final FBKSegment child : change.getAddedSubList()) {
							if (!FBKSegment.this.equals(child.getParent())) {
								child.parent.set(FBKSegment.this);
							}
						}
					}
				}
			}
		});
	}

	public void initGraphics(Pane pane) {
		this.pane = pane;
		this.circle = new Circle(0, 0, 1);
		this.circle.setFill(Color.rgb(0, 180, 220));
		this.circle.setUserData(this);
		this.circle.setOnMouseDragged(e -> {
			FBKVector pos = new FBKVector(e.getSceneX(), e.getSceneY());
			moveHead(pos);
			drawGraphics();
		});

		this.line = new Line();
		this.line.setStroke(Color.rgb(245, 245, 245));
		this.line.setStartX(getCurrentHead().getX());
		this.line.setStartY(getCurrentHead().getY());
		this.line.setEndX(getCurrentTail().getX());
		this.line.setEndY(getCurrentTail().getY());
		this.line.setStrokeWidth(0.5);
		
		this.pane.getChildren().add(0,line);
		this.pane.getChildren().add(circle);
	}

	public void drawGraphics() {
		drawGraphics(this);
	}

	private void drawGraphics(FBKSegment source) {
		line.setStartX(getCurrentHead().getX());
		line.setStartY(getCurrentHead().getY());

		line.setEndX(getCurrentTail().getX());
		line.setEndY(getCurrentTail().getY());

		circle.setCenterX(getCurrentHead().getX());
		circle.setCenterY(getCurrentHead().getY());

		if(getParent()!=null){
			parent.get().drawGraphics(this);
		}

		FBKIterator.Iterate(children,drawIterator,source);
	}

	private final FXIteration<FBKSegment> drawIterator = (current, index, action, args) -> {
		if (!current.equals(args)) {
			current.drawGraphics(FBKSegment.this);
		}
	};

	public FBKSegmentEventListener getListener() {
		return listener;
	}
}
