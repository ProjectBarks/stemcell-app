package org.dasd.stemcell.view.popover;

import javafx.geometry.Pos;
import javafx.scene.transform.Rotate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Side {
	TOP(0, Pos.TOP_CENTER),
	LEFT(-90, Pos.CENTER_LEFT),
	RIGHT(90, Pos.CENTER_RIGHT),
	BOTTOM(180, Pos.BOTTOM_CENTER);

	protected int rotation;
	protected Pos alignStrategy;

	protected Rotate getRotate(double x, double y) {
		return new Rotate(rotation, x, y);
	}
}