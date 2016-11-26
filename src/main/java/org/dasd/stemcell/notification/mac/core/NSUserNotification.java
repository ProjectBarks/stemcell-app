package org.dasd.stemcell.notification.mac.core;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import org.dasd.stemcell.notification.mac.libraries.NSUserNotificationCInterface;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
public class NSUserNotification {

	private static NSUserNotificationCInterface library = null;
	private Pointer notification;

	public NSUserNotification() {
		if (library == null) {
			System.setProperty("jna.library.path", getClass().getResource("/").getPath());
			library = (NSUserNotificationCInterface) Native.loadLibrary("OSXNotification", NSUserNotificationCInterface.class);
		}
		this.notification = library.NSUserNotificationAllocInit();
	}

	public NSUserNotification(Pointer object) {
		if (library == null) {
			library = (NSUserNotificationCInterface) Native.loadLibrary("OSXNotification", NSUserNotificationCInterface.class);
		}
		this.notification = object;
	}

	private static byte[] bytesFromString(String input) {
		return bytesFromString(input, "US-ASCII");
	}

	private static byte[] bytesFromString(String input, String charSet) {
		byte[] returnBytes = null;
		if ((input != null) && (!input.equals(""))) {
			try {
				returnBytes = input.getBytes(charSet);
			} catch (UnsupportedEncodingException e) {
				System.out.println(": Data error:" + e.getMessage());
			}
		}
		return returnBytes;
	}

	private static String stringFromBytes(byte[] input) {
		return stringFromBytes(input, "US-ASCII");
	}

	private static String stringFromBytes(byte[] input, String charSet) {
		String returnString = "";
		if (input != null) {
			try {
				returnString = new String(input, 0, input.length, charSet);
			} catch (UnsupportedEncodingException e) {
				System.out.println("Data error:" + e.getMessage());
			}
		}
		return returnString;
	}

	public Pointer getPointer() {
		return this.notification;
	}

	public String getTitle() {
		if (this.notification == null) {
			return null;
		}
		Pointer p = library.NSUserNotificationGetTitle(this.notification);
		return stringFromPointer(p);
	}

	public void setTitle(String input) {
		if (this.notification == null) {
			return;
		}
		byte[] inputBytes = bytesFromString(input + '\000');
		Pointer titleText = new Memory(inputBytes.length);
		titleText.write(0L, inputBytes, 0, inputBytes.length);
		library.NSUserNotificationSetTitle(this.notification, titleText);
	}

	public String getSubtitle() {
		if (this.notification == null) {
			return null;
		}
		Pointer p = library.NSUserNotificationGetSubtitle(this.notification);
		return stringFromPointer(p);
	}

	public void setSubtitle(String input) {
		if (this.notification == null) {
			return;
		}
		byte[] inputBytes = bytesFromString(input + '\000');
		Pointer subtitleText = new Memory(inputBytes.length);
		subtitleText.write(0L, inputBytes, 0, inputBytes.length);
		library.NSUserNotificationSetSubtitle(this.notification, subtitleText);
	}

	public String getText() {
		if (this.notification == null) {
			return null;
		}
		Pointer p = library.NSUserNotificationGetInformativeText(this.notification);
		return stringFromPointer(p);
	}

	public void setText(String input) {
		if (this.notification == null) {
			return;
		}
		byte[] inputBytes = bytesFromString(input + '\000');
		Pointer informativeText = new Memory(inputBytes.length);
		informativeText.write(0L, inputBytes, 0, inputBytes.length);
		library.NSUserNotificationSetInformativeText(this.notification, informativeText);
	}

	public void enableActionButton() {
		if (this.notification == null) {
			return;
		}
		library.NSUserNotificationSetHasActionButton(this.notification, (byte) 1);
	}

	public void disableActionButton() {
		if (this.notification == null) {
			return;
		}
		library.NSUserNotificationSetHasActionButton(this.notification, (byte) 0);
	}

	public boolean hasActionButton() {
		if (this.notification == null) {
			return false;
		}
		boolean enabled = false;
		if (library.NSUserNotificationHasActionButton(this.notification) == 1) {
			enabled = true;
		}
		return enabled;
	}

	public String getActionButtonTitle() {
		if (this.notification == null) {
			return null;
		}
		Pointer p = library.NSUserNotificationGetActionButtonTitle(this.notification);
		return stringFromPointer(p);
	}

	public void setActionButtonTitle(String input) {
		if (this.notification == null) {
			return;
		}
		byte[] inputBytes = bytesFromString(input + '\000');
		Pointer actionButtonText = new Memory(inputBytes.length);
		actionButtonText.write(0L, inputBytes, 0, inputBytes.length);
		library.NSUserNotificationSetActionButtonTitle(this.notification, actionButtonText);
	}

