package org.dasd.stemcell.view.popover;

import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Triangle {
	@Getter
	protected double width, height;
	@Getter
	protected Side side;
	@Getter
	protected Color color;

	public Triangle(double size, Side side, Color color) {
		this(size, size, side, color);
	}

	protected Rotate getRotate() {
		return side.getRotate(width / 2, height / 2);
	}
}