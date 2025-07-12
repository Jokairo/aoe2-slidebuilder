package slidebuilder.previews;

import java.util.ArrayList;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import slidebuilder.components.ButtonPreviewHelpText;
import slidebuilder.components.PreviewElement;
import slidebuilder.components.ScenarioButton;
import slidebuilder.components.ScenarioHeader;
import slidebuilder.data.DataManager;
import slidebuilder.data.DataScenarios;

public class PreviewScenarios extends PreviewInterface {
	private final ScenarioHeader header;
	private final ArrayList<PreviewElement> buttons = new ArrayList<>();
	private final ButtonPreviewHelpText helptext;
	
	public PreviewScenarios() {
		
		setIsSlidePreview(false);
		
		header = new ScenarioHeader();
		
		helptext = new ButtonPreviewHelpText();
	}

	public void deleteButtons() {
		for (PreviewElement el : buttons) {
			if(getRoot() == null || el == null) continue; //Ignore if preview hasn't been initialised yet
			if(!getRoot().getChildren().contains(el)) continue; //Remove button from the root only if it exists

			getRoot().getChildren().remove(el);
			getRoot().getChildren().removeAll(el.getChild().getElementChildren());
		}

		buttons.clear();
	}
	
	public void createButtons() {
		
		int size = DataManager.getDataCampaign().getCampaignScenarios();
		selectedElement = null;

		for(int i=0; i < buttons.size(); i++) {
			buttons.get(i).setHidden(true);
		}
		
		for(int i=0; i < size; i++) {
			//Button exists, show it
			if(i < buttons.size() && buttons.get(i) != null) {
				buttons.get(i).setHidden(false);
			}
			//Button doesnt exist, create it
			else {
				PreviewElement el = new PreviewElement();
				ScenarioButton sb = new ScenarioButton(i, helptext);
				el.setChild(sb);
				buttons.add(el);

				//Update button if data exists for it (loaded a project file)
				if(DataManager.getDataCampaign().getListScenarios().size() > i) {
					DataScenarios ds = DataManager.getDataCampaign().getListScenarios().get(i);
					if (ds != null) {
						el.setElementX(ds.getButtonX());
						el.setElementY(ds.getButtonY());

						sb.setTextX(ds.getButtonTextX());
						sb.setTextY(ds.getButtonTextY());
						sb.setHelpText(ds.getHelpText());
						sb.setHelpStyle(ds.getHelpStyle());

						el.setElementWidth(ds.getImageWidth());
						el.setElementHeight(ds.getImageHeight());

						sb.getButtonLabel().setText(ds.getButtonText());
						sb.getButtonLabel().setDifficulty(ds.getDifficulty());
						sb.getButtonImage().setButtonImage(ds.getImage());
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
		
		for(PreviewElement el : buttons) {
			//Add button to the root only if it doesn't exists
			if(getRoot().getChildren().contains(el)) continue;

			getRoot().getChildren().add(el);
			getRoot().getChildren().addAll(el.getChild().getElementChildren());
		}
	}
	
	public ScenarioButton getButton(int i) {
		if(i >= buttons.size() || i < 0) return null;
		return (ScenarioButton)buttons.get(i).getChild();
	}

	public PreviewElement getButtonWrapper(int i) {
		if(i >= buttons.size() || i < 0) return null;
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

	@Override
	public ArrayList<PreviewElement> getElements() {
		return buttons;
	}
}
