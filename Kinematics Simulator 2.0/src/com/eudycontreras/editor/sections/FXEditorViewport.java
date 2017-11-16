package com.eudycontreras.editor.sections;

import com.eudycontreras.editor.application.FXEditor;
import com.eudycontreras.utilities.FXIterator;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class FXEditorViewport {

	private Pane viewPort = new Pane();
	 
	public FXEditorViewport(FXEditor editor){
		this.viewPort.setPickOnBounds(false);	
	}
	
	public Node get(){
		return viewPort;
	}
	
	public void addElement(Node...nodes){
		this.viewPort.getChildren().addAll(nodes);
		
		FXIterator.Iterate(viewPort.getChildren(), (current, i)->{
			current.translateYProperty().bind(viewPort.heightProperty().subtract(current.getLayoutBounds().getHeight()));
		});
	}
	
	public void removeElement(Node node){
		this.viewPort.getChildren().remove(node);
		node.translateYProperty().unbind();
	}
}
