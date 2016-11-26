package org.dasd.stemcell.notification;

import org.dasd.stemcell.notification.mac.MacNotification;
import org.dasd.stemcell.notification.universal.UniversalNotification;

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
public class NotificationFactory {

	public static void showNotification(String title, String message, long duration) {
		showNotification(title, message, duration, TimeUnit.MILLISECONDS);
	}

	public static void showNotification(String title, String message, long duration, TimeUnit unit) {
		BasicNotification notification = getNotification(title, message);
		notification.show(duration, unit);
	}

	public static BasicNotification getNotification(String title, String message) {
		BasicNotification notification = getNotification();
		notification.setTitle(title);
		notification.setMessage(message);
		return notification;
	}

	public static BasicNotification getNotification() {
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			return new MacNotification();
		} else {
			return new UniversalNotification();
		}
	}
}
