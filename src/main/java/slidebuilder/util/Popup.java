package slidebuilder.util;

import java.awt.Toolkit;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class Popup {
	public static void showError(String text) {
		//Error sound
		Toolkit.getDefaultToolkit().beep();
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.showAndWait();
	}
	
	public static void showSuccess(String text) {
		//Error sound
		Toolkit.getDefaultToolkit().beep();
				
		Alert alert = new Alert(AlertType.CONFIRMATION, "OK", ButtonType.OK);
		alert.setTitle("Success");
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.showAndWait();
	}
	
	public static void showInformation(String text, int width, int height) {
		Alert alert = new Alert(AlertType.NONE, "OK", ButtonType.OK);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.getDialogPane().setPrefSize(width, height);
		alert.showAndWait();
	}
}
