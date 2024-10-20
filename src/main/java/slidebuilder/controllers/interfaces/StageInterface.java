package slidebuilder.controllers.interfaces;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import slidebuilder.Main;

public abstract class StageInterface<V> {

	protected Stage stage = new Stage();
	protected ControllerStageInterface controller;

	public StageInterface(Parent root, ControllerStageInterface controller, String title) {
		this.controller = controller;

		Scene scene = new Scene(root, 520, 420);
		scene.getStylesheets().add(Main.cssFile);

		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(title);
		stage.setScene(scene);
	}

	public abstract void openWindow(V v);
	
}
