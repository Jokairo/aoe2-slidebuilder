package slidebuilder.generator;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import slidebuilder.Main;
import slidebuilder.data.DataManager;
import slidebuilder.data.DataSlideshow;
import slidebuilder.data.DataSlideshowSlide;
import slidebuilder.resource.ResourceManager;
import slidebuilder.util.FileUtil;
import slidebuilder.util.Popup;

public class GeneratorSlideshow {
	public static String generateSlideshowJson() throws IOException {
		JsonFactory factory = new JsonFactory();
		StringWriter jsonObjectWriter = new StringWriter();
		JsonGenerator generator = factory.createGenerator(jsonObjectWriter);

		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
		generator.setPrettyPrinter(prettyPrinter); // pretty print JSON

		generator.writeStartObject();

		// Write credits
		generator.writeFieldName("_credits");
		generator.writeStartArray();
		String border = "---------------------------------------------------------------";
		generator.writeString(border);
		generator.writeString("Auto-generated using Slide Builder by Jokairo");
		generator.writeString(Main.APP_LINK);
		generator.writeString(border);
		generator.writeEndArray();
		
		generator.writeFieldName("Scenarios");
		generator.writeStartArray();
		
		String camName = DataManager.getDataCampaign().getCampaignName();
		int scenarios = DataManager.getDataCampaign().getCampaignScenarios() * 2; //Multiply by 2 because each scenario has 1 intro and 1 outro
		
		int num = 1;
		
		//Check if there are any custom slide backgrounds
		ArrayList<String> customSlideBgList = Generator.getUsedCustomSlideBackgrounds();
		
		System.out.println("Current slideshows: "+DataManager.getDataCampaign().getListSlideshow().size());
		for(int i=0; i < scenarios; i++) {
			
			//Skip if not initialised
			if(i >= DataManager.getDataCampaign().getListSlideshow().size()) continue;
			
			DataSlideshow dataSlideshow = DataManager.getDataCampaign().getListSlideshow().get(i);
			
			//if odd number it's intro, even it's outro
			String s = "";
			String s2 = "";
			boolean isIntro = true;
			
			//Intro
			if(i%2 == 0) {
				s = "IntroSequence";
				s2 = "intro";
				
				//Write scenario's start object if it's intro
				generator.writeStartObject();
			}
			//Outro
			else {
				s = "OutroSequence";
				s2 = "outro";
				
				isIntro = false;
			}
			
			//Write IntroSequence/OutroSequence
			generator.writeFieldName(s);
			//Write slide's startobject
			generator.writeStartObject();
			
			//If selected slide is disabled, skip it
			//If it's outro, still need to add scenario's endobject to json
			if(DataManager.getDataCampaign().getListSlideshow().get(i).getDisable()) {
				
				//Slide's endobject
				generator.writeEndObject();
				
				//Scenario's endobject
				if(!isIntro)
					generator.writeEndObject();
				continue;
			}
			
			//Get background name
			String bg = dataSlideshow.getBackground();
			String bg_path = "";

			//Custom background
			if(!ResourceManager.instance.isValidSlideBackgroundName(bg)) {
				//The path where the custom background will be saved and what the json file will use to retrieve it
				String fileName = "CustomSlideBackground"+customSlideBgList.indexOf(bg);
				bg_path = "textures/campaign/"+camName+"/" + fileName+".dds";
			}
			//available background
			else {
				bg_path = ResourceManager.instance.getResourceCampaignFromName(bg).getSlideBgJsonPath();
			}
			
			//Sound file name
			String sound = dataSlideshow.getAudioPath();
			String sound_path = "";
			//If not empty, check that the file exists, otherwise halt the process
			if (sound != null && !sound.isEmpty()) {
				if (!FileUtil.fileExists(sound)) {
					Popup.showError("File path in "+s2+" "+i+" doesn't exist. Either remove the file or choose another file.");
					return "";
				}
				Path temp_path = Paths.get(sound);
				String file_name = temp_path.getFileName().toString();
				
				//Remove extension
				int pos = file_name.lastIndexOf(".");
				if (pos > 0 && pos < (file_name.length() - 1)) {
					file_name = file_name.substring(0, pos);
				}

				sound_path = file_name;
			}
			
			generator.writeFieldName("SequenceItems");
			generator.writeStartArray();
			generator.writeStartObject();
			generator.writeStringField("Type", "SlideShow");
			
			generator.writeFieldName("Data");
			generator.writeStartObject();
			generator.writeStringField("SlideBackgroundImage", bg_path);
			generator.writeStringField("SlideImageSequence", "textures/campaign/"+camName+"/"+num+"/"+s2+"/%d.png");
			generator.writeStringField("Sound", sound_path);
			generator.writeFieldName("Slides");
			
			generator.writeStartArray();
			
			int slides = dataSlideshow.getSlides();
			System.out.println("Current slides: "+slides);
			System.out.println("Current List slides: "+dataSlideshow.getListSlides().size());
			//Go through all the individual slides on attached to this scenario intro/outro
			for(int j=0; j < slides; j++) {

				//Skip if not initialised
				if(j >= dataSlideshow.getListSlides().size()) continue;
				
				DataSlideshowSlide dataSlides = dataSlideshow.getListSlides().get(j);
				
				if(dataSlides.getText() == null) {
					String tab = s2+" "+num;
					tab = tab.toUpperCase();
					Popup.showError("Slides for '"+tab+"' have not been initialized. Press 'Edit Slides' for the tab '"+tab+"' to initialize the slides.");
					return "";
				}
				
				generator.writeStartObject();
				generator.writeNumberField("Duration", dataSlides.getDuration());
				generator.writeNumberField("TextFadeDuration", dataSlides.getDuration());
				generator.writeFieldName("ImageRect");
				generator.writeStartObject();
				generator.writeNumberField("x", dataSlides.getImageX());
				generator.writeNumberField("y", dataSlides.getImageY());
				generator.writeNumberField("width", dataSlides.getImageWidth());
				generator.writeNumberField("height", dataSlides.getImageHeight());
				generator.writeEndObject();
				generator.writeStringField("String", dataSlides.getText());
				generator.writeFieldName("TextRect");
				generator.writeStartObject();
				generator.writeNumberField("x", dataSlides.getTextX());
				generator.writeNumberField("y", dataSlides.getTextY());
				generator.writeNumberField("width", dataSlides.getTextWidth());
				generator.writeNumberField("height", dataSlides.getTextHeight());
				generator.writeEndObject();
				generator.writeEndObject();
			}
			
			generator.writeEndArray();
			generator.writeEndObject();
			
			generator.writeEndObject();
			generator.writeEndArray();
			
			//Slide's endobject
			generator.writeEndObject();
			
			//Write scenario's end object if it's outro
			if(!isIntro) {
				generator.writeEndObject();
			}

			if(i%2!=0) num++;
		}
		generator.writeEndArray();
		generator.writeEndObject();

		generator.close(); // to close the generator
		
		return jsonObjectWriter.toString();
	}
}
