package org.dasd.stemcell.view;

import javafx.collections.ObservableList;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Written By: brandon on 11/25/16
 */
public class Popover<T extends Pane> extends BorderPane {

	public static final int CORNER_CURVE = 10;

	@Getter
	private Triangle triangle;
	@Getter
	private int cornerCurve;

	public Popover(T content, DropShadow shadow, Triangle triangle) {
		this(content, shadow, triangle, CORNER_CURVE);
	}

	public Popover(T content, DropShadow shadow, Triangle triangle, int cornerCurve) {
		super();
		setCornerCurve(cornerCurve);
		setContent(content);
		setShadow(shadow);
		setTriangle(triangle);
	}

	public T getContent() {
		return (T) ((Pane) getCenter()).getChildren().get(0);
	}

	public void setContent(T content) {
		T oldContent = getContent();
		if (oldContent != null) oldContent.setClip(null);
		setCenter(new Pane(content));
		updateContent();

	}

	public void setCornerCurve(int cornerCurve) {
		this.cornerCurve = cornerCurve;
		updateContent();
	}

	public void setTriangle(Triangle triangle) {
		this.triangle = triangle;
		setLeft(null);
		setTop(null);
		setRight(null);
		setBottom(null);

		Polygon polygon = new Polygon();
		ObservableList<Double> points = polygon.getPoints();
		for (double x = -triangle.width; x <= triangle.height; x++) {
			points.add(x + triangle.width);
			double y = ((triangle.height / 2) * (Math.cos(x * (Math.PI / triangle.width)) + 1));
			points.add(triangle.height - y);
		}

		polygon.getTransforms().add(triangle.side.getRotate());
		polygon.setFill(triangle.color);
		switch (this.triangle.side) {
			case TOP:
				setTop(polygon);
				break;
			case RIGHT:
				setRight(polygon);
				break;
			case BOTTOM:
				setBottom(polygon);
				break;
			case LEFT:
				setLeft(polygon);
				break;
		}
	}

	public void setShadow(DropShadow shadow) {
		getCenter().setEffect(shadow);
	}


	private void updateContent() {
		T content = getContent();
		Rectangle clip = new Rectangle(content.getPrefWidth(), content.getPrefHeight());
		clip.setArcWidth(cornerCurve);
		clip.setArcHeight(cornerCurve);
		content.setClip(clip);
	}

	@AllArgsConstructor
	public enum Side {
		TOP(0),
		LEFT(-90),
		RIGHT(90),
		BOTTOM(180);

		private int rotation;

		private Rotate getRotate() {
			return new Rotate(rotation, 0, 0);
		}
	}

	@AllArgsConstructor
	public class Triangle {
		@Getter
		private double width, height;
		@Getter
		private Side side;
		@Getter
		private Color color;

		public Triangle(double size, Side side, Color color) {
			this(size, size, side, color);
		}
	}
}
