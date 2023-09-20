package slidebuilder.generator;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import slidebuilder.Main;
import slidebuilder.data.DataManager;
import slidebuilder.data.DataScenarios;
import slidebuilder.resource.ResourceManager;

public class GeneratorLayout {
	
	public static String generateLayoutJson() throws IOException {
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
		
		String camName = DataManager.getDataCampaign().getCampaignName();
		int scenarios = DataManager.getDataCampaign().getCampaignScenarios();
		
		//Get background name for the campaign map layout
		String bg = DataManager.getDataCampaign().getCampaignMenuBackground();
		String bg_path = "";
		boolean isCustomCampaign = false;
		
		//Get layout title
		String titleName = DataManager.getDataCampaign().getCampaignMenuTitle();
		
		//Check if its user created background or one from the game
		//Custom background
		if(!ResourceManager.instance.isValidCampaignBackgroundName(bg)) {
			bg_path = "CustomCampaignBackground";
			isCustomCampaign = true;
		}
		//available background
		else {
			bg_path = ResourceManager.instance.getResourceCampaignFromName(bg).getLayoutBgJsonPath();
		}
		
		//Check if there are any custom button images
		ArrayList<String> customButtonList = Generator.getUsedCustomButtons();

		//Add used custom buttons and custom background to the JSON file
		if(!customButtonList.isEmpty() || isCustomCampaign) {
			//Write json entry
			generator.writeFieldName("UserMaterials");
			generator.writeStartArray();
			
			//Add custom background
			if(isCustomCampaign) {
				generator.writeStartObject();
				generator.writeStringField("Material", bg_path);
				generator.writeStringField("FileName", bg_path+".dds");
				generator.writeEndObject();
			}
			
			//Add custom buttons
			for(int i=0; i < customButtonList.size(); i++) {
				String buttonId = "Button"+i;
				
				//Write custom buttons to json file
				//Normal button
				generator.writeStartObject();
				generator.writeStringField("Material", buttonId+"Normal");
				generator.writeStringField("FileName", buttonId+"Normal.png");
				generator.writeEndObject();
				//Hover button
				generator.writeStartObject();
				generator.writeStringField("Material", buttonId+"Hover");
				generator.writeStringField("FileName", buttonId+"Hover.png");
				generator.writeEndObject();
				//Pressed button
				generator.writeStartObject();
				generator.writeStringField("Material", buttonId+"Pressed");
				generator.writeStringField("FileName", buttonId+"Pressed.png");
				generator.writeEndObject();
			}
			generator.writeEndArray();
		}
		
		generator.writeStringField("BackgroundMaterial", bg_path);
		generator.writeFieldName("Widgets");
		generator.writeStartArray();
		generator.writeStartObject();
		
		//Title
		generator.writeFieldName("Widget");
		
		generator.writeStartObject();
			generator.writeStringField("Type", "Label");
			generator.writeStringField("Name", "TitleLabel");
			generator.writeFieldName("ViewPort");
			generator.writeStartObject();
				generator.writeNumberField("xorigin", 1920);
				generator.writeNumberField("yorigin", 110);
				generator.writeNumberField("width", 1000);
				generator.writeNumberField("height", 100);
				generator.writeStringField("alignment", "CentreCentre");
			generator.writeEndObject();
			generator.writeNumberField("ZPlane", 2);
			generator.writeFieldName("StateMaterials");
			generator.writeStartObject();
				generator.writeFieldName("StateNormal");
				generator.writeStartObject();
					generator.writeFieldName("Font");
					generator.writeStartObject();
						generator.writeNumberField("FontIndex", 0);
						generator.writeNumberField("PointSize", 70);
						generator.writeStringField("Style", "Normal");
						generator.writeFieldName("TextColor");
						generator.writeStartObject();
							generator.writeNumberField("r", 57);
							generator.writeNumberField("g", 28);
							generator.writeNumberField("b", 27);
							generator.writeNumberField("a", 255);
						generator.writeEndObject();
					generator.writeEndObject();
				generator.writeEndObject();
			generator.writeEndObject();
			generator.writeStringField("Text", titleName);
			generator.writeStringField("TextAnchor", "CentreCentre");
			generator.writeFieldName("ChildWidgets");
			generator.writeStartArray();
			generator.writeEndArray();
		generator.writeEndObject();
		
		generator.writeEndObject();
		
		/*
		 * Map overlay, add only if overlay other than NONE
		 * TODO
		 */
		
		/*
		generator.writeStartObject();
		generator.writeFieldName("Widget");
		generator.writeStartObject();
			generator.writeStringField("Type", "Base");
			generator.writeStringField("Name", "BackgroundAsia"); //korvaa
			generator.writeFieldName("ViewPort");
			generator.writeStartObject();
				generator.writeNumberField("xorigin", 1920);
				generator.writeNumberField("yorigin", 1080);
				generator.writeNumberField("width", 5210);
				generator.writeNumberField("height", 2160);
				generator.writeStringField("alignment", "CentreCentre");
			generator.writeEndObject();
			generator.writeNumberField("ZPlane", 1);
		generator.writeEndObject();
		generator.writeEndObject();
		*/
		
		//Go through all the individual buttons
		for(int i=0; i < scenarios; i++) {
			
			//Skip if not initialised
			if(i >= DataManager.getDataCampaign().getListScenarios().size()) continue;
			
			DataScenarios dataScenarios = DataManager.getDataCampaign().getListScenarios().get(i);
			
			String buttonName = dataScenarios.getImage();
			String button_normal_path = "";
			String button_hover_path = "";
			String button_pressed_path = "";

			//Custom button
			if(customButtonList.contains(buttonName)) {
				String buttonId = "Button"+customButtonList.indexOf(buttonName);
				button_normal_path = buttonId+"Normal";
				button_hover_path = buttonId+"Hover";
				button_pressed_path = buttonId+"Pressed";
			}
			//available background
			else {
				String button_path = ResourceManager.instance.getResourceCampaignFromName(buttonName).getButtonJsonPath();
				button_normal_path = button_path+"Normal";
				button_hover_path = button_path+"Hover";
				button_pressed_path = button_path+"Pressed";
			}
			
			//Difficulty
			String selectedDifficulty = dataScenarios.getDifficulty();
			int difficultyLevel = ResourceManager.instance.getResourceDifficultyFromName(selectedDifficulty).getJsonIndex();
			
			//Help style
			int helpStyle = 0;
			String selectedHelpStyle = dataScenarios.getHelpStyle();
			if(ResourceManager.instance.getResourceExpansionFromName(selectedHelpStyle) != null)
				helpStyle = ResourceManager.instance.getResourceExpansionFromName(selectedHelpStyle).getJsonIndex();
			
			generator.writeStartObject();
			generator.writeFieldName("Widget");
			generator.writeStartObject();
			
			//Different buttonType depending if help style is enabled or not
			//If buttonType is Button, help style will not be shown
			//If buttonType is CampaignButton, help style will be shown even if DataValue is missing from the json file, hence it's required to be changed to Button
			String buttonType = "CampaignButton";
			if(helpStyle == 0) buttonType = "Button";
			
			generator.writeStringField("Type", buttonType);
			generator.writeStringField("Name", "ScenarioButton"+(i+1));
			generator.writeFieldName("ViewPort");
			generator.writeStartObject();
				generator.writeNumberField("xorigin", dataScenarios.getButtonX());
				generator.writeNumberField("yorigin", dataScenarios.getButtonY());
				generator.writeNumberField("width", dataScenarios.getImageWidth());
				generator.writeNumberField("height", dataScenarios.getImageHeight());
				generator.writeStringField("alignment", "TopLeft");
			generator.writeEndObject();
			generator.writeFieldName("Image");
			generator.writeStartObject();
				generator.writeNumberField("xorigin", 0);
				generator.writeNumberField("yorigin", 0);
				generator.writeNumberField("width", dataScenarios.getImageWidth());
				generator.writeNumberField("height", dataScenarios.getImageHeight());
				generator.writeStringField("alignment", "TopLeft");
			generator.writeEndObject();
			generator.writeFieldName("StateMaterials");
			generator.writeStartObject();
				generator.writeFieldName("StateNormal");
				generator.writeStartObject();
					generator.writeStringField("Material", button_normal_path);
				generator.writeEndObject();
				generator.writeFieldName("StateHover");
				generator.writeStartObject();
					generator.writeStringField("Material", button_hover_path);
				generator.writeEndObject();
				generator.writeFieldName("StatePressed");
				generator.writeStartObject();
					generator.writeStringField("Material", button_pressed_path);
				generator.writeEndObject();
			generator.writeEndObject();
			generator.writeFieldName("ChildWidgets");
			generator.writeStartArray();
			generator.writeStartObject();
				generator.writeFieldName("Widget");
				generator.writeStartObject();
					generator.writeStringField("Type", "CampaignTitle");
					generator.writeStringField("Name", "CampaignButtonLabel");
					generator.writeFieldName("ViewPort");
					generator.writeStartObject();
						generator.writeNumberField("xorigin", dataScenarios.getButtonTextX());
						generator.writeNumberField("yorigin", dataScenarios.getButtonTextY());
						generator.writeNumberField("width", 700);
						generator.writeNumberField("height", 100);
						generator.writeStringField("alignment", "TopLeft");
					generator.writeEndObject();
					generator.writeStringField("ClippingOverride", "Background");
					generator.writeNumberField("Level", difficultyLevel);
					generator.writeFieldName("StateMaterials");
					generator.writeStartObject();
						generator.writeFieldName("StateNormal");
						generator.writeStartObject();
							generator.writeFieldName("Font");
							generator.writeStartObject();
								generator.writeNumberField("FontIndex", 0);
								generator.writeNumberField("PointSize", 45);
								generator.writeStringField("Style", "Normal");
								generator.writeFieldName("TextColor");
								generator.writeStartObject();
									generator.writeNumberField("r", 255);
									generator.writeNumberField("g", 255);
									generator.writeNumberField("b", 255);
									generator.writeNumberField("a", 255);
								generator.writeEndObject();
							generator.writeEndObject();
						generator.writeEndObject();
					generator.writeEndObject();
					generator.writeStringField("Text", dataScenarios.getButtonText());
					generator.writeStringField("TextAnchor", "TopLeft");
					generator.writeFieldName("ChildWidgets");
					generator.writeStartArray();
					generator.writeEndArray();
				generator.writeEndObject();
			generator.writeEndObject();
			generator.writeEndArray();
			
			//dont put if help text style NONE
			if(helpStyle > 0) {
				generator.writeFieldName("DataValue");
				generator.writeStartArray();
				generator.writeNumber(helpStyle);
				generator.writeNumber(0);
				generator.writeEndArray();
			}
			generator.writeStringField("Help", dataScenarios.getHelpText());
			
			generator.writeEndObject();
			generator.writeEndObject();
		}
		//
		generator.writeEndArray();
		generator.writeEndObject();

		generator.close(); // to close the generator
		
		return jsonObjectWriter.toString();
	}
}
