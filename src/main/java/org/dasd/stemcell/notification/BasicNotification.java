package org.dasd.stemcell.notification;

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
public interface BasicNotification<T> {

	String getTitle();

	void setTitle(String title);

	String getMessage();

	void setMessage(String message);

	void show(long duration, TimeUnit unit);

	T getRoot();

}
