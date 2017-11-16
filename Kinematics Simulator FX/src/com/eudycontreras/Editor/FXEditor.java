package com.eudycontreras.Editor;

import java.util.LinkedList;

import com.eudycontreras.components.handlers.FXBone;
import com.eudycontreras.components.handlers.FXFlesh;
import com.eudycontreras.components.handlers.FXJoint;
import com.eudycontreras.components.models.Bone;
import com.eudycontreras.components.models.Bone.MovementType;
import com.eudycontreras.components.models.Skeleton;
import com.eudycontreras.components.visuals.FXBoneView;
import com.eudycontreras.components.visuals.FXBoneViewType;
import com.eudycontreras.components.visuals.FXFleshView;
import com.eudycontreras.components.visuals.FXFleshViewType;
import com.eudycontreras.components.visuals.FXJointView;
import com.eudycontreras.components.visuals.FXJointViewType;
import com.eudycontreras.components.visuals.IFXBoneView;
import com.eudycontreras.components.visuals.IFXFleshView;
import com.eudycontreras.components.visuals.IFXJointView;
import com.eudycontreras.dialogs.FXSeparator;
import com.eudycontreras.models.ChildSource;
import com.eudycontreras.models.ParentSource;
import com.eudycontreras.models.Point;
import com.eudycontreras.observers.FXObserver;
import com.eudycontreras.utilities.FXBounds;
import com.eudycontreras.views.FXTriangle;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXEditor implements FXObserver<FXJoint> {

	private int jointId = 0;
	private int linkCount = 0;

	private double width = 0;
	private double height = 0;

	private boolean wasDragging = false;
	private boolean useCurrent = false;

	private boolean openingTools = false;
	private boolean closingTools = false;
	private boolean toolsClosed = false;
	private boolean toolsOpened = true;

	private FXJoint currentJoint = null;

	private Group boneRoot = new Group();

	private Pane mainPane = new Pane();
	private Scene scene = new Scene(mainPane);

	private SplitPane splitPane = new SplitPane();

	private FXEditorJointDialog dialog = null;

	private VBox toolsLayout = new VBox(10);
	private HBox toolsArea = new HBox();

	private FXTriangle triangle = new FXTriangle();

	private LinkedList<FXBone> bones = new LinkedList<>();
	private LinkedList<FXJoint> joints = new LinkedList<>();

	private LinkedList<ParentSource> sourceParents = new LinkedList<>();
	private LinkedList<ChildSource> sourceChildren = new LinkedList<>();

	public FXEditor(Stage stage, double width, double height, Paint paint) {
		this.width = width;
		this.height = height;

		mainPane.setOnMouseDragged(mouseHandler);
		mainPane.setOnMousePressed(mouseHandler);
		mainPane.setOnMouseReleased(mouseHandler);

		mainPane.getChildren().add(new FXEditorGrid(width, height, 2, Color.rgb(40, 40, 40), Color.rgb(130, 130, 130)).get());
		mainPane.getChildren().addAll(boneRoot);

		toolsLayout.setPrefHeight(height);

		toolsArea.getChildren().add(toolsLayout);
		toolsArea.getChildren().addAll(new FXSeparator().Create(10, height, Color.rgb(40, 40, 40)), triangle);
		toolsArea.setOnMousePressed(e -> e.consume());
		toolsArea.setOnMouseReleased(e -> e.consume());

		toolsLayout.setBackground(new Background(new BackgroundFill(Color.rgb(140, 140, 140), CornerRadii.EMPTY, Insets.EMPTY)));

		splitPane.setDividerPositions(0.22);
		mainPane.getChildren().add(toolsArea);

		scene.setOnKeyPressed(keyHandler);
		scene.getStylesheets().add(FXEditorApp.class.getResource("application.css").toExternalForm());

		stage.setScene(scene);
		stage.setWidth(width);
		stage.setHeight(height);
		stage.show();

	}

	private ParentSource createParentSource(double x, double y, double jointRadius) {

		final Skeleton skeleton = new Skeleton();

		final Bone bone = new Bone(0, 0);

		final FXJoint jointView = joint(bone, null, jointRadius, Color.rgb(0, 150, 255));

		bone.getContent().add(jointView.getJoint().getNode());
		bone.setSourceSkeleton(skeleton);

		skeleton.setTranslateX(x);
		skeleton.setTranslateY(y);

		joints.add(jointView);

		return new ParentSource(skeleton, bone, jointRadius);
	}

	private Bone createChild(ParentSource source, double length, double angle) {

		Bone start = new Bone(length, angle);
		
		Bone end = new Bone(0, 0);

		FXBone boneView = bone(start, source.getJointRadius(), 0, length - source.getJointRadius() * 2, 40);

		FXFlesh fleshView = flesh(start, source.getJointRadius(), 0, length - source.getJointRadius() * 2, 30);

		FXJoint jointView = joint(end, start, source.getJointRadius(), Color.rgb(0, 150, 255));

		ChildSource sourceParent = new ChildSource(source, start, source.getJointRadius());

		ChildSource sourceChild = new ChildSource(source, end, source.getJointRadius());
		
		source.getChildren().add(sourceParent);
		source.getParent().getChildren().add(start);
		source.setHasChildren(true);

		sourceParent.getParent().getContent().add(fleshView.getFlesh().getShape());
		sourceParent.getParent().getContent().add(boneView.getBone().getShape());
		sourceParent.getSource().getChildren().add(sourceChild);
		sourceParent.getParent().getChildren().add(end);	
		sourceChild.getParent().getContent().add(jointView.getJoint().getNode());
		sourceParent.getParent().setLength(length);
		sourceParent.getParent().setRotation(angle);
		
		boneView.mapFlesh(fleshView);
		jointView.setParentBone(boneView);
		fleshView.addEventHandling();
		//jointView.setParentBone(start);
		
		joints.add(jointView);
		sourceChildren.add(sourceParent);
		sourceChildren.add(sourceChild);

		return end;
	}

	private Bone createChild(ChildSource source, double length, double angle) {

		Bone bone = new Bone(0, 0);

		FXFlesh fleshView = flesh(bone, 0 + source.getJointRadius(), 0, length - source.getJointRadius() * 2, 20);

		FXBone boneView = bone(bone, source.getJointRadius(), 0, length - source.getJointRadius() * 2, 40);

		FXJoint jointView = joint(bone, source.getParent(), source.getJointRadius(), Color.rgb(0, 150, 255));

		ChildSource sourceChild = new ChildSource(source.getSource(), bone, source.getJointRadius());

		source.getParent().getContent().add(fleshView.getFlesh().getShape());
		source.getParent().getContent().add(boneView.getBone().getShape());
		source.getSource().getChildren().add(sourceChild);
		source.getParent().getChildren().add(bone);
		source.getParent().setLength(length);
		source.getParent().setRotation(angle);
		
		boneView.mapFlesh(fleshView);
		jointView.setParentBone(boneView);
		fleshView.addEventHandling();
		
		bone.getContent().add(jointView.getJoint().getNode());

		joints.add(jointView);
		sourceChildren.add(sourceChild);

		return bone;
	}

	private Bone createChildAlt(ChildSource source, double length, double angle) {

		Bone start = new Bone(length, angle);

		Bone end = new Bone(0, 0);

		FXFlesh fleshView = flesh(end, 0 + source.getJointRadius(), 0, length - source.getJointRadius() * 2, 20);

		FXBone boneView = bone(end, source.getJointRadius(), 0, length - source.getJointRadius() * 2, 40);

		FXJoint jointView = joint(end, start, source.getJointRadius(), Color.rgb(0, 150, 255));

		ChildSource sourceChildStart = new ChildSource(source.getSource(), start, source.getJointRadius());

		ChildSource sourceChildEnd = new ChildSource(sourceChildStart.getSource(), end, sourceChildStart.getJointRadius());

		source.getSource().getChildren().add(sourceChildStart);
		source.getParent().getParent().getChildren().add(start);

		sourceChildStart.getParent().getContent().add(fleshView.getFlesh().getShape());
		sourceChildStart.getParent().getContent().add(boneView.getBone().getShape());
		sourceChildStart.getSource().getChildren().add(sourceChildEnd);
		sourceChildStart.getParent().getChildren().add(end);
		sourceChildStart.getParent().setLength(length);
		sourceChildStart.getParent().setRotation(angle);
		
		boneView.mapFlesh(fleshView);
		jointView.setParentBone(boneView);
		fleshView.addEventHandling();
		
		end.getContent().add(jointView.getJoint().getNode());

		joints.add(jointView);

		sourceChildren.add(sourceChildStart);
		sourceChildren.add(sourceChildEnd);

		return end;
	}

	
	private Bone createChildAlt(ParentSource source, double length, double angle) {

		Bone start = new Bone(length, angle);

		Bone end = new Bone(0, 0);

		FXFlesh fleshView = flesh(end, 0 + source.getJointRadius(), 0, length - source.getJointRadius() * 2, 35);

		FXBone boneView = bone(end, source.getJointRadius(), 0, length - source.getJointRadius() * 2, 40);

		FXJoint jointView = joint(end, start, source.getJointRadius(), Color.rgb(0, 150, 255));

		ChildSource sourceChildStart = new ChildSource(source, start, source.getJointRadius());

		ChildSource sourceChildEnd = new ChildSource(sourceChildStart.getSource(), end, sourceChildStart.getJointRadius());

		source.getChildren().add(sourceChildStart);
		source.getParent().getChildren().add(start);

		sourceChildStart.getParent().getContent().add(fleshView.getFlesh().getShape());
		sourceChildStart.getParent().getContent().add(boneView.getBone().getShape());
		sourceChildStart.getParent().setLength(length);
		sourceChildStart.getParent().setRotation(angle);
		sourceChildStart.getSource().getChildren().add(sourceChildEnd);
		sourceChildStart.getParent().getChildren().add(end);

		boneView.mapFlesh(fleshView);
		jointView.setParentBone(boneView);
		fleshView.addEventHandling();
		
		end.getContent().add(jointView.getJoint().getNode());

		joints.add(jointView);

		sourceChildren.add(sourceChildStart);
		sourceChildren.add(sourceChildEnd);

		return end;
	}

	public void log(Object obj) {
		System.out.println(obj);
	}

	EventHandler<MouseEvent> mouseHandler = e -> {

		double x = e.getSceneX();
		double y = e.getSceneY();

		if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {

			wasDragging = false;

		} else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {

			if (wasDragging)
				return;

			Point2D point = mainPane.sceneToLocal(x, y);

			if (e.isControlDown() && linkCount >= 1 && !sourceParents.isEmpty()) {

				if (!sourceParents.getLast().hasChildren()) {

					Point2D start = mainPane.sceneToLocal(sourceParents.getLast().getSkeleton().getTranslateX(),sourceParents.getLast().getSkeleton().getTranslateY());

					double length = new Point2D(point.getX(), point.getY()).distance(start);

					createChild(sourceParents.getLast(), length, getAngle(start, point));

				} else {

					log("check");

					FXJoint selected = joints.stream().filter(j -> j.isSelected()).findFirst().orElse(null);

					if (selected != null) {

						if(selected.getCurrentBone().getParent() != null){
							ChildSource source = sourceChildren.stream()
									.filter(s -> s.getParent().compareTo(selected.getCurrentBone()) == 0).findFirst()
									.orElse(sourceParents.getLast().getChildren().getLast());

							if (source.compareTo(sourceParents.getLast().getChildren().getLast().getParent()) == 0) {

								log("CHILD");

								Point2D start = source.getParent().getSkeleton().localToScene(source.getParent().getStartPoint());

								double length = new Point2D(point.getX(), point.getY()).distance(start);

								createChild(source, length, getAngle(start, point));
							} else {

								log("ALT CHILD");

								Point2D start = source.getParent().getSkeleton().localToScene(source.getParent().getStartPoint());

								double length = new Point2D(point.getX(), point.getY()).distance(start);

								createChildAlt(source, length, getAngle(start, point));
							}
						}else{
							log("MASTER MASTER");
							
							ParentSource source = sourceParents.stream()
									.filter(s -> s.getParent().equals(selected.getCurrentBone())).findFirst()
									.orElse(null);
							
							if(source != null){

								Point2D start = source.getParent().getSkeleton().localToScene(source.getParent().getStartPoint());

								double length = new Point2D(point.getX(), point.getY()).distance(start);

								createChildAlt(source, length, getAngle(start, point));
							}
						}

					} else {

						ChildSource source = sourceParents.getLast().getChildren().getLast();

						Point2D start = source.getParent().getSkeleton().localToScene(source.getParent().getStartPoint());

						double length = new Point2D(point.getX(), point.getY()).distance(start);

						createChild(source, length, getAngle(start, point));
					}
				}
				linkCount++;

			} else {
				unselectAll();

				ParentSource source = createParentSource(point.getX(), point.getY(), 12);

				sourceParents.add(source);

				mainPane.getChildren().add(source.getSkeleton());

				linkCount = 1;
			}

		} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {

			wasDragging = true;
		}

		if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {

			if (e.getButton().equals(MouseButton.PRIMARY)) {
				if (e.getClickCount() == 2) {
					if (e.isControlDown()) {
						// openJointDialog(null);

					}
				}
			}
		}
	};

	public float getAngle(Point2D start, Point2D end) {

		float angle = (float) Math.toDegrees(Math.atan2(end.getY() - start.getY(), end.getX() - start.getX()));

		return angle;
	}

	public static Point2D getCoordinates(Bounds localBounds) {
		return new Point2D(localBounds.getMinX(), localBounds.getMaxY());
	}

	public FXJoint getCurrentJoint() {
		if (useCurrent) {
			return currentJoint;
		} else {
			return joints.getLast();
		}
	}

	public void setCurrentJoint(FXJoint joint) {
		if (joint != null) {
			this.currentJoint = joint;
			this.useCurrent = true;
		} else {
			this.currentJoint = joint;
			this.useCurrent = false;
		}
	}

	EventHandler<KeyEvent> keyHandler = e -> {

		switch (e.getCode()) {

		case D:
			if (e.isControlDown()) {

				deleteSelected();
			}
			break;
		default:
			break;

		}
	};

	private void deleteSelected() {

		FXJoint joint = joints.stream().filter(j -> j.isSelected()).findFirst().orElse(null);

		if (joint != null) {

			if(joint.getCurrentBone().getParent() == null){

				ParentSource source = this.sourceParents.stream().filter(s -> s.getParent().compareTo(joint.getCurrentBone()) == 0).findFirst().orElse(null);
				
				if(source != null){
									
					 sourceParents.remove(source);
					
					 mainPane.getChildren().remove(source.getSkeleton());
				}
			}
			else{

				ChildSource source = this.sourceChildren.stream().filter(s -> s.getParent().compareTo(joint.getCurrentBone()) == 0).findFirst().orElse(null);
				
				if(source != null){

					source.getParent().setData(source);
					
					ChildSource c = source.getParent().getData();
					
					if(!source.getParent().getChildren().isEmpty()){
						
						 source.getParent().clearChildren();
						 source.getParent().resetState();
						 source.getParent().getContent().clear();
						 source.getParent().getContent().add(joint.getJoint().getNode());
						 joint.setCurrentBone(source.getParent());
					}
				}
			}			
		}
	}

	public void unselectRest(int index) {
		for (FXJoint joint : joints) {

			if (joint.isSelected()) {

				joint.setSelected(false);
			}
		}
	}

	public void unselectAll() {
		for (FXJoint joint : joints) {

			if (joint.isSelected()) {

				joint.setSelected(false);
			}
		}
	}
	
	public Pane getMainPane(){
		return mainPane;
	}

	public LinkedList<FXBone> getBones() {
		return bones;
	}

	public LinkedList<FXJoint> getJoints() {
		return joints;
	}

	public void setJointAdjust(FXJoint joint) {

		if (dialog != null) {
			dialog.setJoint(joint);
		}

	}

	public void openJointDialog(FXJoint joint) {
		
		dialog = new FXEditorJointDialog(0, 0, 300, 400);

		mainPane.getChildren().add(dialog);

		dialog.computeSize();

		popup(scene, dialog, dialog);

	}

	private double xOffset = 0;
	private double yOffset = 0;
	private boolean jointDialogOpen = false;

	public void popup(Scene scene, FXEditorDialog dialog, Parent parent) {
		Stage stage = new Stage();

		Group root = new Group(parent);

		stage.setTitle("Confirmation");

		stage.initModality(Modality.NONE);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.initOwner(scene.getWindow());

		root.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				stage.setX(event.getScreenX() - xOffset);
				stage.setY(event.getScreenY() - yOffset);
			}
		});

		dialog.addCloseAction(() -> {
			stage.close();
			jointDialogOpen = false;
		});

		// yes.addEventHandler(MouseEvent.MOUSE_CLICKED,
		// new EventHandler<MouseEvent>() {
		// @Override
		// public void handle(MouseEvent e) {
		// // inside here you can use the minimize or close the previous stage//
		// dialog.close();
		// }
		// });
		// no.addEventHandler(MouseEvent.MOUSE_CLICKED,
		// new EventHandler<MouseEvent>() {
		// @Override
		// public void handle(MouseEvent e) {
		// dialog.close();
		// }
		// });

		stage.setScene(new Scene(root, 500, 500, Color.TRANSPARENT));
		stage.show();
		jointDialogOpen = true;
		dialog.computeSize();
	}

	public boolean IsJointDialogOpen() {

		return jointDialogOpen;
	}

	@Override
	public void update() {

	}

	@Override
	public void setSubject(FXJoint subject) {

	}

	@Override
	public void notify(FXJoint subject) {
		// dialog.setJoint(subject);
	}

	public Group getBoneRoot() {
		return boneRoot;
	}

	public static Node Head(double centerX, double centerY, double radiusX, double radiusY) {

		Color color = Color.WHITE;

		Ellipse muscle = new Ellipse(centerX, centerY, radiusX, radiusY);
		muscle.setFill(color.deriveColor(1, 1, 1, 0.4));
		muscle.setStroke(color);
		muscle.setStrokeType(StrokeType.OUTSIDE);
		muscle.setStrokeWidth(2);

		return muscle;
	}

	public FXFlesh flesh(Bone bone, double centerX, double centerY, double radiusX, double radiusY) {

		Color color = Color.WHITE;

		IFXFleshView fleshView = FXFleshView.getFXFleshView(FXFleshViewType.TYPE_B, centerX, centerY, radiusX / 2,
				radiusY);

		fleshView.setFill(color.deriveColor(1, 1, 1, 0.25));
		fleshView.setStrokeWidth(2);

		return new FXFlesh(this, fleshView, bone);
	}

	public FXBone bone(Bone bone, double centerX, double centerY, double radiusX, double radiusY) {

		Color color = Color.WHITE;

		IFXBoneView boneView = FXBoneView.getFXBoneView(FXBoneViewType.TYPE_A, centerX, centerY, radiusX / 2,
				radiusY / 2);

		boneView.setStrokeWidth(2);
		boneView.setStroke(color);

		return new FXBone(boneView, bone);
	}

	public FXJoint joint(Bone currentBone, Bone parentBone, double radius) {
		return joint(currentBone, parentBone, 0, 0, radius, Color.rgb(0, 150, 250));
	}

	public FXJoint joint(Bone currentBone, Bone parentBone, double radius, Color color) {
		return joint(currentBone, parentBone, 0, 0, radius, color);
	}

	public FXJoint joint(Bone currentBone, Bone parentBone, double centerX, double centerY, double radius) {
		return joint(currentBone, parentBone, centerX, centerY, radius, Color.rgb(0, 150, 250));
	}

	public FXJoint joint(Bone currentBone, Bone parentBone, double centerX, double centerY, double radius,
			Color color) {

		jointId++;

		IFXJointView joint = FXJointView.getFXJointView(FXJointViewType.TYPE_A, centerX, centerY, radius, color);

		joint.setCenterColor(Color.WHITE);

		return new FXJoint(this, joint, currentBone, parentBone, jointId);
	}
}
