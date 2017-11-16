package com.eudycontreras.components.visuals;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;

public abstract class FXFleshView implements IFXFleshView{
	

	public static final IFXFleshView FLESH_A = getFXFleshView(FXFleshViewType.TYPE_A);
	
	public static final IFXFleshView FLESH_B = getFXFleshView(FXFleshViewType.TYPE_B);
	
	public static final IFXFleshView FLESH_C = getFXFleshView(FXFleshViewType.TYPE_C);	

	protected double x = 0;
	protected double y = 0;
	
	protected double width = 0;
	protected double height = 0;
	
	protected double strokeWidth = 1;
	
	protected Color fill = Color.WHITE.deriveColor(1, 1, 1, 0.5);
	protected Color stroke = Color.WHITE;
	

	private static IFXFleshView getFXFleshView(FXFleshViewType type) {
		switch(type){
		case TYPE_A:
			return new FXFleshViewA(0,0,0,0);
		case TYPE_B:
			return new FXFleshViewB(0,0,0,0);
		case TYPE_C:
			return new FXFleshViewC(0,0,0,0);
		}
		return null;
	}
	
	public static IFXFleshView getFXFleshView(FXFleshViewType type, double width, double height, Color fill, Color stroke) {
		switch(type){
		case TYPE_A:
			return new FXFleshViewA(width, height, Color.WHITE.deriveColor(1, 1, 1, 0.5), Color.WHITE);
		case TYPE_B:
			return new FXFleshViewB(width, height, Color.WHITE.deriveColor(1, 1, 1, 0.5), Color.WHITE);
		case TYPE_C:
			return new FXFleshViewC(width, height, Color.WHITE.deriveColor(1, 1, 1, 0.5), Color.WHITE);
		}
		
		return null;
	}

	public static IFXFleshView getFXFleshView(FXFleshViewType type, double x, double y, double width, double height) {
		switch(type){
		case TYPE_A:
			return new FXFleshViewA(x, y, width, height);
		case TYPE_B:
			return new FXFleshViewB(x, y, width, height);
		case TYPE_C:
			return new FXFleshViewC(x, y, width, height);
		}
		
		return null;
	}

