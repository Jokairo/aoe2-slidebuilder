package slidebuilder.data;

import slidebuilder.enums.SceneEnum;
import slidebuilder.previews.PreviewScenarios;
import slidebuilder.previews.PreviewSlideshow;
import slidebuilder.previews.PreviewSlideshowLive;

public class DataManager {
	
	//Previews
	private static PreviewSlideshow previewSlideshow;
	private static PreviewScenarios previewScenarios;

	private static PreviewSlideshowLive previewSlideshowLive;
	
	//All user placed data
	private static DataCampaign dataCampaign = new DataCampaign();
	
	//File locations
	private static DataFolderLocation dataFolderLocation = new DataFolderLocation();
	
	public static int globalTabIndex = 0;
	public static SceneEnum currentScene = null;
	
	/*
	 * Getters
	 */
	
	public static PreviewSlideshow getPreviewSlideshow() {
		return previewSlideshow;
	}
	
	public static PreviewScenarios getPreviewScenarios() {
		return previewScenarios;
	}

	public static PreviewSlideshowLive getPreviewSlideshowLive() {
		return previewSlideshowLive;
	}
	
	public static DataCampaign getDataCampaign() {
		return dataCampaign;
	}
	
	public static DataFolderLocation getDataFolderLocation() {
		return dataFolderLocation;
	}

	public static void createPreviews() {
		previewSlideshow = new PreviewSlideshow();
		previewScenarios = new PreviewScenarios();
		previewSlideshowLive = new PreviewSlideshowLive();
	}
	
	/*
	 * Setters
	 */
	
	//Used when loading project file/creating new project
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
		if (getPreviewSlideshowLive().isOpen()) return;

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

	public static void openPreviewSlideshowLive() {
		// Close slideshow preview so you cant edit it
		getPreviewSlideshow().closeWindow();
		//Open slideshow preview
		getPreviewSlideshowLive().openWindow();

		//Update background if it doesn't exist (when preview is first opened and background hasn't been changed)
		if(getPreviewSlideshowLive().getBackgroundName() != null) return;

		//Get which slideshow currently editing
		int currentSlideTabIndex = globalTabIndex;

		if(currentSlideTabIndex >= getDataCampaign().getListSlideshow().size())
			currentSlideTabIndex = getDataCampaign().getListSlideshow().size() - 1;

		//Get saved current slideshow background and set it as preview background
		String selectedBg = getDataCampaign().getListSlideshow().get(currentSlideTabIndex).getBackground();
		getPreviewSlideshowLive().setBackground(selectedBg);
	}

	public static void closePreviews() {
		getPreviewSlideshow().closeWindow();
		getPreviewSlideshowLive().closeWindow();
		getPreviewScenarios().closeWindow();
	}
}
