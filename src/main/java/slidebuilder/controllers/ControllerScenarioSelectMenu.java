package slidebuilder.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
		button_bg.getSelectionModel().select("Default");
		
		//Automatically update preview
		textfield_title.textProperty().addListener(new ChangeListener<String>() {
			@Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		    	String newValue) {
		    		getPreview().setHeaderText(textfield_title.getText());;
		    }
		});
		
		//Save default data so preview can be instantly used when project launched
		saveCurrentData();
	}
	
	@FXML
	private void setDisabled(ActionEvent event) {
		setDisabledValues();
	}
	
	@FXML
	private void changeBackground(ActionEvent event) {
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
