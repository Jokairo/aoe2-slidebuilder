package slidebuilder.previews;

import java.util.ArrayList;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import slidebuilder.components.ButtonPreviewHelpText;
import slidebuilder.components.ScenarioButton;
import slidebuilder.components.ScenarioHeader;
import slidebuilder.data.DataManager;

public class PreviewScenarios extends PreviewInterface {
	
	private String currentBg;
	private ScenarioHeader header;
	
	private ArrayList<ScenarioButton> buttons = new ArrayList<>();
	private ButtonPreviewHelpText helptext;
	
	public PreviewScenarios() {
		
		setIsSlidePreview(false);
		
		header = new ScenarioHeader();
		
		helptext = new ButtonPreviewHelpText();
	}
	
	public void createButtons() {
		
		int size = DataManager.getDataCampaign().getCampaignScenarios();
		
		for(int i=0; i < buttons.size(); i++) {
			buttons.get(i).setVisible(false);
		}
		
		for(int i=0; i < size; i++) {
			//Button exists, show it
			if(i < buttons.size() && buttons.get(i) != null) {
				buttons.get(i).setVisible(true);
			}
			//Button doesnt exist, create it
			else {
				ScenarioButton sb = new ScenarioButton(helptext);
				buttons.add(sb);
				
				//Update button image if data exists for it (loaded a project file)
				if(DataManager.getDataCampaign().getListScenarios().size() > i) {
					if(DataManager.getDataCampaign().getListScenarios().get(i).getImage() != null) {
						sb.getButtonImage().setButtonImage(DataManager.getDataCampaign().getListScenarios().get(i).getImage());
					}
				}
			}
		}
		
		//Update buttons to be shown at the preview
		addButtonsToRoot();
	}

	private void addButtonsToRoot() {
		
		//No need to add if preview is closed
		if(!isOpen()) return;
		
		for(ScenarioButton sb : buttons) {
			
			//Add button to the root only if it doesn't exists
			if(getRoot().getChildren().contains(sb.getButtonImage())) continue;
			
			getRoot().getChildren().add(sb.getButtonImage());
			getRoot().getChildren().add(sb.getButtonLabel().getLabelArea());
		}
	}
	
	public ScenarioButton getButton(int i) {
		return buttons.get(i);
	}
	
	public void setHeaderText(String text) {
		header.setTitle(text);
	}
	
	@Override
	public void addStuffToRoot() {
		
		VBox.setVgrow(getRoot(), Priority.ALWAYS);
		
		setBackgroundDefaultSize();
		setBackground();
		
		getRoot().getChildren().add(getBackground());

		getRoot().getChildren().add(header.getHeader());
		
		getRoot().getChildren().add(helptext.getTextAreaComponent());

		getRoot().getChildren().add(helptext.getTextComponent());
		
		addButtonsToRoot();
	}
}
