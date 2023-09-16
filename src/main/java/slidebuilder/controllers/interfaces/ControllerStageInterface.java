package slidebuilder.controllers.interfaces;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public abstract class ControllerStageInterface {

    @FXML protected Label slide_title_s;
    @FXML
    protected void closeWindow(ActionEvent event) {
        Stage stage = (Stage) slide_title_s.getScene().getWindow();
        stage.close();
    }

    protected void closeWindow() {
        Stage stage = (Stage) slide_title_s.getScene().getWindow();
        stage.close();
    }
}
