package org.dasd.stemcell.schedule;

import com.calendarfx.model.Entry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Written By: brandon on 11/19/16
 */
@AllArgsConstructor
public class Period implements Comparable<Period> {

	@Getter
	private String name;
	@Getter
	@Setter
	private int period;
	@Getter
	@Setter
	private LocalTime startTime;
	@Getter
	@Setter
	private LocalTime endTime;

	public int compareTo(Period o) {
		return period - o.period;
	}

	public boolean isCurrentPeriod() {
		return this.isCurrentPeriod(LocalTime.now());
	}

	public String getShortenedForm() {
		List<String> filtered = Stream.of(name.split(" ")).filter(x -> x.length() > 3).collect(Collectors.toList());
		if (filtered.size() > 1) return filtered.stream().map(x -> x.toUpperCase().charAt(0) + "").collect(Collectors.joining());
		return filtered.get(0).substring(0, Math.min(filtered.get(0).length(), 3)) + ".";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Period) return name.equals(((Period)obj).name);
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public boolean isCurrentPeriod(LocalTime time) {
		return time.isAfter(startTime) && time.isBefore(endTime);
	}


	public Entry<Period> toEvent() {
		return toEvent(null);
	}

	public Entry<Period> toEvent(Day day) {
		Entry<Period> entry = new Entry<>(name);
		entry.setStartTime(startTime);
		entry.setEndTime(endTime);
		if (day != null) {
			entry.setStartDate(day.getDate());
			entry.setEndDate(day.getDate());

		}
		entry.setUserObject(this);
		return entry;
	}


	public Entry<Period> toEvent() {
		Entry<Period> entry = new Entry<>(name);
		entry.setStartTime(startTime);
		entry.setEndTime(endTime);
		entry.setUserObject(this);
		return entry;
	}
}
