package com.eudycontreras.components.handlers;

import com.eudycontreras.Editor.FXEditor;
import com.eudycontreras.components.models.Bone;
import com.eudycontreras.components.visuals.FXFleshViewType;
import com.eudycontreras.components.visuals.IFXBoneView;
import com.eudycontreras.components.visuals.IFXFleshView;


import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;

public class FXFlesh{

	private boolean showFlesh;
	
	private IFXFleshView fleshView;

	private FXEditor editor;
	
	private Bone bone;
	
	public FXFlesh(FXEditor editor, IFXFleshView fleshView, Bone bone){
		this.fleshView = fleshView;
		this.editor = editor;
		this.bone = bone;
	}
	
	public IFXFleshView getFlesh(){
		return fleshView;
	}
	
	public void showFlesh(boolean showBones){
		this.showFlesh = showBones;
	}
	
	public boolean showingFlesh(){
		return showFlesh;
	}

	private double center = 0;

	private double mouseX = 0;
	private double mouseY = 0;
	
	private double lastWidth = 0;
	private double lastHeigth = 0;

	public void addEventHandling(){
		
        fleshView.getShape().setOnMouseClicked(e -> {
        	if(!e.isControlDown()){
        		if(e.getButton().equals(MouseButton.PRIMARY)){
                    if(e.getClickCount() == 2){ 
                    	cycleFleshType();
                    }
                }
        	}
        	e.consume();
        });
        
        fleshView.getShape().setOnMouseDragged(e -> {	
        	
        	if(e.isControlDown()){
        		
        		Point2D location = fleshView.getShape().sceneToLocal(e.getSceneX(), e.getSceneY());

            	computeMass(location);
            	
        	}
        });
        
        fleshView.getShape().setOnMousePressed(e -> {

        	lastWidth = fleshView.getWidth();
        	lastHeigth = fleshView.getHeight();
        	
        	mouseX = fleshView.getX() + fleshView.getHeight() / 2;
        	mouseY = fleshView.getY() + fleshView.getWidth() / 2;
        	
        	fleshView.getShape().getScene().setCursor(Cursor.CLOSED_HAND);    	
        });
        
        fleshView.getShape().setOnMouseReleased(e -> {
        	fleshView.getShape().getScene().setCursor(Cursor.OPEN_HAND);
        	e.consume();
        });
        
        fleshView.getShape().setOnMouseEntered(e -> {
        	fleshView.getShape().getScene().setCursor(Cursor.OPEN_HAND);
        });
        
        fleshView.getShape().setOnMouseExited(e -> {
        	fleshView.getShape().getScene().setCursor(Cursor.DEFAULT);
        });
        fleshView.getShape().setOnDragDetected(e -> {
        	fleshView.getShape().getScene().setCursor(Cursor.CROSSHAIR);
        });
        
        fleshView.getShape().setOnDragDone(e -> {
        	fleshView.getShape().getScene().setCursor(Cursor.DEFAULT);
        });
	}
	

	private void cycleFleshType(){
		
	}
		
	public void switchFleshType(FXFleshViewType type) {
		
	}

	private void computeMass(Point2D location){
				
    	double deltaX = Math.abs(location.getX() - mouseX);
    	double deltaY = Math.abs(location.getY() - mouseY);   
    	
    	double differenceX = deltaX - lastWidth/2;
    	double differenceY = deltaY - lastHeigth/2;
    	
    	double mass = (int)((differenceX + differenceY));
		
		this.fleshView.setHeight(mass);
	}
	
	public void computeLength(double length, double radius){
		fleshView.setWidth(length - radius);	
		fleshView.setX(-(length - (radius * 2)));
	}
	
	public void mapLength(IFXBoneView bone, double radius){
		fleshView.setX(radius);
		fleshView.setWidth((bone.getWidth()/2) - (radius * 0.7));	
		fleshView.setHeight(fleshView.getHeight());
	}
	
}
