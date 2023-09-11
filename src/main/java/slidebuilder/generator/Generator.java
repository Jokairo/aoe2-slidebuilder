package slidebuilder.generator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import ddsutil.DDSUtil;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import jogl.DDSImage;
import slidebuilder.data.CustomImage;
import slidebuilder.data.DataManager;
import slidebuilder.data.DataScenarios;
import slidebuilder.data.DataSlideshow;
import slidebuilder.data.DataSlideshowSlide;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.resource.ResourceManager;
import slidebuilder.util.BackgroundUtil;
import slidebuilder.util.FileUtil;
import slidebuilder.util.ImageTypeUtil;
import slidebuilder.util.Popup;
import slidebuilder.util.WWiseConverter;
import util.ImageUtils;


public class Generator {
	
	//Path where the json files and aoe2campaign file will be stored
	public final static String CAM_PATH = "resources/_common/campaign/";
	public final static String AUDIO_PATH = "resources/_common/drs/sounds/";
	
	public static void generateFolder(String dest_path) throws IOException, InterruptedException, URISyntaxException {
		//User defined name for the campaign
		String camName = DataManager.getDataCampaign().getCampaignName();
		
		//Path where the images will be stored
		String widget_path = "widgetui/textures/campaign/"+camName+"/";
		
		//Create json files first
		String json = GeneratorSlideshow.generateSlideshowJson();
		String json_layout = null;
		
		boolean menuDisabled = DataManager.getDataCampaign().getCampaignMenuDisabled();
		
		//Don't create layout json if it's disabled
		if(!menuDisabled)
			json_layout = GeneratorLayout.generateLayoutJson();
		
		//Error, stop method.
		if(json == null || json.equals("")) {
			return;
		}
		
		//Create the folders
		File directory = new File(dest_path+"/"+CAM_PATH);
		File directory_widget = new File(dest_path+"/"+widget_path);
		directory.mkdirs();
		directory_widget.mkdirs();
		
		//Write slideshow json to a file
		PrintWriter writer = new PrintWriter(dest_path+"/"+CAM_PATH+camName+".json", "UTF-8");
		writer.println(json);
		writer.close();
		
		//Write layout json to a file if it's not disabled
		if (!menuDisabled) {
			writer = new PrintWriter(dest_path+"/"+CAM_PATH+camName+"_layout.json", "UTF-8");
			writer.println(json_layout);
			writer.close();
		}
		
		//Copy campaign file if user has selected it
		if(DataManager.getDataCampaign().getCampaignFilePath() != null && !DataManager.getDataCampaign().getCampaignFilePath().isEmpty()) {
			File campaignFile = new File(DataManager.getDataCampaign().getCampaignFilePath());
			File outputFile = new File(directory.getAbsolutePath()+"/"+camName+".aoe2campaign");
			FileUtil.copyFile(campaignFile, outputFile);
		}
		
		//Create user content if needed
		
		//Create slideshow backgrounds if needed
		writeCustomSlideBackgrounds(dest_path+"/"+widget_path);
		
		//Create images
		writeCustomSlideImages(dest_path+"/"+widget_path);
		
		//Layout content does not need to be created if layout is disabled
		if (!menuDisabled) {
		
			//Create campaign background if needed (there can only be 1)
			writeCustomLayoutBackground(dest_path+"/"+CAM_PATH);
			
			//Create campaign menu buttons if needed
			writeCustomLayoutButtons(dest_path+"/"+CAM_PATH);
		}
		
		//Create and Convert audio files if needed
		writeCustomAudio(dest_path+"/"+AUDIO_PATH);
		
		Popup.showSuccess("Folder successfully created!");
	}
	
