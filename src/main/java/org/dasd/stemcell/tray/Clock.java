package org.dasd.stemcell.tray;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.dasd.stemcell.schedule.Day;
import org.dasd.stemcell.schedule.Period;
import org.dasd.stemcell.service.ServiceManager;
import org.dasd.stemcell.service.TimedService;
import sun.awt.AWTAutoShutdown;
import sun.lwawt.macosx.CTrayIcon;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.lang.reflect.Method;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class Clock implements TimedService {

	private TrayIcon icon;
	private BaseRenderer renderer;
	private String defaultMessage;

	public Clock(Stage stage, BaseRenderer renderer) {
		this(stage, renderer, "STEM");
	}

	public Clock(Stage stage, BaseRenderer renderer, String defaultMessage) {
		this.renderer = renderer;
		this.defaultMessage = defaultMessage;
		this.icon = new TrayIcon(renderer.drawIcon(defaultMessage));
		this.icon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Optional<Point2D> testPoint = getMacTrayPosition();
				Point2D point = null;
				if (testPoint.isPresent()) point = testPoint.get();
				stage.setX(point.getX() + (icon.getImage().getWidth(null) / 2) - (stage.getWidth() / 2));
				stage.setY(point.getY());
				Platform.runLater(() -> {
					if (stage.isShowing()) stage.hide();
					else stage.show();
				});
			}
		});
		try {
			SystemTray.getSystemTray().add(icon);
		} catch (AWTException ex) {
			System.err.println("Unable to init icon");
		}
	}

	@Override
	public void onDayChanged(ServiceManager manager) {
	}

	@Override
	public void onMinuteChanged(ServiceManager manager) {
		Optional<Day> today = manager.getToday();
		Optional<Period> currentPeriod;
		if (today.isPresent() && (currentPeriod = today.get().getCurrentPeriod()).isPresent()) {
			long remainingTime = Math.abs(ChronoUnit.MINUTES.between(LocalTime.now(), currentPeriod.get().getEndTime()));
			Image icon = renderer.drawIcon(String.valueOf(remainingTime));
			this.icon.setImage(icon);
		} else this.icon.setImage(this.renderer.drawIcon(this.defaultMessage));
	}

	/*private static int updatePosition(int point, int boundsPoint, int boundsSize, int preferredSize) {
		point = point < boundsPoint ? boundsPoint : point;
		point = point > boundsPoint + boundsSize ? boundsPoint + boundsSize : point;
		point = point + preferredSize > boundsPoint + boundsSize ? (point + preferredSize) - (boundsPoint + boundsSize) : point;
		return point;
	}

	private static Optional<Rectangle> getSafeScreenBounds(Point pos) {
		Optional<Rectangle> bounds = getGraphicsDeviceAt(pos).map(graphicsDevice -> graphicsDevice.getDefaultConfiguration().getBounds());
		Optional<Insets> insets = getGraphicsDeviceAt(pos).map(graphicsDevice -> Toolkit.getDefaultToolkit().getScreenInsets(graphicsDevice.getDefaultConfiguration()));

		if (!insets.isPresent() || !bounds.isPresent()) return Optional.empty();

		Rectangle safeRegion = bounds.get();
		Insets safeOffsets = insets.get();

		safeRegion.x += safeOffsets.left;
		safeRegion.y += safeOffsets.top;
		safeRegion.width -= (safeOffsets.left + safeOffsets.right);
		safeRegion.height -= (safeOffsets.top + safeOffsets.bottom);

		return Optional.of(safeRegion);

	}

	private static Optional<GraphicsDevice> getGraphicsDeviceAt(Point pos) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Optional<GraphicsDevice> devices = Stream.of(ge.getScreenDevices())
				.filter(x -> x.getDefaultConfiguration().getBounds().contains(pos))
				.findFirst();

		return devices.isPresent() ? devices : Optional.of(ge.getDefaultScreenDevice());
	}*/

	private Optional<Point2D> getMacTrayPosition() {
		try {
			AWTAutoShutdown instance = AWTAutoShutdown.getInstance();
			Method peer = AWTAutoShutdown.class.getDeclaredMethod("getPeer", Object.class);
			peer.setAccessible(true);
			CTrayIcon trayIcon = (CTrayIcon) peer.invoke(instance, icon);

			Method ptr = trayIcon.getClass().getDeclaredMethod("getModel");
			ptr.setAccessible(true);
			long id = (long) ptr.invoke(trayIcon);

			Method nativeGetIconLocation = CTrayIcon.class.getDeclaredMethod("nativeGetIconLocation", long.class);
			nativeGetIconLocation.setAccessible(true);
			return Optional.of((Point2D) nativeGetIconLocation.invoke(trayIcon, id));
		} catch (Exception ex) {
			ex.printStackTrace();
			return Optional.empty();
		}
	}
}
