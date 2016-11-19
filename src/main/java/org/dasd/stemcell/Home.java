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

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.DayView;
import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.dasd.stemcell.schedule.Day;
import org.dasd.stemcell.schedule.Period;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Home implements Initializable {

	private static LocalTime START_TIME = LocalTime.of(7, 35), END_TIME = LocalTime.of(14, 35);

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
	private GridPane weeklySchedule;

	@FXML
	private JFXButton lastWeek;

	@FXML
	private Label classPeriod;

	private Day[] week;
	private LocalDate lastDate;
	private LocalTime lastTime;
	private Calendar calendar;


	public void initialize(URL location, ResourceBundle resources) {
		week = TestData.week;

		CalendarSource source = new CalendarSource();
		calendar = new Calendar();
		calendar.setReadOnly(true);
		calendar.setStyle(Calendar.Style.STYLE2);
		source.getCalendars().add(calendar);
		dailySchedule.getCalendarSources().add(source);


		render();

		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
			LocalDateTime now = LocalDateTime.now();
			if (now.getDayOfYear() != lastDate.getDayOfYear()) {
				render();
			} else if (now.getHour() != lastTime.getHour() || now.getMinute() != lastTime.getMinute()) {
				Optional<Day> today = getToday();
				updateDailyLine();
				renderTopBar(today);
				lastTime = LocalTime.now();
			}
		}));

		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	public void render() {
		Optional<Day> today = getToday();
		renderTopBar(today);
		renderDay(today);
		renderWeek(today, week);
		lastDate = LocalDate.now();
		lastTime = LocalTime.now();
	}

	private void renderTopBar(Optional<Day> today) {
		if (!today.isPresent()) {
			letterDay.setText("-");
			scheduleType.setText("-");
			classPeriod.setText("-");
		} else {
			Day day = today.get();
			letterDay.setText(day.getLetterDay().getTitle());
			scheduleType.setText(day.getLetterDayType().getTitle());
			Optional<Period> currentPeriod = day.getCurrentPeriod();
			classPeriod.setText(currentPeriod.isPresent() ? String.valueOf(currentPeriod.get().getPeriod()) : "-");
		}
	}

	private void renderDay(Optional<Day> day) {
		calendar.clear();
		if (!day.isPresent()) {
			return;
		}
		System.out.println("Update");

		dailySchedule.setVisibleHours((int) ((END_TIME.getHour() + Math.floor(END_TIME.getMinute() / 60)) - START_TIME.getHour()));
		dailySchedule.setStartTime(START_TIME);
		dailySchedule.setEndTime(END_TIME);
		dailySchedule.setDate(LocalDate.now());
		dailySchedule.setToday(LocalDate.now());
		dailySchedule.dateProperty().setValue(LocalDate.now());

		day.get().transfer(calendar);
		updateDailyLine();
	}

	private void updateDailyLine() {
		dailySchedule.setTime(LocalTime.now());
		dailySchedule.setRequestedTime(LocalTime.now());
	}

	private void renderWeek(Optional<Day> today, Day... days) {
		List<Day> week = Arrays.asList(days);
		Collections.sort(week);
		List<Period> periods = week.stream()
				.map(Day::getPeriods)
				.flatMap(Collection::stream)
				.distinct()
				.sorted()
				.collect(Collectors.toList());

		weeklySchedule.getChildren().clear();
		createGrid(week.size(), periods.size());
		addLabels(week, periods);
		addDividers();
		addDots(week, periods);

		if (today.isPresent()) {
			Pane highlight = new Pane();
			Insets insets = new Insets(-5, 0, 0, 0);
			Background fill = new Background(new BackgroundFill(Color.rgb(0, 0, 0, .1), CornerRadii.EMPTY, insets));
			highlight.setBackground(fill);
			int pos = week.indexOf(today.get());
			weeklySchedule.add(highlight, offsetGrid(pos), 0, 1, weeklySchedule.getRowConstraints().size());
			highlight.toBack();
		}
	}

	private void createGrid(int totalDays, int totalClasses) {
		weeklySchedule.getRowConstraints().clear();
		ObservableList<RowConstraints> row = weeklySchedule.getRowConstraints();
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

		weeklySchedule.getColumnConstraints().clear();
		ObservableList<ColumnConstraints> column = weeklySchedule.getColumnConstraints();
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

	private void addLabels(List<Day> days, List<Period> periods) {
		int i = 0;
		for (Day day : days) {
			Label label = new Label(day.getLetterDay().getTitle());
			weeklySchedule.add(label, offsetGrid(i), 0);
			center(label);
			i++;
		}

		i = 0;
		for (Period period : periods) {
			Label label = new Label(period.getShortenedForm());
			label.setTooltip(new Tooltip(period.getName()));
			weeklySchedule.add(label, 0, offsetGrid(i));
			center(label);
			i++;
		}
	}

	private Node center(Node node) {
		GridPane.setValignment(node, VPos.CENTER);
		GridPane.setHalignment(node, HPos.CENTER);
		return node;
	}

	private void addDividers() {
		int columnSpan = weeklySchedule.getColumnConstraints().size();
		int rowSpan = weeklySchedule.getRowConstraints().size();

		for (int i = 1; i < columnSpan; i += 2)
			weeklySchedule.add(new Separator(Orientation.VERTICAL), i, 1, 1, rowSpan);
		for (int i = 1; i < rowSpan; i += 2) weeklySchedule.add(new Separator(), 1, i, columnSpan, 1);
	}

	private void addDots(List<Day> days, List<Period> allPeriods) {
		int column = 0;
		for (Day day : days) {
			for (Period period : day.getPeriods()) {
				int row = allPeriods.indexOf(period);
				Circle circle = new Circle(5, Color.valueOf("#1565c0"));
				center(circle);
				weeklySchedule.add(circle, offsetGrid(column), offsetGrid(row));
			}
			column++;
		}
	}

	private int offsetGrid(int i) {
		return (i + 1) * 2;
	}

	private Optional<Day> getToday() {
		return Stream.of(week).filter(x -> x.getDate().equals(LocalDate.now())).findAny();
	}
}
