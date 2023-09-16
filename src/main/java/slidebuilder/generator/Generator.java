package slidebuilder.generator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import ddsutil.DDSUtil;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.concurrent.Task;
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
import slidebuilder.util.*;
import util.ImageUtils;

public class Generator {
	
	//Path where the json files and aoe2campaign file will be stored
	public final static String CAM_PATH = "resources/_common/campaign/";
	public final static String AUDIO_PATH = "resources/_common/drs/sounds/";

	private final static ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper();
	private final static ReadOnlyStringWrapper progressMessage = new ReadOnlyStringWrapper();

	private static int currentProgress = 0;
	private static int maxProgress = 0;

	private static void incrementProgress() {
		currentProgress += 1;
		progress.set((double)currentProgress/maxProgress);
	}

	public static Task<Void> getTask(String dest_path) {

		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {

				progress.getReadOnlyProperty().addListener((obs, oldProgress, newProgress) ->  {
					updateProgress(newProgress.doubleValue(), 1);
				});
				progressMessage.getReadOnlyProperty().addListener((obs, oldMessage, newMessage) ->  {
					updateMessage(newMessage);
				});

				progress.set(0.01);
				progressMessage.set("Export started");

				//Max value for progress bar is 2 json files + slide bgs + slide images + menu bg + menu buttons + audio files
				maxProgress = 2 + getUsedCustomSlideBackgrounds().size() + DataManager.getDataCampaign().getListSlideshow().size();
				boolean menuDisabled = DataManager.getDataCampaign().getCampaignMenuDisabled();
				if(!menuDisabled) {
					maxProgress += 1 + getUsedCustomButtons().size();
				}
				for(DataSlideshow ds : DataManager.getDataCampaign().getListSlideshow()) {
					String slide_audio_path = ds.getAudioPath();
					if (slide_audio_path != null && !slide_audio_path.isEmpty()) {
						maxProgress += 1;
					}
				}

				currentProgress = 0;

				//Wait 1 second before executing so that the Export window will properly open
				Thread.sleep(1 * 1000);

				generateFolder(dest_path);
				progress.set(1);
				progressMessage.set("Finished exporting!");
				return null;
			}
		};
	}

	private static void generateFolder(String dest_path) throws IOException, InterruptedException, URISyntaxException {
		//User defined name for the campaign
		String camName = DataManager.getDataCampaign().getCampaignName();

		//Path where the images will be stored
		String widget_path = "widgetui/textures/campaign/"+camName+"/";

		progressMessage.set("Generating Json files...");

		//Create json files first
		String json = GeneratorSlideshow.generateSlideshowJson();
		String json_layout = null;

		incrementProgress();

		boolean menuDisabled = DataManager.getDataCampaign().getCampaignMenuDisabled();

		//Don't create layout json if it's disabled
		if(!menuDisabled)
			json_layout = GeneratorLayout.generateLayoutJson();

		incrementProgress();

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

		String message = "Creating slide backgrounds ";
		int max = customSlideBgList.size();

		for(int i=0; i < max; i++) {
			String bgName = customSlideBgList.get(i);
			String fileName = "CustomSlideBackground"+i;

			progressMessage.set(message + i+"/"+max);

			//Write new DDS to widget folder
			String dds_path = dest_folder+fileName+".dds";
			writeBackground(bgName, dds_path, CreatorEnum.SLIDE_BG);

			incrementProgress();
		}
	}
	
	private static void writeCustomLayoutBackground(String dest_folder) {
		String bg_name = DataManager.getDataCampaign().getCampaignMenuBackground();
		
		//Is custom background
		if(!ResourceManager.instance.isValidCampaignBackgroundName(bg_name)) {

			String message = "Creating campaign menu background";
			progressMessage.set(message);

			//Write new DDS to widget folder
			String dds_path = dest_folder+"CustomCampaignBackground.dds";
			writeBackground(bg_name, dds_path, CreatorEnum.CAMPAIGN_BG);

			incrementProgress();
		}
	}
	
	
	private static void writeCustomLayoutButtons(String dest_folder) {
		ArrayList<String> customButtonList = getUsedCustomButtons();

		String message = "Creating campaign map button images ";
		int max = customButtonList.size();

		for(int i=0; i < max; i++) {
			String button_name = customButtonList.get(i);

			progressMessage.set(message + i+"/"+max);

			//Write new images to campaign folder
			CustomImage customButton = DataManager.getDataCampaign().getCustomImageData().getCustomImage(CreatorEnum.ICON, button_name);

			//Get the original size image from path
			Image image = new Image("file:///"+customButton.getPath());

			/*
				We are currently in another thread and need to switch back to GUI thread
				because createImageFile() method uses GUI components to create the image files
			 */

			UpdateUIFromOtherThread.call(() -> {
				//Get the index of the button from the saved custom button list
				//This index will be used for naming the button
				int buttonIndex = customButtonList.indexOf(button_name);

				String file_path1 = dest_folder + "Button" + buttonIndex + "Normal.png";
				String file_path2 = dest_folder + "Button" + buttonIndex + "Pressed.png";
				String file_path3 = dest_folder + "Button" + buttonIndex + "Hover.png";
				ImageTypeUtil.createImageFile(image, 0, file_path1);
				ImageTypeUtil.createImageFile(image, 1, file_path2);
				ImageTypeUtil.createImageFile(image, 2, file_path3);
			});
			incrementProgress();
		}
	}
	
	private static void writeCustomSlideImages(String dest_folder) throws IOException {
		//Determines which slideshow it is (Slideshow 1, Slideshow 2)
		int num = 1;
		
		//Determines if it's intro or outro (Slideshow 1 intro, Slideshow 1 outro)
		int i = 0;

		String message = "Creating slide images ";
		int max = DataManager.getDataCampaign().getListSlideshow().size();
		
		for(DataSlideshow ds : DataManager.getDataCampaign().getListSlideshow()) {
			String s = "intro";
			if(i%2!=0) {
				s = "outro";
			}

			progressMessage.set(message + i+"/"+max);

			//The name of the image file and also the order number
			int slideNum = 1;
			
			for(DataSlideshowSlide dss : ds.getListSlides()) {
				String imageName = dss.getImagePath();

				File target = new File(dest_folder+num+"/"+s+"/"+slideNum+".png");

				//Create empty image if user hasn't selected one
				if(imageName == null || imageName.equals("") || imageName.equalsIgnoreCase("None")) {
					InputStream source = Generator.class.getResourceAsStream("/images/empty.png");
					FileUtil.copyFile(source, target);
				}
				else {
					String source_path = DataManager.getDataCampaign().getCustomImageData().getCustomImage(CreatorEnum.SLIDE_IMAGE, imageName).getPath();
					File source = new File(source_path);
					FileUtil.copyFile(source, target);
				}
				
				slideNum++;
			}
			incrementProgress();

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
			progressMessage.set("Converting audio files");
			convert_path = WWiseConverter.convert(path_list, dest_folder);
		}

		if (convert_path != null) {
			File output = new File(convert_path + "/Windows");
			System.out.println(output);//
			File[] contents = output.listFiles();
			System.out.println(contents);//

			String message = "Converting audio files ";
			int max = contents.length;

			if (contents != null) {
				for(int i=0; i < contents.length; i++) {

					progressMessage.set(message + i+"/"+max);

					//Copy from temp location to zip
					String wem_path = contents[i].getAbsolutePath();
					String wem_file = contents[i].getName();

					//If not WEM file continue
					int length = wem_path.length();
					if(!wem_path.substring(length-3, length).equals("wem")) {
						Files.deleteIfExists(Paths.get(wem_path));
						incrementProgress();
						continue;
					}

					File source = new File(wem_path);
					File target = new File(dest_folder+"/"+wem_file);

					System.out.println("Source path: "+source.getAbsolutePath());
					System.out.println("Target path: "+target.getAbsolutePath());

					FileUtil.copyFile(source, target);

					//Delete temp WEM file from desktop
					Files.deleteIfExists(Paths.get(wem_path));

					incrementProgress();
				}
			}
			//Delete temp folder from desktop
			Files.deleteIfExists(Paths.get(convert_path+"/Windows"));
		}
	}

	public static void cancelExport(String path) {

		progress.set(0.0);
		progressMessage.set("Cancelling...");

		//Delete all files and folders that were created before user pressed cancel
		try {
			//Wait 1 second so user will see that the task got cancelled
			Thread.sleep(1 * 1000);

			File dir = new File(path);
			deleteFolder(dir);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void deleteFolder(File dir) {

		//Cant delete if it doesn't exist
		if(!dir.exists()) return;

		for (File file: dir.listFiles()) {
			if (file.isDirectory())
				deleteFolder(file);
			file.delete();
		}
		dir.delete();
	}
}