	private static void writeBackground(String bg_name, String dest_path, CreatorEnum ce) {
		CustomImage ci = DataManager.getDataCampaign().getCustomImageData().getCustomImage(ce, bg_name);
		
		int new_width = BackgroundUtil.getBackgroundWidth((int)ci.getWidth(), (int)ci.getHeight());
		int divider = BackgroundUtil.getBackgroundDivider((int)ci.getWidth(), (int)ci.getHeight());
		
		int w = new_width;
		int h = 540;
		
		if(divider != 0) {
			h = h*divider;
			w = new_width * divider;
		}
		
		String path = "file:///"+ci.getPath();
		Image img = new Image(path, w, h, false, false);
		createDDSFile(img, w, h, dest_path);
	}
	
	private static void createDDSFile(Image image, int w, int h, String dest_path) {
		File outputFile = new File(dest_path);
	    //Image resized = makeThumbnail(image, w, h);
	    BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
		bImage = ImageUtils.convert(bImage, BufferedImage.TYPE_4BYTE_ABGR);
		
	    try {
	    	DDSUtil.write(outputFile, bImage, DDSImage.D3DFMT_DXT1, false);
	    } catch (IOException e) {
	    	throw new RuntimeException(e);
	    }
	}
	
	private static Image makeThumbnail(Image original, int width, int height) {
	    ImageView i = new ImageView(original);
	    i.setFitHeight(height); i.setFitWidth(width);

	    return i.snapshot(new SnapshotParameters(), new WritableImage(width, height));
	}
	
	public static ArrayList<String> getUsedCustomButtons() {
		//Check if there are any custom button images
		ArrayList<String> customButtonList = new ArrayList<>();
		
		for(DataScenarios ds : DataManager.getDataCampaign().getListScenarios()) {
			String buttonName = ds.getImage();
			
			//Add all unique custom button names to a list
			if(!ResourceManager.instance.isValidCampaignButtonName(buttonName)) {
				if (!customButtonList.contains(buttonName)) {
					customButtonList.add(buttonName);
				}
			}
		}
		
		return customButtonList;
	}
	
	public static ArrayList<String> getUsedCustomSlideBackgrounds() {
		//Check if there are any custom slideshow backgrounds
		ArrayList<String> customSlideBgList = new ArrayList<>();
		
		for(DataSlideshow ds : DataManager.getDataCampaign().getListSlideshow()) {
			String bgName = ds.getBackground();
			
			//Add all unique custom button names to a list
			if(!ResourceManager.instance.isValidSlideBackgroundName(bgName)) {
				if (!customSlideBgList.contains(bgName)) {
					customSlideBgList.add(bgName);
				}
			}
		}

		return customSlideBgList;
	}
	
	private static void writeCustomSlideBackgrounds(String dest_folder) {
		ArrayList<String> customSlideBgList = getUsedCustomSlideBackgrounds();
		
		for(int i=0; i < customSlideBgList.size(); i++) {
			String bgName = customSlideBgList.get(i);
			String fileName = "CustomSlideBackground"+i;

			//Write new DDS to widget folder
			String dds_path = dest_folder+fileName+".dds";
			writeBackground(bgName, dds_path, CreatorEnum.SLIDE_BG);
		}
	}
	
	private static void writeCustomLayoutBackground(String dest_folder) {
		String bg_name = DataManager.getDataCampaign().getCampaignMenuBackground();
		
		//Is custom background
		if(!ResourceManager.instance.isValidCampaignBackgroundName(bg_name)) {
			
			//Write new DDS to widget folder
			String dds_path = dest_folder+"CustomCampaignBackground.dds";
			writeBackground(bg_name, dds_path, CreatorEnum.CAMPAIGN_BG);
		}
	}
	
	
	private static void writeCustomLayoutButtons(String dest_folder) {
		ArrayList<String> customButtonList = getUsedCustomButtons();
		for(int i=0; i < customButtonList.size(); i++) {
			String button_name = customButtonList.get(i);
				
			//Write new images to campaign folder
			CustomImage customButton = DataManager.getDataCampaign().getCustomImageData().getCustomImage(CreatorEnum.ICON, button_name);
			
			//Get the original size image from path
			Image image = new Image("file:///"+customButton.getPath());
			
			//Get the index of the button from the saved custom button list
			//This index will be used for naming the button
			int buttonIndex = customButtonList.indexOf(button_name);
			
			String file_path1 = dest_folder+"Button"+buttonIndex+"Normal.png";
			String file_path2 = dest_folder+"Button"+buttonIndex+"Pressed.png";
			String file_path3 = dest_folder+"Button"+buttonIndex+"Hover.png";
			ImageTypeUtil.createImageFile(image, 0, file_path1);
			ImageTypeUtil.createImageFile(image, 1, file_path2);
			ImageTypeUtil.createImageFile(image, 2, file_path3);
		}
	}
	
