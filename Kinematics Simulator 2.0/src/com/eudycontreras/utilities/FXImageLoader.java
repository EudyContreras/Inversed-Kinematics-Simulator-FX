package com.eudycontreras.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.eudycontreras.editor.application.FXEditor;
import com.eudycontreras.models.FXImage;

import javafx.scene.image.Image;

/**
 * This is the image loading class. this class takes care of loading images and
 * providing information according to the image dimensions. this class makes it
 * so each image is only loaded once as a check will occur every time an image
 * is loaded in order to make sure there are no duplicates. if an image already
 * located in the HashMap is loaded the image will be discarded and the instance
 * of it located in the HashMap will be returned, this effectively reduces
 * memory consumption and the load time of images when a high amount of images
 * are used in game.
 *
 * @author Eudy Contreras
 */
public class FXImageLoader {

	private static FXImage image;
	private static String name;
	private static int width, height;
	private final static Map<String, FXImage> TEXTURE_MAP = new HashMap<>();

	public FXImageLoader() {

	}

	/**
	 * constructor which creates a bufferedImage from a given name and will then
	 * store the image inside a hashMap.
	 *
	 * @param name
	 */
	public FXImageLoader(String name) {
		FXImageLoader.name = name;
		FXImage oldImage = TEXTURE_MAP.get(name);
		if (oldImage != null) {
			image = oldImage;
		} else {

			image = new FXImage(loadResource(name));
			TEXTURE_MAP.put(name, image);

		}
		width = (int)image.getImage().getWidth();
		height = (int)image.getImage().getHeight();
	}

	/**
	 * method which creates a bufferedImage from a given name and returns that
	 * image ready to be used
	 *
	 * @param name
	 * @throws FileNotFoundException
	 */
	public static FXImage loadImage(String name) throws FileNotFoundException {
		FXImageLoader.name = name;
		FXImage oldImage = TEXTURE_MAP.get(name);
		if (oldImage != null) {
			image = oldImage;
		} else {

			Image i = new Image(new FileInputStream(name));
			image = new FXImage(name);
			image.setImage(i);
			image.setUrl(name);
			TEXTURE_MAP.put(name, image);

		}
		width = (int) image.getImage().getWidth();
		height = (int) image.getImage().getHeight();

		return image;
	}
	public static Boolean checkForDuplicate(String name) {
		FXImageLoader.name = name;
		boolean duplicate =  TEXTURE_MAP.containsKey(name);
		return duplicate;
	}
	public static String loadResource(String image) {
		String url = FXEditor.IMAGE_SOURCE_DIRECTORY + image;
		return url;
	}
	public static FXImage getImage(String name){
		return TEXTURE_MAP.get(name);
	}
	public static FXImage getImage(){
		return image;
	}
	public static void clear() {
		TEXTURE_MAP.clear();
	}

	public static String getPath() {
		return name;
	}
	public static void setPath(String name) {
		FXImageLoader.name = name;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public static FXImage resample(FXImage input, int size) {
		
		FXImage image =  new FXImage(input.getUrl());
		image.setImage(new Image(input.getUrl(), size, size, true, true));
		image.setUrl(input.getUrl());
		
		return image;
	}

}
