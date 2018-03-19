package user.ui.componentJavaFX;

import java.awt.geom.Rectangle2D;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import user.tools.GraphicalCharter;

public class MyLabel extends Label{

	public MyLabel(String text,Rectangle2D.Float bounds) {
		setText(text);
		setLayoutX(bounds.getX());
		setLayoutY(bounds.getY());
		setPrefWidth(bounds.getWidth());
		setPrefHeight(bounds.getHeight());
		setFont(Font.font(GraphicalCharter.FONT,GraphicalCharter.FONT_SIZE));
	}
	
	public MyLabel(String text,Rectangle2D.Float bounds,float fontMultiplier) {
		setText(text);
		setLayoutX(bounds.getX());
		setLayoutY(bounds.getY());
		setPrefWidth(bounds.getWidth());
		setPrefHeight(bounds.getHeight());
		setFont(Font.font(GraphicalCharter.FONT,GraphicalCharter.FONT_SIZE * fontMultiplier));
	}
	
}