package org.dasd.stemcell.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.dasd.stemcell.STEMCell;
import org.dasd.stemcell.schedule.Day;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
public class ServiceManager {

	@Setter
	@Getter
	private SortedSet<Day> days;
	@Getter
	private Set<TimedService> services;
	private LocalDate lastDate;
	private LocalTime lastTime;

	public ServiceManager() {
		this.days = new TreeSet<>();
		this.services = new HashSet<>();
	}

	public void start() {
		STEMCell.run(() -> {
			LocalDateTime now = LocalDateTime.now();
			if (lastDate == null || now.getDayOfYear() != lastDate.getDayOfYear()) {
				services.forEach(x -> x.onDayChanged(this));
				lastDate = LocalDate.now();
			}
			if (lastTime == null || now.getHour() != lastTime.getHour() || now.getMinute() != lastTime.getMinute()) {
				services.forEach(x -> x.onMinuteChanged(this));
				lastTime = LocalTime.now();
			}
		}, 5, TimeUnit.SECONDS);
	}

	public List<Day> getWeek() {
		LocalDate monday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate friday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
		return days.stream()
				.filter(x -> (x.getDate().isAfter(monday) || x.getDate().isEqual(monday)) && (x.getDate().isBefore(friday) || x.getDate().isEqual(friday)))
				.sorted()
				.collect(Collectors.toList());
	}

	public Optional<Day> getToday() {
		return days.stream().filter(x -> x.getDate().equals(LocalDate.now())).findAny();
	}
}
