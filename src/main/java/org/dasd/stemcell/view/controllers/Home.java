package org.dasd.stemcell.view.controllers;

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
import javafx.application.Platform;
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
import org.dasd.stemcell.view.ControlledScreen;
import org.dasd.stemcell.view.ScreensController;
import org.dasd.stemcell.schedule.Day;
import org.dasd.stemcell.schedule.Period;
import org.dasd.stemcell.service.ServiceManager;
import org.dasd.stemcell.service.TimedService;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class Home implements Initializable, ControlledScreen, TimedService {

	private final static double CIRCLE_SIZE = 5, LARGE_CIRCLE_SIZE = 6;
	private static LocalTime START_TIME = LocalTime.of(7, 35), END_TIME = LocalTime.of(14, 35);
	private final Color MAIN_COLOR = Color.web("#1565c0");

	@FXML
	private DayView dailySchedule;

	@FXML
	private Label scheduleType;

	@FXML
	private Label letterDay;

	@FXML
	private Label weekTitle;

	@FXML
	private GridPane weeklySchedule;

	@FXML
	private JFXButton nextWeek;

	@FXML
	private JFXButton lastWeek;

	@FXML
	private FontIcon settings;

	@FXML
	private Label classPeriod;

	private Calendar calendar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		CalendarSource source = new CalendarSource();
		calendar = new Calendar();
		calendar.setReadOnly(true);
		calendar.setStyle(Calendar.Style.STYLE2);
		source.getCalendars().add(calendar);
		dailySchedule.getCalendarSources().add(source);
	}

	@Override
	public void onDayChanged(ServiceManager manager) {
		Platform.runLater(() -> {
			Optional<Day> today = manager.getToday();
			renderTopBar(today);
			renderDay(today);
			renderWeek(today, manager.getWeek());
		});
	}

	@Override
	public void onMinuteChanged(ServiceManager manager) {
		Platform.runLater(() -> {
			Optional<Day> today = manager.getToday();
			List<Day> week = manager.getWeek();
			updateDailyLine();
			renderTopBar(today);
			highlightPeriod(today, week, getAllPeriods(week));
		});
	}

	@Override
	public void setScreenParent(ScreensController screenPage) {
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

	private void renderDay(Optional<Day> today) {
		calendar.clear();
		if (!today.isPresent()) return;

		dailySchedule.setVisibleHours((int) ((END_TIME.getHour() + Math.floor(END_TIME.getMinute() / 60)) - START_TIME.getHour()));
		dailySchedule.setStartTime(START_TIME);
		dailySchedule.setEndTime(END_TIME);
		dailySchedule.setDate(LocalDate.now());
		dailySchedule.setToday(LocalDate.now());

		today.get().transfer(calendar);
		updateDailyLine();
	}

	private void renderWeek(Optional<Day> today, List<Day> week) {
		List<Period> periods = getAllPeriods(week);
		Collections.sort(week);

		weeklySchedule.getChildren().clear();
		createGrid(week.size(), periods.size());
		addLabels(week, periods);
		addDividers();
		addDots(week, periods);
		updateWeekTitle(week);
		highlightPeriod(today, week, periods);

		if (today.isPresent()) highlightDay(today.get(), week, periods);
	}

	private void updateDailyLine() {
		dailySchedule.setTime(LocalTime.now());
		dailySchedule.setRequestedTime(LocalTime.now());
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

	private void updateWeekTitle(List<Day> week) {
		String title = "";
		LocalDate first = week.get(0).getDate();
		LocalDate last = week.get(week.size() - 1).getDate();
		if (first.getMonthValue() != last.getMonthValue()) {
			title += first.getMonth().getDisplayName(TextStyle.SHORT, Locale.US) + " " + first.getDayOfMonth();
			title += " - ";
			title += last.getMonth().getDisplayName(TextStyle.SHORT, Locale.US) + " " + last.getDayOfMonth();
		} else {
			title += first.getMonth().getDisplayName(TextStyle.FULL, Locale.US) + " ";
			title += first.getDayOfMonth() + "-" + last.getDayOfMonth();
		}
		weekTitle.setText(title);

	}

	private void highlightDay(Day today, List<Day> week, List<Period> periods) {
		Pane highlight = new Pane();
		Insets insets = new Insets(-5, 0, 0, 0);
		Background fill = new Background(new BackgroundFill(Color.rgb(0, 0, 0, .1), CornerRadii.EMPTY, insets));
		highlight.setBackground(fill);
		int pos = offsetGrid(week.indexOf(today));
		weeklySchedule.add(highlight, pos, 0, 1, weeklySchedule.getRowConstraints().size());
		highlight.toBack();
	}

	private void highlightPeriod(Optional<Day> today, List<Day> week, List<Period> periods) {
		int columnPos = today.isPresent() ? offsetGrid(week.indexOf(today.get())) : -1;
		int rowPos = today.isPresent() && today.get().getCurrentPeriod().isPresent() ? offsetGrid(periods.indexOf(today.get().getCurrentPeriod().get())) : -1;

		weeklySchedule.getChildren()
				.stream()
				.filter(x -> x instanceof Circle)
				.map(x -> (Circle) x)
				.peek(x -> {
					x.setRadius(CIRCLE_SIZE);
					x.setFill(MAIN_COLOR);
					x.setStroke(Color.color(0, 0, 0, 0));
					x.setStrokeWidth(0);
				})
				.filter(x -> GridPane.getColumnIndex(x) == columnPos && GridPane.getRowIndex(x) == rowPos)
				.forEach(x -> {
					x.setRadius(CIRCLE_SIZE);
					x.setFill(Color.color(0, 0, 0, 0));
					x.setStroke(MAIN_COLOR);
					x.setStrokeWidth(1);
				});
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
				Circle circle = new Circle(CIRCLE_SIZE, MAIN_COLOR);
				center(circle);
				weeklySchedule.add(circle, offsetGrid(column), offsetGrid(row));
			}
			column++;
		}
	}

	private List<Period> getAllPeriods(List<Day> week) {
		return week
				.stream()
				.map(Day::getPeriods)
				.flatMap(Collection::stream)
				.distinct()
				.sorted()
				.collect(Collectors.toList());
	}

	private Node center(Node node) {
		GridPane.setValignment(node, VPos.CENTER);
		GridPane.setHalignment(node, HPos.CENTER);
		return node;
	}

	private int offsetGrid(int i) {
		return (i + 1) * 2;
	}
}
