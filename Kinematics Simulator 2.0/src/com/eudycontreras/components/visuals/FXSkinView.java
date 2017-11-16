package com.eudycontreras.components.visuals;
//package com.eudycontreras.components.visual.views;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.layout.Pane;
//import javafx.scene.paint.Color;
//import javafx.scene.paint.ImagePattern;
//import javafx.stage.Stage;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.effect.DisplacementMap;
//import javafx.scene.effect.Effect;
//import javafx.scene.effect.FloatMap;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.StackPane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
//import javafx.stage.Stage;
//import static javafx.application.Application.launch;
//
////revised from JavaFX API
//
//import javafx.application.Application;
//import javafx.scene.Group;
//import javafx.scene.Scene;
//import javafx.scene.effect.DisplacementMap;
//import javafx.scene.effect.FloatMap;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.scene.text.Text;
//import javafx.stage.Stage;
//
//public class FXGraphicView extends Application {
//	public static void main(String[] args) {
//		Application.launch(args);
//	}
//
//	@Override
//	public void start(Stage primaryStage) {
//		primaryStage.setTitle("");
//		Group root = new Group();
//		Scene scene = new Scene(root, 1200, 1, Color.WHITE);
//
//		int w = 220;
//		int h = 600;
//		FloatMap map = new FloatMap();
//		map.setWidth(w);
//		map.setHeight(h);
//
//		for (int i = 0; i < w; i++) {
//			double v = (Math.sin(i / 20.0 * Math.PI) - 0.5) / 40.0;
//			for (int j = 0; j < h; j++) {
//				map.setSamples(i, j, 0.0f, (float) v);
//			}
//		}
//
//		Group g = new Group();
//		DisplacementMap dm = new DisplacementMap();
//		dm.setMapData(map);
//
//		g.setEffect(dm);
//		g.setCache(true);
//
//
//		Rectangle r = new Rectangle();
//		r.setX(20.0);
//		r.setY(20.0);
//		r.setWidth(w);
//		r.setHeight(h);
//		r.setFill(new ImagePattern(new Image(getClass().getResource("augmentation-robotic-arm.jpg").toString())));
//
//		g.getChildren().add(r);
//
//		Text t = new Text();
//		t.setX(40.0);
//		t.setY(80.0);
//		t.setText("Wavy Text");
//		t.setFill(Color.YELLOW);
//		t.setFont(Font.font(null, FontWeight.BOLD, 36));
//
//		g.getChildren().add(t);
//
//		root.getChildren().add(g);
//		primaryStage.setScene(scene);
//		primaryStage.show();
//	}
//}
