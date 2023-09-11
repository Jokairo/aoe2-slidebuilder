package slidebuilder.controllers.interfaces;

import slidebuilder.data.SceneManager;
import slidebuilder.enums.SceneEnum;

public abstract class ControllerDataInterface extends ControllerInterface {

	protected void goToScene(SceneEnum se, boolean b) {
		saveCurrentData();
		SceneManager.getInstance().switchScene(se, b);
	}
	
	public abstract void loadData();
	public abstract void saveCurrentData();
	protected abstract void setDisabledValues();
	
}
