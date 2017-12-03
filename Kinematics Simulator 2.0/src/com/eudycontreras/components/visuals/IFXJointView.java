package com.eudycontreras.components.visuals;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public interface IFXJointView {

	 	public Node getNode();
	    
	 	public Shape getShape();
	 	
		public void setStroke(Color colorNormal);
	 	
	    public void setRadius(double radius);
	    
	    public void setOuterRadius(double outerRadius);

		public void setInnerRadius(double innerRadius);

		public void setCenterRadius(double centerRadius);
		
		public void setSectionDistance(double sectionDistance);

		public void setCenterX(double centerX);

		public void setCenterY(double centerY);

		public void setMainColor(Color mainColor);

		public void setCenterColor(Color centerColor);

		public double getOuterRadius();

		public double getInnerRadius();

		public double getCenterRadius();

		public double getSectionDistance();
		
		public double getCenterX();

		public double getCenterY();
		
		public void setSelected(boolean selected);

		public Color getMainColor();

		public Color getCenterColor();
}
