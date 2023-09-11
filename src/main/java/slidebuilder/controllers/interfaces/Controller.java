package slidebuilder.controllers.interfaces;

public abstract class Controller {
	
	private Controller subController = null;
	
	//Load necessary data when this controller scene turns active
	public abstract void sceneIn();
	
	//Save necessary data when this controller scene turns inactive
	public abstract void sceneOut();
	
	public void setSubController(Controller subController) {
		this.subController = subController;
	}
	
	public Controller getSubController() {
		return subController;
	}
	
}
