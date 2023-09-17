package slidebuilder.data;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import slidebuilder.controllers.ControllerMenuBar;
import slidebuilder.controllers.StageCreator;
import slidebuilder.controllers.StageExport;
import slidebuilder.controllers.interfaces.Controller;
import slidebuilder.controllers.interfaces.ControllerDataInterface;
import slidebuilder.controllers.interfaces.TabControllerInterface;
import slidebuilder.enums.SceneEnum;
import slidebuilder.util.FileChooserUtil;
import slidebuilder.util.FileSaverUtil;
import slidebuilder.util.FileUtil;
import slidebuilder.util.Popup;

public class SceneManager {
	
	private static SceneManager instance;
	
	private Map<SceneEnum, FXMLObject> sceneMap = new EnumMap<SceneEnum, FXMLObject>(SceneEnum.class);
	private Scene menuBar;
	private ControllerMenuBar controllerMenuBar;
	private StageCreator stageCreator;

	private StageExport projectExport;
	
	public SceneManager() {
		
		instance = this;
		
		try {
			createFXMLFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static SceneManager getInstance() {
		return instance;
	}
	
	public Scene getMenuBar() {
		return menuBar;
	}
	
	public StageCreator getStageCreator() {
		return stageCreator;
	}
	
	/*
	 * Creating FXML Files and storing them
	 */
	
	// Create all FXML files once
	private void createFXMLFiles() throws IOException {
		
		//Menu bar
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/FXMLMenuBar.fxml"));
		menuBar = new Scene(loader.load(), 960, 540);
		controllerMenuBar = loader.getController();
		
		//Creator
		loader = new FXMLLoader(getClass().getResource("/FXML/FXMLCreateCustomImage.fxml"));
		stageCreator = new StageCreator(loader.load(), loader.getController(), "Add Images");

		//Project export window
		loader = new FXMLLoader(getClass().getResource("/FXML/FXMLExportProject.fxml"));
		projectExport = new StageExport(loader.load(), loader.getController(), "Export Project");
		
		loadFXMLFile(SceneEnum.CAMPAIGN_SLIDE, "/FXML/FXMLSlideMenu.fxml");
		loadFXMLFile(SceneEnum.CAMPAIGN_SLIDE_EDIT, "/FXML/FXMLSlideEdit.fxml");
		loadFXMLFile(SceneEnum.CAMPAIGN_AUDIO_EDIT, "/FXML/FXMLAudioEdit.fxml");
		loadFXMLFile(SceneEnum.CAMPAIGN_MENU, "/FXML/FXMLCampaignMenu.fxml");
		loadFXMLFile(SceneEnum.CAMPAIGN_SCENARIOSELECT, "/FXML/FXMLScenarioSelectMenu.fxml");
		loadFXMLFile(SceneEnum.CAMPAIGN_SCENARIOSELECT_EDIT, "/FXML/FXMLScenarioSelectEdit.fxml");
		
		getSceneController(SceneEnum.CAMPAIGN_SLIDE).setSubController(getSceneController(SceneEnum.CAMPAIGN_SLIDE_EDIT));
		getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT).setSubController(getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT_EDIT));
	}
	
	// Store all FXML files controller and root so they can be later referenced
	private void loadFXMLFile(SceneEnum se, String path) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
		FXMLObject obj = new FXMLObject(loader.load(), loader.getController());
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
		DataManager.currentController = getSceneController(se);
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
		if(DataManager.currentController != null) {
			if(DataManager.currentController instanceof ControllerDataInterface) {
				ControllerDataInterface cdi = (ControllerDataInterface) DataManager.currentController;
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
	
	// Load project file from computer
	public void loadProject() {
		FileChooserUtil fc = new FileChooserUtil();
		File file = fc.loadProject();
		
		if(file != null) {
			//Go to campaign menu scene when project file is loaded
			switchScene(SceneEnum.CAMPAIGN_MENU, false);
			
			//Load the file
			DataFileManager.loadFromFile(file.getAbsolutePath());
			
			//Init all the data
			((ControllerDataInterface)getSceneController(SceneEnum.CAMPAIGN_MENU)).loadData();
			((ControllerDataInterface)getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT)).loadData();
			((ControllerDataInterface)getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT)).sceneIn();
			((TabControllerInterface)getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT_EDIT)).initData();
			((TabControllerInterface)getSceneController(SceneEnum.CAMPAIGN_SCENARIOSELECT_EDIT)).loadData(0);
			getSceneController(SceneEnum.CAMPAIGN_SLIDE).sceneIn();

			//Update previews backgrounds and images
			DataManager.getPreviewScenarios().setBackground(DataManager.getDataCampaign().getCampaignMenuBackground());
			DataManager.getPreviewSlideshow().setBackground(DataManager.getDataCampaign().getListSlideshow().get(0).getBackground());
			DataManager.getPreviewSlideshow().setImage(DataManager.getDataCampaign().getListSlideshow().get(0).getListSlides().get(0).getImagePath());
			DataManager.getPreviewScenarios().createButtons();
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
