package slidebuilder.controllers;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import slidebuilder.components.TextFieldFile;
import slidebuilder.controllers.interfaces.ControllerDataInterface;
import slidebuilder.data.DataManager;
import slidebuilder.enums.SceneEnum;

public class ControllerCampaignMenu extends ControllerDataInterface {

	@FXML private Spinner<Integer> slide_scenarios;
	@FXML private VBox vbox;
	private TextFieldFile textFieldFile;

	//INIT
	@FXML
	public void initialize() {
		textFieldFile = new TextFieldFile();
		textFieldFile.setTitle("Add Campaign File");
		textFieldFile.setFileTypeIsCampaign(true);
		
		vbox.getChildren().addAll(textFieldFile.getContainer().getChildren());
		
		//Setting Min and Max scenario amount, always at least 1 scenario
		slide_scenarios.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20));
	}

	@FXML
	private void openSlideEditor() {
		goToScene(SceneEnum.CAMPAIGN_SLIDE, true);
	}
	
	@FXML
	private void openScenarioEditor() {
		goToScene(SceneEnum.CAMPAIGN_SCENARIOSELECT, true);
	}
	
	@Override
	public void saveCurrentData() {
		DataManager.getDataCampaign().saveCampaignValues(getCampaignNameFromPath(), slide_scenarios.getValue(), textFieldFile.getTextFieldString());
	}
	
	@Override
	public void loadData() {
		slide_scenarios.getValueFactory().setValue(DataManager.getDataCampaign().getCampaignScenarios());
		textFieldFile.setTextFieldString(DataManager.getDataCampaign().getCampaignFilePath());
		textFieldFile.filePathInvalid();
	}

	private String getCampaignNameFromPath() {
		String s = "";
		if(textFieldFile.getTextFieldString() != null)
			s = textFieldFile.getTextFieldString();
		File f = new File(s);

		//File name without extensions
		String fileName = f.getName();
		return fileName.replace(".aoe2campaign", "");
	}

	@Override
	protected void setDisabledValues() {
		// Not used
	}

	@Override
	public void sceneIn() {
		// Not used
	}

	@Override
	public void sceneOut() {
		// Not used
	}
}
