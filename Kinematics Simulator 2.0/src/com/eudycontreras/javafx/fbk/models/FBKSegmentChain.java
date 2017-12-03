
package com.eudycontreras.javafx.fbk.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.eudycontreras.javafx.fbk.models.FBKSegment.FBKConstraintPivot;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class FBKSegmentChain extends Parent {

	public enum FBKAttachPivot {
		HEAD, TAIL
	}

	private float segmentLength = 15;
	
	private FBKSegment absoluteAncestor;

	private FBKSegment head;
	private FBKSegment tail;
	
	private FBKSegment root;

	private FBKVector base;

	private LinkedList<FBKAttachment> attachments = new LinkedList<>();
	
	private final ObservableList<FBKSegment> bones = FXCollections.observableArrayList();

	private final ObservableList<FBKSegment> bonesView = FXCollections.unmodifiableObservableList(bones);

	private FBKConstraintPivot constraintPivot = FBKConstraintPivot.NONE;

	public interface FBKSegmentAction {

		void performAction(FBKSegment segment);
	}

	public FBKSegmentChain() {
		
		bones.addListener(new ListChangeListener<FBKSegment>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends FBKSegment> change) {

				final List<Node> children = getChildren();

				while (change.next()) {
					children.subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();

					final List<Node> nodes = new ArrayList<>(change.getAddedSize());

					for (final FBKSegment bone : change.getAddedSubList()) {
						nodes.add(bone.getGroup());
					}

					children.addAll(change.getFrom(), nodes);
				}
			}
		});
	}

	public void addChild(Node node){
		getChildren().add(node);
	}
	
	public void removeChild(Node node){
		getChildren().remove(node);
	}
	
	public FBKSegmentChain(Pane pane, int segmentCount, float x, float y) {

		this.base = new FBKVector(x, y);
		this.head = new FBKSegment(base, segmentLength);
		this.head.initGraphics(pane);
		this.tail = new FBKSegment(head.getCurrentTail(), segmentLength);
		this.tail.initGraphics(pane);
		createLink(head, tail);
		for (int i = 0; i < segmentCount - 2; i++) {
			FBKSegment last = new FBKSegment(tail.getCurrentHead(), segmentLength);
			last.initGraphics(pane);
			createLink(tail, last);
			tail = last;

		}
	}
	
	public ObservableList<FBKSegment> getSegmentsWritable() {
		return bones;
	}

	public ObservableList<FBKSegment> getSegments() {
		return bonesView;
	}
	
	public void setAbsoluteAncestor(FBKSegment absoluteAncestor) {
		this.absoluteAncestor = absoluteAncestor;
	}
	
	public FBKSegment getAbsoluteAncestor() {
		return absoluteAncestor;
	}
	
	private static void createLink(FBKSegment segmentOne, FBKSegment segmentTwo) {
		segmentOne.addChild(segmentTwo);
	}

	public FBKSegment getRoot() {
		return root;
	}

	public void setRoot(FBKSegment root) {
		this.root = root;
		this.root.setSkeleton(this);
	}

	public float getSegmentLength() {
		return segmentLength;
	}

	public void setSegmentLength(float segmentLength) {
		this.segmentLength = segmentLength;
	}

	public FBKSegment getHead() {
		return head;
	}

	public FBKSegment getTail() {
		return tail;
	}

	public FBKVector getBase() {
		return base;
	}

	public void constrain(FBKConstraintPivot constraintPivot) {
		this.constraintPivot = constraintPivot;
	}

	public void attachChain(FBKSegmentChain chain, FBKAttachPivot pivot) {
		this.attachments.add(new FBKAttachment(chain, pivot));
		switch (pivot) {
		case HEAD:
			createLink(tail, chain.getHead());
			// tail.addNeighbor(chain.getHead());
			break;
		case TAIL:
			createLink(tail, chain.getTail());
			// tail.addNeighbor(chain.getTail());
			break;
		default:
			break;

		}
	}

	public void detachChain(FBKSegmentChain chain) {
		this.attachments.remove(attachments.stream().filter(a -> a.getChain().equals(chain)).findFirst().orElse(null));
		if (tail.getChildren().contains(chain.getHead())) {
			tail.getChildren().remove(chain.getHead());
		} else {
			tail.getChildren().remove(chain.getTail());
		}
	}

	public void appendSegment(FBKSegment segment) {
		FBKSegment last = segment;

		createLink(tail, last);
		tail = last;
	}

	public void appendSegment(FBKSegment... segments) {
		for (int i = 0; i < segments.length; i++) {
			appendSegment(segments[i]);
		}
	}

	public void removeSegment(FBKSegment segment) {

	}

	public void removeSegment(FBKSegment... segments) {

	}

	public void update(FBKVector pos) {
		update(null, pos);
	}

	public void update(MouseEvent event, FBKVector pos) {
		if (constraintPivot == FBKConstraintPivot.HEAD) {
			head.moveHead(pos.getX(), pos.getY());
			// tail.enforseConstraint(base, FBKConstraintPivot.HEAD);
		} else if (constraintPivot == FBKConstraintPivot.TAIL) {
			tail.moveHead(pos.getX(), pos.getY());
			// head.enforseConstraint(base, FBKConstraintPivot.TAIL);
		} else {
			head.moveHead(pos.getX(), pos.getY());
			tail.enforseConstraint(base, FBKConstraintPivot.TAIL);
		}
	}

	public void draw() {
		head.drawGraphics();
	}

	public void setGraphicFactory(FBKGraphicCallback<FBKSegmentChain, FBKSegment> callback) {

	}

	public interface FBKGraphicCallback<P, V> {

		public V call(P param);
	}
}
