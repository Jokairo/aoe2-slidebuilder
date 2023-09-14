package slidebuilder.controllers.interfaces;

public abstract class Controller {
	
	private Controller subController = null;
	private boolean hasParentController = false;
	
	//Load necessary data when this controller scene turns active
	public abstract void sceneIn();
	
	//Save necessary data when this controller scene turns inactive
	public abstract void sceneOut();
	
	public void setSubController(Controller subController) {
		this.subController = subController;
		subController.hasParentController = true;
	}
	
	public Controller getSubController() {
		return subController;
	}

	protected boolean getHasParentController() {
		return hasParentController;
	}
	
}
