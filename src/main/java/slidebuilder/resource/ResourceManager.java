package slidebuilder.resource;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import slidebuilder.enums.CreatorEnum;

public class ResourceManager {
	
	public static ResourceManager instance;
	private ArrayList<ResourceCampaign> resourceCampaignList;
	private ArrayList<ResourceExpansion> resourceExpansionList;
	private ArrayList<ResourceDifficulty> resourceDifficultyList;
	
	private HashMap<String, ResourceCampaign> resourceCampaignMap;
	private HashMap<String, ResourceExpansion> resourceExpansionMap;
	private HashMap<String, ResourceDifficulty> resourceDifficultyMap;
	
	public ResourceManager() {
		
		instance = this;
		
		resourceCampaignList = new ArrayList<>();
		resourceExpansionList = new ArrayList<>();
		resourceDifficultyList = new ArrayList<>();
		
		resourceCampaignMap = new HashMap<>();
		resourceExpansionMap = new HashMap<>();
		resourceDifficultyMap = new HashMap<>();
		
		createCampaignResource("Alaric", "button_alaric.png", "CampaignIcon10Alaric", "alaric", "CampaignBackground10Alaric", "alaric_background", "fcam1");
		createCampaignResource("Almeida", "button_francisco.png", "CampaignIcon18Francisco", "almeida", "CampaignBackground18Francisco", "almeida_background", "acam3");
		createCampaignResource("Art of War", "button_art.png", "CampaignIcon00Challenges", null, null, "challenges_background", "cam0");
		createCampaignResource("Attila", "button_attila.png", "CampaignIcon06Attila", "attila", "CampaignBackground06Attila", "attila_background", "xcam1");
		createCampaignResource("Barbarossa", "button_barbarossa.png", "CampaignIcon05Barbarossa", "barbarossa", "CampaignBackground05Barbarossa", "barbarossa_background", "cam5");
		createCampaignResource("Bari", "button_bari.png", "CampaignIcon12Bari", "bari", "CampaignBackground12Bari", "bari_background", "fcam3");
		createCampaignResource("Bayinnaung", "button_bayinnaung.png", "CampaignIcon22Bayinnaung", "bayinnaung", "CampaignBackground22Bayinnaung", "bayinnaung_background", "rcam3");
		createCampaignResource("Default", "button_default.png", "CampaignIcon27Custom", "cam_overview_bg", "CampaignSelectBackground", null, "campaign_selection");
		createCampaignResource("Dracula", "button_dracula.png", "CampaignIcon11Dracula", "dracula", "CampaignBackground11Dracula", "dracula_background", "fcam2");
		createCampaignResource("El Cid", "button_elcid.png", "CampaignIcon07ElCid", "elcid", "CampaignBackground07ElCid", "elcid_background", "xcam2");
		createCampaignResource("Gajah Mada", "button_gajahmada.png", "CampaignIcon20GajahMada", "gajahmada", "CampaignBackground20GajahMada", "gajahmada_background", "rcam1");
		createCampaignResource("Genghis Khan", "button_genghis.png", "CampaignIcon04GenghisKhan", "genghiskhan", "CampaignBackground04GenghisKhan", "genghis_khan_background", "cam4");
		createCampaignResource("Historical Battles", "button_historical.png", "CampaignIcon00HistoricalBattles", "historical_battles", "CampaignBackgroundHistorical", "forgotten_background", "fcam7");
		createCampaignResource("Historical Agincourt", "button_historical_agincourt.png", "ScenarioIcon05Agincourt", null, null, null, "campaign_seletion");
		createCampaignResource("Historical Bapheus", "button_historical_bapheus.png", "ScenarioIcon15Bapheus", null, null, null, "campaign_seletion");
		createCampaignResource("Historical Bukhara", "button_historical_bukhara.png", "ScenarioIcon09Bukhara", null, null, null, "campaign_seletion");
		createCampaignResource("Historical Cyprus", "button_historical_cyprus.png", "ScenarioIcon14Cyprus", null, null, null, "campaign_seletion");
		createCampaignResource("Historical Dos Pilas", "button_historical_dospilas.png", "ScenarioIcon10DosPilas", null, null, null, "campaign_seletion");
		createCampaignResource("Historical Hastings", "button_historical_hastings.png", "ScenarioIcon03Hastings", null, null, null, "campaign_seletion");
		createCampaignResource("Historical Honfoglalas", "button_historical_honfoglalas.png", "ScenarioIcon12Honfoglalas", null, null, null, "campaign_seletion");
		createCampaignResource("Historical Kurikara", "button_historical_kurikara.png", "ScenarioIcon13Kurikara", null, null, null, "campaign_seletion");
		createCampaignResource("Historical Kyoto", "button_historical_kyoto.png", "ScenarioIcon07Kyoto", null, null, null, "campaign_seletion");
		createCampaignResource("Historical Lake Poyang", "button_historical_lake_poyang.png", "ScenarioIcon16LakePoyang", null, null, null, "campaign_seletion");
		createCampaignResource("Historical Lepanto", "button_historical_lepanto.png", "ScenarioIcon06Lepanto", null, null, null, "campaign_seletion");
		createCampaignResource("Historical Manzikert", "button_historical_manzikert.png", "ScenarioIcon04Manzikert", null, null, null, "campaign_seletion");
		createCampaignResource("Historical Noryang", "button_historical_noryang.png", "ScenarioIcon08NoryangPoint", null, null, null, "campaign_seletion");
		createCampaignResource("Historical Tours", "button_historical_tours.png", "ScenarioIcon01Tours", null, null, null, "campaign_seletion");
		createCampaignResource("Historical Vinlandsaga", "button_historical_vinlandsaga.png", "ScenarioIcon02Vinlandsaga", null, null, null, "campaign_seletion");
		createCampaignResource("Historical York", "button_historical_york.png", "ScenarioIcon11York", null, null, null, "campaign_seletion");
		createCampaignResource("Ivaylo", "button_ivaylo.png", "CampaignIcon25Ivaylo", "ivaylo", "CampaignBackground25Ivaylo", "ivaylo_background", "kcam2");
		createCampaignResource("Joan of Arc", "button_joan.png", "CampaignIcon02Joan", "joanofarc", "CampaignBackground02Joan", "joan_background", "cam2");
		createCampaignResource("Kotyan Khan", "button_kotyan.png", "CampaignIcon26Kotyan", "kotyan", "CampaignBackground26Kotyan", "kotyan_background", "kcam3");
		createCampaignResource("Le Loi", "button_leloi.png", "CampaignIcon23LeLoi", "leloi", "CampaignBackground23LeLoi", "leloi_background", "rcam4");
		createCampaignResource("Montezuma", "button_montezuma.png", "CampaignIcon08Montezuma", "montezuma", "CampaignBackground08Montezuma", "montezuma_background", "xcam3");
		createCampaignResource("Pachacuti", "button_pachacuti.png", "CampaignIcon14Pachacuti", "pachacuti", "CampaignBackground14Pachacuti", "pachacuti_background", "fcam5");
		createCampaignResource("Prithviraj", "button_prithviraj.png", "CampaignIcon15Prithviraj", "prithviraj", "CampaignBackground15Prithviraj", "prithviraj_background", "fcam6");
		createCampaignResource("Saladin", "button_saladin.png", "CampaignIcon03Saladin", "saladin", "CampaignBackground03Saladin", "saladin_background", "cam3");
		createCampaignResource("Sforza", "button_sforza.png", "CampaignIcon13Sforza", "sforza", "CampaignBackground13Sforza", "sforza_background", "fcam4");
		createCampaignResource("Sundjata", "button_sundjata.png", "CampaignIcon17Sundjata", "sundjata", "CampaignBackground17Sundjata", "sundjata_background", "acam2");
		createCampaignResource("Suryavarman", "button_suryavarman.png", "CampaignIcon21Suryavarman", "suryavarman", "CampaignBackground21Suryavarman", "suryavarman_background", "rcam2");
		createCampaignResource("Tamerlane", "button_tamerlane.png", "CampaignIcon24Tamerlane", "tamerlane", "CampaignBackground24Tamerlane", "tamerlane_background", "kcam1");
		createCampaignResource("Tariq", "button_tariq.png", "CampaignIcon16TariqIbnZiyad", "tariq", "CampaignBackground16TariqIbnZiyad", "tariq_background", "acam1");
		createCampaignResource("Wallace", "button_wallace.png", "CampaignIcon01WilliamWallace", "wallace", "CampaignBackground01WilliamWallace", "wallace_background", "cam1");
		createCampaignResource("Yodit", "button_yodit.png", "CampaignIcon19Yodit", "yodit", "CampaignBackground19Yodit", "yodit_background", "acam4");
		
		createExpansionResource("Age of Kings", "The Age of Kings Campaign", Color.WHITE, "#cc0000", createExpansionImage("help_icon1.png"), 0);
		createExpansionResource("Conquerors", "The Conquerors Campaign", Color.WHITE, "#0044cc", createExpansionImage("help_icon2.png"), 1);
		createExpansionResource("Forgotten", "The Forgotten Campaign", Color.WHITE, "#009900", createExpansionImage("help_icon3.png"), 2);
		createExpansionResource("African Kingdoms", "African Kingdoms Campaign", Color.BLACK, "#b9b500", createExpansionImage("help_icon4.png"), 3);
		createExpansionResource("Rise of Rajas", "Rise of the Rajas Campaign", Color.BLACK, "#21a690", createExpansionImage("help_icon5.png"), 4);
		createExpansionResource("Last Khans", "The Last Khans Campaign", Color.WHITE, "#6a4080", createExpansionImage("help_icon6.png"), 5);
		createExpansionResource("Definitive Edition", "Definitive Edition Campaign", Color.WHITE, "#2b2b2b", createExpansionImage("help_icon7.png"), 6);
		createExpansionResource("Conquerors and Forgotten", "The Conquerors & The Forgotten Campaign", Color.WHITE, "#009973", createExpansionImage("help_icon8.png"), 7);
		createExpansionResource("Lords of the West", "Lords of the West Campaign", Color.WHITE, "#bc6e00", createExpansionImage("help_icon9.png"), 8);
		createExpansionResource("Dawn of the Dukes", "Dawn of the Dukes Campaign", Color.BLACK, "#eae6e0", createExpansionImage("help_icon10.png"), 10);
		
		createDifficultyResource("None", null, 0);
		createDifficultyResource("Easy", createDifficultyImage("difficulty_easy.png"), 1);
		createDifficultyResource("Moderate", createDifficultyImage("difficulty_medium.png"), 2);
		createDifficultyResource("Hard", createDifficultyImage("difficulty_hard.png"), 3);
	}

