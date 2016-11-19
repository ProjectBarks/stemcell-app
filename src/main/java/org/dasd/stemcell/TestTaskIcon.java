package org.dasd.stemcell;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class TestTaskIcon {

	public static void main(String[] args) {
		TrayIcon icon = new TrayIcon(img, "Tooltip");
		icon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Optional<Rectangle> safeBounds = getSafeScreenBounds(e.getPoint());
				if (!safeBounds.isPresent()) {
					System.err.println("Screen location not found!");
					return;
				}

				Rectangle bounds = safeBounds.get();
				Point point = e.getPoint();

				int x = updatePosition(point.x, bounds.x, bounds.width, 50);
				int y = updatePosition(point.y, bounds.y, bounds.height, 50);
			}
		});
		try {
			SystemTray.getSystemTray().add(ti);
		} catch (AWTException ex) {
			Logger.getLogger(TestTaskIcon.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private static int updatePosition(int point, int boundsPoint, int boundsSize, int preferredSize) {
		point = point < boundsPoint ? boundsPoint : point;
		point = point > boundsPoint + boundsSize ? boundsPoint + boundsSize : point;
		point = point + preferredSize > boundsPoint + boundsSize ? (point + preferredSize) - (boundsPoint + boundsSize) : point;
		return point;
	}

	private static Optional<Rectangle> getSafeScreenBounds(Point pos) {
		Optional<Rectangle> bounds = getScreenBoundsAt(pos);
		Optional<Insets> insets = getScreenInsetsAt(pos);

		if (!insets.isPresent() || !bounds.isPresent()) return Optional.empty();

		Rectangle safeRegion = bounds.get();
		Insets safeOffsets = insets.get();

		safeRegion.x += safeOffsets.left;
		safeRegion.y += safeOffsets.top;
		safeRegion.width -= (safeOffsets.left + safeOffsets.right);
		safeRegion.height -= (safeOffsets.top + safeOffsets.bottom);

		return Optional.of(safeRegion);

	}

	private static Optional<Insets> getScreenInsetsAt(Point pos) {
		Optional<GraphicsDevice> gd = getGraphicsDeviceAt(pos);
		return gd.map(graphicsDevice -> Toolkit.getDefaultToolkit().getScreenInsets(graphicsDevice.getDefaultConfiguration()));
	}

	private static Optional<Rectangle> getScreenBoundsAt(Point pos) {
		Optional<GraphicsDevice> gd = getGraphicsDeviceAt(pos);
		return gd.map(graphicsDevice -> graphicsDevice.getDefaultConfiguration().getBounds());
	}

	private static Optional<GraphicsDevice> getGraphicsDeviceAt(Point pos) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Optional<GraphicsDevice> devices = Stream.of(ge.getScreenDevices())
				.filter(x -> x.getDefaultConfiguration().getBounds().contains(pos))
				.findFirst();

		return devices.isPresent() ? devices : Optional.of(ge.getDefaultScreenDevice());
	}
}
