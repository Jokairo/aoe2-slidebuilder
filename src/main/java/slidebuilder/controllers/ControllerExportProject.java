package slidebuilder.controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import slidebuilder.controllers.interfaces.ControllerStageInterface;
import slidebuilder.generator.Generator;
import slidebuilder.util.Popup;
import slidebuilder.util.UpdateUIFromOtherThread;

public class ControllerExportProject extends ControllerStageInterface {

	private Task<Void> task;

	@FXML private ProgressBar progressBar;
	@FXML private Label progressBarText;
	@FXML private Button image_button_cancel;

	//INIT
	@FXML
	public void initialize() {
		//Progressbar text
		progressBarText.setId("progressbar-text");
		progressBarText.setWrapText(true);
		progressBarText.setAlignment(Pos.CENTER);
	}

	public void export(String path) {
		image_button_cancel.setDisable(false);

		task = Generator.getTask(path);

		task.setOnSucceeded(e -> {
			Popup.showSuccess("Project export succeed!");
			closeWindow();
		});
		task.setOnFailed(e -> {
			Popup.showError("Project export failed.");
			closeWindow();
		});
		task.setOnCancelled(e -> {
			Task<Void> cancelTask = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Generator.cancelExport(path);

					UpdateUIFromOtherThread.call(() -> {
						Popup.showError("Project export cancelled. No folders or files were generated.");
						closeWindow();
					});

					return null;
				}
			};
			new Thread(cancelTask).start();
		});

		progressBar.progressProperty().bind(task.progressProperty());
		progressBarText.textProperty().bind(task.messageProperty());

		new Thread(task).start();
	}

	@FXML
	private void cancel(ActionEvent event) {
		if (task == null) return;

		image_button_cancel.setDisable(true);
		task.cancel();
	}
}
