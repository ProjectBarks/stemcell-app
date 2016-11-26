package org.dasd.stemcell.notification.mac;

import org.dasd.stemcell.STEMCell;
import org.dasd.stemcell.notification.BasicNotification;
import org.dasd.stemcell.notification.mac.core.NSUserNotification;
import org.dasd.stemcell.notification.mac.core.NSUserNotificationCenter;

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
public class MacNotification implements BasicNotification<NSUserNotification> {

	private NSUserNotification notification;

	public MacNotification() {
		notification = new NSUserNotification();
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
		return notification.getText();
	}

	@Override
	public void setMessage(String message) {
		notification.setText(message);
	}

	@Override
	public void show(long duration, TimeUnit unit) {
		STEMCell.run(() -> {
			try {
				NSUserNotificationCenter.getInstance().deliverNotification(notification);
				Thread.sleep(unit.toMillis(duration));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				notification.close();
			}
		});
	}

	@Override
	public NSUserNotification getRoot() {
		return notification;
	}
}
