package slidebuilder.controllers.interfaces;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class StageInterface<V> {

	protected Stage stage = new Stage();
	protected ControllerStageInterface controller;

	public StageInterface(Parent root, ControllerStageInterface controller, String title) {
		this.controller = controller;

		String css = this.getClass().getResource("/css/menu.css").toExternalForm();

		Scene scene = new Scene(root, 520, 420);
		scene.getStylesheets().add(css);

		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(title);
		stage.setScene(scene);
	}

	public abstract void openWindow(V v);
	
}