	private static void writeCustomSlideImages(String dest_folder) throws IOException {
		//Determines which slideshow it is (Slideshow 1, Slideshow 2)
		int num = 1;
		
		//Determines if it's intro or outro (Slideshow 1 intro, Slideshow 1 outro)
		int i = 0;
		
		for(DataSlideshow ds : DataManager.getDataCampaign().getListSlideshow()) {
			String s = "intro";
			if(i%2!=0) {
				s = "outro";
			}

			//The name of the image file and also the order number
			int slideNum = 1;
			
			for(DataSlideshowSlide dss : ds.getListSlides()) {
				String imageName = dss.getImagePath();
				String source_path;
				
				
				//Create empty image if user hasn't selected one
				if(imageName == null || imageName.equals("") || imageName.equalsIgnoreCase("None")) {
					source_path = Generator.class.getResource("/images/empty.png").getPath();
				}
				else source_path = DataManager.getDataCampaign().getCustomImageData().getCustomImage(CreatorEnum.SLIDE_IMAGE, imageName).getPath();

				File source = new File(source_path);
				File target = new File(dest_folder+num+"/"+s+"/"+slideNum+".png");
				
				FileUtil.copyFile(source, target);
				
				slideNum++;
			}
			if(i%2!=0) num++;
			
			i++;
		}
	}
	
	private static void writeCustomAudio(String dest_folder) throws IOException, InterruptedException {
		ArrayList<String> path_list = new ArrayList<>();
		
		//Add all files that are needed to be converted
		for(DataSlideshow ds : DataManager.getDataCampaign().getListSlideshow()) {
			String slide_audio_path = ds.getAudioPath();
			if (slide_audio_path != null && !slide_audio_path.isEmpty()) {
				path_list.add(slide_audio_path);
				System.out.println("Audio path: "+slide_audio_path);
			}
		}

		String convert_path = null;
		
		if(!path_list.isEmpty()) {
			convert_path = WWiseConverter.convert(path_list, dest_folder);
		}
		
		if (convert_path != null) {
			File output = new File(convert_path + "/Windows");
			System.out.println(output);//
			File[] contents = output.listFiles();
			System.out.println(contents);//
			if (contents != null) {
				for(int i=0; i < contents.length; i++) {
					//Copy from temp location to zip
					String wem_path = contents[i].getAbsolutePath();
					String wem_file = contents[i].getName();
					
					//If not WEM file continue
					int length = wem_path.length();
					if(!wem_path.substring(length-3, length).equals("wem")) {
						Files.deleteIfExists(Paths.get(wem_path));
						continue;
					}
					
					File source = new File(wem_path);
					File target = new File(dest_folder+"/"+wem_file);
					
					System.out.println("Source path: "+source.getAbsolutePath());
					System.out.println("Target path: "+target.getAbsolutePath());
					
					FileUtil.copyFile(source, target);
					
					//Delete temp WEM file from desktop
					Files.deleteIfExists(Paths.get(wem_path));
				}
			}
			//Delete temp folder from desktop
			Files.deleteIfExists(Paths.get(convert_path+"/Windows"));
		}
	}
}
