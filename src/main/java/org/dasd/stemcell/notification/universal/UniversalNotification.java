package org.dasd.stemcell.notification.universal;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.TrayNotification;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.dasd.stemcell.notification.BasicNotification;

import java.util.concurrent.TimeUnit;

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
public class UniversalNotification implements BasicNotification<TrayNotification> {

	private TrayNotification notification;

	public UniversalNotification() {
		notification = new TrayNotification();
		notification.setAnimation(Animations.FADE);
		notification.setRectangleFill(Color.valueOf("#1565C0"));
	}

	@Override
	public String getTitle() {
		return notification.getTitle();
	}

	@Override
	public void setTitle(String title) {
		notification.setTitle(title);
	}

	@Override
	public String getMessage() {
		return notification.getMessage();
	}

	@Override
	public void setMessage(String message) {
		notification.setMessage(message);
	}


	@Override
	public void show(long duration, TimeUnit unit) {
		notification.showAndDismiss(new Duration(unit.toMillis(duration)));
	}

	@Override
	public TrayNotification getRoot() {
		return notification;
	}
}
