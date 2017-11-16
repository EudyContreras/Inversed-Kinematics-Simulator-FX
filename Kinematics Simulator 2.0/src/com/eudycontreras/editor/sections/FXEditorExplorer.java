package com.eudycontreras.editor.sections;

import com.eudycontreras.editor.application.FXEditor;
import com.eudycontreras.editor.application.FXImageResources;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class FXEditorExplorer {

    StackPane root = new StackPane();

	public FXEditorExplorer(FXEditor editor){
		

		TreeItem<String> treeItemRoot = new TreeItem<String> ("Root");
        
        TreeItem<String> nodeItemA = new TreeItem<String>("Item A");
        TreeItem<String> nodeItemB = new TreeItem<String>("Item B");
        TreeItem<String> nodeItemC = new TreeItem<String>("Item C");
        
        treeItemRoot.getChildren().addAll(nodeItemA, nodeItemB, nodeItemC);
         
        TreeItem<String> nodeItemA1 = new TreeItem<String>("Item A1");
        TreeItem<String> nodeItemA2 = new TreeItem<String>("Item A2");
        TreeItem<String> nodeItemA3 = new TreeItem<String>("Item A3");
    
        nodeItemA.getChildren().addAll(nodeItemA1, nodeItemA2, nodeItemA3);
         
        TreeView<String> treeView = new TreeView<String>(treeItemRoot);

        root.getChildren().add(treeView);
         
        
	}
	
	public Region get(){
		return root;
	}
}
