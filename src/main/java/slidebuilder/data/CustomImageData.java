package slidebuilder.data;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import slidebuilder.enums.CreatorEnum;

public class CustomImageData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//Custom images
	private ArrayList<CustomImage> listCustomSlideshowBackground = new ArrayList<>();
	private ArrayList<CustomImage> listCustomCampaignBackground = new ArrayList<>();
	private ArrayList<CustomImage> listCustomCampaignButton = new ArrayList<>();
	private ArrayList<CustomImage> listCustomSlideshowImage = new ArrayList<>();
	
	/*
	 * List getters
	 */
	
	public ArrayList<CustomImage> getListCustomSlideshowBackground() {
		return listCustomSlideshowBackground;
	}
	
	public ArrayList<CustomImage> getListCustomCampaignBackground() {
		return listCustomCampaignBackground;
	}
	
	public ArrayList<CustomImage> getListCustomCampaignButton() {
		return listCustomCampaignButton;
	}
	
	public ArrayList<CustomImage> getListCustomSlideshowImage() {
		return listCustomSlideshowImage;
	}
	
	/*
	 * Custom image getter
	 */
	
	public CustomImage getCustomImage(CreatorEnum ce, String name) {
		ObservableList<String> ol = CustomImageComboBox.getCustomImageNameList(ce);;
		ArrayList<CustomImage> al;
		
		if(ce == CreatorEnum.SLIDE_BG) {
			al = listCustomSlideshowBackground;
		} else if(ce == CreatorEnum.CAMPAIGN_BG) {
			al = listCustomCampaignBackground;
		} else if(ce == CreatorEnum.SLIDE_IMAGE) {
			al = listCustomSlideshowImage;
		} else {
			al = listCustomCampaignButton;
		}
		
		int index = ol.indexOf(name);
		if (index == -1) return null;
		
		for(int i=0; i < al.size(); i++) {
			if (al.get(i).getName().equals(name))
				return al.get(i);
		}
		
		return null;
	}
	
	/*
	 * Saving a custom image
	 */
	
	public void saveCustomImage(CreatorEnum ce, CustomImage ci) {
		
		CustomImageComboBox.getCustomImageNameList(ce).add(ci.getName());
		
		if(ce == CreatorEnum.SLIDE_BG) {
			listCustomSlideshowBackground.add(ci);
		} else if(ce == CreatorEnum.CAMPAIGN_BG) {
			listCustomCampaignBackground.add(ci);
		} else if(ce == CreatorEnum.SLIDE_IMAGE) {
			listCustomSlideshowImage.add(ci);
		} else {
			listCustomCampaignButton.add(ci);
		}
	}
	
	/*
	 * Removing a custom image
	 */
	
	public void removeCustomImage(CreatorEnum ce, CustomImage ci) {
		
		CustomImageComboBox.getCustomImageNameList(ce).remove(ci.getName());
		
		if(ce == CreatorEnum.SLIDE_BG) {
			listCustomSlideshowBackground.remove(ci);
		} else if(ce == CreatorEnum.CAMPAIGN_BG) {
			listCustomCampaignBackground.remove(ci);
		} else if(ce == CreatorEnum.SLIDE_IMAGE) {
			listCustomSlideshowImage.remove(ci);
		} else {
			listCustomCampaignButton.remove(ci);
		}
	}
	
	/*
	 * Create all images
	 */
	
	public void createImages() {
		//Create all images
		//Custom layout background
		for(CustomImage ci : getListCustomCampaignBackground()) {
			ci.setImage();
			//Add names manually as they are not serializable
			CustomImageComboBox.getCustomImageNameList(CreatorEnum.CAMPAIGN_BG).add(ci.getName());
		}
		//Custom layout button image
		for(CustomImage ci : getListCustomCampaignButton()) {
			ci.setImage();
			//Add names manually as they are not serializable
			CustomImageComboBox.getCustomImageNameList(CreatorEnum.ICON).add(ci.getName());
		}
		//Custom slideshow background
		for(CustomImage ci : getListCustomSlideshowBackground()) {
			ci.setImage();
			//Add names manually as they are not serializable
			CustomImageComboBox.getCustomImageNameList(CreatorEnum.SLIDE_BG).add(ci.getName());
		}
		//Custom slideshow image
		for(CustomImage ci : getListCustomSlideshowImage()) {
			ci.setImage();
			//Add names manually as they are not serializable
			CustomImageComboBox.getCustomImageNameList(CreatorEnum.SLIDE_IMAGE).add(ci.getName());
		}
	}
}
