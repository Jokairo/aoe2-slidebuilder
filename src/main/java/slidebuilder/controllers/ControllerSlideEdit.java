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
import slidebuilder.components.PreviewElement;
import slidebuilder.controllers.interfaces.TabControllerInterface;
import slidebuilder.data.CustomImage;
import slidebuilder.data.CustomImageComboBox;
import slidebuilder.data.DataManager;
import slidebuilder.data.DataSlideshowSlide;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.enums.SceneEnum;
import slidebuilder.previews.PreviewSlideshow;
import slidebuilder.resource.ResourceManager;
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
	@FXML private CheckBox checkbox_keep_aspect;
	@FXML private TextField slide_bar_duration;

	private boolean ignoreWidthHeightListener = false;
	
	//INIT
	@FXML
	public void initialize() {
		
		setSceneBack(SceneEnum.CAMPAIGN_SLIDE);
		
		//Automatically add user added images
		image_button.setItems(CustomImageComboBox.getCustomImageNameList(CreatorEnum.SLIDE_IMAGE));

		//Change to list's default value if currently selected custom resource is deleted
		String defaultImage = ResourceManager.instance.getDefaultResource(CreatorEnum.SLIDE_IMAGE);
		image_button.valueProperty().addListener((options, oldValue, newValue) -> {
			boolean currentResourceExists = CustomImageComboBox.getCustomImageNameList(CreatorEnum.SLIDE_IMAGE).contains(oldValue);
			//Change to default value
			if(oldValue != null && !currentResourceExists) {
				image_button.getSelectionModel().select(defaultImage);
			}
		});
		image_button.getSelectionModel().select(defaultImage);
		setCurrentImageAspectRatio();
		
		setTextFormatters();

		/*
			Listeners
		 */

		image_button.valueProperty().addListener((observable, oldValue, newValue) -> {
			changeImage();
		});

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
			// Calculate aspect only when user types in width textfield and keep aspect checked
			boolean useAspect = !ignoreWidthHeightListener && checkbox_keep_aspect.isSelected();
			setImageWidth(useAspect);
			ignoreWidthHeightListener = false;
		});
		
		image_bar_height.textProperty().addListener((observable, oldValue, newValue) -> {
			// Calculate aspect only when user types in height textfield and keep aspect checked
			boolean useAspect = !ignoreWidthHeightListener && checkbox_keep_aspect.isSelected();
			setImageHeight(useAspect);
			ignoreWidthHeightListener = false;
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

		getPreview().getTextProperties().getX().addListener((observable, oldValue, newValue) -> {
			text_bar_x.textProperty().set(newValue);
		});

		getPreview().getTextProperties().getY().addListener((observable, oldValue, newValue) -> {
			text_bar_y.textProperty().set(newValue);
		});

		getPreview().getTextProperties().getWidth().addListener((observable, oldValue, newValue) -> {
			text_bar_width.textProperty().set(newValue);
		});

		getPreview().getTextProperties().getHeight().addListener((observable, oldValue, newValue) -> {
			text_bar_height.textProperty().set(newValue);
		});

		getPreview().getImageProperties().getX().addListener((observable, oldValue, newValue) -> {
			image_bar_x.textProperty().set(newValue);
		});

		getPreview().getImageProperties().getY().addListener((observable, oldValue, newValue) -> {
			image_bar_y.textProperty().set(newValue);
		});

		getPreview().getImageProperties().getWidth().addListener((observable, oldValue, newValue) -> {
			ignoreWidthHeightListener = true; // Prevent aspect ratio being calculated multiple times
			image_bar_width.textProperty().set(newValue);

		});

		getPreview().getImageProperties().getHeight().addListener((observable, oldValue, newValue) -> {
			ignoreWidthHeightListener = true; // Prevent aspect ratio being calculated multiple times
			image_bar_height.textProperty().set(newValue);
		});
		
		//Save default data so preview can be instantly used when project launched
		setOwnerId(0);
		initData();
	}

	private void changeImage() {
		setPreviewImage();
		setImageSizeOnSelection();
	}
	
	@FXML
	private void setImageDefaultSize(ActionEvent event) {
		setImageSizeOnSelection();
	}


	private void setCurrentImageAspectRatio() {
		PreviewElement image = getPreview().getImageWrapper();
		if (image == null) return;

		int width = ParseUtil.parseInt(image_bar_width.getText());
		int height = ParseUtil.parseInt(image_bar_height.getText());
		image.setAspectRatio(width, height);
	}
	@FXML
	private void setKeepAspect(ActionEvent event) {
		boolean keepAspect = checkbox_keep_aspect.isSelected();
		if (keepAspect) {
			setCurrentImageAspectRatio();
		}
		PreviewElement image = getPreview().getImageWrapper();
		image.setKeepAspect(keepAspect);
	}
	
	@FXML
	private void openAudioEditor(ActionEvent event) throws IOException {
		goToScene(SceneEnum.CAMPAIGN_AUDIO_EDIT, true);
	}
	
	private void setImageSizeOnSelection() {
		setImageDefaultSize();
		setImageWidth(false);
		setImageHeight(false);
	}
	
	public void setPreviewImage() {
		//Set null if the first value (which means no image)
		if (image_button.getSelectionModel().getSelectedIndex() == 0) {
			getPreview().setImage(null);
		}
		//Otherwise set the value that is in the combobox
		else {
			getPreview().setImage(image_button.getValue());
			if(checkbox_keep_aspect.isSelected()) {
				setImageDefaultSize();
			}
			setCurrentImageAspectRatio();
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
				checkbox_keep_aspect.isSelected(),
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
		checkbox_keep_aspect.setSelected(enabled);
		slide_bar_duration.setText(""+duration);

		setCurrentImageAspectRatio();
	}
	
	@Override
	protected void setDisabledValues() {
		// Not used
	}
	
	@Override
	protected void setTabDefaultValues(int i) {
		if(getListSlides().size()-1 < i) {
			DataSlideshowSlide ds = new DataSlideshowSlide();
			ds.save("", 1200, 700, 500, 1500, image_button.getItems().get(0), 910, 550, 1920, 1080, true, 10.00);
			getListSlides().add(ds);
		}
	}
	
	private String getTextfieldValue(TextField tf) {
		if(tf.getText().equals(""))
			return "0";
		else
			return tf.getText();
	}

	private void setImageX() {
		getPreview().setImageX(ParseUtil.parseInt(getTextfieldValue(image_bar_x)));
	}
	
	private void setImageY() {
		getPreview().setImageY(ParseUtil.parseInt(getTextfieldValue(image_bar_y)));
	}
	
	private void setImageWidth(boolean useAspect) {
		boolean changeAspect = !checkbox_keep_aspect.isSelected();
		getPreview().setImageWidth(ParseUtil.parseInt(getTextfieldValue(image_bar_width)), useAspect, changeAspect);
	}
	
	private void setImageHeight(boolean useAspect) {
		boolean changeAspect = !checkbox_keep_aspect.isSelected();
		getPreview().setImageHeight(ParseUtil.parseInt(getTextfieldValue(image_bar_height)), useAspect, changeAspect);
	}
	
	private void setTextX() {
		getPreview().setTextX(ParseUtil.parseInt(getTextfieldValue(text_bar_x)));
	}
	
	private void setTextY() {
		getPreview().setTextY(ParseUtil.parseInt(getTextfieldValue(text_bar_y)));
	}
	
	private void setTextWidth() {
		getPreview().setTextWidth(ParseUtil.parseInt(getTextfieldValue(text_bar_width)));
	}
	
	private void setTextHeight() {
		getPreview().setTextHeight(ParseUtil.parseInt(getTextfieldValue(text_bar_height)));
	}
	
	private void setText() {
		getPreview().setText(text_bar.getText());
	}
	
	private void setImageDefaultSize() {
		int index = image_button.getSelectionModel().getSelectedIndex();
		int width, height;
		if(index > 0) {
			String name = image_button.getValue();
			CustomImage ci = DataManager.getDataCampaign().getCustomImageData().getCustomImage(CreatorEnum.SLIDE_IMAGE, name);
			width = ci.getWidth();
			height = ci.getHeight();
			image_bar_width.setText(""+width);
			image_bar_height.setText(""+height);
		}
		else {
			width = 1920;
			height = 1080;
		}

		image_bar_width.setText(""+width);
		image_bar_height.setText(""+height);
		setCurrentImageAspectRatio();
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
		getPreview().setSlideshowIndex(i);
		
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
		getPreview().setSlideshowIndex(-1);

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
