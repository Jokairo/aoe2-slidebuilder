package slidebuilder.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import slidebuilder.Main;
import slidebuilder.data.DataManager;
import slidebuilder.data.SceneManager;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.util.Popup;

public class ControllerMenuBar {
	
	@FXML private BorderPane pane;
	@FXML private MenuItem preview_slide;
	@FXML private MenuItem preview_campaign;

	@FXML private MenuItem menu_button_open;
	@FXML private MenuItem menu_button_save;
	@FXML private MenuItem menu_button_save_as;
	@FXML private MenuItem menu_button_export;
	@FXML private MenuItem menu_button_about;

	@FXML
	public void initialize() {

		MenuItem[] menuItems = {
			menu_button_open,
			menu_button_save,
			menu_button_save_as,
			menu_button_export,
			menu_button_about
		};

		String[] imagePaths = {
			"/images/icon_open.png",
			"/images/icon_save.png",
			"/images/icon_save_as.png",
			"/images/icon_export.png",
			"/images/icon_about.png"
		};

		//Create menu item icons
		try {
			for(int i = 0; i < menuItems.length; i++) {
				Image icon = new Image(getClass().getResourceAsStream(imagePaths[i]));
				ImageView iconView = new ImageView(icon);
				iconView.setFitWidth(16);
				iconView.setFitHeight(16);

				menuItems[i].setGraphic(iconView);
			}
		}
		catch(NullPointerException e) {
			e.printStackTrace();
		}
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
		SceneManager.getInstance().getStageCreator().openWindow(CreatorEnum.SLIDE_IMAGE);
	}
	
	@FXML
	private void openEditorSlideBackground() {
		SceneManager.getInstance().getStageCreator().openWindow(CreatorEnum.SLIDE_BG);
	}
	
	@FXML
	private void openEditorCampaignBackground() {
		SceneManager.getInstance().getStageCreator().openWindow(CreatorEnum.CAMPAIGN_BG);
	}
	
	@FXML
	private void openEditorCampaignImage() {
		SceneManager.getInstance().getStageCreator().openWindow(CreatorEnum.ICON);
	}
	
	@FXML
	private void openAbout() {
		Popup.showAbout();
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
	private void newProject() {
		boolean pressedOk = true;

		//Show confirmation popup if there's unsaved changes
		if(DataManager.getDataCampaign().getUnsavedChanges())
			pressedOk = Popup.showConfirm("Are you sure you want to create a new project? Any unsaved changes to the current project will be lost.");

		if(pressedOk)
			SceneManager.getInstance().newProject();
	}

	@FXML
	private void openProject() {
		boolean pressedOk = true;

		//Show confirmation popup if there's unsaved changes
		if(DataManager.getDataCampaign().getUnsavedChanges())
			pressedOk = Popup.showConfirm("Are you sure you want to load another project? Any unsaved changes to the current project will be lost.");

		if(pressedOk)
			SceneManager.getInstance().loadProject();
	}

	@FXML
	private void exit() {
		Window window = Main.primaryStage.getScene().getWindow();
		window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
	}
	
	public void switchScene(Parent root) {
		pane.setCenter(root);
	}
}
