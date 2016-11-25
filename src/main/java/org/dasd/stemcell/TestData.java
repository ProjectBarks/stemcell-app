package org.dasd.stemcell;

import org.dasd.stemcell.schedule.Day;
import org.dasd.stemcell.schedule.LetterDay;
import org.dasd.stemcell.schedule.Period;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
public class TestData {

	public static final List<Day> days = new ArrayList<>();

	static  {
		Period[] oneToSeven = new Period[]{
				new Period("Information System Pathway", 1, LocalTime.of(7, 30), LocalTime.of(8, 30)),
				new Period("Literature", 2, LocalTime.of(8, 35), LocalTime.of(9, 30)),
				new Period("Spanish", 3, LocalTime.of(9, 35), LocalTime.of(10, 23)),
				new Period("History", 4, LocalTime.of(10, 27), LocalTime.of(11, 17)),
				new Period("Biology", 5, LocalTime.of(11, 22), LocalTime.of(12, 11)),
				new Period("Mathematics", 6, LocalTime.of(12, 45), LocalTime.of(13, 39)),
				new Period("Business", 7, LocalTime.of(13, 39), LocalTime.of(14, 35))
		};
		//"9:15", "10:49", "12:23", "12:57!", "14:35"};
		Period[] oneToFour = new Period[]{
				new Period("Information System Pathway", 1, LocalTime.of(7, 30), LocalTime.of(9, 15)),
				new Period("Literature", 2, LocalTime.of(9, 20), LocalTime.of(10, 49)),
				new Period("Spanish", 3, LocalTime.of(10, 54), LocalTime.of(12, 23)),
				new Period("History", 4, LocalTime.of(12, 57), LocalTime.of(14, 35))
		};
		Period[] fiveToSeven = new Period[]{
				new Period("Biology", 5, LocalTime.of(7, 30), LocalTime.of(9, 15)),
				new Period("Mathematics", 6, LocalTime.of(9, 20), LocalTime.of(10, 49)),
				new Period("Business", 7, LocalTime.of(10, 54), LocalTime.of(12, 23))
		};


		LocalDate end = LocalDate.now().plusDays(30);
		LetterDay letterDay = LetterDay.values()[new Random().nextInt(LetterDay.values().length)];
		for (LocalDate x = LocalDate.now().minusDays(30); x.isBefore(end); x = x.plusDays(1)) {
			if (x.getDayOfWeek() == DayOfWeek.SATURDAY || x.getDayOfWeek() == DayOfWeek.SUNDAY) {
				continue;
			}
			switch (letterDay.getType()) {
				case ONE_THROUGH_FOUR:
					days.add(new Day(x, letterDay, oneToFour));
					break;
				case FIVE_THROUGH_SEVEN:
					days.add(new Day(x, letterDay, fiveToSeven));
					break;
				case ALL:
					days.add(new Day(x, letterDay, oneToSeven));
					break;
			}
			letterDay = letterDay.getNext();
		}
	}

}
