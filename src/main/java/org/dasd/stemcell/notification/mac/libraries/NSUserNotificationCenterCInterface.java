package org.dasd.stemcell.notification.mac.libraries;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface NSUserNotificationCenterCInterface extends Library {
	void NSUserNotificationCenterScheduleNotification(Pointer paramPointer);

	int NSUserNotificationCenterGetDeliveredNotificationsCount();

	Pointer NSUserNotificationCenterGetDeliveredNotification(int paramInt);

	void NSUserNotificationCenterRemoveScheduledNotification(Pointer paramPointer);

	void NSUserNotificationCenterDeliverNotification(Pointer paramPointer);

	int NSUserNotificationCenterGetScheduledNotificationsCount();

	Pointer NSUserNotificationCenterGetScheduledNotification(int paramInt);

	void NSUserNotificationCenterRemoveDeliveredNotification(Pointer paramPointer);

	void NSUserNotificationCenterRemoveAllDeliveredNotifications();

	void NSUserNotificationCenterSetDefaultDelegate();

	void NSUserNotificationCenterSetDelegate(Callback paramCallback1, Callback paramCallback2, Callback paramCallback3);
}