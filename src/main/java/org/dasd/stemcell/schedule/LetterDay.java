package org.dasd.stemcell.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
public enum LetterDay {

	A(LetterDayType.ALL),
	B(LetterDayType.ONE_THROUGH_FOUR),
	C(LetterDayType.FIVE_THROUGH_SEVEN),
	D(LetterDayType.ALL),
	E(LetterDayType.ALL),
	F(LetterDayType.ONE_THROUGH_FOUR),
	G(LetterDayType.FIVE_THROUGH_SEVEN),
	H(LetterDayType.ALL),
	I(LetterDayType.ALL),
	J(LetterDayType.ONE_THROUGH_FOUR),
	K(LetterDayType.FIVE_THROUGH_SEVEN),
	L(LetterDayType.ALL);

	@Getter
	private LetterDayType type;

	public String getTitle() {
		return name().toUpperCase();
	}

	public static boolean isLetterDay(String letter) {
		final String filteredDay = String.valueOf(letter.toUpperCase().indexOf(0));
		return Stream.of(values()).anyMatch(x -> x.getTitle().equals(filteredDay));
	}
}
