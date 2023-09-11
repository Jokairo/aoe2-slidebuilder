package slidebuilder.data;

import java.io.Serializable;
import java.util.ArrayList;

public class DataCampaign implements Serializable {

	private static final long serialVersionUID = 1L;
	private String campaign_name;
	private int campaign_scenarios;
	private String campaign_file_path;
	
	private String campaign_menu_title;
	private String campaign_menu_background;
	private boolean campaign_menu_disabled;
	
	//Data for campaign menu / slideshow tabs
	private ArrayList<DataSlideshow> listSlideshow = new ArrayList<>();
	private ArrayList<DataScenarios> listScenarios = new ArrayList<>();
	
	//Data for custom images
	private CustomImageData customImageData = new CustomImageData();
	
	public String getCampaignName() {
		return campaign_name;
	}
	
	public int getCampaignScenarios() {
		return campaign_scenarios;
	}
	
	public String getCampaignFilePath() {
		return campaign_file_path;
	}
	
	public String getCampaignMenuTitle() {
		return campaign_menu_title;
	}
	
	public String getCampaignMenuBackground() {
		return campaign_menu_background;
	}
	
	public boolean getCampaignMenuDisabled() {
		return campaign_menu_disabled;
	}
	
	public void saveCampaignValues(String name, int scenarios, String path) {
		campaign_name = name;
		campaign_scenarios = scenarios;
		campaign_file_path = path;
	}
	
	public void saveCampaignMenuValues(String title, String background, boolean disabled) {
		campaign_menu_title = title;
		campaign_menu_background = background;
		campaign_menu_disabled = disabled;
	}
	
	public ArrayList<DataSlideshow> getListSlideshow() {
		return listSlideshow;
	}
	
	public ArrayList<DataScenarios> getListScenarios() {
		return listScenarios;
	}
	
	public CustomImageData getCustomImageData() {
		return customImageData;
	}
}
