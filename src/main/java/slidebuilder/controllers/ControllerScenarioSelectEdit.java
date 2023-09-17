package slidebuilder.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import slidebuilder.components.ScenarioButton;
import slidebuilder.controllers.interfaces.TabControllerInterface;
import slidebuilder.data.CustomImageComboBox;
import slidebuilder.data.DataManager;
import slidebuilder.data.DataScenarios;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.enums.SceneEnum;
import slidebuilder.previews.PreviewScenarios;
import slidebuilder.resource.ResourceManager;
import slidebuilder.util.ParseUtil;
import slidebuilder.util.TextFieldFormatter;

public class ControllerScenarioSelectEdit extends TabControllerInterface {
	@FXML private Label slide_title;
	@FXML private TextField textfield_text;
	@FXML private TextField textfield_text_x;
	@FXML private TextField textfield_text_y;
	@FXML private TextField textfield_button_x;
	@FXML private TextField textfield_button_y;
	@FXML private ComboBox<String> button_difficulty;
	@FXML private ComboBox<String> button_image;
	@FXML private Button button_create_image;
	@FXML private TextField textfield_image_width;
	@FXML private TextField textfield_image_height;
	@FXML private CheckBox checkbox_image;
	@FXML private TextArea textfield_help;
	@FXML private ComboBox<String> button_help;
	@FXML private ImageView button_color_white;
	@FXML private ImageView button_color_blue;
	@FXML private ImageView button_color_red;
	@FXML private ImageView button_color_yellow;
	@FXML private ImageView button_color_cyan;
	@FXML private ImageView button_color_purple;
	@FXML private ImageView button_color_gray;
	@FXML private ImageView button_color_orange;
	@FXML private ImageView button_color_italic;
	
	//INIT
	@FXML
	public void initialize() {
		
		setSceneBack(SceneEnum.CAMPAIGN_SCENARIOSELECT);
		
		setTextFormatters();

		//Button combobox
		//Automatically add user added images
		button_image.setItems(CustomImageComboBox.getCustomImageNameList(CreatorEnum.ICON));
		button_image.getSelectionModel().selectFirst();
		
		//Difficulty combobox
		int size = ResourceManager.instance.getDifficultyResourceList().size();
		for(int i=0; i < size; i++) {
			button_difficulty.getItems().add(ResourceManager.instance.getDifficultyResourceList().get(i).getName());
		}
		button_difficulty.getSelectionModel().selectFirst();
		
		//Help combobox
		//Add an option for empty value
		button_help.getItems().add("None");
		size = ResourceManager.instance.getExpansionResourceList().size();
		for(int i=0; i < size; i++) {
			button_help.getItems().add(ResourceManager.instance.getExpansionResourceList().get(i).getId());
		}
		button_help.getSelectionModel().selectFirst();
		
		//Automatically update preview
		textfield_text.textProperty().addListener((observable, oldValue, newValue) -> {
		    		getPreview().getButton(getCurrentTabIndex()).getButtonLabel().setText(textfield_text.getText());
		});
		
		textfield_text_x.textProperty().addListener((observable, oldValue, newValue) -> {
			setTextX();
		});
		
		textfield_text_y.textProperty().addListener((observable, oldValue, newValue) -> {
			setTextY();
		});
		
		textfield_button_x.textProperty().addListener((observable, oldValue, newValue) -> {
			setButtonX();
		});
		
		textfield_button_y.textProperty().addListener((observable, oldValue, newValue) -> {
			setButtonY();
		});
		
		textfield_image_width.textProperty().addListener((observable, oldValue, newValue) -> {
			setImageWidth();
		});
		
		textfield_image_height.textProperty().addListener((observable, oldValue, newValue) -> {
			setImageHeight();
		});
		
		textfield_help.textProperty().addListener((observable, oldValue, newValue) -> {
			setHelpText();
		});
		
		button_help.valueProperty().addListener((observable, oldValue, newValue) -> {
			setHelpStyle();
		});
		
		//Color Button handlers
		addHelpButtonEventHandler(button_color_white, "<default>");
		addHelpButtonEventHandler(button_color_blue, "<blue>");
		addHelpButtonEventHandler(button_color_red, "<red>");
		addHelpButtonEventHandler(button_color_yellow, "<yellow>");
		addHelpButtonEventHandler(button_color_cyan, "<cyan>");
		addHelpButtonEventHandler(button_color_purple, "<purple>");
		addHelpButtonEventHandler(button_color_gray, "<gray>");
		addHelpButtonEventHandler(button_color_orange, "<orange>");
		addHelpButtonEventHandler(button_color_italic, "<i>");
	}
	
