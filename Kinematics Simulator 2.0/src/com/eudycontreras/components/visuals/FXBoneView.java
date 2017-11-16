package com.eudycontreras.components.visuals;

import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

public abstract class FXBoneView implements IFXBoneView{
	

	public static final IFXBoneView BONE_A = getFXBoneView(FXBoneViewType.TYPE_A);
	
	public static final IFXBoneView BONE_B = getFXBoneView(FXBoneViewType.TYPE_B);
	

	protected double x = 0;
	protected double y = 0;
	
	protected double width = 0;
	protected double height = 0;
	
	protected double strokeWidth;
	
	protected Color fill = Color.WHITE.deriveColor(1, 1, 1, 0.5);
	protected Color stroke = Color.WHITE;
	

	private static IFXBoneView getFXBoneView(FXBoneViewType type) {
		switch(type){
		case TYPE_A:
			return new FXBoneViewA(0,0,0,0);
		case TYPE_B:
			return new FXBoneViewB(0,0,0,0);
		}
		return null;
	}
	
	public static IFXBoneView getFXBoneView(FXBoneViewType type, double width, double height) {
		switch(type){
		case TYPE_A:
			return new FXBoneViewA(width, height);
		case TYPE_B:
			return new FXBoneViewB(width, height);
		}
		return null;
	}

	public static IFXBoneView getFXBoneView(FXBoneViewType type, double centerX, double y, double width, double height) {
		switch(type){
		case TYPE_A:
			return new FXBoneViewA(centerX, y, width, height);
		case TYPE_B:
			return new FXBoneViewB(centerX, y, width, height);
		}
		return null;
	}

	public static IFXBoneView getFXBoneView(FXBoneViewType type, double width, double height, Color fill, Color stroke) {
		switch(type){
		case TYPE_A:
			return new FXBoneViewA(width, height, fill, stroke);
		case TYPE_B:
			return new FXBoneViewB(width, height, fill, stroke);
		}
		return null;
	}

	public static IFXBoneView getFXBoneView(FXBoneViewType type, double x, double y, double width, double height, Color fill, Color stroke) {
		switch(type){
		case TYPE_A:
			return new FXBoneViewA(x, y, width, height, fill, stroke);
		case TYPE_B:
			return new FXBoneViewB(x, y, width, height, fill, stroke);
		}
		return null;
	}
	
	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}
	
	@Override
	public Color getFill() {
		return fill;
	}

	@Override
	public Color getStroke() {
		return stroke;
	}
	
	@Override
	public double getStrokeWidth() {
		return strokeWidth;
	}
	
	private static class FXBoneViewA extends FXBoneView{

		private Arc shape = new Arc();

		public FXBoneViewA(double width, double height) {
			this(width, height, Color.TRANSPARENT, Color.WHITE);
		}

		public FXBoneViewA(double centerX, double y, double width, double height) {
			this(centerX, y, width, height, Color.TRANSPARENT, Color.WHITE);
		}

		public FXBoneViewA(double width, double height, Color fill, Color stroke) {
			this(0, 0, width, height, fill, stroke);
		}

		public FXBoneViewA(double x, double y, double width, double height, Color fill, Color stroke) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.fill = fill;
			this.stroke = stroke;

			shape.setCenterX(x+width*2);
			shape.setCenterY(y);
			shape.setRadiusX(width*2);
			shape.setRadiusY(height);
			shape.setStartAngle(160);
			shape.setLength(40);
			shape.setStroke(stroke);
			shape.setFill(fill);
			shape.setType(ArcType.ROUND);
			shape.setStrokeLineJoin(StrokeLineJoin.ROUND);
			shape.setStrokeLineCap(StrokeLineCap.ROUND);
			shape.setStrokeType(StrokeType.CENTERED);
			shape.setStrokeWidth(2);
		}
		
		public Shape getShape(){
			return shape;
		}

		public void setX(double x) {
			this.x = x + width * 2;
			this.shape.setCenterX(x + width*2);
		}

		public void setY(double y) {
			this.y = y;
			this.shape.setCenterX(y + height);
		}

		public void setWidth(double width) {
			this.width = width;
			this.shape.setRadiusX(width);
		}

		public void setHeight(double height) {
			this.height = height;
			this.shape.setRadiusY(height);
		}

		public void setStrokeWidth(double strokeWidth) {
			this.strokeWidth = strokeWidth;
			this.shape.setStrokeWidth(strokeWidth);
		}

		public void setFill(Color fill) {
			this.fill = fill;
			this.shape.setFill(fill);
		}

		public void setStroke(Color stroke) {
			this.stroke = stroke;
			this.shape.setStroke(stroke);
		}
	}

	
	private static class FXBoneViewB extends FXBoneView{
		
		private Rectangle shape = new Rectangle();

		public FXBoneViewB(double width, double height) {
			this(width, height, Color.TRANSPARENT, Color.WHITE);
		}

		public FXBoneViewB(double centerX, double y, double width, double height) {
			this(centerX, y, width, height, Color.TRANSPARENT, Color.WHITE);
		}

		public FXBoneViewB(double width, double height, Color fill, Color stroke) {
			this(0, 0, width, height, fill, stroke);
		}

		public FXBoneViewB(double x, double y, double width, double height, Color fill, Color stroke) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.fill = fill;
			this.stroke = stroke;

			shape.setX(x);
			shape.setY(y - height / 4);
			shape.setWidth(width * 2);
			shape.setHeight(height / 2);
			shape.setFill(fill);
			shape.setStroke(stroke);
			shape.setArcHeight(height);
			shape.setArcWidth(width / 2);
			shape.setStrokeLineCap(StrokeLineCap.ROUND);
			shape.setStrokeType(StrokeType.CENTERED);
			shape.setStrokeWidth(3);
		}

		public Shape getShape() {
			return shape;
		}

		public void setX(double x) {
			this.x = x;
			this.shape.setX(x);
		}

		public void setY(double y) {
			this.y = y;
			this.shape.setY(y - height / 4);
		}

		public void setWidth(double width) {
			this.width = width * 2;
			this.shape.setWidth(width * 2);
		}

		public void setHeight(double height) {
			this.height = height / 2;
			this.shape.setHeight(height);
		}

		public void setStrokeWidth(double strokeWidth) {
			this.strokeWidth = strokeWidth;
			this.shape.setStrokeWidth(strokeWidth);
		}

		public void setFill(Color fill) {
			this.fill = fill;
			this.shape.setFill(fill);
		}

		public void setStroke(Color stroke) {
			this.stroke = stroke;
			this.shape.setStroke(stroke);
		}
	}
}
