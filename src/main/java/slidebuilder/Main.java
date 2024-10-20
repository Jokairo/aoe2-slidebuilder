package slidebuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import slidebuilder.data.CustomImageComboBox;
import slidebuilder.data.DataManager;
import slidebuilder.data.SceneManager;
import slidebuilder.resource.ResourceManager;
import slidebuilder.util.Popup;

public class Main extends Application {

	public static String APP_VERSION = "0.9.0";
	public static String APP_LINK = "https://github.com/Jokairo/aoe2-slidebuilder";

	private static final String title = "Slide Builder";
	public static Label loadingLabel = new Label("Loading...");
	public static Stage primaryStage;

	public static String cssFile;

	@Override
	public void start(Stage stage) throws Exception {
		
		primaryStage = stage;
		cssFile = getClass().getResource("/css/menu.css").toExternalForm();
		
		ResourceManager rm = new ResourceManager();
		CustomImageComboBox.initCustomImageNameLists();

		stage.setOnCloseRequest(event -> {
			if(DataManager.getDataCampaign().getUnsavedChanges()) {
				boolean pressedOk = Popup.showConfirm("Are you sure you want to close the program? Any unsaved changes will be lost.");
				if (!pressedOk) event.consume();
			}
		});

		//Stop app when main stage is closed
		stage.setOnHiding(event -> Platform.exit());

		// Loading screen until FXML files loaded
		BorderPane pane = new BorderPane();
		pane.setCenter(loadingLabel);
		Scene s = new Scene(pane, 960, 540);
		s.getStylesheets().add(cssFile);
		stage.setTitle(title);
		stage.setScene(s);
		stage.show();

		DataManager.createPreviews();
		SceneManager sm = new SceneManager(); // Load FXML files
	}

	public static void showUnsavedChangesInTitle(boolean b) {
		if(b)
			primaryStage.setTitle(title + " *");
		else
			primaryStage.setTitle(title);
	}
}
