package com.eudycontreras.editor.sections;

import com.eudycontreras.editor.application.FXEditor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FXEditorFooter {

    private VBox vbox = new VBox();
    
    private HBox hbox = new HBox(20);
    
	public FXEditorFooter(FXEditor editor){  
		vbox.setId("footer-bar");
		
        hbox.setPadding(new Insets(5));
        hbox.setAlignment(Pos.CENTER);
         
        
//        vbox.getChildren().addAll(new Separator(), hbox);
                 
	}
	
	public Node get(){
		return vbox;
	}
}
