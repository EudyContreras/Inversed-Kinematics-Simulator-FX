package com.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

public class ImageChooser extends StackPane {

	public ImageChooser() {
		super();
		ImageView imageView = new ImageView();

		Button btnLoad = new Button("Load");
		btnLoad.setOnAction(chooserAction(imageView));
		imageView.setPreserveRatio(true);
		imageView.setFitHeight(255);
		imageView.setFitWidth(255);

		BorderPane rootBox = new BorderPane();
		BorderPane buttons = new BorderPane();

		buttons.setRight(btnLoad);
		rootBox.setBottom(buttons);

		getChildren().add(imageView);
		getChildren().add(rootBox);
	}

	private EventHandler<ActionEvent> chooserAction(ImageView view) {

		EventHandler<ActionEvent> eventListener = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				FileChooser fXFileChooser = new FileChooser();

				FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)","*.JPG");
				FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)","*.PNG");

				fXFileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

				File file = fXFileChooser.showOpenDialog(null);

				try {
					BufferedImage bufferedImage = ImageIO.read(file);
					Image image = SwingFXUtils.toFXImage(bufferedImage, null);
					view.setImage(image);
				} catch (IOException | IllegalArgumentException ex) {

				}
			}
		};
		return eventListener;
	}

}