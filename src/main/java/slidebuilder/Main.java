package slidebuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import slidebuilder.data.CustomImageComboBox;
import slidebuilder.data.DataManager;
import slidebuilder.data.SceneManager;
import slidebuilder.enums.SceneEnum;
import slidebuilder.resource.ResourceManager;
import slidebuilder.util.Popup;

public class Main extends Application {

	public static String APP_VERSION = "0.9.0";
	public static String APP_LINK = "https://github.com/Jokairo/aoe2-slidebuilder";

	private static Stage primaryStage;

	private static String title = "Slide Builder";
	
	public static Stage getStage() {
		return primaryStage;
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		primaryStage = stage;
		
		ResourceManager rm = new ResourceManager();
		
		CustomImageComboBox.initCustomImageNameLists();
		
		SceneManager ss = new SceneManager();
		ss.switchScene(SceneEnum.CAMPAIGN_MENU, true);
		
		Scene mainScene = ss.getMenuBar();
		
		stage.setTitle(title);
		String css = this.getClass().getResource("/css/menu.css").toExternalForm();
		mainScene.getStylesheets().add(css);
		stage.setScene(mainScene);

		stage.show();

		stage.setOnCloseRequest(event -> {
			if(DataManager.getDataCampaign().getUnsavedChanges()) {
				boolean pressedOk = Popup.showConfirm("Are you sure you want to close the program? Any unsaved changes will be lost.");
				if (!pressedOk) event.consume();
			}
		});

		//Stop app when main stage is closed
		stage.setOnHiding(event -> Platform.exit());
	}

	public static void showUnsavedChangesInTitle(boolean b) {
		if(b)
			primaryStage.setTitle(title + " *");
		else
			primaryStage.setTitle(title);
	}
}
