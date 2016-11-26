package org.dasd.stemcell.notification.mac.libraries;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface NSUserNotificationCInterface extends Library {
	Pointer NSUserNotificationAllocInit();

	void NSUserNotificationSetTitle(Pointer paramPointer1, Pointer paramPointer2);

	Pointer NSUserNotificationGetTitle(Pointer paramPointer);

	void NSUserNotificationSetSubtitle(Pointer paramPointer1, Pointer paramPointer2);

	Pointer NSUserNotificationGetSubtitle(Pointer paramPointer);

	void NSUserNotificationSetInformativeText(Pointer paramPointer1, Pointer paramPointer2);

	Pointer NSUserNotificationGetInformativeText(Pointer paramPointer);

	void NSUserNotificationSetHasActionButton(Pointer paramPointer, byte paramByte);

	byte NSUserNotificationHasActionButton(Pointer paramPointer);

	void NSUserNotificationSetActionButtonTitle(Pointer paramPointer1, Pointer paramPointer2);

	Pointer NSUserNotificationGetActionButtonTitle(Pointer paramPointer);

	void NSUserNotificationSetOtherButtonTitle(Pointer paramPointer1, Pointer paramPointer2);

	Pointer NSUserNotificationGetOtherButtonTitle(Pointer paramPointer);

	void NSUserNotificationSetDeliveryDate(Pointer paramPointer1, Pointer paramPointer2);

	Pointer NSUserNotificationGetDeliveryDate(Pointer paramPointer);

	Pointer NSUserNotificationGetActualDeliveryDate(Pointer paramPointer);

	void NSUserNotificationSetDeliveryRepeatInterval(Pointer paramPointer, int paramInt);

	int NSUserNotificationGetDeliveryRepeatInterval(Pointer paramPointer);

	void NSUserNotificationSetDeliveryTimeZone(Pointer paramPointer1, Pointer paramPointer2);

	Pointer NSUserNotificationGetDeliveryTimeZone(Pointer paramPointer);

	byte NSUserNotificationIsPresented(Pointer paramPointer);

	byte NSUserNotificationIsRemote(Pointer paramPointer);

	Pointer NSUserNotificationGetSoundName(Pointer paramPointer);

	void NSUserNotificationSetSoundName(Pointer paramPointer1, Pointer paramPointer2);

	void NSUserNotificationSetDefaultSoundName(Pointer paramPointer);

	byte NSUserNotificationGetActivationType(Pointer paramPointer);

	void NSUserNotificationRelease(Pointer paramPointer);
}