	private void createCampaignResource(String name, String buttonPath, String buttonJsonPath, String layoutPath, String layoutBgJsonPath, String slidePath, String folder ) {
		ResourceCampaign re = new ResourceCampaign(name, buttonPath, buttonJsonPath, layoutPath, layoutBgJsonPath, slidePath, folder);
		resourceCampaignList.add(re);
		resourceCampaignMap.put(name, re);
	}
	
	public ArrayList<ResourceCampaign> getCampaignResourceList() {
		return resourceCampaignList;
	}
	
	public ResourceCampaign getResourceCampaignFromName(String name) {
		return resourceCampaignMap.get(name);
	}
	
	private void createExpansionResource(String id, String text, Color textColor, String areaColor, Image imagePath, int jsonIndex) {
		ResourceExpansion re = new ResourceExpansion(id, text, textColor, areaColor, imagePath, jsonIndex);
		resourceExpansionList.add(re);
		resourceExpansionMap.put(id, re);
	}
	
	public ArrayList<ResourceExpansion> getExpansionResourceList() {
		return resourceExpansionList;
	}
	
	public ResourceExpansion getResourceExpansionFromName(String name) {
		return resourceExpansionMap.get(name);
	}
	
	private void createDifficultyResource(String name, Image image, int jsonIndex) {
		ResourceDifficulty re = new ResourceDifficulty(name, image, jsonIndex);
		resourceDifficultyList.add(re);
		resourceDifficultyMap.put(name, re);
	}
	
