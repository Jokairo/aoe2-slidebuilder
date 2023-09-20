package slidebuilder.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import slidebuilder.controllers.interfaces.ControllerDataInterface;
import slidebuilder.data.CustomImageComboBox;
import slidebuilder.data.DataManager;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.enums.SceneEnum;
import slidebuilder.previews.PreviewScenarios;
import slidebuilder.resource.ResourceManager;

public class ControllerScenarioSelectMenu extends ControllerDataInterface {
	
	@FXML private Label slide_title;
	@FXML private TextField textfield_title;
	@FXML private ComboBox<String> button_bg;
	@FXML private Button button_edit;
	@FXML private CheckBox checkbox_disable;
	
	//INIT
	@FXML
	public void initialize() {

		setSceneBack(SceneEnum.CAMPAIGN_MENU);
		setSceneNext(SceneEnum.CAMPAIGN_SCENARIOSELECT_EDIT);
		
		//Campaign backgrounds combobox
		//Automatically add user added images
		button_bg.setItems(CustomImageComboBox.getCustomImageNameList(CreatorEnum.CAMPAIGN_BG));

		//Change to list's default value if currently selected custom resource is deleted
		String defaultBg = ResourceManager.instance.getDefaultResource(CreatorEnum.CAMPAIGN_BG);
		button_bg.valueProperty().addListener((options, oldValue, newValue) -> {
			boolean currentResourceExists = CustomImageComboBox.getCustomImageNameList(CreatorEnum.CAMPAIGN_BG).contains(oldValue);
			//Change to default value
			if(oldValue != null && !currentResourceExists) {
				button_bg.getSelectionModel().select(defaultBg);
			}
		});
		button_bg.getSelectionModel().select(defaultBg);
		
		/*
			Listeners
		 */

		button_bg.valueProperty().addListener((observable, oldValue, newValue) -> {
			changeBackground();
		});

		textfield_title.textProperty().addListener((observable, oldValue, newValue) -> {
			getPreview().setHeaderText(textfield_title.getText());
		});
		
		//Save default data so preview can be instantly used when project launched
		saveCurrentData();
	}
	
	@FXML
	private void setDisabled(ActionEvent event) {
		setDisabledValues();
	}

	private void changeBackground() {
		getPreview().setBackground(button_bg.getValue());
	}
	
	private PreviewScenarios getPreview() {
		return DataManager.getPreviewScenarios();
	}
	
	@Override
	protected void setDisabledValues() {
		//Checked off
		if(checkbox_disable.isSelected()) {
			textfield_title.setDisable(true);
			button_bg.setDisable(true);
			button_edit.setDisable(true);
		}
		//Checked on
		else {
			textfield_title.setDisable(false);
			button_bg.setDisable(false);
			button_edit.setDisable(false);
		}
	}
	
	@Override
	public void saveCurrentData() {
		DataManager.getDataCampaign().saveCampaignMenuValues(textfield_title.getText(), button_bg.getValue(), checkbox_disable.isSelected());
	}

	@Override
	public void loadData() {
		textfield_title.setText(DataManager.getDataCampaign().getCampaignMenuTitle());
		System.out.println(DataManager.getPreviewScenarios().getBackgroundName());
		String bg = DataManager.getDataCampaign().getCampaignMenuBackground();
		button_bg.getSelectionModel().select(bg);
		System.out.println(DataManager.getPreviewScenarios().getBackgroundName());
		checkbox_disable.setSelected(DataManager.getDataCampaign().getCampaignMenuDisabled());
	}

	@Override
	public void sceneIn() {
		//Create as many buttons as num of scenarios
		DataManager.getPreviewScenarios().createButtons();

		//Initialise the default button placements
		getSubController().sceneIn();
	}

	@Override
	public void sceneOut() {
		// Not used
	}
}
