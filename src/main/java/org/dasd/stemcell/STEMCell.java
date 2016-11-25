package org.dasd.stemcell;

import com.sun.javafx.application.PlatformImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.dasd.stemcell.service.ServiceManager;
import org.dasd.stemcell.service.TimedService;
import org.dasd.stemcell.tray.Clock;
import org.dasd.stemcell.tray.OutlineRenderer;
import org.dasd.stemcell.view.ScreensController;

import java.awt.*;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Written By: brandon on 11/19/16
 */
public class STEMCell extends Application {


	public final static String HOME_SCREEN = "home", LOGIN_SCREEN = "login";
	public final static int WIDTH = 525, HEIGHT = 325;
	private final static ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

	public static void main(String[] args) {
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		PlatformImpl.setTaskbarApplication(false);
		Platform.setImplicitExit(false);

		Application.launch(STEMCell.class, args);
	}

	public static void run(Runnable runnable) {
		service.submit(() -> {
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			runnable.run();
		});
	}

	public static void run(final Runnable runnable, long interval, TimeUnit unit) {
		service.submit(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					runnable.run();
					Thread.sleep(unit.toMillis(interval));
				} catch (Throwable ex) {
					ex.printStackTrace();
					Thread.currentThread().interrupt();
				}
			}
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		});
	}

	@Override
	public void start(Stage stage) {
		ScreensController controller = new ScreensController(WIDTH, HEIGHT);
		controller.loadScreen(HOME_SCREEN);
		controller.loadScreen(LOGIN_SCREEN);

		ServiceManager serviceManager = new ServiceManager();
		serviceManager.setDays(new TreeSet<>(TestData.days));
		Set<TimedService> services = serviceManager.getServices();
		services.add(controller.getController(HOME_SCREEN));
		services.add(new Clock(stage, new OutlineRenderer(new Font("Helvetica", Font.PLAIN, 15))));

		BorderPane wrapper = new BorderPane();
		double triangleSize = 15;
		Polygon polygon = generateTriangle(triangleSize, triangleSize, false);
		BorderPane.setAlignment(polygon, Pos.CENTER);
		polygon.setFill(Color.valueOf("#1565C0"));
		wrapper.setCenter(controller);
		wrapper.setTop(polygon);
		wrapper.setBackground(null);


		controller.setEffect(new DropShadow(5, Color.BLACK));
		controller.setPadding(new Insets(-10, 0, 0, 0));

		Scene scene = new Scene(wrapper);
		scene.setFill(null);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setWidth(WIDTH + 10);
		stage.setHeight(HEIGHT + triangleSize + 10);
		stage.setAlwaysOnTop(true);

		controller.setScreen(LOGIN_SCREEN);
		serviceManager.start();
	}

	private Polygon generateTriangle(double width, double height, boolean flipped) {
		Polygon polygon = new Polygon();
		ObservableList<Double> points = polygon.getPoints();
		for (double x = -width; x <= width; x++) {
			points.add(x + width);
			double y = ((height / 2) * (Math.cos(x * (Math.PI / width)) + 1));
			points.add(flipped ? y : height - y);
		}
		return polygon;
	}
}
