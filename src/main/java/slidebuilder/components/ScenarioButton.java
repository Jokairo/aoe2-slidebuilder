package slidebuilder.components;

import slidebuilder.data.DataManager;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.resource.ResourceManager;

public class ScenarioButton {
	
	private final double size_percentage = 0.25; //Buttons are displayed in 25% of the real size
	
	private ButtonPreview button;
	private ButtonPreviewLabel buttonLabel;
	
	public ScenarioButton(ButtonPreviewHelpText helpObject) {

		button = new ButtonPreview(helpObject, size_percentage);
		buttonLabel = new ButtonPreviewLabel(button, size_percentage);
		
		setButtonX(0);
		setButtonY(0);
		
		setImageWidth((int)button.getImage().getWidth());
		setImageHeight((int)button.getImage().getHeight());
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
	
	public void setButtonX(int i) {
		button.setTranslateX(i*size_percentage);
		buttonLabel.setLabelCoordinates();
	}
	
	public void setButtonY(int i) {
		button.setTranslateY(i*size_percentage);
		buttonLabel.setLabelCoordinates();
	}
	
	public double getButtonX() {
		return button.getButtonX();
	}
	
	public double getButtonY() {
		return button.getButtonY();
	}
	
	public void setImageWidth(int i) {
		button.setFitWidth(i*size_percentage);
		buttonLabel.setLabelCoordinates();
	}
	
	public void setImageHeight(int i) {
		button.setFitHeight(i*size_percentage);
		buttonLabel.setLabelCoordinates();
	}
	
	public void setTextX(int i) {
		buttonLabel.setLabelX(i*size_percentage);
		buttonLabel.setLabelCoordinates();
	}
	
	public void setTextY(int i) {
		buttonLabel.setLabelY(i*size_percentage);
		buttonLabel.setLabelCoordinates();
	}
	
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
