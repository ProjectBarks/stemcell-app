package org.dasd.stemcell.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

public class ScreensController extends StackPane {

	private HashMap<String, Container> screens;

	public ScreensController(int width, int height) {
		super();
		this.setPrefWidth(width);
		this.setPrefHeight(height);
		this.setBackground(null);
		screens = new HashMap<>();


	}

	private static Timeline getTimeline(int duration, DoubleProperty opacity) {
		KeyFrame startingFrame = new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0));
		KeyFrame endingFrame = new KeyFrame(new Duration(duration), new KeyValue(opacity, 1.0));
		return new Timeline(startingFrame, endingFrame);
	}

	public boolean setScreen(final String name) {
		if (!screens.containsKey(name)) {
			if (loadScreen(name)) setScreen(name);
			else return false;
		}

		final DoubleProperty opacity = opacityProperty();
		Timeline fade;

		if (!getChildren().isEmpty()) {
			fade = new Timeline(
					new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
					new KeyFrame(new Duration(25), event -> {
						getChildren().remove(0);
						getChildren().add(0, initScreen(name));
						Timeline fadeIn = getTimeline(500, opacity);
						fadeIn.play();
					}, new KeyValue(opacity, 0)));
		} else {
			setOpacity(0);
			getChildren().add(initScreen(name));
			fade = getTimeline(500, opacity);
		}
		fade.play();
		return true;
	}

	public boolean loadScreen(String name) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + name + ".fxml"));
			Pane screen = loader.load();
			ControlledScreen controller = loader.getController();
			controller.setScreenParent(this);
			addScreen(name, screen, controller);
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	public void addScreen(String name, Pane screen, Object controller) {
		screens.put(name, new Container(controller, screen));
	}

	public void unloadScreen(String name) {
		if (screens.containsKey(name)) screens.remove(name);
	}

	public <T> T getController(String name) {
		return (T) screens.get(name).getController();
	}

	public Pane getScreen(String name) {
		return screens.get(name).getPane();
	}

	private Pane initScreen(String name) {
		Pane pane = getScreen(name);
		Rectangle rectangle = new Rectangle(this.getPrefWidth(), this.getPrefHeight());
		rectangle.setArcHeight(15);
		rectangle.setArcWidth(15);
		pane.setClip(rectangle);
		pane.setPrefWidth(this.getPrefWidth());
		pane.setPrefHeight(this.getPrefHeight());
		return pane;
	}

	@AllArgsConstructor
	private class Container {
		@Getter
		private Object controller;
		@Getter
		private Pane pane;
	}
}