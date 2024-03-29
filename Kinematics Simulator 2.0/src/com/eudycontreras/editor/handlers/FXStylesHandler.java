package com.eudycontreras.editor.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.scene.Parent;
import javafx.scene.paint.Color;

import com.sun.javafx.css.converters.ColorConverter;


public class FXStylesHandler extends Parent{
	
  public static final Color DEFAULT_NAMED_COLOR = null;

  private ObjectProperty<Color> namedColor;

  public ObjectProperty<Color> namedColorProperty() {
	  
    if(namedColor == null) {
      namedColor = new StyleableObjectProperty<Color>(DEFAULT_NAMED_COLOR) {

        @Override
        protected void invalidated() {
          super.invalidated();
        }

        @Override
        public CssMetaData<? extends Styleable, Color> getCssMetaData() {
          return StyleableProperties.NAMED_COLOR;
        }

        @Override
        public Object getBean() {
          return FXStylesHandler.this;
        }

        @Override
        public String getName() {
          return "namedColor";
        }
      };
    }
    return namedColor;
  }

  public Color getNamedColor() {
    return namedColorProperty().get();
  }

  public FXStylesHandler() {
    setFocusTraversable(false);
    getStyleClass().add("css-to-color-helper");
  }

  private static class StyleableProperties {
	  
    private static final CssMetaData<FXStylesHandler, Color> NAMED_COLOR = new CssMetaData<FXStylesHandler, Color>("-named-color", ColorConverter.getInstance(),
            DEFAULT_NAMED_COLOR) {

          @Override
          public boolean isSettable(FXStylesHandler n) {
            return n.namedColor == null || !n.namedColor.isBound();
          }

          @Override
          public StyleableProperty<Color> getStyleableProperty(FXStylesHandler n) {
            return (StyleableProperty<Color>) (WritableValue<Color>) n.namedColorProperty();
          }

      };

      private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
      
      static {
        final List<CssMetaData<? extends Styleable, ?>> styleables =
            new ArrayList<>(Parent.getClassCssMetaData());
        styleables.add(NAMED_COLOR);
        STYLEABLES = Collections.unmodifiableList(styleables);
      }
    }

  @Override
  public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
    return StyleableProperties.STYLEABLES;
  }
}