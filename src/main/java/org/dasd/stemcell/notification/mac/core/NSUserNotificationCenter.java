package org.dasd.stemcell.notification.mac.core;

import com.sun.jna.Callback;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import org.dasd.stemcell.notification.mac.libraries.NSUserNotificationCenterCInterface;
import org.dasd.stemcell.notification.mac.libraries.NSUserNotificationCenterDelegate;

import java.util.ArrayList;

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
 * Written By: brandon on 12/8/14
 */
public class NSUserNotificationCenter {
	private static NSUserNotificationCenterCInterface library = null;
	private static NSUserNotificationCenter instance = null;
	private static Callback defaultActivateCallback = new Callback() {
		public void callback(Pointer n) {
			System.out.println("no delegate defined.");
		}
	};
	private static Callback defaultDeliverCallback = new Callback() {
		public void callback(Pointer n) {
			System.out.println("no delegate defined.");
		}
	};
	private static Callback defaultPresentCallback = new Callback() {
		public byte callback(Pointer n) {
			System.out.println("no delegate defined.");
			return 0;
		}
	};

	private NSUserNotificationCenter() {
		if (library == null)
			library = (NSUserNotificationCenterCInterface) Native.loadLibrary("OSXNotification", NSUserNotificationCenterCInterface.class);
	}

	public static NSUserNotificationCenter getInstance() {
		if (instance == null) {
			instance = new NSUserNotificationCenter();
			instance.setDelegate(null);
		}
		return instance;
	}

	public ArrayList<NSUserNotification> getDeliveredNotifications() {
		int count = library.NSUserNotificationCenterGetDeliveredNotificationsCount();
		ArrayList mNotifications = new ArrayList();
		for (int i = 0; i < count; i++) {
			Pointer tmp = library.NSUserNotificationCenterGetDeliveredNotification(i);
			mNotifications.add(new NSUserNotification(tmp));
		}
		return mNotifications;
	}

	public void deliverNotification(NSUserNotification notice) {
		library.NSUserNotificationCenterDeliverNotification(notice.getPointer());
	}

	public ArrayList<NSUserNotification> getScheduledNotifications() {
		int count = library.NSUserNotificationCenterGetScheduledNotificationsCount();
		ArrayList mNotifications = new ArrayList();
		for (int i = 0; i < count; i++) {
			Pointer tmp = library.NSUserNotificationCenterGetScheduledNotification(i);
			mNotifications.add(new NSUserNotification(tmp));
		}
		return mNotifications;
	}

	public void scheduleNotification(NSUserNotification notice) {
		library.NSUserNotificationCenterScheduleNotification(notice.getPointer());
	}

	public void removeScheduledNotification(NSUserNotification notice) {
		library.NSUserNotificationCenterRemoveScheduledNotification(notice.getPointer());
	}

	public void removeDeliveredNotification(NSUserNotification notice) {
		library.NSUserNotificationCenterRemoveDeliveredNotification(notice.getPointer());
	}

	public void removeAllDeliveredNotifications() {
		library.NSUserNotificationCenterRemoveAllDeliveredNotifications();
	}

	public void setDefaultDelegate() {
		library.NSUserNotificationCenterSetDefaultDelegate();
	}

	public void setDelegate(final NSUserNotificationCenterDelegate delegate) {
		Callback activate;
		Callback deliver;
		Callback present;
		if (delegate != null) {
			System.out.println("delegate is not null");
			activate = new Callback() {
				public void callback(Pointer n) {
					delegate.didActivateNotification(new NSUserNotification(n));
				}
			};
			deliver = new Callback() {
				public void callback(Pointer n) {
					delegate.didDeliverNotification(new NSUserNotification(n));
				}
			};
			present = new Callback() {
				public byte callback(Pointer n) {
					return delegate.shouldPresentNotification(new NSUserNotification(n));
				}
			};
		} else {
			activate = defaultActivateCallback;
			deliver = defaultDeliverCallback;
			present = defaultPresentCallback;
		}
		library.NSUserNotificationCenterSetDelegate(activate, deliver, present);
	}
}