package slidebuilder.controllers.interfaces;

import slidebuilder.data.SceneManager;
import slidebuilder.enums.SceneEnum;

public abstract class ControllerDataInterface extends ControllerInterface {

	protected void goToScene(SceneEnum se, boolean b) {
		//Save data automatically when switching scenes
		//When going back from SlideEdit to SlideMenu view, don't save and instead save in its sceneOut method
		//This is so that we can switch to slideshow's first slide when going back to slideshow menu, which fixes some bugs related to the preview
		if(!getHasParentController())
			saveCurrentData();
		SceneManager.getInstance().switchScene(se, b);
	}
	
	public abstract void loadData();
	public abstract void saveCurrentData();
	protected abstract void setDisabledValues();
	
}
