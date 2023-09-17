package slidebuilder.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import slidebuilder.controllers.interfaces.TabControllerInterface;
import slidebuilder.data.CustomImage;
import slidebuilder.data.CustomImageComboBox;
import slidebuilder.data.DataManager;
import slidebuilder.data.DataSlideshowSlide;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.enums.SceneEnum;
import slidebuilder.previews.PreviewSlideshow;
import slidebuilder.util.ParseUtil;
import slidebuilder.util.TextFieldFormatter;

public class ControllerSlideEdit extends TabControllerInterface {
	
	@FXML private TextArea text_bar;
	@FXML private TextField text_bar_x;
	@FXML private TextField text_bar_y;
	@FXML private TextField text_bar_width;
	@FXML private TextField text_bar_height;
	@FXML private ComboBox<String> image_button;
	@FXML private TextField image_bar_x;
	@FXML private TextField image_bar_y;
	@FXML private TextField image_bar_width;
	@FXML private TextField image_bar_height;
	@FXML private Label slide_title;
	@FXML private CheckBox image_box;
	@FXML private TextField slide_bar_duration;
	
	
	//INIT
	@FXML
	public void initialize() {
		
		setSceneBack(SceneEnum.CAMPAIGN_SLIDE);
		
		//Automatically add user added images
		image_button.setItems(CustomImageComboBox.getCustomImageNameList(CreatorEnum.SLIDE_IMAGE));
		
		//Set first as default value
		image_button.getSelectionModel().selectFirst();
		
		setTextFormatters();

		text_bar.textProperty().addListener((observable, oldValue, newValue) -> {
			setText();
		});
		
		image_bar_x.textProperty().addListener((observable, oldValue, newValue) -> {
			setImageX();
		});
		
		image_bar_y.textProperty().addListener((observable, oldValue, newValue) -> {
			setImageY();
		});
		
		image_bar_width.textProperty().addListener((observable, oldValue, newValue) -> {
			setImageWidth();
		});
		
		image_bar_height.textProperty().addListener((observable, oldValue, newValue) -> {
			setImageHeight();
		});
		
		text_bar_x.textProperty().addListener((observable, oldValue, newValue) -> {
			setTextX();
		});
		
		text_bar_y.textProperty().addListener((observable, oldValue, newValue) -> {
			setTextY();
		});
		
		text_bar_width.textProperty().addListener((observable, oldValue, newValue) -> {
			setTextWidth();
		});
		
		text_bar_height.textProperty().addListener((observable, oldValue, newValue) -> {
			setTextHeight();
		});
		
		//Save default data so preview can be instantly used when project launched
		setOwnerId(0);
		initData();
	}
	
	@FXML
	private void changeImage(ActionEvent event) {
		setPreviewImage();
		setImageSizeOnSelection();
	}
	
	@FXML
	private void setImageDefaultSize(ActionEvent event) {
		setDisabledValues();
		setImageSizeOnSelection();
	}
	
	@FXML
	private void openAudioEditor(ActionEvent event) throws IOException {
		goToScene(SceneEnum.CAMPAIGN_AUDIO_EDIT, true);
	}
	
	private void setImageSizeOnSelection() {
		if(image_box.isSelected()) {
			setImageDefaultSize();
			setImageWidth();
			setImageHeight();
		}
	}
	
	public void setPreviewImage() {
		//Set null if the first value (which means no image)
		if (image_button.getSelectionModel().getSelectedIndex() == 0) {
			getPreview().setImage(null);
		}
		//Otherwise set the value that is in the combobox
		else {
			getPreview().setImage(image_button.getValue());
		}
	}
	
	private PreviewSlideshow getPreview() {
		return DataManager.getPreviewSlideshow();
	}
	
	private void setTextFormatters() {
		text_bar_x.setTextFormatter(new TextFieldFormatter(0, false));
		text_bar_y.setTextFormatter(new TextFieldFormatter(0, false));
		text_bar_width.setTextFormatter(new TextFieldFormatter(0, false));
		text_bar_height.setTextFormatter(new TextFieldFormatter(0, false));
		
		image_bar_x.setTextFormatter(new TextFieldFormatter(0, false));
		image_bar_y.setTextFormatter(new TextFieldFormatter(0, false));
		image_bar_width.setTextFormatter(new TextFieldFormatter(0, false));
		image_bar_height.setTextFormatter(new TextFieldFormatter(0, false));

		slide_bar_duration.setTextFormatter(new TextFieldFormatter(2, false));
	}
	
	@Override
	public void saveCurrentData(int index) {
		getListSlides().get(index).save(
				text_bar.getText(),
				ParseUtil.parseInt(text_bar_x.getText()),
				ParseUtil.parseInt(text_bar_y.getText()),
				ParseUtil.parseInt(text_bar_width.getText()),
				ParseUtil.parseInt(text_bar_height.getText()),
				image_button.getValue(),
				ParseUtil.parseInt(image_bar_x.getText()),
				ParseUtil.parseInt(image_bar_y.getText()),
				ParseUtil.parseInt(image_bar_width.getText()),
				ParseUtil.parseInt(image_bar_height.getText()),
				image_box.isSelected(),
				ParseUtil.parseDouble(slide_bar_duration.getText())
		);
	}
	
