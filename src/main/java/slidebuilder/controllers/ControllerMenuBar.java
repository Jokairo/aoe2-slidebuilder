package slidebuilder.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import slidebuilder.Main;
import slidebuilder.data.DataManager;
import slidebuilder.data.SceneManager;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.util.Popup;

public class ControllerMenuBar {
	
	private String name = "AoE2:DE Slide Builder";
	private String version = "Version 1.0.0";
	private String credits = "Created by Jokairo";
	
	@FXML private BorderPane pane;
	@FXML private MenuItem preview_slide;
	@FXML private MenuItem preview_campaign;
	
	//INIT
	@FXML
	public void initialize() {
	}
	
	@FXML
	private void showPreviewSlideshow() {
		DataManager.openPreviewSlideshow();
	}
	
	@FXML
	private void showPreviewCampaignMenu() {
		DataManager.openPreviewCampaignMenu();
	}
	
	@FXML
	private void openEditorSlideImage() {
		SceneManager.getInstance().getStageCreator().openCreator(CreatorEnum.SLIDE_IMAGE);
	}
	
	@FXML
	private void openEditorSlideBackground() {
		SceneManager.getInstance().getStageCreator().openCreator(CreatorEnum.SLIDE_BG);
	}
	
	@FXML
	private void openEditorCampaignBackground() {
		SceneManager.getInstance().getStageCreator().openCreator(CreatorEnum.CAMPAIGN_BG);
	}
	
	@FXML
	private void openEditorCampaignImage() {
		SceneManager.getInstance().getStageCreator().openCreator(CreatorEnum.ICON);
	}
	
	@FXML
	private void openAbout() {
		Popup.showInformation(name+"\n"+version+"\n\n"+credits, 250, 150);
	}
	
	@FXML
	private void export() {
		SceneManager.getInstance().exportProject();
	}
	
	@FXML
	private void save() {
		SceneManager.getInstance().saveExistingProject();
	}
	
	@FXML
	private void saveAs() {
		SceneManager.getInstance().saveProject();
	}
	
	@FXML
	private void openProject() {
		SceneManager.getInstance().loadProject();
	}
	
	public void switchScene(Parent root) {
		pane.setCenter(root);
	}
}
