package slidebuilder.controllers;

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
import slidebuilder.util.FileFormats;

public class ControllerCampaignMenu extends ControllerDataInterface {
	
	@FXML private TextField slide_bar;
	@FXML private Spinner<Integer> slide_scenarios;
	@FXML private VBox vbox;
	private TextFieldFile textFieldFile;

	//INIT
	@FXML
	public void initialize() {
		textFieldFile = new TextFieldFile();
		textFieldFile.setTitle("Add Campaign File (Optional)");
		textFieldFile.setFileFormat(FileFormats.FILE_FORMAT_CAMPAIGN);
		textFieldFile.setFileExtensions(new String[]{FileFormats.FILE_EXTENSION_CAMPAIGN});
		
		vbox.getChildren().addAll(textFieldFile.getContainer().getChildren());
		
		//Setting Min and Max scenario amount, always at least 1 scenario
		slide_scenarios.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20));
		
		//Campaign name cannot have " quotes
		slide_bar.setTextFormatter(new TextFormatter<String>(change -> {
			if(change.getText().contains("\""))
				return null;
			return change;
		}));
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
		DataManager.getDataCampaign().saveCampaignValues(slide_bar.getText(), slide_scenarios.getValue(), textFieldFile.getTextFieldString());
	}
	
	@Override
	public void loadData() {
		slide_bar.setText(DataManager.getDataCampaign().getCampaignName());
		slide_scenarios.getValueFactory().setValue(DataManager.getDataCampaign().getCampaignScenarios());
		textFieldFile.setTextFieldString(DataManager.getDataCampaign().getCampaignFilePath());
		textFieldFile.filePathInvalid();
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
