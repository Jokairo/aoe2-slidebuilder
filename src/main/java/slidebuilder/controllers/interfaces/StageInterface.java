package slidebuilder.controllers.interfaces;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import slidebuilder.Main;

import java.io.InputStream;

public abstract class StageInterface<V> {

	protected Stage stage = new Stage();
	protected ControllerStageInterface controller;

	public StageInterface(Parent root, ControllerStageInterface controller, String title) {
		this.controller = controller;

		InputStream icon = getClass().getResourceAsStream("/icon/icon.png");
		if (icon != null)
			stage.getIcons().add(new Image(icon));

		Scene scene = new Scene(root, 520, 420);
		scene.getStylesheets().add(Main.cssFile);

		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(title);
		stage.setScene(scene);
	}

	public abstract void openWindow(V v);
	
}
