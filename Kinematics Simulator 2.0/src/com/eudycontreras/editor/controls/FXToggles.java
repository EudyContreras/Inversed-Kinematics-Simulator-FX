package com.eudycontreras.editor.controls;




import java.util.ArrayList;
import java.util.List;

import com.eudycontreras.models.ToggleInfo;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;


public class FXToggles extends HBox {

    private final static double TOGGLEBUTTON_WIDTH = 72;

    private final static double TOGGLEBUTTON_HEIGHT = 40;

    public FXToggles(ToggleInfo... dataHolders) {

    	addToggleInfo(dataHolders);
        setAlignment(Pos.CENTER);
        setSpacing(60);
    }
    
    
    public FXToggles addToggleInfo(ToggleInfo... dataHolders){
    	
    	if(dataHolders == null || dataHolders.length<=0) return this;
    	
    	List<RadioButton> toggles = new ArrayList<>();

        ToggleGroup group = new ToggleGroup();
        
    	for(ToggleInfo info : dataHolders){
    		
    		RadioButton toggle = new RadioButton(info.getName());

    		toggle.setMinSize(TOGGLEBUTTON_WIDTH, TOGGLEBUTTON_HEIGHT);

    		toggle.setUserData(info);
    		toggle.setToggleGroup(group);
    		toggle.setId("dark");
    		
    		toggles.add(toggle);
    	}
    	
        group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle selectedToggle) -> {

            if (selectedToggle != null) {
            	
            	ToggleInfo info = (ToggleInfo)((ToggleButton) selectedToggle).getUserData();
                
            	if(info.getAction() != null){
            		info.getAction().run();
            	}
            }

        });


        group.selectToggle(toggles.get(0));

        getChildren().addAll(toggles);
        
        return this;
    }

}