	private void addHelpButtonEventHandler(ImageView button, String text) {
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> textfield_help.insertText(textfield_help.getCaretPosition(), text));
	}

	@FXML
	private void setDisabled(ActionEvent event) {
		setDisabledValues();
		setImageSizeOnSelection();
	}
	
	private void setImageSizeOnSelection() {
		if(checkbox_image.isSelected()) {
			setImageDefaultSize();
			setImageWidth();
			setImageHeight();
		}
	}
	
	@FXML
	private void setDifficulty(ActionEvent event) {
		getPreview().getButton(getCurrentTabIndex()).getButtonLabel().setDifficulty(button_difficulty.getValue());
	}
	
	@FXML
	private void setIcon(ActionEvent event) {
		getPreview().getButton(getCurrentTabIndex()).getButtonImage().setButtonImage((button_image.getValue()));
		if(getKeepOriginalSize()) {
			setImageDefaultSize();
		}
	}
	
	private void setTextFormatters() {
		textfield_text_x.setTextFormatter(new TextFieldFormatter(0, true));
		textfield_text_y.setTextFormatter(new TextFieldFormatter(0, true));
		textfield_button_x.setTextFormatter(new TextFieldFormatter(0, false));
		textfield_button_y.setTextFormatter(new TextFieldFormatter(0, false));
		textfield_image_width.setTextFormatter(new TextFieldFormatter(0, false));
		textfield_image_height.setTextFormatter(new TextFieldFormatter(0, false));
	}

	private void setImageSize(int width, int height) {
		textfield_image_width.setText(""+width);
		textfield_image_height.setText(""+height);
	}
	
	private void setImageDefaultSize() {
		String name = button_image.getValue();
		int index = getTabPane().getSelectionModel().getSelectedIndex();
		ScenarioButton sb = getPreview().getButton(index);
		
		int width = (int)sb.getImageOriginalWidth(name);
		int height = (int)sb.getImageOriginalHeight(name);
		setImageSize(width, height);
	}
	
	private boolean getKeepOriginalSize() {
		return checkbox_image.isSelected();
	}
	
	@Override
	protected void setTabDefaultValues(int i) {
		//Init button data if it doesnt exist yet
		if(DataManager.getDataCampaign().getListScenarios().size()-1 < i) {

			//Button default properties
			int bXdefault = 1300;
			int bYdefault = 150;
			int labelY = 250;
			int row = i/3; //Buttons are created in rows of 3

			if (i < 9) {
				bXdefault -= row * 200;
				bYdefault += row * 400;
			}

			int bX = 100;
			int bY = 100;
			int column = i%3;
			int scenarioNum = i + 1;
			String buttonText = scenarioNum+". Scenario";

			if (i < 9) {
				bX = bXdefault + column * 700;
				bY = bYdefault + column * 250;
			}

			//Save the button
			DataScenarios ds = new DataScenarios();
			ds.save(buttonText, bX, bY, 0, labelY, button_image.getItems().get(0), 280, 280, "", button_help.getItems().get(0), button_difficulty.getItems().get(0), true);
			DataManager.getDataCampaign().getListScenarios().add(ds);

			//Update preview button
			getPreview().getButton(i).getButtonLabel().setText(buttonText);
			getPreview().getButton(i).setButtonX(bX);
			getPreview().getButton(i).setButtonY(bY);
			getPreview().getButton(i).setTextY(labelY);
		}
	}
	
	private PreviewScenarios getPreview() {
		return DataManager.getPreviewScenarios();
	}
	
	@Override
	public void saveCurrentData(int index) {
		DataManager.getDataCampaign().getListScenarios().get(index).save(
				textfield_text.getText(),
				ParseUtil.parseInt(textfield_button_x.getText()),
				ParseUtil.parseInt(textfield_button_y.getText()),
				ParseUtil.parseInt(textfield_text_x.getText()),
				ParseUtil.parseInt(textfield_text_y.getText()),
				button_image.getValue(),
				ParseUtil.parseInt(textfield_image_width.getText()),
				ParseUtil.parseInt(textfield_image_height.getText()),
				textfield_help.getText(),
				button_help.getValue(),
				button_difficulty.getValue(),
				checkbox_image.isSelected()
		);
	}
	
	@Override
	public void loadCurrentData(int index) {
		
		DataScenarios ds = DataManager.getDataCampaign().getListScenarios().get(index);
		
		String text = ds.getButtonText();
		int text_x = ds.getButtonTextX();
		int text_y = ds.getButtonTextY();
		int button_x = ds.getButtonX();
		int button_y = ds.getButtonY();
		String image = ds.getImage();
		int image_width = ds.getImageWidth();
		int image_height = ds.getImageHeight();
		boolean is_def_size = ds.getIsDefaultSize();
		String help_text = ds.getHelpText();
		String help_style = ds.getHelpStyle();
		String difficulty = ds.getDifficulty();
		
		textfield_text.setText(text);
		textfield_text_x.setText(""+text_x);
		textfield_text_y.setText(""+text_y);
		textfield_button_x.setText(""+button_x);
		textfield_button_y.setText(""+button_y);
		button_image.setValue(image);
		textfield_image_width.setText(""+image_width);
		textfield_image_height.setText(""+image_height);
		checkbox_image.setSelected(is_def_size);
		textfield_help.setText(""+help_text);
		button_help.setValue(help_style);
		button_difficulty.setValue(difficulty);
	}
	
	@Override
	protected void setDisabledValues() {
		if(checkbox_image.isSelected()) {
			textfield_image_width.setDisable(true);
			textfield_image_height.setDisable(true);
		} else {
			textfield_image_width.setDisable(false);
			textfield_image_height.setDisable(false);
		}
	}
	
	private String getTextfieldValue(TextField tf) {
		if(tf.getText().equals(""))
			return "0";
		else
			return tf.getText();
	}
	
	private void setTextX() {
		getPreview().getButton(getCurrentTabIndex()).setTextX(ParseUtil.parseInt(getTextfieldValue(textfield_text_x)));
	}
	
	private void setTextY() {
		getPreview().getButton(getCurrentTabIndex()).setTextY(ParseUtil.parseInt(getTextfieldValue(textfield_text_y)));
	}
	
	private void setButtonX() {
		getPreview().getButton(getCurrentTabIndex()).setButtonX(ParseUtil.parseInt(getTextfieldValue(textfield_button_x)));
	}
	
	private void setButtonY() {
		getPreview().getButton(getCurrentTabIndex()).setButtonY(ParseUtil.parseInt(getTextfieldValue(textfield_button_y)));
	}
	
	private void setImageWidth() {
		getPreview().getButton(getCurrentTabIndex()).setImageWidth(ParseUtil.parseInt(getTextfieldValue(textfield_image_width)));
	}
	
	private void setImageHeight() {
		getPreview().getButton(getCurrentTabIndex()).setImageHeight(ParseUtil.parseInt(getTextfieldValue(textfield_image_height)));
	}
	
	private void setHelpText() {
		getPreview().getButton(getCurrentTabIndex()).setHelpText(textfield_help.getText());
	}
	
	private void setHelpStyle() {
		getPreview().getButton(getCurrentTabIndex()).setHelpStyle(button_help.getValue());
	}
	
	@Override
	protected void setTitle() {
		//Set slide title
		int tab_num = getCurrentTabIndex() + 1; //+1, dont want to start counting from 0
		slide_title.setText("Scenario "+tab_num+" Button");
	}

	@Override
	public void sceneIn() {
		//Create as many tabs as num of scenarios
		int scenarios = DataManager.getDataCampaign().getCampaignScenarios();
		setTabSize(scenarios);
		
		//Load data for the new root
		initData();
		loadData(0);
	}

	@Override
	public void sceneOut() {
		saveCurrentData();
	}

	@Override
	protected void setTabName(Tab tab, int index) {
		tab.setText(""+(index+1));
	}
}
