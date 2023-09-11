package slidebuilder.controllers.interfaces;

import javafx.fxml.FXML;
import slidebuilder.data.SceneManager;
import slidebuilder.enums.SceneEnum;

public abstract class ControllerInterface extends Controller {
	
	private SceneEnum scene_back;
	private SceneEnum scene_next;
	
	@FXML
	public void goBack() {
		sceneOut();
		goToScene(scene_back, false);
	}
	
	@FXML
	public void goNext() {
		goToScene(scene_next, true);
	}
	
	protected void goToScene(SceneEnum se, boolean b) {
		SceneManager.getInstance().switchScene(se, b);
	}
	
	public void setSceneBack(SceneEnum scene) {
		scene_back = scene;
	}
	
	public void setSceneNext(SceneEnum scene) {
		scene_next = scene;
	}
}