	@Override
	public void loadCurrentData(int index) {
		DataSlideshowSlide ds = getListSlides().get(index);
		
		String text = ds.getText();
		int text_x = ds.getTextX();
		int text_y = ds.getTextY();
		int text_width = ds.getTextWidth();
		int text_height = ds.getTextHeight();
		String image_path = ds.getImagePath();
		int image_x = ds.getImageX();
		int image_y = ds.getImageY();
		int image_width = ds.getImageWidth();
		int image_height = ds.getImageHeight();
		boolean enabled = ds.getEnabled();
		double duration = ds.getDuration();

		text_bar.setText(text);
		text_bar_x.setText(""+text_x);
		text_bar_y.setText(""+text_y);
		text_bar_width.setText(""+text_width);
		text_bar_height.setText(""+text_height);
		image_button.setValue(image_path);
		image_bar_x.setText(""+image_x);
		image_bar_y.setText(""+image_y);
		image_bar_width.setText(""+image_width);
		image_bar_height.setText(""+image_height);
		image_box.setSelected(enabled);
		slide_bar_duration.setText(""+duration);
	}
	
	@Override
	protected void setDisabledValues() {
		if(image_box.isSelected()) {
			image_bar_width.setDisable(true);
			image_bar_height.setDisable(true);
		} else {
			image_bar_width.setDisable(false);
			image_bar_height.setDisable(false);
		}
	}
	
	@Override
	protected void setTabDefaultValues(int i) {
		if(getListSlides().size()-1 < i) {
			DataSlideshowSlide ds = new DataSlideshowSlide();
			ds.save("", 1200, 700, 500, 1500, image_button.getItems().get(0), 910, 550, 1920, 1080, true, 10.00);
			getListSlides().add(ds);
		}
	}
	
	public void setNewPreviewValues() {
		setImageX();
		setImageY();
		setImageWidth();
		setImageHeight();
		
		setTextX();
		setTextY();
		setTextWidth();
		setTextHeight();
		
		setPreviewImage();
		
		setText();
	}
	
	private String getTextfieldValue(TextField tf) {
		if(tf.getText().equals(""))
			return "0";
		else
			return tf.getText();
	}

	private void setImageX() {
		getPreview().setImageX(ParseUtil.parseDouble(getTextfieldValue(image_bar_x)));
	}
	
	private void setImageY() {
		getPreview().setImageY(ParseUtil.parseDouble(getTextfieldValue(image_bar_y)));
	}
	
	private void setImageWidth() {
		getPreview().setImageWidth(ParseUtil.parseDouble(getTextfieldValue(image_bar_width)));
	}
	
	private void setImageHeight() {
		getPreview().setImageHeight(ParseUtil.parseDouble(getTextfieldValue(image_bar_height)));
	}
	
	private void setTextX() {
		getPreview().setTextX(ParseUtil.parseDouble(getTextfieldValue(text_bar_x)));
	}
	
	private void setTextY() {
		getPreview().setTextY(ParseUtil.parseDouble(getTextfieldValue(text_bar_y)));
	}
	
	private void setTextWidth() {
		getPreview().setTextWidth(ParseUtil.parseDouble(getTextfieldValue(text_bar_width)));
	}
	
	private void setTextHeight() {
		getPreview().setTextHeight(ParseUtil.parseDouble(getTextfieldValue(text_bar_height)));
	}
	
	private void setText() {
		getPreview().setText(text_bar.getText());
	}
	
	private void setImageDefaultSize() {
		int index = image_button.getSelectionModel().getSelectedIndex();
		if(index > 0) {
			String name = image_button.getValue();
			CustomImage ci = DataManager.getDataCampaign().getCustomImageData().getCustomImage(CreatorEnum.SLIDE_IMAGE, name);
			int width = ci.getWidth();
			int height = ci.getHeight();
			image_bar_width.setText(""+width);
			image_bar_height.setText(""+height);
		}
		else {
			image_bar_width.setText(""+1920);
			image_bar_height.setText(""+1080);
		}
	}
	
	private ArrayList<DataSlideshowSlide> getListSlides() {
		return DataManager.getDataCampaign().getListSlideshow().get(getOwnerId()).getListSlides();
	}
	
	@Override
	protected void setTitle() {
		//The number of the slideshow
		int num = getOwnerId()/2 + 1; //+1 because we dont want it to start from 0
		
		//The number of the individual slide in the slideshow
		int num2 = getCurrentTabIndex() + 1; //+1 because we dont want it to start from 0
		
		if(getOwnerId()%2==0) {
			//Is intro
			slide_title.setText("Intro "+num + " Slide "+num2);
		}
		else {
			//Is outro
			slide_title.setText("Outro "+num + " Slide "+num2);
		}
	}

	@Override
	public void sceneIn() {
		int i = DataManager.globalTabIndex;
		
		//Create as many tabs as num of slides
		setTabSize(DataManager.getDataCampaign().getListSlideshow().get(i).getSlides());
		
		//Save the index of the slideshow menu tab
		setOwnerId(i);
		
		//Load data for the new root
		initData();
		loadData(0);
	}

	@Override
	public void sceneOut() {
		DataManager.globalTabIndex = getOwnerId();

		//Save current slide data before loading to first slide
		saveCurrentData(getCurrentTabIndex());

		//Reset view back to first slide so that preview always shows first slide in menu view
		loadData(0);
	}

	@Override
	protected void setTabName(Tab tab, int index) {
		tab.setText(""+(index+1));
	}
}
