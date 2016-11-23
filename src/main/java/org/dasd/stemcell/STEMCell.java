package org.dasd.stemcell;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public class STEMCell extends Application {

	private static Stage stage;

	public static void main(String[] args) {
		Application.launch(STEMCell.class, args);
	}

	public static void setScene(String session) {
		try {
			Pane pane = FXMLLoader.load(STEMCell.class.getResource("/fxml/" + session + ".fxml"));
			stage.setScene(new Scene(pane));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		try {
			STEMCell.stage = stage;
			setScene("login");
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setWidth(525);
			stage.setHeight(325);
			stage.setTitle("STEMCell");
			stage.show();
		} catch (Exception ex) {
			Logger.getLogger(STEMCell.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
