package slidebuilder.util;

import java.awt.Toolkit;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import slidebuilder.Main;

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

	public static void showAbout() {
		Alert alert = new Alert(AlertType.NONE, "OK", ButtonType.OK);
		alert.setTitle("About");
		alert.setHeaderText(null);

		VBox pane = new VBox();
		Label name = new Label("AoE2:DE Campaign Slide Builder");
		Label version = new Label("Version "+ Main.APP_VERSION);
		Label emptySpace = new Label("");
		Label author = new Label("Made by Jokairo");

		Hyperlink link = new Hyperlink();
		String projectUrl = Main.APP_LINK;
		link.setText(projectUrl);
		link.setOnAction(e -> {
			try {
				Desktop.getDesktop().browse(new URI(projectUrl));
			} catch (IOException | URISyntaxException ex) {
				throw new RuntimeException(ex);
			}
		});

		pane.getChildren().addAll(name, version, emptySpace, author, link);

		alert.getDialogPane().setContent(pane);
		alert.getDialogPane().setPrefSize(400, 250);
		alert.showAndWait();
	}

	public static boolean showConfirm(String text) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm");
		alert.setHeaderText(null);
		alert.setContentText(text);
		
		//Returns whether user pressed OK or not
		return !alert.showAndWait().filter(r -> r != ButtonType.OK).isPresent();
	}
}