	public static IFXFleshView getFXFleshView(FXFleshViewType type, double x, double y, double width, double height, Color fill, Color stroke) {
		switch(type){
		case TYPE_A:
			return new FXFleshViewA(x, y, width, height, fill, stroke);
		case TYPE_B:
			return new FXFleshViewB(x, y, width, height, fill, stroke);
		case TYPE_C:
			return new FXFleshViewC(x, y, width, height, fill, stroke);
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
	
	private static class FXFleshViewC extends FXFleshView{
	   	
		private Rectangle shape = new Rectangle();
		
		public FXFleshViewC(double width, double height, Color fill, Color stroke) {
			this(0, 0, width, height, Color.WHITE.deriveColor(1, 1, 1, 0.5), Color.WHITE);
		}

		public FXFleshViewC(double x, double y, double width, double height) {
			this(x, y, width, height, Color.WHITE.deriveColor(1, 1, 1, 0.5), Color.WHITE);
		}

		public FXFleshViewC(double inputX, double inputY, double inputWidth, double inputHeight, Color fill, Color stroke) {
			super();
			this.x = inputX + width;
			this.y = inputY;
			this.width = inputWidth;
			this.height = inputHeight;
			this.fill = fill;
			this.stroke = stroke;		

			shape.setX(x);
			shape.setY(y - height/2);
			shape.setWidth(width * 2);
			shape.setHeight(height);
			shape.setArcWidth(height);
			shape.setArcHeight(height);
			shape.setStrokeWidth(2);
			shape.setStrokeLineCap(StrokeLineCap.ROUND);
			shape.setStrokeType(StrokeType.OUTSIDE);
			shape.setStroke(Color.WHITE);
			shape.setFill(Color.WHITE.deriveColor(1, 1, 1, 0.6));
		}

		@Override
		public Shape getShape(){
			return shape;
		}
		
		@Override
		public void setX(double x) {
			this.x = x;
			this.shape.setX(x);
		}

		@Override
		public void setY(double y) {
			this.y = y;
			this.shape.setY(y);
		}

		@Override
		public void setWidth(double width) {
			this.width = width;
			this.shape.setWidth(width);
		}

		@Override
		public void setHeight(double height) {
			this.height = height;
			this.shape.setHeight(height);
		}
		
		@Override
		public void setFill(Color fill) {
			this.fill = fill;
			this.shape.setFill(fill);
		}

		@Override
		public void setStroke(Color stroke) {
			this.stroke = stroke;
			this.shape.setStroke(stroke);
		}

		@Override
		public void setStrokeWidth(double width) {
			this.shape.setStrokeWidth(width);		
		}  
	}
	
	private static class FXFleshViewB extends FXFleshView{
		
		private Shape shape = null;
		
		private Group wrapper = new Group();
		
		private Ellipse circle1 = new Ellipse ();
		private Ellipse circle2 = new Ellipse ();		
		
		public FXFleshViewB(double width, double height, Color fill, Color stroke) {
			this(0, 0, width, height, Color.WHITE.deriveColor(1, 1, 1, 0.5), Color.WHITE);
		}

		public FXFleshViewB(double x, double y, double width, double height) {
			this(x, y, width, height, Color.WHITE.deriveColor(1, 1, 1, 0.5), Color.WHITE);
		}

		public FXFleshViewB(double inputX, double inputY, double w, double h, Color fill, Color stroke) {
			super();
			this.x = inputX;
			this.y = inputY;
			this.width = w;
			this.height = h;
			this.fill = fill;
			this.stroke = stroke;
			
			circle1 = new Ellipse ();
			circle2 = new Ellipse ();
			
			circle1.setCenterX(0 - width);
			circle1.setCenterY(0);
			circle1.setRadiusX(width * 2);
			circle1.setRadiusY(height * 0.8);
			
			circle2.setCenterX(0);
			circle2.setCenterY(0);
			circle2.setRadiusX(width);
			circle2.setRadiusY(height); 
			
			shape = Shape.intersect(circle1, circle2);
			shape.setStrokeWidth(strokeWidth);
			shape.setStrokeLineCap(StrokeLineCap.ROUND);
			shape.setStrokeType(StrokeType.OUTSIDE);
			shape.setStroke(Color.WHITE);
			shape.setFill(Color.WHITE.deriveColor(1, 1, 1, 0.6));
			
			wrapper.getChildren().add(shape);
			wrapper.setTranslateX(x+ wrapper.getBoundsInLocal().getWidth() / 2);
			wrapper.setTranslateY(y );
		}

		@Override
		public Node getShape(){
			return wrapper;
		}
		
		@Override
		public void setX(double x) {
			this.x = x;
			wrapper.setTranslateX(x+ wrapper.getBoundsInLocal().getWidth() / 2);
		}

		@Override
		public void setY(double y) {
			this.y = y;
			wrapper.setTranslateY(y);
		}

		@Override
		public void setWidth(double width) {
			this.width = width;
			circle1.setRadiusX(width * 2);
			circle1.setRadiusY(height/2);
			
			circle2.setCenterX(0);
			circle2.setCenterY(0);
			circle2.setRadiusX(width);
			circle2.setRadiusY(height); 
			
			wrapper.getChildren().remove(shape);
			shape = Shape.intersect(circle1, circle2);
			shape.setStrokeWidth(strokeWidth);
			shape.setStrokeLineCap(StrokeLineCap.ROUND);
			shape.setStrokeType(StrokeType.OUTSIDE);
			shape.setStroke(stroke);
			shape.setFill(fill);
			wrapper.getChildren().add(shape);
		}

		@Override
		public void setHeight(double height) {
			this.height = height;
			circle1.setCenterX(0 - width);
			circle1.setCenterY(0);
			circle1.setRadiusX(width * 2);
			circle1.setRadiusY(height * 0.8);
			
			circle2.setCenterX(0);
			circle2.setCenterY(0);
			circle2.setRadiusX(width);
			circle2.setRadiusY(height); 

			wrapper.getChildren().remove(shape);
			shape = Shape.intersect(circle1, circle2);
			shape.setStrokeWidth(strokeWidth);
			shape.setStrokeLineCap(StrokeLineCap.ROUND);
			shape.setStrokeType(StrokeType.OUTSIDE);
			shape.setStroke(stroke);
			shape.setFill(fill);
			wrapper.getChildren().add(shape);
		}

		@Override
		public void setFill(Color fill) {
			this.fill = fill;
			this.shape.setFill(fill);
		}

		@Override
		public void setStroke(Color stroke) {
			this.stroke = stroke;
			this.shape.setStroke(stroke);
		}
		
		@Override
		public void setStrokeWidth(double width) {	
			this.strokeWidth = width;
			this.shape.setStrokeWidth(width);
		}
	}
	
	private static class FXFleshViewA extends FXFleshView {
		
		private Ellipse shape = new Ellipse ();
		
		public FXFleshViewA(double width, double height, Color fill, Color stroke) {
			this(0, 0, width, height, Color.WHITE.deriveColor(1, 1, 1, 0.5), Color.WHITE);
		}

		public FXFleshViewA(double x, double y, double width, double height) {
			this(x, y, width, height, Color.WHITE.deriveColor(1, 1, 1, 0.5), Color.WHITE);
		}

		public FXFleshViewA(double inputX, double inputY, double inputWidth, double inputHeight, Color fill, Color stroke) {
			super();
			this.x = inputX + inputWidth;
			this.y = inputY;
			this.width = inputWidth;
			this.height = inputHeight/2;
			this.fill = fill;
			this.stroke = stroke;
			
			shape.setCenterX(x);
			shape.setCenterY(y);
			shape.setRadiusX(width);
			shape.setRadiusY(height);
			shape.setStrokeWidth(2);
			shape.setStrokeLineCap(StrokeLineCap.ROUND);
			shape.setStrokeType(StrokeType.OUTSIDE);
			shape.setStroke(Color.WHITE);
			shape.setFill(Color.WHITE.deriveColor(1, 1, 1, 0.6));
		}
		
		@Override
		public Shape getShape(){
			return shape;
		}

		@Override
		public void setX(double x) {
			this.x = x + width;
			this.shape.setCenterX(x + width);
		}

		@Override
		public void setY(double y) {
			this.y = y;
			this.shape.setCenterY(y + height/2);
		}

		@Override
		public void setWidth(double width) {
			this.width = width;
			this.shape.setRadiusX(width/2);
		}

		@Override
		public void setHeight(double height) {
			this.height = height;
			this.shape.setRadiusY(height/2);
		}

		@Override
		public void setFill(Color fill) {
			this.fill = fill;
			this.shape.setFill(fill);
		}

		@Override
		public void setStroke(Color stroke) {
			this.stroke = stroke;
			this.shape.setStroke(stroke);
		}
		
		@Override
		public void setStrokeWidth(double width) {	
			this.strokeWidth = width;
			this.shape.setStrokeWidth(width);
		}
	}
}
