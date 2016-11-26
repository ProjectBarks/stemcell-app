package org.dasd.stemcell.view.popover;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import lombok.Getter;

import java.util.Optional;

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
	private Pane wrapper;

	public Popover(T content, DropShadow shadow, Triangle triangle) {
		this(content, shadow, triangle, CORNER_CURVE);
	}

	public Popover(T content, DropShadow shadow, Triangle triangle, int cornerCurve) {
		super();
		setBackground(Background.EMPTY);
		setContent(content);
		setCornerCurve(cornerCurve);
		setShadow(shadow);
		setTriangle(triangle);
	}

	public Optional<T> getContent() {
		if (wrapper == null) return Optional.empty();
		return Optional.of((T) wrapper.getChildren().get(0));
	}

	public void setContent(T content) {
		Optional<T> oldContent = getContent();
		if (oldContent.isPresent()) oldContent.get().setClip(null);
		wrapper = new Pane(content);
		setCenter(new Group(wrapper));
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

		polygon.getTransforms().add(triangle.getRotate());
		polygon.setFill(triangle.color);
		BorderPane.setAlignment(polygon, Pos.CENTER);
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
			// FIXME: 11/25/16
			case LEFT:
				setLeft(polygon);
				break;
		}
		if (wrapper != null) BorderPane.setAlignment(wrapper.getParent(), this.triangle.side.alignStrategy);
	}

	public void setShadow(DropShadow shadow) {
		getCenter().setEffect(shadow);
	}

	private void updateContent() {
		Optional<T> content = getContent();
		if (!content.isPresent()) return;
		T item = content.get();
		Rectangle clip = new Rectangle(item.getPrefWidth(), item.getPrefHeight());
		clip.setArcWidth(cornerCurve);
		clip.setArcHeight(cornerCurve);
		item.setClip(clip);
	}
}
