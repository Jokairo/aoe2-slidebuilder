package slidebuilder.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import slidebuilder.Main;
import slidebuilder.controllers.*;
import slidebuilder.controllers.interfaces.Controller;
import slidebuilder.controllers.interfaces.ControllerDataInterface;
import slidebuilder.controllers.interfaces.TabControllerInterface;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.enums.SceneEnum;
import slidebuilder.resource.ResourceManager;
import slidebuilder.util.FileChooserUtil;
import slidebuilder.util.FileSaverUtil;
import slidebuilder.util.FileUtil;
import slidebuilder.util.Popup;

public class SceneManager {
	
	private static SceneManager instance;
	
	private final Map<SceneEnum, FXMLObject> sceneMap = new EnumMap<SceneEnum, FXMLObject>(SceneEnum.class);
	private final ArrayList<String> fxmlFilePaths = new ArrayList<>();
	private final ArrayList<SceneEnum> fxmlFileEnums = new ArrayList<>();

	private Scene menuBar;
	private ControllerMenuBar controllerMenuBar;
	private StageCreator stageCreator;
	private StageExport projectExport;

	
	public SceneManager() {
		instance = this;
		addToLoadingList("/FXML/FXMLMenuBar.fxml", null);
		addToLoadingList("/FXML/FXMLCreateCustomImage.fxml", null);
		addToLoadingList("/FXML/FXMLExportProject.fxml", null);
		addToLoadingList("/FXML/FXMLSlideMenu.fxml", SceneEnum.CAMPAIGN_SLIDE);
		addToLoadingList("/FXML/FXMLSlideEdit.fxml", SceneEnum.CAMPAIGN_SLIDE_EDIT);
		addToLoadingList("/FXML/FXMLAudioEdit.fxml", SceneEnum.CAMPAIGN_AUDIO_EDIT);
		addToLoadingList("/FXML/FXMLCampaignMenu.fxml", SceneEnum.CAMPAIGN_MENU);
		addToLoadingList("/FXML/FXMLScenarioSelectMenu.fxml", SceneEnum.CAMPAIGN_SCENARIOSELECT);
		addToLoadingList("/FXML/FXMLScenarioSelectEdit.fxml", SceneEnum.CAMPAIGN_SCENARIOSELECT_EDIT);

		try {
			createFXMLFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addToLoadingList(String path, SceneEnum se) {
		fxmlFilePaths.add(path);
		fxmlFileEnums.add(se);
	}
	
	public static SceneManager getInstance() {
		return instance;
	}
	
	public StageCreator getStageCreator() {
		return stageCreator;
	}
	
	/*
	 * Creating FXML Files and storing them
	 */
	
	// Create all FXML files once
	private void createFXMLFiles() throws IOException {
		Task<Void> loadTask = new Task<Void>() {
			@Override
			protected Void call() throws IOException {
				ArrayList<Parent> parents = new ArrayList<>();
				ArrayList<Object> controllers = new ArrayList<>();

				FXMLLoader loader;
				int size = fxmlFilePaths.size();
				// Load FXML files
				for(int i = 0; i<size; i++) {
					try {
						loader = new FXMLLoader(getClass().getResource(fxmlFilePaths.get(i)));
						parents.add(loader.load());
						controllers.add(loader.getController());
						updateMessage("Loading FXML files " + i + "/" + size);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}

				// Load/init FXML controllers
				for (int i = 0; i < size; i++) {
					String fileName = fxmlFilePaths.get(i);
					SceneEnum sceneEnum = fxmlFileEnums.get(i);
					Parent root = parents.get(i);
					Object controller = controllers.get(i);

                    switch (fileName) {
                        case "/FXML/FXMLMenuBar.fxml":
                            Platform.runLater(() -> {
                                menuBar = new Scene(root, 960, 540);
                                controllerMenuBar = (ControllerMenuBar) controller;
                            });
                            break;
                        case "/FXML/FXMLCreateCustomImage.fxml":
                            Platform.runLater(() -> stageCreator = new StageCreator(root, (ControllerCreateCustomImage) controller, "Add Images"));
                            break;
                        case "/FXML/FXMLExportProject.fxml":
                            Platform.runLater(() -> projectExport = new StageExport(root, (ControllerExportProject) controller, "Export Project"));
                            break;
                        default:
                            Platform.runLater(() -> loadFXMLFile(sceneEnum, root, (Controller) controller));
                            break;
                    }
					updateMessage("Loading FXML controllers "+i+"/"+size);				}
				return null;
			}

			@Override
			protected void succeeded() {
				getSceneController(SceneEnum.CAMPAIGN_SLIDE).setSubController(getSceneController(SceneEnum.CAMPAIGN_SLIDE_EDIT));
				getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT).setSubController(getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT_EDIT));

				menuBar.getStylesheets().add(Main.cssFile);
				Main.primaryStage.setScene(menuBar);
				switchScene(SceneEnum.CAMPAIGN_MENU, true);
				fxmlFilePaths.clear();
				fxmlFileEnums.clear();
			}

			@Override
			protected void failed() {
				updateMessage("Failed loading FXML files.");
			}
		};

		Main.loadingLabel.textProperty().bind(loadTask.messageProperty());
		Thread loadThread = new Thread(loadTask);
		loadThread.setDaemon(true);
		loadThread.start();
	}
	
	// Store all FXML files controller and root so they can be later referenced
	private void loadFXMLFile(SceneEnum se, Parent root, Controller controller)  {
		FXMLObject obj = new FXMLObject(root, controller);
		sceneMap.put(se, obj);
	}
	
	private Parent getSceneRoot(SceneEnum se) {
		return sceneMap.get(se).getRoot();
	}
	
	private Controller getSceneController(SceneEnum se) {
		return sceneMap.get(se).getController();
	}
	
	/*
	 * Switching scenes (or FXMLs)
	 */
	
	// Determine what happens when scene is loaded
	public void switchScene(SceneEnum se, boolean forward) {
		
		//Init the scene if moving to it (not going back to it)
		if (forward) {
			
			//Update slide editor if user is going to use audio editor
			if (se == SceneEnum.CAMPAIGN_AUDIO_EDIT)
				getSceneController(SceneEnum.CAMPAIGN_SLIDE_EDIT).sceneIn();
			
			//Init scene
			getSceneController(se).sceneIn();
		}
		
		//Switch to the scene
		switchMainScene(se);
	}

	// Switch the active scene
	private void switchMainScene(SceneEnum se) {
		controllerMenuBar.switchScene(getSceneRoot(se));
		
		//Keep track which scene the user is currently using
		DataManager.currentScene = se;
	}

	/*
	 * Save, Load, Export
	 */
	
	// Saving Campaign Menu scene (the first scene)
	private void saveCampaignMenu() {
		((ControllerDataInterface)getSceneController(SceneEnum.CAMPAIGN_MENU)).saveCurrentData();;
	}
	
	// Saving every controller data to DataManager
	private void saveAll() {
		//saveCampaignMenu();
		
		//Save the data for the controller where user is currently
		if(DataManager.currentScene != null) {
			Controller currentController = getSceneController(DataManager.currentScene);
			if(currentController instanceof ControllerDataInterface) {
				ControllerDataInterface cdi = (ControllerDataInterface) currentController;
				cdi.saveCurrentData();
			}
		}
		
		//Bring user to main screen
		//If user is in tab scene, initialising data may cause bugs, so bringing user here is safer as it's non-tab scene
		switchScene(SceneEnum.CAMPAIGN_MENU, false);

		// Init and save data for all controllers
		getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT).sceneIn();
		((ControllerDataInterface)getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT)).saveCurrentData();
		
		getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT_EDIT).sceneIn();

		getSceneController(SceneEnum.CAMPAIGN_SLIDE).sceneIn();
		
		for(int i=0; i < DataManager.getDataCampaign().getListSlideshow().size(); i++) {
			DataManager.globalTabIndex = i;
			getSceneController(SceneEnum.CAMPAIGN_SLIDE_EDIT).sceneIn();
		}

		DataManager.getDataCampaign().setUnsavedChanges(false);
	}
	
	// Save project file to computer
	public void saveExistingProject() {
		saveCampaignMenu();
		
		File projectPath = DataManager.getDataFolderLocation().getProjectPath();
		
		//Project path doesn't exists, let user define it
		if(projectPath == null || !FileUtil.fileExists(projectPath.getAbsolutePath())) {
			saveProject();
			return;
		}
		
		//Project path exists, save and replace the existing project file
		saveAll();
		DataFileManager.saveToFile(projectPath.getAbsolutePath());
	}
	
	// Save project file to computer
	public void saveProject() {
		saveCampaignMenu();
		
		FileSaverUtil fs = new FileSaverUtil();
		File file = fs.saveProjectFile();
		
		if (file != null) {
			saveAll();
			DataFileManager.saveToFile(file.getAbsolutePath());
		}
	}

	// Create new empty project
	public void newProject() {

		//Go to campaign menu scene
		switchScene(SceneEnum.CAMPAIGN_MENU, false);

		//Create new project
		DataCampaign dc = new DataCampaign();
		DataManager.setDataCampaign(dc);

		//Set campaign map default bg, otherwise it will be null
		String defaultBg = ResourceManager.instance.getDefaultResource(CreatorEnum.CAMPAIGN_BG);
		DataManager.getDataCampaign().setDefaultCampaignMenuBackground(defaultBg);

		//Init all the data
		((ControllerDataInterface)getSceneController(SceneEnum.CAMPAIGN_MENU)).loadData();
		((ControllerDataInterface)getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT)).loadData();
		((ControllerDataInterface)getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT)).sceneIn();
		((TabControllerInterface<?>)getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT_EDIT)).initData();
		((TabControllerInterface<?>)getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT_EDIT)).loadData(0);
		getSceneController(SceneEnum.CAMPAIGN_SLIDE).sceneIn();

		//Delete all preview buttons
		DataManager.getPreviewScenarios().deleteButtons();
	}
	
	// Load project file from computer
	public void loadProject() {
		FileChooserUtil fc = new FileChooserUtil();
		File file = fc.loadProject();
		
		if(file != null) {
			//Go to campaign menu scene when project file is loaded
			switchScene(SceneEnum.CAMPAIGN_MENU, false);
			
			//Load the file
			DataFileManager.loadFromFile(file.getAbsolutePath());

			//Delete previous buttons so new ones can be created
			DataManager.getPreviewScenarios().deleteButtons();
			
			//Init all the data (and create new buttons)
			((ControllerDataInterface)getSceneController(SceneEnum.CAMPAIGN_MENU)).loadData();
			((ControllerDataInterface)getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT)).loadData();
			((ControllerDataInterface)getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT)).sceneIn();
			((TabControllerInterface<?>)getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT_EDIT)).initData();
			((TabControllerInterface<?>)getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT_EDIT)).loadData(0);
			getSceneController(SceneEnum.CAMPAIGN_SLIDE).sceneIn();
		}
	}
	
	// Export ready project
	public void exportProject() {
		saveCampaignMenu();
		
		//Project must have a name
		if(DataManager.getDataCampaign().getCampaignName() == null || DataManager.getDataCampaign().getCampaignName().isEmpty()) {
			Popup.showError("You must add a campaign file.");
			return;
		}
		
		FileSaverUtil fs = new FileSaverUtil();
		File file = fs.exportProject();
		
		if(file != null) {
			try {
				saveAll();
				projectExport.openWindow(file.getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
