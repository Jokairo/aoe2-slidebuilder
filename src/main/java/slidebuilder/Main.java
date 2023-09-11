package slidebuilder;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import slidebuilder.data.CustomImageComboBox;
import slidebuilder.data.SceneManager;
import slidebuilder.enums.SceneEnum;
import slidebuilder.resource.ResourceManager;

public class Main extends Application {
	
	private static Stage primaryStage;

	
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
		
		stage.setTitle("AoE2:DE Campaign Slide Builder");
		String css = this.getClass().getResource("/css/menu.css").toExternalForm();
		mainScene.getStylesheets().add(css);
		stage.setScene(mainScene);

		stage.show();
		
	}

	/*
	public static void main(String[] args) {
		launch(args);
	}
	 */
	
}
