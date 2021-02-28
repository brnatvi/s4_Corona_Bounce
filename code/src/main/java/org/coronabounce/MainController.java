package org.coronabounce;

import java.io.IOException;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.coronabounce.controllers.Controller;
import org.coronabounce.models.CoquilleBille;
import org.coronabounce.models.Zone;
import org.coronabounce.mvcconnectors.Controllable;
import org.coronabounce.mvcconnectors.Displayable;

import static javafx.scene.paint.Paint.valueOf;


public class MainController
{
    private Controllable controller;
    @FXML
    Pane panel;

    public MainController()
    {
        this.controller = new Controller();
    }

    @FXML
    private void initialize()
    {
        Zone z = new Zone(controller);
        Displayable model = z.getPopulation();                     //TODO check it
        List<CoquilleBille> allPoints = model.getAllPoints();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(33), ev -> {
            panel.getChildren().retainAll();
            for (CoquilleBille cb : allPoints)
            {
                String state = cb.getIndividual().healthState();
                double coordX = cb.getPosition().getX();
                double coordY = cb.getPosition().getY();
                Circle point = new Circle(coordX, coordY, controller.getRadiusDot());
                if (state.equals("Healthy")) { point.setFill(valueOf("#A9E0F4")); }    //light blue
                if (state.equals("Recovered")) { point.setFill(valueOf("#CF7EEE")); }  //lilas
                if (state.equals("Sick")) { point.setFill(valueOf("#830B0B")); }      // red-brown
                panel.getChildren().add(point);
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        z.moving();
    }

                         
    @FXML
    private void switchToSettings() throws IOException {
        App.setRoot("settings");
    }

}
