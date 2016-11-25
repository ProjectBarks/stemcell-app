package org.dasd.stemcell.view.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.dasd.stemcell.STEMCell;
import org.dasd.stemcell.view.ControlledScreen;
import org.dasd.stemcell.view.ScreensController;

import java.net.URL;
import java.util.ResourceBundle;

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
 * Written By: brandon on 11/21/16
 */
public class Login implements Initializable, ControlledScreen {

	@FXML
	private JFXPasswordField password;

	@FXML
	private JFXTextField username;

	private ScreensController controller;

	@FXML
	void onLogin(ActionEvent event) {
		if (!username.validate() && !password.validate()) {
			return;
		}
		controller.setScreen(STEMCell.HOME_SCREEN);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		RequiredFieldValidator validator = new RequiredFieldValidator();
		validator.setMessage("Input Required");
		//validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.WARNING));
		username.getValidators().add(validator);
		password.getValidators().add(validator);
	}

	@Override
	public void setScreenParent(ScreensController controller) {
		this.controller = controller;
	}
}
