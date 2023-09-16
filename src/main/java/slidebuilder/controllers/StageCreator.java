package slidebuilder.controllers;

import javafx.scene.Parent;
import slidebuilder.controllers.interfaces.StageInterface;
import slidebuilder.enums.CreatorEnum;

public class StageCreator extends StageInterface<CreatorEnum> {
	
	public StageCreator(Parent root, ControllerCreateCustomImage controller, String title) {
		super(root, controller, title);
	}

	@Override
	public void openWindow(CreatorEnum ce) {
		((ControllerCreateCustomImage)controller).initData(ce);
		
		if(stage.isShowing()) {
			stage.requestFocus();
			return;
		}
		
		stage.showAndWait();
	}
}
