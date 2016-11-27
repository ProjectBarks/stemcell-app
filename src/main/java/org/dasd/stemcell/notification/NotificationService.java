package org.dasd.stemcell.notification;

import org.dasd.stemcell.schedule.Day;
import org.dasd.stemcell.schedule.Period;
import org.dasd.stemcell.service.ServiceManager;
import org.dasd.stemcell.service.TimedService;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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
public class NotificationService implements TimedService {
	@Override
	public void onDayChanged(ServiceManager manager) {
	}

	@Override
	public void onMinuteChanged(ServiceManager manager) {
		Optional<Day> today = manager.getToday();
		if (!today.isPresent() || !today.get().getCurrentPeriod().isPresent()) {
			return;
		}
		Period period = today.get().getCurrentPeriod().get();
		long between = ChronoUnit.MINUTES.between(LocalTime.now(), period.getEndTime());
		NotificationFactory.showNotification("STEMCell ~ Class Update", between + " minutes before class is over.", 5000);
	}
}
