package com.eudycontreras.examples;

import com.eudycontreras.editor.application.FXFontResources;
import com.eudycontreras.editor.application.FXImageResources;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ButtonEx extends Application {
    @Override 
    public void start(Stage stage) throws Exception {
        Image image = FXImageResources.create_new_project_icon.getImage();
        ImageView imageView = new ImageView(image);

        InnerShadow shadow = new InnerShadow();
        shadow.setChoke(1);
        shadow.setColor(Color.rgb(50, 140, 160));
        
        imageView.setEffect(shadow);
        imageView.setCache(true);
        imageView.setCacheHint(CacheHint.SPEED);

        stage.setScene(new Scene(new Group(createButton("Open",image)), Color.GRAY));
        stage.show();
    }
    
    private Node createButton(String text, Image image){
    	
    	VBox button = new VBox(1);
    	Text label = new Text(text);
    	
    	ImageView graphic = new ImageView(image);   	

    	label.setFont(Font.font(null,FontWeight.BOLD,12));
    	label.setFill(Color.WHITE);
   
    	button.setPadding(new Insets(4,4,4,4));
    	button.setAlignment(Pos.CENTER);
    	button.getChildren().add(graphic);
    	button.getChildren().add(label);
    	
    	return button;
    }

    public static void main(String[] args) {
        Application.launch();
    }
}