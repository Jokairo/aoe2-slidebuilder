package slidebuilder.previews;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import slidebuilder.data.DataManager;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.resource.ResourceManager;
import slidebuilder.util.BackgroundUtil;

public abstract class PreviewInterface {

	private ImageView background = new ImageView();
	private Stage stage = new Stage();
	private Pane root;
	private String backgroundName;
	private boolean is_slide;
	
	protected abstract void addStuffToRoot();
	
	private void setBackgroundCustomSize(int width, int pos) {
		background.setFitWidth(width);
		background.setFitHeight(540);
		background.setTranslateX(pos);
	}
	
	private boolean isCustomBackground() {
		if(is_slide) {
			return (!ResourceManager.instance.isValidSlideBackgroundName(backgroundName) && getCustomBackground() != null);
		}
		else {
			return (!ResourceManager.instance.isValidCampaignBackgroundName(backgroundName) && getCustomBackground() != null);
		}
	}
	
	private Image getCustomBackground() {
		if(is_slide) {
			if(DataManager.getDataCampaign().getCustomImageData().getCustomImage(CreatorEnum.SLIDE_BG, backgroundName) == null) return null;
			return DataManager.getDataCampaign().getCustomImageData().getCustomImage(CreatorEnum.SLIDE_BG, backgroundName).getImage();
		}
		else {
			if(DataManager.getDataCampaign().getCustomImageData().getCustomImage(CreatorEnum.CAMPAIGN_BG, backgroundName) == null) return null;
			return DataManager.getDataCampaign().getCustomImageData().getCustomImage(CreatorEnum.CAMPAIGN_BG, backgroundName).getImage();
		}
	}
	
	private Image getAvailableBackground() {
		if (backgroundName == null) return null;
		
		if(is_slide) {
			return ResourceManager.instance.getResourceCampaignFromName(backgroundName).getSlideBgImage();
		}
		else {
			System.out.println("BG: "+backgroundName);
			return ResourceManager.instance.getResourceCampaignFromName(backgroundName).getLayoutBgImage();
		}
	}
	
	private void calculateNewSize(int image_width, int image_height) {
		//Get image width
		int new_width = BackgroundUtil.getBackgroundWidth(image_width, image_height);
		
		//Center image
		int change = (960 - new_width) / 2;
		
		setBackgroundCustomSize(new_width, change);

	}
	
	private String getTitle() {
		if(is_slide) {
			return "Slide Preview";
		}
		else {
			return "Campaign Menu Preview";
		}
	}
	
	protected Pane getRoot() {
		return root;
	}
	
	protected ImageView getBackground() {
		return background;
	}
	
	protected boolean isOpen() {
		return stage.isShowing();
	}
	
	protected void setIsSlidePreview(boolean b) {
		is_slide = b;
	}
	
	protected void setBackgroundDefaultSize() {
		background.setFitWidth(960);
		background.setFitHeight(540);
		background.setTranslateX(0);
	}
	
	public void setBackground() {
		//No need to update background if preview is not open
		if(!isOpen()) return;
		
		//Custom background
		if(isCustomBackground()) {
			background.setImage(getCustomBackground());
			
			int width = (int)background.getImage().getWidth();
			int height = (int)background.getImage().getHeight();
			
			calculateNewSize(width, height);
		}
		//available background
		else {
			setBackgroundDefaultSize();
			background.setImage(getAvailableBackground());
		}
	}
	
	public void setBackground(String bg) {
		backgroundName = bg;
		setBackground();
	}

	public String getBackgroundName() {
		return backgroundName;
	}
	
	public void openWindow() {
		if(isOpen()) {
			//Set focused if trying to open while already open
			stage.requestFocus();
			return;
		}

		root = new Pane();
		
		stage.show();
		
		addStuffToRoot();
		
		Scene scene = new Scene(root, 960, 540);
		
		stage.setTitle(getTitle());
		stage.setScene(scene);

		Scale scale = new Scale(1, 1);
		scale.xProperty().bind(scene.widthProperty().divide(960));
	    scale.yProperty().bind(scene.heightProperty().divide(540));
	    
	    scene.getRoot().getTransforms().add(scale);

		stage.setOnHiding(event -> System.out.println("Preview closed."));
	}

}