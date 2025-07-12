package slidebuilder.controllers;

import java.util.List;
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
import slidebuilder.data.DataManager;
import slidebuilder.data.DataSlideshowSlide;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.enums.SceneEnum;
import slidebuilder.previews.PreviewSlideshow;
import slidebuilder.resource.ResourceManager;
import slidebuilder.util.*;

public class ControllerSlideEdit extends TabControllerInterface<DataSlideshowSlide> {
	
	@FXML private TextArea text_bar;
	@FXML private TextField text_bar_x, text_bar_y, text_bar_width, text_bar_height;
	@FXML private ComboBox<String> image_button;
	@FXML private TextField image_bar_x, image_bar_y, image_bar_width, image_bar_height;
	@FXML private Label slide_title;
	@FXML private CheckBox checkbox_keep_aspect;
	@FXML private TextField slide_bar_duration;

	private boolean ignoreWidthHeightListener = false;

	@FXML
	public void initialize() {
		setSceneBack(SceneEnum.CAMPAIGN_SLIDE);

		ComboBoxInitializer.init(image_button, CreatorEnum.SLIDE_IMAGE, ResourceManager.instance.getDefaultResource(CreatorEnum.SLIDE_IMAGE));
		setCurrentImageAspectRatio();

		// Text formatters
		FormatterHelper.applyIntFormat(false,
				text_bar_x, text_bar_y, text_bar_width, text_bar_height,
				image_bar_x, image_bar_y, image_bar_width, image_bar_height);
		FormatterHelper.applyDecimalFormat(slide_bar_duration, 2);

		// Listeners (auto update preview when textfield changes)
		FieldBinder.bindCombo(image_button, v -> changeImage());
		FieldBinder.bindTextArea(text_bar, v -> setText());
		FieldBinder.bindText(image_bar_x, v -> setImageX());
		FieldBinder.bindText(image_bar_y, v -> setImageY());
		FieldBinder.bindText(image_bar_y, v -> setImageY());
		FieldBinder.bindText(image_bar_width, newValue -> {
			boolean useAspect = !ignoreWidthHeightListener && checkbox_keep_aspect.isSelected();
			setImageWidth(useAspect);
			ignoreWidthHeightListener = false;
		});
		FieldBinder.bindText(image_bar_height, newValue -> {
			boolean useAspect = !ignoreWidthHeightListener && checkbox_keep_aspect.isSelected();
			setImageHeight(useAspect);
			ignoreWidthHeightListener = false;
		});
		FieldBinder.bindText(text_bar_x, v -> setTextX());
		FieldBinder.bindText(text_bar_y, v -> setTextY());
		FieldBinder.bindText(text_bar_width, v -> setTextWidth());
		FieldBinder.bindText(text_bar_height, v -> setTextHeight());

		// Listeners (auto update textfields when preview changes)
		FieldBinder.bindPropertyToText(getPreview().getTextProperties().getX(), text_bar_x);
		FieldBinder.bindPropertyToText(getPreview().getTextProperties().getY(), text_bar_y);
		FieldBinder.bindPropertyToText(getPreview().getTextProperties().getWidth(), text_bar_width);
		FieldBinder.bindPropertyToText(getPreview().getTextProperties().getHeight(), text_bar_height);
		FieldBinder.bindPropertyToText(getPreview().getImageProperties().getX(), image_bar_x);
		FieldBinder.bindPropertyToText(getPreview().getImageProperties().getY(), image_bar_y);
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

	@FXML
	private void setKeepAspect(ActionEvent event) {
		boolean keepAspect = checkbox_keep_aspect.isSelected();
		if (keepAspect) {
			setCurrentImageAspectRatio();
		}
		getPreview().getImageWrapper().setKeepAspect(keepAspect);
	}

	@FXML
	private void setImageDefaultSize(ActionEvent event) {
		setImageSizeOnSelection();
	}

	@Override
	protected List<DataSlideshowSlide> getList() {
		return DataManager.getDataCampaign().getListSlideshow().get(getOwnerId()).getListSlides();
	}

	@Override
	protected DataSlideshowSlide createDefault(int index) {
		String defaultImg = image_button.getItems().isEmpty() ? "" : image_button.getItems().get(0);
		DataSlideshowSlide ds = new DataSlideshowSlide();
		ds.save("", 1200, 700, 500, 1500, defaultImg, 910, 550, 1920, 1080, true, 10.0);
		return ds;
	}

	@Override
	public void saveCurrentData(int index) {
		getList().get(index).save(
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
	protected void applyData(DataSlideshowSlide ds) {
		text_bar.setText(ds.getText());
		text_bar_x.setText(String.valueOf(ds.getTextX()));
		text_bar_y.setText(String.valueOf(ds.getTextY()));
		text_bar_width.setText(String.valueOf(ds.getTextWidth()));
		text_bar_height.setText(String.valueOf(ds.getTextHeight()));
		image_button.setValue(ds.getImagePath());
		image_bar_x.setText(String.valueOf(ds.getImageX()));
		image_bar_y.setText(String.valueOf(ds.getImageY()));
		image_bar_width.setText(String.valueOf(ds.getImageWidth()));
		image_bar_height.setText(String.valueOf(ds.getImageHeight()));
		checkbox_keep_aspect.setSelected(ds.getEnabled());
		slide_bar_duration.setText(String.valueOf(ds.getDuration()));
		setCurrentImageAspectRatio();
	}

	@Override
	protected int getItemCount() {
		return DataManager.getDataCampaign().getListSlideshow().get(getOwnerId()).getSlides();
	}

	@Override
	protected void setTitle() {
		slide_title.setText(ControllerHelper.getSlideIntroOutroTitle(getOwnerId(), getCurrentTabIndex()));
	}

	@Override
	public void sceneIn() {
		int i = DataManager.globalTabIndex;
		getPreview().setSlideshowIndex(i);
		//Save the index of the slideshow menu tab
		setOwnerId(i);
		super.sceneIn();
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
		String tab_num = ControllerHelper.getNumericTabLabel(index);
		tab.setText(tab_num);
	}

	@Override
	protected void setDisabledValues() {
		// Not used
	}

	private void changeImage() {
		setPreviewImage();
		setImageSizeOnSelection();
	}

	private void setCurrentImageAspectRatio() {
		ControllerHelper.setCurrentImageAspectRatio(getPreview().getImageWrapper(), image_bar_width, image_bar_height);
	}
	
	private void setImageSizeOnSelection() {
		setImageDefaultSize();
		setImageWidth(false);
		setImageHeight(false);
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

	/*
	 	Listener functions
 	*/
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

}
