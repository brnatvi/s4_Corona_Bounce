package org.coronabounce;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.coronabounce.controllers.Controller;
import org.coronabounce.mvcconnectors.Controllable;

public class SettingsController
{

    MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML TextField individualsNumberSettings;
    @FXML Slider sliderCovidDuration;
    @FXML Slider sliderContaminationRadius;


//    public void attributeIndividualsNumber() {
//        int newIndividualsNumber = Integer.parseInt(individualsNumberSettings.getText());
//    }

    @FXML
    public void passSettingsToController(MouseEvent mouseEvent) throws IOException {
        Controllable c = new Controller();
//        int newIndividualsNumber = 20;
        int newIndividualsNumber = Integer.parseInt(individualsNumberSettings.getText());
        c.setPersonsCount(newIndividualsNumber);
        double newCovidDuration = (sliderCovidDuration.getValue())*1000;
//        c.setPersonsCount(8);
        c.setDurationCovid((long)newCovidDuration);
        double newContaminationRadius = (sliderContaminationRadius.getValue());
        c.setContaminationRadius(newContaminationRadius);

        mainController.changeController(c);

        // init graphPanel, fil mainGrid by graphPanel and draw new populations
        mainController.initNewPopulation();
        mainController.changeEnableDisable(mainController.btnStart);

        App.setRoot("corona bounce");
    }

    public void performAction(ActionEvent actionEvent) throws IOException {

        MenuItem target  = (MenuItem) actionEvent.getSource();
        System.out.println("Clicked On Item:"+target.getId());

        Controllable c = new Controller();
        c.setPersonsCount(99);

        mainController.changeController(c);
        App.setRoot("corona bounce");
    }
}