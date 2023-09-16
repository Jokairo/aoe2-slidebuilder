package slidebuilder.controllers;

import javafx.scene.Parent;
import slidebuilder.controllers.interfaces.StageInterface;

public class StageExport extends StageInterface<String> {

	public StageExport(Parent root, ControllerExportProject controller, String title) {
		super(root, controller, title);
	}

	@Override
	public void openWindow(String path) {
		((ControllerExportProject)controller).export(path);

		if(stage.isShowing()) {
			stage.requestFocus();
			return;
		}

		stage.showAndWait();
	}
}
