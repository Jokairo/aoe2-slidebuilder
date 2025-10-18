package slidebuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import slidebuilder.data.CustomImageComboBox;
import slidebuilder.data.DataManager;
import slidebuilder.data.SceneManager;
import slidebuilder.resource.ResourceManager;
import slidebuilder.util.Popup;
import slidebuilder.util.VersionUtil;

import java.io.InputStream;

public class Main extends Application {
	public static String APP_VERSION = VersionUtil.getShortVersion();
	public static String APP_LINK = VersionUtil.getUrl();
	public static String APP_AUTHOR = VersionUtil.getAuthor();
	public static String APP_NAME = VersionUtil.getAppName();
	public static String APP_YEAR = VersionUtil.getYear();

	public static Label loadingLabel = new Label("Loading...");
	public static Stage primaryStage;

	public static String cssFile;

	@Override
	public void start(Stage stage) {
		
		primaryStage = stage;
		cssFile = getClass().getResource("/css/menu.css").toExternalForm();
		InputStream icon = getClass().getResourceAsStream("/icon/icon.png");
		if (icon != null)
			stage.getIcons().add(new Image(icon));
		
		ResourceManager rm = new ResourceManager();
		CustomImageComboBox.initCustomImageNameLists();

		stage.setOnCloseRequest(event -> {
			if(DataManager.getDataCampaign().getUnsavedChanges()) {
				boolean pressedOk = Popup.showConfirm("Are you sure you want to close the program? Any unsaved changes will be lost.");
				if (!pressedOk) {
					event.consume();
					return;
				}
			}

			Platform.exit();
			System.exit(0);
		});

		// Loading screen until FXML files loaded
		BorderPane pane = new BorderPane();
		pane.setCenter(loadingLabel);
		Scene s = new Scene(pane, 960, 540);
		s.getStylesheets().add(cssFile);
		stage.setTitle(APP_NAME);
		stage.setScene(s);
		stage.show();

		DataManager.createPreviews();
		SceneManager sm = new SceneManager(); // Load FXML files
	}

	public static void showUnsavedChangesInTitle(boolean b) {
		if(b)
			primaryStage.setTitle(APP_NAME + " *");
		else
			primaryStage.setTitle(APP_NAME);
	}
}
