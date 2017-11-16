package com.eudycontreras.utilities;


import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class FXPaintUtility {

public List<Color> createPalette(int size, Color color){
		
		return null;
	}
	
	public static Color color(int color){
		return Color.rgb(color, color, color);
	}
	
	public static Color color(int color, double opacity){
		return Color.rgb(color, color, color, opacity);
	}
	
	public static Color color(int red, int green, int blue){
		return Color.rgb(red, green, blue, 1);
	}
	
	public static Color color(int red, int green, int blue, double opacity){
		return Color.rgb(red, green, blue, opacity);
	}
	
	public static Color darker(Color color, int times){
		double r = color.getRed();
		double g = color.getGreen();
		double b = color.getBlue();
		double o = color.getOpacity();
		
		for(int i = 0; i<times; i++){
			
			if(r <= 0.01 || g <= 0.01 || g <= 0.01) break;
			
			r -= 0.01;
			g -= 0.01;
			b -= 0.01;
		}

		return Color.color(r, g, b, o);
	}
	
	public static Color brighter(Color color, int times){
		double r = color.getRed();
		double g = color.getGreen();
		double b = color.getBlue();
		double o = color.getOpacity();
		
		for(int i = 0; i<times; i++){
			
			if(r >= 0.99 || g >= 0.99 || g >= 0.99) break;
			
			r += 0.01;
			g += 0.01;
			b += 0.01;
		}

		return Color.color(r, g, b, o);
	}
 
	public final static RadialGradient radialGradient(Color colorOne, Color colorTwo){

		Stop[] stops = new Stop[] { new Stop(0, colorOne), new Stop(1, colorTwo)};

		RadialGradient RADIAL_GRADIENT = new RadialGradient(0, .1, 100,100,20,false,CycleMethod.NO_CYCLE,stops);

		return RADIAL_GRADIENT;
	}

	public final static LinearGradient linearGradient(Color... colors){

		double offset = 1.0/colors.length;

		Stop[] stops = new Stop[colors.length];

		for(int i = 0; i<colors.length; i++){
			stops[i] = new Stop(offset*i, colors[i]);
		}

		LinearGradient LINEAR_GRADIENT = new LinearGradient(0, 	0.9, 0, 0, true, CycleMethod.NO_CYCLE, stops);

		return LINEAR_GRADIENT;
	}

	public final static ImagePattern imageFill(Image image){

		ImagePattern IMAGE_FILL = new ImagePattern(image);

		return IMAGE_FILL;
	}
	
	public final static Background background(Paint fill){
		return new Background(new BackgroundFill(fill,null,null));
	}

	public final static Color colorFill(Color color, double hueShift, double saturation, double brightness, double opacity){

		Color COLOR_FILL =color.deriveColor(hueShift, saturation, brightness, opacity);

		return COLOR_FILL;
	}

	
}
