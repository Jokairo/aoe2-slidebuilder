package slidebuilder.controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import slidebuilder.enums.CreatorEnum;

public class StageCreator {
	
	private Stage stageCreator = new Stage();
	private Scene sceneCreator;
	
	private Parent root;
	private ControllerCreateCustomImage controller;
	
	public StageCreator(Parent root, ControllerCreateCustomImage controller) {
		
		this.root = root;
		this.controller = controller;
		
		String css = this.getClass().getResource("/css/menu.css").toExternalForm();
		
		//Creator init
		sceneCreator = new Scene(root, 520, 420);
		sceneCreator.getStylesheets().add(css);
		
		stageCreator.initModality(Modality.APPLICATION_MODAL);
		stageCreator.setTitle("Add Images");
		stageCreator.setScene(sceneCreator);
	}
	
	
	public void openCreator(CreatorEnum ce) {
		controller.initData(ce);
		
		if(stageCreator.isShowing()) {
			stageCreator.requestFocus();
			return;
		}
		
		stageCreator.showAndWait();
	}
	
	
}
