package org.dasd.stemcell;

import com.sun.javafx.application.PlatformImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.dasd.stemcell.notification.NotificationService;
import org.dasd.stemcell.service.ServiceManager;
import org.dasd.stemcell.service.TimedService;
import org.dasd.stemcell.tray.Clock;
import org.dasd.stemcell.tray.OutlineRenderer;
import org.dasd.stemcell.view.ScreensController;
import org.dasd.stemcell.view.popover.Popover;
import org.dasd.stemcell.view.popover.Side;
import org.dasd.stemcell.view.popover.Triangle;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
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

	public static void toFocus() {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("AppleScript");

		try {
			engine.eval("tell me to activate");
		} catch (ScriptException e) {
			e.printStackTrace();
		}
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
		services.add(new NotificationService());

		Scene scene = new Scene(new Popover<>(controller, new DropShadow(15, Color.GREY), new Triangle(15, Side.TOP, Color.valueOf("#1565C0"))));
		scene.setFill(null);
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setWidth(WIDTH + 40);
		stage.setHeight(HEIGHT + 35);
		stage.setAlwaysOnTop(true);

		controller.setScreen(LOGIN_SCREEN);
		serviceManager.start();
	}
}
