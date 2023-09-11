package slidebuilder.resource;

import javafx.scene.image.Image;

public class ResourceCampaign {
	private String name;
	private String buttonName;
	private String buttonJsonPath;
	private String layoutBgName;
	private String layoutBgJsonPath;
	private String slideBgName;
	private String folder;
	
	private Image buttonImage;
	private Image layoutBgImage;
	private Image slideBgImage;
	
	//Background size
	private final int width = 480;
	private final int height = 270;
	
	public ResourceCampaign(String name, String buttonName, String buttonJsonPath, String layoutBgName, String layoutBgJsonPath, String slideBgName, String folder) {
		this.name = name;
		this.buttonName = buttonName;
		this.buttonJsonPath = buttonJsonPath;
		this.layoutBgName = layoutBgName;
		this.layoutBgJsonPath = layoutBgJsonPath;
		this.slideBgName = slideBgName;
		this.folder = folder;
		
		createButtonImage();
		createLayoutBgImage();
		createSlideBgImage();
	}
	
	//Resource name that is shown in menus, eg. "Alaric"
	public String getName() {
		return name;
	}
	
	//Path to the corresponding button image found in the project file, eg. "button_alaric"
	public String getButtonName() {
		return buttonName;
	}
	
	//Path to the corresponding button image that the game will use in its JSON file, eg. "CampaignIcon10AlaricNormal"
	public String getButtonJsonPath() {
		return buttonJsonPath;
	}
	
	//Path to the corresponding background image that the Campaign Menu uses, found in the project file, eg. "alaric.jpg"
	public String getLayoutBgName() {
		if (layoutBgName == null) return null;
		return layoutBgName+".jpg";
	}
	
	//Path to the corresponding background image that the game will use in its JSON file for Campaign Menu, eg. textures/campaign/fcam1/alaric.dds
	public String getLayoutBgJsonPath() {
		return layoutBgJsonPath;
	}
	
	//Path to the corresponding background image that the Slideshow uses, found in the project file, eg. alaric_background.jpg
	public String getSlideBgName() {
		if (slideBgName == null) return null;
		return slideBgName+".jpg";
	}
	
	//Path to the corresponding background image that the game will use in its JSON file for Slideshows, eg. textures/campaign/fcam1/alaric_background.dds
	public String getSlideBgJsonPath() {
		if (slideBgName == null) return null;
		return "textures/campaign/"+folder+"/"+slideBgName+".dds";
	}
	
	//Folder name where the game resource is located, eg. "fcam1"
	public String getFolder() {
		return folder;
	}
	
	public Image getButtonImage() {
		return buttonImage;
	}
	
	public Image getLayoutBgImage() {
		return layoutBgImage;
	}
	
	public Image getSlideBgImage() {
		return slideBgImage;
	}
	
	private void createButtonImage() {
		if (getButtonName() == null || getButtonName().isEmpty()) return;
		buttonImage = new Image(getClass().getResource("/images/"+getButtonName()).toString());
	}
	private void createLayoutBgImage() {
		if (getLayoutBgName() == null || getLayoutBgName().isEmpty()) return;
		layoutBgImage = new Image(getClass().getResource("/images/"+getLayoutBgName()).toString(), width, height, false, false);
	}
	private void createSlideBgImage() {
		if (getSlideBgName() == null || getSlideBgName().isEmpty()) return;
		slideBgImage = new Image(getClass().getResource("/images/"+getSlideBgName()).toString(), width, height, false, false);
	}
}
