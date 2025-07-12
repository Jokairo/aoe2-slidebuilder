package slidebuilder.controllers;

import java.util.List;
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
import slidebuilder.data.DataManager;
import slidebuilder.data.DataSlideshow;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.enums.SceneEnum;
import slidebuilder.resource.ResourceManager;
import slidebuilder.util.*;

public class ControllerSlideMenu extends TabControllerInterface<DataSlideshow> {

	@FXML private Spinner<Integer> slide_slides;
	@FXML private ComboBox<String> slide_background;
	@FXML private Button slide_button_edit, slide_button_back, slide_button_preview, slide_button_sync;
	@FXML private CheckBox slide_disable;
	@FXML private Label slide_title;
	@FXML private VBox vbox;
	private TextFieldFile textFieldFile; 

	@FXML
	public void initialize() {
		setSceneBack(SceneEnum.CAMPAIGN_MENU);
		setSceneNext(SceneEnum.CAMPAIGN_SLIDE_EDIT);
		
		textFieldFile = new TextFieldFile();
		textFieldFile.setTitle("Add Slideshow Audio (Optional)");
		textFieldFile.setFileFormat(FileFormats.FILE_FORMAT_AUDIO);
		textFieldFile.setFileExtensions(new String[]{FileFormats.FILE_EXTENSION_WAV});
		vbox.getChildren().addAll(textFieldFile.getContainer().getChildren());

		slide_slides.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20));
		ComboBoxInitializer.init(slide_background, CreatorEnum.SLIDE_BG, ResourceManager.instance.getDefaultResource(CreatorEnum.SLIDE_BG));

		// Listeners (auto update preview when textfield changes)
		FieldBinder.bindCombo(slide_background, v -> changeBackground());
		FieldBinder.bindText(textFieldFile.getTextField(), v -> setAudioEditorDisabled());
		
		//Save default data so preview can be instantly used when project launched
		initData();
	}
	
	@FXML
	private void openAudioEditor(ActionEvent event) {
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

	@FXML
	private void openLivePreview() {
		saveCurrentData();
		DataManager.openPreviewSlideshowLive();
		setEverythingDisabled(true);
		DataManager.getPreviewSlideshowLive().setOnCloseCallback(() -> {
			setEverythingDisabled(false);
		});
	}

	@Override
	protected void setDisabledValues() {
		boolean disable = slide_disable.isSelected();
		slide_background.setDisable(disable);
		slide_slides.setDisable(disable);
		slide_button_edit.setDisable(disable);
		textFieldFile.setTextFieldDisabled(disable);
		slide_button_preview.setDisable(disable);
		setAudioEditorDisabled();
	}

	@Override
	protected List<DataSlideshow> getList() {
		return DataManager.getDataCampaign().getListSlideshow();
	}

	@Override
	protected DataSlideshow createDefault(int index) {
		String defaultBg = slide_background.getItems().isEmpty()
				? ResourceManager.instance.getDefaultResource(CreatorEnum.SLIDE_BG)
				: slide_background.getItems().get(0);
		DataSlideshow ds = new DataSlideshow();
		ds.save(1, defaultBg, false, null);
		return ds;
	}

	@Override
	protected void applyData(DataSlideshow ds) {
		slide_slides.getValueFactory().setValue(ds.getSlides());
		slide_background.getSelectionModel().select(ds.getBackground());
		slide_disable.setSelected(ds.getDisable());
		textFieldFile.setTextFieldString(ds.getAudioPath());
		setAudioEditorDisabled();
		textFieldFile.filePathInvalid(); // will show error if invalid
	}

	@Override
	public void saveCurrentData(int index) {
		getList().get(index).save(
			slide_slides.getValue(),
			slide_background.getValue(),
			slide_disable.isSelected(),
			textFieldFile.getTextFieldString()
		);
	}

	@Override
	protected int getItemCount() {
		return DataManager.getDataCampaign().getCampaignScenarios() * 2;
	}

	@Override
	protected void setTabName(Tab tab, int index) {
		tab.setText(ControllerHelper.getTabLabelWithPrefix(index, "I", "O"));
	}

	@Override
	protected void setTitle() {
		slide_title.setText(ControllerHelper.getIntroOutroTitle(getCurrentTabIndex()));
	}

	private void changeBackground() {
		DataManager.getPreviewSlideshow().setBackground(slide_background.getValue());
		DataManager.getPreviewSlideshowLive().setBackground(slide_background.getValue());
	}

	private void setEverythingDisabled(boolean disable) {
		slide_background.setDisable(disable);
		slide_slides.setDisable(disable);
		slide_button_edit.setDisable(disable);
		textFieldFile.setTextFieldDisabled(disable);
		setAudioEditorDisabled();
		getTabPane().setDisable(disable);
		slide_button_back.setDisable(disable);
		slide_button_preview.setDisable(disable);
		slide_disable.setDisable(disable);

		// Apply slide_disable value
		if (!disable)
			setDisabledValues();
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
}
