package slidebuilder.components;

import slidebuilder.data.DataManager;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.enums.PreviewEnums;
import slidebuilder.resource.ResourceManager;

public class ScenarioButton extends PreviewElementChild {
	
	private final double size_percentage = 0.25; //Buttons are displayed in 25% of the real size
	
	private final ButtonPreview button;
	private final ButtonPreviewLabel buttonLabel;
	
	public ScenarioButton(int elementIndex, ButtonPreviewHelpText helpObject) {
		super(PreviewEnums.Elements.BUTTON, elementIndex);

		button = new ButtonPreview(helpObject, size_percentage);
		buttonLabel = new ButtonPreviewLabel(button, size_percentage, elementIndex);

		addElementChildren(button);
		addElementChildren(buttonLabel.getLabelArea());
		addElementChildren(buttonLabel.getBorder());
	}

	@Override
	public void onInit() {
		getWrapperClass().setElementX(0);
		getWrapperClass().setElementY(0);
		getWrapperClass().setElementWidth((int)button.getImage().getWidth());
		getWrapperClass().setElementHeight((int)button.getImage().getHeight());
	}
	
	public void setHelpText(String text) {
		button.setHelpText(text);
	}
	
	public void setHelpStyle(String he) {
		button.setHelpStyle(he);
	}
	
	public ButtonPreview getButtonImage() {
		return button;
	}
	
	public ButtonPreviewLabel getButtonLabel() {
		return buttonLabel;
	}

	@Override
	public void setElementX(double i) {
		button.setTranslateX(i);
		buttonLabel.setLabelCoordinates();
	}

	@Override
	public void setElementY(double i) {
		button.setTranslateY(i);
		buttonLabel.setLabelCoordinates();
	}

	@Override
	public void setElementWidth(double i) {
		button.setFitWidth(i);
		buttonLabel.setLabelCoordinates();
	}

	@Override
	public void setElementHeight(double i) {
		button.setFitHeight(i);
		buttonLabel.setLabelCoordinates();
	}

	@Override
	public void onPress(boolean b) {
		button.onPress(b);
	}

	@Override
	public void onHover(boolean b) {
		button.onHover(b);
	}

	
	public void setTextX(int i) {
		buttonLabel.setLabelX(i*size_percentage);
		buttonLabel.setLabelCoordinates();
	}

	public void setTextY(int i) {
		buttonLabel.setLabelY(i*size_percentage);
		buttonLabel.setLabelCoordinates();
	}

	@Override
	public void setVisible(boolean b) {
		buttonLabel.setVisible(b);
		button.setVisible(b);
	}
	
	public double getImageOriginalWidth(String name) {
		//Normal button
		if(ResourceManager.instance.isValidCampaignButtonName(name)) {
			return button.getImage().getWidth();
		}
		//Custom button
		else {
			return DataManager.getDataCampaign().getCustomImageData().getCustomImage(CreatorEnum.ICON, name).getWidth();
		}
	}
	
	public double getImageOriginalHeight(String name) {
		//Normal button
		if(ResourceManager.instance.isValidCampaignButtonName(name)) {
			return button.getImage().getHeight();
		}
		//Custom button
		else {
			return DataManager.getDataCampaign().getCustomImageData().getCustomImage(CreatorEnum.ICON, name).getHeight();
		}
	}

}