	public ArrayList<ResourceDifficulty> getDifficultyResourceList() {
		return resourceDifficultyList;
	}
	
	public ResourceDifficulty getResourceDifficultyFromName(String name) {
		return resourceDifficultyMap.get(name);
	}
	
	private Image createExpansionImage(String name) {
		return createImage(name, 16);
	}
	
	private Image createDifficultyImage(String name) {
		return createImage(name, 0);	
	}
	
	private Image createImage(String name, int size) {
		if (name != null && !name.isEmpty()) {
			Image img;
			//Default size of the image
			if (size == 0)
				img = new Image(getClass().getResource("/images/"+name).toString());
			//Custom size
			else
				img = new Image(getClass().getResource("/images/"+name).toString(), size, size, false, false);
			return img;
		}
		return null;	
	}
	
	//Check if Slide Bg with the name exists
	public boolean isValidSlideBackgroundName(String s) {
		ResourceCampaign res = resourceCampaignMap.get(s);
		//Return null if no such resource exists or that resource has no slide bg
		if (res == null || res.getSlideBgImage() == null) {
			return false;
		}
		return true;
	}
	
	//Check if Campaign Bg with the name exists
	public boolean isValidCampaignBackgroundName(String s) {
		ResourceCampaign res = resourceCampaignMap.get(s);
		//Return null if no such resource exists or that resource has no campaign bg
		if (res == null || res.getLayoutBgImage() == null) {
			return false;
		}
		return true;
	}
	
	//Check if Campaign Button with the name exists
	public boolean isValidCampaignButtonName(String s) {
		ResourceCampaign res = resourceCampaignMap.get(s);
		//Return null if no such resource exists or that resource has no campaign button
		if (res == null || res.getButtonImage() == null) {
			return false;
		}
		return true;
	}

	// Default values for images, this value is used to set a combobox's default value
	// 1. when project is launched
	// 2. when user deletes user created image and a controller is using that image
	public String getDefaultResource(CreatorEnum ce) {
		switch (ce) {
			case SLIDE_BG:
				return resourceCampaignList.get(0).getName();
			case SLIDE_IMAGE:
				return "None";
			case CAMPAIGN_BG:
				return resourceCampaignMap.get("Default").getName();
			case ICON:
				return resourceCampaignList.get(0).getName();
		}
        return null;
    }
}
