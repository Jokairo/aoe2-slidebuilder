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
		dataCampaign.setUnsavedChanges(false);
	}
	
	/*
	 * Opening previews
	 */
	
	public static void openPreviewSlideshow() {
		//Open slideshow preview
		getPreviewSlideshow().openWindow();

		//Update background if it doesn't exist (when preview is first opened and background hasn't been changed)
		if(getPreviewSlideshow().getBackgroundName() != null) return;

		//Get which slideshow currently editing
		int currentSlideTabIndex = globalTabIndex;

		if(currentSlideTabIndex >= getDataCampaign().getListSlideshow().size())
			currentSlideTabIndex = getDataCampaign().getListSlideshow().size() - 1;
		
		//Get saved current slideshow background and set it as preview background
		String selectedBg = getDataCampaign().getListSlideshow().get(currentSlideTabIndex).getBackground();
		getPreviewSlideshow().setBackground(selectedBg);
	}
	
	public static void openPreviewCampaignMenu() {
		//Open campaign map preview
		getPreviewScenarios().openWindow();

		//Update background if it doesn't exist (when preview is first opened and background hasn't been changed)
		if(getPreviewScenarios().getBackgroundName() != null) return;

		//Get saved current map background and set it as preview background
		String selectedBg = getDataCampaign().getCampaignMenuBackground();
		getPreviewScenarios().setBackground(selectedBg);
	}
}
