package slidebuilder.components;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import slidebuilder.data.CustomImage;
import slidebuilder.data.DataManager;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.resource.ResourceManager;
import slidebuilder.util.ButtonColors;
import slidebuilder.util.ImageTypeUtil;

public class ButtonPreview extends ImageView {

	private final double size_percentage;
	
	private Image image_normal;
	private Image image_hover;
	
	private final ColorAdjust colorNormal = new ColorAdjust();
	private final ColorAdjust colorHover = new ColorAdjust();
	private final ColorAdjust colorPress = new ColorAdjust();
	
	private final ButtonPreviewHelpText helptextObject;
	
	private String help_text = "";
	private String help_style = "";
	private boolean isPressed = false;
	private boolean isHovered = false;
	
	public ButtonPreview(ButtonPreviewHelpText helptextObject, double size_percentage) {
		//Default image is the Alaric campaign icon
		String defaultIcon = ResourceManager.instance.getCampaignResourceList().get(0).getName();
		setButtonImage(defaultIcon);
		
		//Color values
		colorNormal.setBrightness(0);
		colorNormal.setContrast(0);
		colorNormal.setSaturation(0);
		
		colorHover.setBrightness(ButtonColors.PREVIEW_HOVER_BRIGHTNESS);
		colorHover.setContrast(ButtonColors.PREVIEW_HOVER_CONTRAST);
		colorHover.setSaturation(ButtonColors.PREVIEW_HOVER_SATURATION);
		
		colorPress.setBrightness(ButtonColors.PREVIEW_PRESS_BRIGHTNESS);
		colorPress.setContrast(ButtonColors.PREVIEW_PRESS_CONTRAST);
		colorPress.setSaturation(ButtonColors.FILE_PRESS_SATURATION);
		
		this.helptextObject = helptextObject;
		this.size_percentage = size_percentage;

		setMouseTransparent(true); // Allow clicking on wrapper
	}
	
	public void setHelpText(String text) {
		help_text = text;
	}
	
	public void setHelpStyle(String he) {
		help_style = he;
	}
	
	public void setButtonImage(String name) {
		
		//Custom image
		if(!ResourceManager.instance.isValidCampaignButtonName(name)) {
			CustomImage ci = DataManager.getDataCampaign().getCustomImageData().getCustomImage(CreatorEnum.ICON, name);
			if(ci != null && ci.getImage() != null) {
				image_normal = ci.getImage();
			}
		}
		//available background
		else {
			image_normal = ResourceManager.instance.getResourceCampaignFromName(name).getButtonImage();
		}
		
		//Place new image as default
		setImage(image_normal);
		
		//Create hover image that will be used when cursor enters the image
		createImageHover(image_normal);
	}

	public void onHover(boolean b) {
		if (isHovered == b) return;
		isHovered = b;

		if (b) {
			changeImageType(1);
			helptextObject.showHelpText(help_text, help_style);
		}
		else {
			helptextObject.disableHelpText();
			onPress(false);
			changeImageType(0);
		}
	}
	
	public void onPress(boolean b) {
		if (isPressed == b) return;

		isPressed = b;

		// Move button downwards 3 pixels when it's pressed, when released move it back to original location
		if (b) {
			setTranslateY(getTranslateY() + 3 * size_percentage);
			changeImageType(2);
		}
		else {
			setTranslateY(getTranslateY() - 3 * size_percentage);
			changeImageType(isHovered ? 1 : 0);
		}
	}
	
	private void createImageHover(Image i) {
		image_hover = ImageTypeUtil.createImageHover(i);
	}

	
	private void changeImageType(int i) {
		if (i == 0) {
			setImage(image_normal);
			setEffect(colorNormal);
		}
		else if (i == 1) {
			setImage(image_hover);
			setEffect(colorHover);
		}
		else {
			setImage(image_normal);
			setEffect(colorPress);
		}
	}

}
