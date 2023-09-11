package slidebuilder.controllers;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import slidebuilder.components.TextFieldFile;
import slidebuilder.controllers.interfaces.TabControllerInterface;
import slidebuilder.data.CustomImageComboBox;
import slidebuilder.data.DataManager;
import slidebuilder.data.DataSlideshow;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.enums.SceneEnum;
import slidebuilder.util.FileFormats;
import slidebuilder.util.Popup;

public class ControllerSlideMenu extends TabControllerInterface {

	@FXML private Spinner<Integer> slide_slides;
	@FXML private ComboBox<String> slide_background;
	@FXML private Button slide_button_edit;
	@FXML private CheckBox slide_disable;
	@FXML private Label slide_title;
	@FXML private Button slide_button_sync;
	@FXML private VBox vbox;
	private TextFieldFile textFieldFile; 
	
	//INIT
	@FXML
	public void initialize() {
		
		setSceneBack(SceneEnum.CAMPAIGN_MENU);
		setSceneNext(SceneEnum.CAMPAIGN_SLIDE_EDIT);
		
		textFieldFile = new TextFieldFile();
		textFieldFile.setTitle("Add Slideshow Audio (Optional)");
		textFieldFile.setFileFormat(FileFormats.FILE_FORMAT_AUDIO);
		textFieldFile.setFileExtensions(new String[]{FileFormats.FILE_EXTENSION_WAV});
		
		vbox.getChildren().addAll(textFieldFile.getContainer().getChildren());
		
		setAudioEditorListener();
		
		//Setting Min and Max slide amount
		slide_slides.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20));
		
		//Slide bg Combobox
		//Automatically add user added images
		slide_background.setItems(CustomImageComboBox.getCustomImageNameList(CreatorEnum.SLIDE_BG));
		slide_background.getSelectionModel().selectFirst();
		
		//Save default data so preview can be instantly used when project launched
		initData();
	}
	
	@FXML
	private void changeBackground(ActionEvent event) {
		DataManager.getPreviewSlideshow().setBackground(slide_background.getValue());
	}

	
	@FXML
	private void openAudioEditor(ActionEvent event) throws IOException {
		saveCurrentData();
		if(!textFieldFile.filePathInvalid()) {
			goToScene(SceneEnum.CAMPAIGN_AUDIO_EDIT, true);
		}
		else {
			Popup.showError("The selected file path doesn't exist. Either remove the path or select a new file.");
		}
	}
	
	@FXML
	private void setDisabled(ActionEvent event) {
		setDisabledValues();
	}
	
	private void setAudioEditorListener() {

		textFieldFile.getTextField().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				setAudioEditorDisabled();
			}
		});
	}
	
	private void setAudioEditorDisabled() {
		//If slideshow is disabled, can't access audio editor
		if (slide_disable.isSelected())
			slide_button_sync.setDisable(true);
		//If there is no path selected to audio file, audio editor is disabled
		else if (textFieldFile.getTextFieldString() == null || textFieldFile.getTextFieldString().isEmpty())
			slide_button_sync.setDisable(true);
		else
			slide_button_sync.setDisable(false);
	}
	
	@Override
	protected void setDisabledValues() {
		boolean disable = slide_disable.isSelected();
		slide_background.setDisable(disable);
		slide_slides.setDisable(disable);
		slide_button_edit.setDisable(disable);
		textFieldFile.setTextFieldDisabled(disable);
		setAudioEditorDisabled();
	}
	
	@Override
	protected void setTabDefaultValues(int i) {
		if(DataManager.getDataCampaign().getListSlideshow().size()-1 < i) {
			DataSlideshow ds = new DataSlideshow();
			ds.save(1, slide_background.getItems().get(0), false, null);
			DataManager.getDataCampaign().getListSlideshow().add(ds);
		}
	}
	
	@Override
	protected void setTabName(Tab tab, int index) {
		String prefix = "I";
		if(index%2 != 0) prefix = "O";
		
		int num = (int) Math.floor(index/2) + 1;
		
		tab.setText(num+prefix);
	}
	
	@Override
	public void saveCurrentData(int index) {
		DataManager.getDataCampaign().getListSlideshow().get(index).save(
				slide_slides.getValue(),
				slide_background.getValue(),
				slide_disable.isSelected(),
				textFieldFile.getTextFieldString()
		);
	}
	
	@Override
	public void loadCurrentData(int index) {
		DataSlideshow ds = DataManager.getDataCampaign().getListSlideshow().get(index);
		
		int slides = ds.getSlides();
		slide_slides.getValueFactory().setValue(slides);

		String bg = ds.getBackground();
		slide_background.getSelectionModel().select(bg);

		boolean b = ds.getDisable();
		slide_disable.setSelected(b);
		
		String audio = ds.getAudioPath();
		textFieldFile.setTextFieldString(audio);
		
		setAudioEditorDisabled();
		//Check if the path to the file exists, if not it displays error
		textFieldFile.filePathInvalid();
	}
	
	@Override
	protected void setTitle() {
		
		int tab = getCurrentTabIndex();
		
		int num = tab/2 + 1; //+1 because we dont want it to start from 0
		
		if(tab%2==0) {
			//Is intro
			slide_title.setText("Intro "+num);
		}
		else {
			//Is outro
			slide_title.setText("Outro "+num);
		}
	}

	@Override
	public void sceneIn() {
		//Create twice as many tabs as num of scenarios, because every scenario has 1 intro and 1 outro slide
		int slides = DataManager.getDataCampaign().getCampaignScenarios() * 2;
		setTabSize(slides);
		
		//Load data for the new root
		initData();
		loadData(0);
	}

	@Override
	public void sceneOut() {
		// Not used
	}
	
}