	public String getOtherButtonTitle() {
		if (this.notification == null) {
			return null;
		}
		Pointer p = library.NSUserNotificationGetOtherButtonTitle(this.notification);
		return stringFromPointer(p);
	}

	public void setOtherButtonTitle(String input) {
		if (this.notification == null) {
			return;
		}
		byte[] inputBytes = bytesFromString(input + '\000');
		Pointer otherButtonText = new Memory(inputBytes.length);
		otherButtonText.write(0L, inputBytes, 0, inputBytes.length);
		library.NSUserNotificationSetOtherButtonTitle(this.notification, otherButtonText);
	}

	public Calendar getDeliveryDate() {
		if (this.notification == null) {
			return null;
		}
		Pointer p = library.NSUserNotificationGetDeliveryDate(this.notification);
		String deliveryString = stringFromPointer(p);
		SimpleDateFormat NSDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		ParsePosition pos = new ParsePosition(0);
		Date deliveryDate = NSDateFormat.parse(deliveryString, pos);
		Calendar deliveryCalendar = Calendar.getInstance();
		deliveryCalendar.setTime(deliveryDate);
		return deliveryCalendar;
	}

	public void setDeliveryDate(Calendar date) {
		if (this.notification == null) {
			return;
		}
		Calendar now = Calendar.getInstance();
		DateFormat NSDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		String NSDateString = NSDateFormat.format(date.getTime());
		byte[] inputBytes = bytesFromString(NSDateString + '\000');
		if (date.after(now)) {
			Pointer deliveryDate = new Memory(inputBytes.length);
			deliveryDate.write(0L, inputBytes, 0, inputBytes.length);
			library.NSUserNotificationSetDeliveryDate(this.notification, deliveryDate);
		}
	}

	public Calendar getActualDeliveryDate() {
		if (this.notification == null) {
			return null;
		}
		Pointer p = library.NSUserNotificationGetActualDeliveryDate(this.notification);
		String deliveryString = stringFromPointer(p);
		SimpleDateFormat NSDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		ParsePosition pos = new ParsePosition(0);
		Date deliveryDate = NSDateFormat.parse(deliveryString, pos);
		Calendar deliveryCalendar = Calendar.getInstance();
		deliveryCalendar.setTime(deliveryDate);
		return deliveryCalendar;
	}

	public int getDeliveryRepeatInterval() {
		if (this.notification == null) {
			return -1;
		}
		return library.NSUserNotificationGetDeliveryRepeatInterval(this.notification);
	}

	public void setDeliveryRepeatInterval(int seconds) {
		if (this.notification == null) {
			return;
		}
		if (seconds >= 60) library.NSUserNotificationSetDeliveryRepeatInterval(this.notification, seconds);
	}

	public String getDeliveryTimeZone() {
		if (this.notification == null) {
			return null;
		}
		Pointer p = library.NSUserNotificationGetDeliveryTimeZone(this.notification);
		return stringFromPointer(p);
	}

	public void setDeliveryTimeZone(String input) {
		if (this.notification == null) {
			return;
		}
		byte[] inputBytes = bytesFromString(input + '\000');
		Pointer deliveryTimeZone = new Memory(inputBytes.length);
		deliveryTimeZone.write(0L, inputBytes, 0, inputBytes.length);
		library.NSUserNotificationSetDeliveryTimeZone(this.notification, deliveryTimeZone);
	}

	public boolean isPresented() {
		if (this.notification == null) {
			return false;
		}
		boolean presented = false;
		if (library.NSUserNotificationIsPresented(this.notification) == 1) {
			presented = true;
		}
		return presented;
	}

	public boolean isRemote() {
		if (this.notification == null) {
			return false;
		}
		boolean remote = false;
		if (library.NSUserNotificationIsRemote(this.notification) == 1) {
			remote = true;
		}
		return remote;
	}

	public String getSoundName() {
		if (this.notification == null) {
			return null;
		}
		Pointer p = library.NSUserNotificationGetSoundName(this.notification);
		return stringFromPointer(p);
	}

	public void setDefaultSoundName() {
		if (this.notification == null) {
			return;
		}
		library.NSUserNotificationSetDefaultSoundName(this.notification);
	}

	public int getActivationType() {
		if (this.notification == null) {
			return -1;
		}
		return library.NSUserNotificationGetActivationType(this.notification);
	}

	public void close() {
		if (this.notification == null) {
			return;
		}
		library.NSUserNotificationRelease(this.notification);
		this.notification = null;
	}

	private String stringFromPointer(Pointer p) {
		byte[] buffer = new byte[1024];
		int index = 0;
		byte tmp;
		while ((tmp = p.getByte(index)) != 0) {
			buffer[index] = tmp;
			index++;
		}
		String returnString = stringFromBytes(buffer);
		returnString = returnString.substring(0, index);
		return returnString;
	}
}