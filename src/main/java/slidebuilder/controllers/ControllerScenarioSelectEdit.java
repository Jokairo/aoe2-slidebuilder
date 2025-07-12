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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import slidebuilder.components.PreviewElement;
import slidebuilder.components.ScenarioButton;
import slidebuilder.controllers.interfaces.TabControllerInterface;
import slidebuilder.data.DataManager;
import slidebuilder.data.DataScenarios;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.enums.ResourceEnum;
import slidebuilder.enums.SceneEnum;
import slidebuilder.previews.PreviewScenarios;
import slidebuilder.resource.ResourceManager;
import slidebuilder.util.*;

public class ControllerScenarioSelectEdit extends TabControllerInterface<DataScenarios> {
	@FXML private Label slide_title;
	@FXML private TextField textfield_text, textfield_text_x, textfield_text_y, textfield_button_x, textfield_button_y, textfield_image_width, textfield_image_height;
	@FXML private ComboBox<String> button_difficulty, button_image, button_help;
	@FXML private CheckBox checkbox_keep_aspect;
	@FXML private TextArea textfield_help;
	@FXML private ImageView button_color_white, button_color_blue, button_color_red, button_color_yellow, button_color_cyan, button_color_purple, button_color_gray, button_color_orange, button_color_italic;
	private boolean ignoreWidthHeightListener = false;

