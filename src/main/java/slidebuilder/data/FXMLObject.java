package slidebuilder.data;

import javafx.scene.Parent;
import slidebuilder.controllers.interfaces.Controller;

public class FXMLObject {
	private Parent root;
	private Controller controller;
	
	public FXMLObject(Parent root, Controller controller) {
		this.root = root;
		this.controller = controller;
	}
	
	public Parent getRoot() {
		return root;
	}
	
	public Controller getController() {
		return controller;
	}
}
