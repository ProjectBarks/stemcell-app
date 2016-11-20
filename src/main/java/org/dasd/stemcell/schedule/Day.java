package org.dasd.stemcell.schedule;

import lombok.Getter;

import java.time.LocalDate;
import java.util.*;

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
public class Day implements Comparable<Day> {

	@Getter
	private LocalDate date;
	@Getter
	private LetterDay letterDay;
	private SortedSet<Period> periods;

	public Day(LocalDate date, LetterDay letterDay) {
		this(date, letterDay, new Period[0]);
	}

	public Day(LocalDate date, LetterDay letterDay, Period... periods) {
		this.periods = new TreeSet<>();
		this.periods.addAll(Arrays.asList(periods));
		this.date = date;
		this.letterDay = letterDay;
	}

	public List<Period> getPeriods() {
		return Arrays.asList(periods.toArray(new Period[periods.size()]));
	}

	public Optional<Period> getCurrentPeriod() {
		return periods.stream().filter(Period::isCurrentPeriod).findAny();
	}

	public int compareTo(Day o) {
		return date.compareTo(o.date);
	}
}
