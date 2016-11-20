package org.dasd.stemcell;

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
import com.calendarfx.view.DayView;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import org.dasd.stemcell.schedule.Day;
import org.dasd.stemcell.schedule.Period;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Home implements Initializable {

	@FXML
	private DayView dailySchedule;

	@FXML
	private JFXButton nextWeek;

	@FXML
	private FontIcon settings;

	@FXML
	private Label scheduleType;

	@FXML
	private Label letterDay;

	@FXML
	private GridPane weekView;

	@FXML
	private JFXButton lastWeek;

	@FXML
	private Label classPeriod;


	public void initialize(URL location, ResourceBundle resources) {
		Day[] week = TestData.week;
		renderWeek(week);
	}

	public void renderWeek(Day... days) {
		List<Day> week = Arrays.asList(days);
		List<Period> periods = week.stream()
				.map(Day::getPeriods)
				.flatMap(Collection::stream)
				.distinct()
				.sorted()
				.collect(Collectors.toList());

		createGrid(week.size(), periods.size());
		addLabels(week, periods);
		addDividers();
	}

	private void addLabels(Collection<Day> days, Collection<Period> periods) {
		int i = 0;
		for (Day day : days) {
			Label label = new Label(day.getLetterDay().getTitle());
			weekView.add(label, (i + 1) * 2, 0);
			center(label);
			i++;
		}

		i = 0;
		for (Period period : periods) {
			Label label = new Label(period.getShortenedForm());
			weekView.add(label, 0, (i + 1) * 2);
			center(label);
			i++;
		}
	}


	private Node center(Node node) {
		GridPane.setValignment(node, VPos.CENTER);
		GridPane.setHalignment(node, HPos.CENTER);
		return node;
	}


	private void createGrid(int totalDays, int totalClasses) {
		ObservableList<RowConstraints> row = weekView.getRowConstraints();
		row.add(new RowConstraints(15, 15, 15));
		for (int i = 0; i < totalClasses * 2; i++) {
			RowConstraints constraint;
			if (i % 2 == 0) {
				constraint = new RowConstraints(1, 1, 1);
				constraint.setVgrow(Priority.NEVER);
			} else {
				constraint = new RowConstraints();
				constraint.setVgrow(Priority.ALWAYS);
			}
			row.add(constraint);
		}

		ObservableList<ColumnConstraints> column =  weekView.getColumnConstraints();
		column.add(new ColumnConstraints(35, 35, 35));
		for (int i = 0; i < totalDays * 2; i++) {
			ColumnConstraints constraint;
			if (i % 2 == 0) {
				constraint = new ColumnConstraints(1, 1, 1);
				constraint.setHgrow(Priority.NEVER);
			} else {
				constraint = new ColumnConstraints();
				constraint.setHgrow(Priority.ALWAYS);
			}
			column.add(constraint);
		}
	}

	private void addDividers() {
		int columnSpan = weekView.getColumnConstraints().size();
		int rowSpan = weekView.getRowConstraints().size();

		for (int i = 1; i < columnSpan; i += 2) weekView.add(new Separator(Orientation.VERTICAL), i, 1, 1, rowSpan);
		for (int i = 1; i < rowSpan; i += 2) weekView.add(new Separator(), 1, i, columnSpan, 1);
	}
}
