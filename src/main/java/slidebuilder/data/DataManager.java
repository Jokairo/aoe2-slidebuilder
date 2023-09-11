package slidebuilder.data;

import java.io.File;

import slidebuilder.controllers.interfaces.Controller;
import slidebuilder.previews.PreviewScenarios;
import slidebuilder.previews.PreviewSlideshow;

public class DataManager {
	
	//Previews
	private static PreviewSlideshow previewSlideshow = new PreviewSlideshow();
	private static PreviewScenarios previewScenarios = new PreviewScenarios();
	
	//All user placed data
	private static DataCampaign dataCampaign = new DataCampaign();
	
	//File locations
	private static DataFolderLocation dataFolderLocation = new DataFolderLocation();
	
	public static int globalTabIndex = 0;
	public static Controller currentController = null;
	
	/*
	 * Getters
	 */
	
	public static PreviewSlideshow getPreviewSlideshow() {
		return previewSlideshow;
	}
	
	public static PreviewScenarios getPreviewScenarios() {
		return previewScenarios;
	}
	
	public static DataCampaign getDataCampaign() {
		return dataCampaign;
	}
	
	public static DataFolderLocation getDataFolderLocation() {
		return dataFolderLocation;
	}
	
	/*
	 * Setters
	 */
	
	//Used when loading project file
	public static void setDataCampaign(DataCampaign dataCampaign) {
		DataManager.dataCampaign = dataCampaign;
		CustomImageComboBox.initCustomImageNameLists();
		dataCampaign.getCustomImageData().createImages();
	}
	
	/*
	 * Opening previews
	 */
	
	public static void openPreviewSlideshow() {
		//Open slideshow preview
		getPreviewSlideshow().openWindow();
		
		//Get saved current slideshow background 
		int currentSlideTabIndex = globalTabIndex;
		String selectedBg = getDataCampaign().getListSlideshow().get(currentSlideTabIndex).getBackground();
		
		//Get the first slide's saved image
		String selectedImage = getDataCampaign().getListSlideshow().get(currentSlideTabIndex).getListSlides().get(0).getImagePath();
		
		//Set the background as the preview background
		if(selectedBg != null) {
			getPreviewSlideshow().setBackground(selectedBg);
		}
		
		//Set the image as the preview image, no need to check null since null = no image
		getPreviewSlideshow().setImage(selectedImage);
	}
	
	public static void openPreviewCampaignMenu() {
		//Open campaign map preview
		getPreviewScenarios().openWindow();
		
		//Get saved current map background
		String selectedBg = getDataCampaign().getCampaignMenuBackground();
		
		//Set it as the preview background
		if(selectedBg != null) {
			getPreviewScenarios().setBackground(selectedBg);
		}
	}
}
