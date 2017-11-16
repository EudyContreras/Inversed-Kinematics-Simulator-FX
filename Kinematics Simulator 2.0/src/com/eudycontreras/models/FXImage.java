package com.eudycontreras.models;

import javafx.scene.image.Image;

public class FXImage{
	
    private String url;
    private Image image;

    public FXImage(String url) {
        this.url = url;
        this.image = new Image(url);
    }

    public String getUrl() {
        return url;
    }

	public void setUrl(String name) {
		this.url = name;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	
}