	@FXML
	public void initialize() {
		setSceneBack(SceneEnum.CAMPAIGN_SCENARIOSELECT);

		// Text formatters
		FormatterHelper.applyIntFormat(true, textfield_text_x, textfield_text_y);
		FormatterHelper.applyIntFormat(false, textfield_button_x, textfield_button_y, textfield_image_width, textfield_image_height);

		// Init comboboxes
		ComboBoxInitializer.init(button_image, CreatorEnum.ICON, ResourceManager.instance.getDefaultResource(CreatorEnum.ICON));
		ComboBoxInitializer.initFromResourceList(button_difficulty, ResourceEnum.DIFFICULTY);
		ComboBoxInitializer.initFromResourceList(button_help, ResourceEnum.EXPANSION, "None");

		// Listeners (auto update preview when textfield changes)
		FieldBinder.bindText(textfield_text, v -> getButton().getButtonLabel().setText(v));
		FieldBinder.bindText(textfield_text_x, v -> getButton().setTextX(parse(textfield_text_x)));
		FieldBinder.bindText(textfield_text_y, v -> getButton().setTextY(parse(textfield_text_y)));
		FieldBinder.bindText(textfield_button_x, v -> getWrapper().setElementX(parse(textfield_button_x)));
		FieldBinder.bindText(textfield_button_y, v -> getWrapper().setElementY(parse(textfield_button_y)));
		FieldBinder.bindText(textfield_image_width, v -> applyImageSize(true, true));
		FieldBinder.bindText(textfield_image_height, v -> applyImageSize(false, true));
		FieldBinder.bindTextArea(textfield_help, v -> getButton().setHelpText(v));
		FieldBinder.bindCombo(button_help, v -> getButton().setHelpStyle(v));
		FieldBinder.bindCombo(button_image, v -> {
			if (getButton() == null) return;
			getButton().getButtonImage().setButtonImage(v);
			if (checkbox_keep_aspect.isSelected()) setImageDefaultSize();
			setCurrentImageAspectRatio();
		});
		FieldBinder.bindCombo(button_difficulty, v -> getButton().getButtonLabel().setDifficulty(v));

		// Listeners (auto update textfields when preview changes)
		FieldBinder.bindPropertyToText(getPreview().getButtonProperties().getX(), textfield_button_x);
		FieldBinder.bindPropertyToText(getPreview().getButtonProperties().getY(), textfield_button_y);
		FieldBinder.bindPropertyToText(getPreview().getButtonLabelProperties().getX(), textfield_text_x);
		FieldBinder.bindPropertyToText(getPreview().getButtonLabelProperties().getY(), textfield_text_y);
		getPreview().getButtonProperties().getWidth().addListener((observable, oldValue, newValue) -> {
			ignoreWidthHeightListener = true; // Prevent aspect ratio being calculated multiple times
			textfield_image_width.textProperty().set(newValue);
		});
		getPreview().getButtonProperties().getHeight().addListener((observable, oldValue, newValue) -> {
			ignoreWidthHeightListener = true; // Prevent aspect ratio being calculated multiple times
			textfield_image_height.textProperty().set(newValue);
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

	@FXML
	private void setResize(ActionEvent event) {
		setImageDefaultSize();
		applyImageSize(true, false);
		applyImageSize(false, false);
	}

	@FXML
	private void setKeepAspect(ActionEvent event) {
		boolean keepAspect = checkbox_keep_aspect.isSelected();
		if (keepAspect) {
			setCurrentImageAspectRatio();
		}
		getWrapper().setKeepAspect(keepAspect);
	}

	@Override
	protected List<DataScenarios> getList() {
		return DataManager.getDataCampaign().getListScenarios();
	}

	@Override
	protected DataScenarios createDefault(int index) {
		int bXdefault = 1300, bYdefault = 150, labelY = 250;
		int row = index / 3, column = index % 3, scenarioNum = index + 1;
		String buttonText = scenarioNum + ". Scenario";

		int bX = 100, bY = 100;
		if (index < 9) {
			bXdefault -= row * 200;
			bYdefault += row * 400;

			bX = bXdefault + column * 700;
			bY = bYdefault + column * 250;
		}

		DataScenarios ds = new DataScenarios();
		ds.save(
			buttonText, bX, bY, 0, labelY,
			button_image.getItems().get(0), 280, 280, "",
			button_help.getItems().get(0), button_difficulty.getItems().get(0), true
		);

		//Update preview button
		getPreview().getButton(index).getButtonLabel().setText(buttonText);
		getPreview().getButtonWrapper(index).setElementX(bX);
		getPreview().getButtonWrapper(index).setElementY(bY);
		getPreview().getButton(index).setTextY(labelY);

		return ds;
	}
	
	@Override
	public void saveCurrentData(int index) {
		getList().get(index).save(
			textfield_text.getText(),
			parse(textfield_button_x),
			parse(textfield_button_y),
			parse(textfield_text_x),
			parse(textfield_text_y),
			button_image.getValue(),
			parse(textfield_image_width),
			parse(textfield_image_height),
			textfield_help.getText(),
			button_help.getValue(),
			button_difficulty.getValue(),
			checkbox_keep_aspect.isSelected()
		);
	}

	@Override
	protected void applyData(DataScenarios ds) {
		textfield_text.setText(ds.getButtonText());
		textfield_text_x.setText("" + ds.getButtonTextX());
		textfield_text_y.setText("" + ds.getButtonTextY());
		textfield_button_x.setText("" + ds.getButtonX());
		textfield_button_y.setText("" + ds.getButtonY());
		button_image.setValue(ds.getImage());
		textfield_image_width.setText("" + ds.getImageWidth());
		textfield_image_height.setText("" + ds.getImageHeight());
		checkbox_keep_aspect.setSelected(ds.getIsDefaultSize());
		textfield_help.setText(ds.getHelpText());
		button_help.setValue(ds.getHelpStyle());
		button_difficulty.setValue(ds.getDifficulty());
		setCurrentImageAspectRatio();
	}

	@Override
	protected int getItemCount() {
		return DataManager.getDataCampaign().getCampaignScenarios();
	}

	@Override
	protected void setDisabledValues() {
		// Not used
	}

	@Override
	protected void setTitle() {
		String tab_num = ControllerHelper.getNumericTabLabel(getCurrentTabIndex());
		slide_title.setText("Scenario "+tab_num+" Button");
	}

	@Override
	protected void setTabName(Tab tab, int index) {
		String tab_num = ControllerHelper.getNumericTabLabel(index);
		tab.setText(tab_num);
	}

	private void setCurrentImageAspectRatio() {
		ControllerHelper.setCurrentImageAspectRatio(getWrapper(), textfield_image_width, textfield_image_height);
	}

	private void setImageDefaultSize() {
		String name = button_image.getValue();
		ScenarioButton sb = getButton();
		if (sb == null || name == null) return;

		int w = (int) sb.getImageOriginalWidth(name);
		int h = (int) sb.getImageOriginalHeight(name);
		getWrapper().setAspectRatio(w, h);
		textfield_image_width.setText("" + w);
		textfield_image_height.setText("" + h);
		ignoreWidthHeightListener = true;
	}

	private void addHelpButtonEventHandler(ImageView button, String text) {
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> textfield_help.insertText(textfield_help.getCaretPosition(), text));
	}

	private void applyImageSize(boolean isWidth, boolean useAspectIfPossible) {
		boolean useAspect = !ignoreWidthHeightListener && checkbox_keep_aspect.isSelected();
		int val = parse(isWidth ? textfield_image_width : textfield_image_height);
		if (useAspect && useAspectIfPossible) {
			if (isWidth) getWrapper().setElementWidthWithAspect(val);
			else getWrapper().setElementHeightWithAspect(val);
		}
		else {
			if (isWidth) getWrapper().setElementWidth(val);
			else getWrapper().setElementHeight(val);
		}
		ignoreWidthHeightListener = false;
	}

	private ScenarioButton getButton() { return getPreview().getButton(getCurrentTabIndex()); }
	private PreviewElement getWrapper() { return getPreview().getButtonWrapper(getCurrentTabIndex()); }
	private PreviewScenarios getPreview() { return DataManager.getPreviewScenarios(); }
	private int parse(TextField tf) { return ParseUtil.parseInt(tf.getText()); }
}
