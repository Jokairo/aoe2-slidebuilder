package slidebuilder.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.resource.ResourceCampaign;
import slidebuilder.resource.ResourceManager;

public class CustomImageComboBox {
	private static ObservableList<String> listCustomSlideshowBackgroundNames = FXCollections.observableArrayList();
	private static ObservableList<String> listCustomLayoutBackgroundNames = FXCollections.observableArrayList();
	private static ObservableList<String> listCustomLayoutButtonNames = FXCollections.observableArrayList();
	private static ObservableList<String> listCustomSlideshowImageNames = FXCollections.observableArrayList();
	
	public static void initCustomImageNameLists() {
		
		listCustomSlideshowBackgroundNames.clear();
		listCustomLayoutBackgroundNames.clear();
		listCustomLayoutButtonNames.clear();
		listCustomSlideshowImageNames.clear();
		
		//Add empty value to custom slide images
		listCustomSlideshowImageNames.add("None");
		
		int size = ResourceManager.instance.getCampaignResourceList().size();
		for(int i=0; i < size; i++) {
			ResourceCampaign res = ResourceManager.instance.getCampaignResourceList().get(i);
			
			//Layout background
			if(res.getLayoutBgImage() != null) {
				listCustomLayoutBackgroundNames.add(res.getName());
			}
			//Layout button
			if(res.getButtonImage() != null) {
				listCustomLayoutButtonNames.add(res.getName());
			}
			//Slide background
			if(res.getSlideBgImage() != null) {
				listCustomSlideshowBackgroundNames.add(res.getName());
			}
		}
	}
	
	public static ObservableList<String> getCustomImageNameList(CreatorEnum ce) {
		if(ce == CreatorEnum.CAMPAIGN_BG) return listCustomLayoutBackgroundNames;
		else if(ce == CreatorEnum.ICON) return listCustomLayoutButtonNames;
		else if(ce == CreatorEnum.SLIDE_BG) return listCustomSlideshowBackgroundNames;
		else return listCustomSlideshowImageNames;
	}
}
