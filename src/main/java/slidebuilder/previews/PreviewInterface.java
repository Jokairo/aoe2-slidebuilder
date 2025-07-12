package slidebuilder.previews;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import slidebuilder.components.ButtonPreviewLabel;
import slidebuilder.components.PreviewElement;
import slidebuilder.components.ScenarioButton;
import slidebuilder.data.DataManager;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.enums.PreviewEnums;
import slidebuilder.enums.SceneEnum;
import slidebuilder.resource.ResourceManager;
import slidebuilder.util.BackgroundUtil;
import java.util.ArrayList;

public abstract class PreviewInterface {

	private final ImageView background = new ImageView();
	private final Stage stage = new Stage();
	private Pane root;
	private String backgroundName;
	private boolean is_slide;
	private boolean isEditable = true;

	protected PreviewElement selectedElement = null;
	protected PreviewEnums.Actions action = PreviewEnums.Actions.NONE;
	protected PreviewEnums.DragDirection dragDirection = null;
	protected double clickedX = 0.0;
	protected double clickedY = 0.0;
	private boolean isClick = false;
	private int slideshowIndex = -1;

	PreviewElementProperties buttonProperties = new PreviewElementProperties();
	PreviewElementProperties buttonLabelProperties = new PreviewElementProperties();
	PreviewElementProperties imageProperties = new PreviewElementProperties();
	PreviewElementProperties textProperties = new PreviewElementProperties();
	
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
			if (!isEditable) return "Slide Live Preview";
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
	
	public boolean isOpen() {
		return stage.isShowing();
	}
	
	protected void setIsSlidePreview(boolean b) {
		is_slide = b;
	}

	protected void setIsEditable(boolean b) {
		isEditable = b;
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

		scene.getRoot().setOnMouseMoved(e -> {
			onMouseMoved(e.getSceneX(), e.getSceneY());
		});

		scene.getRoot().setOnMouseClicked(e -> {
			onMouseClicked(e.getSceneX(), e.getSceneY());
		});

		scene.getRoot().setOnMousePressed(e -> {
			onMousePressed(e.getSceneX(), e.getSceneY());
		});

		scene.getRoot().setOnMouseReleased(e -> {
			onMouseReleased();
		});

		scene.getRoot().setOnMouseDragged(e -> {
			onMouseDragged(e.getSceneX(), e.getSceneY());
		});

		stage.setOnHiding(event -> onClose());
	}

	public void closeWindow() {
		onClose();
		stage.close();
	}

	public int getSceneWidth() {
		return (int)stage.getScene().getWidth();
	}

	public int getSceneHeight() {
		return (int)stage.getScene().getHeight();
	}

	public double getClickedX() {
		return clickedX;
	}

	public double getClickedY() {
		return clickedY;
	}
	public PreviewEnums.DragDirection getDragDirection() {
		return dragDirection;
	}

	public PreviewElementProperties getButtonProperties() {
		return buttonProperties;
	}

	public PreviewElementProperties getButtonLabelProperties() {
		return buttonLabelProperties;
	}

	public PreviewElementProperties getImageProperties() {
		return imageProperties;
	}

	public PreviewElementProperties getTextProperties() {
		return textProperties;
	}

	private void setCursor(Cursor c) {
		stage.getScene().setCursor(c);
	}

	private Cursor getDragDirectionCursor(PreviewEnums.DragDirection dragDirection) {
		if (dragDirection == PreviewEnums.DragDirection.LEFT) return Cursor.W_RESIZE;
		else if (dragDirection == PreviewEnums.DragDirection.RIGHT) return Cursor.E_RESIZE;
		else if (dragDirection == PreviewEnums.DragDirection.TOP) return Cursor.N_RESIZE;
		else if (dragDirection == PreviewEnums.DragDirection.BOTTOM) return Cursor.S_RESIZE;
		else if (dragDirection == PreviewEnums.DragDirection.TOP_LEFT) return Cursor.NW_RESIZE;
		else if (dragDirection == PreviewEnums.DragDirection.TOP_RIGHT) return Cursor.NE_RESIZE;
		else if (dragDirection == PreviewEnums.DragDirection.BOTTOM_LEFT) return Cursor.SW_RESIZE;
		return Cursor.SE_RESIZE;
	}

	public void onMouseMoved(double mouseX, double mouseY) {
		if (!isEditable) return;

		boolean elementHovered = false;

		// Priority on selected element
		if (selectedElement != null) {
			boolean insideElement = selectedElement.isMouseInside(mouseX, mouseY);
			if (insideElement) {
				PreviewEnums.DragDirection tempDragDir = selectedElement.isMouseInEdge(mouseX, mouseY);
				if (tempDragDir != null) setCursor(getDragDirectionCursor(tempDragDir));
				else setCursor(Cursor.OPEN_HAND);

				selectedElement.getChild().onHover(true);
				elementHovered = true;
			}
		}

		// Reverse so we check top most element first
		int lastElement = getElements().size() - 1;
		for (int i = lastElement; i >= 0; i--) {
			PreviewElement b = getElements().get(i);

			if (elementHovered || b.getHidden()) {
				if (b != selectedElement)
					b.getChild().onHover(false);
				continue;
			}

			boolean insideLabel = false;
			ButtonPreviewLabel label = null;
			if (b.getChild().getElementType() == PreviewEnums.Elements.BUTTON) {
				label = ((ScenarioButton)b.getChild()).getButtonLabel();
			}

			if (label != null && label.isMouseInside(mouseX, mouseY)) {
				insideLabel = true;
			}
			boolean insideElement = b.isMouseInside(mouseX, mouseY);
			if (insideElement || insideLabel) {
				if (insideLabel) {
					elementHovered = true;
					if (!label.getSelected()) setCursor(Cursor.HAND);
					else setCursor(Cursor.OPEN_HAND);
				}
				else if (insideElement) {
					setCursor(Cursor.HAND);

					b.getChild().onHover(true);
					elementHovered = true;
				}
			}
			else
				b.getChild().onHover(false);
		}
		if (!elementHovered) setCursor(Cursor.DEFAULT);
	}

	public void onMouseClicked(double mouseX, double mouseY) {
		if (!isEditable) return;
		if (!isClick) return;
		boolean elementSelected = false;

		// Reverse so we check top most element first
		int lastElement = getElements().size() - 1;
		for (int i = lastElement; i >= 0; i--) {
			PreviewElement b = getElements().get(i);

			if (b.getHidden()) continue;

			ButtonPreviewLabel label = null;
			if (b.getChild().getElementType() == PreviewEnums.Elements.BUTTON) {
				label = ((ScenarioButton)b.getChild()).getButtonLabel();
			}

			if (!elementSelected && label != null && label.isMouseInside(mouseX, mouseY)) {
				elementSelected = true;
				label.setSelected(true);
				b.setSelected(false);
				selectedElement = null;
			}

			else if (!elementSelected && b.isMouseInside(mouseX, mouseY)) {
				b.setSelected(true);
				elementSelected = true;
				selectedElement = b;
				if (label != null) label.setSelected(false);
			}
			else {
				b.setSelected(false);
				if (label != null) label.setSelected(false);
			}
		}
		if (!elementSelected) selectedElement = null;
	}

	public void onMousePressed(double mouseX, double mouseY) {
		if (!isEditable) return;
		isClick = true;
		if (selectedElement == null) return;

		if (selectedElement.isMouseInside(mouseX, mouseY)) {
			dragDirection = selectedElement.isMouseInEdge(mouseX, mouseY);
			if (dragDirection != null) {
				action = PreviewEnums.Actions.SCALING;
			}
			else action = PreviewEnums.Actions.MOVING;
			clickedX = mouseX;
			clickedY = mouseY;
			selectedElement.setOriginalCoordinates();
			selectedElement.getChild().onPress(true);
		}
		else {
			action = PreviewEnums.Actions.NONE;
			dragDirection = null;
		}
	}

	public void onMouseReleased() {
		if (!isEditable) return;
		for (PreviewElement b : getElements()) {
			b.getChild().onPress(false);
		}
	}

	public void onMouseDragged(double mouseX, double mouseY) {
		if (!isEditable) return;
		isClick = false;
		if (selectedElement == null) return;

		if (action == PreviewEnums.Actions.MOVING) {
			selectedElement.getChild().onMove(mouseX, mouseY);
			setCursor(Cursor.OPEN_HAND);
		}
		else if (action == PreviewEnums.Actions.SCALING) {
			selectedElement.getChild().onScale(mouseX, mouseY);
			setCursor(getDragDirectionCursor(dragDirection));
		}
	}

	public void setSlideshowIndex(int i) {
		slideshowIndex = i;
	}

	private int getCurrentSlideshowIndex() {
		if (DataManager.currentScene == SceneEnum.CAMPAIGN_SLIDE) {
			return DataManager.globalTabIndex;
		}
		else if (DataManager.currentScene == SceneEnum.CAMPAIGN_SLIDE_EDIT) {
			return slideshowIndex;
		}
		return 0;
	}

	public void saveElementData(PreviewEnums.Elements elementType, PreviewEnums.DataType dataType, int elementIndex, int value) {
		if (!isEditable) return;
		int slide = getCurrentSlideshowIndex();
		switch (elementType) {
			case BUTTON:
				if (dataType == PreviewEnums.DataType.X)
					DataManager.getDataCampaign().getListScenarios().get(elementIndex).saveButtonX(value);
				else if (dataType == PreviewEnums.DataType.Y)
					DataManager.getDataCampaign().getListScenarios().get(elementIndex).saveButtonY(value);
				else if (dataType == PreviewEnums.DataType.WIDTH)
					DataManager.getDataCampaign().getListScenarios().get(elementIndex).saveButtonWidth(value);
				else if (dataType == PreviewEnums.DataType.HEIGHT)
					DataManager.getDataCampaign().getListScenarios().get(elementIndex).saveButtonHeight(value);
				break;
			case BUTTON_LABEL:
				if (dataType == PreviewEnums.DataType.X)
					DataManager.getDataCampaign().getListScenarios().get(elementIndex).saveLabelX(value);
				else if (dataType == PreviewEnums.DataType.Y)
					DataManager.getDataCampaign().getListScenarios().get(elementIndex).saveLabelY(value);
				break;
			case TEXT:
				if (dataType == PreviewEnums.DataType.X)
					DataManager.getDataCampaign().getListSlideshow().get(slide).getListSlides().get(elementIndex).saveTextX(value);
				else if (dataType == PreviewEnums.DataType.Y)
					DataManager.getDataCampaign().getListSlideshow().get(slide).getListSlides().get(elementIndex).saveTextY(value);
				else if (dataType == PreviewEnums.DataType.WIDTH)
					DataManager.getDataCampaign().getListSlideshow().get(slide).getListSlides().get(elementIndex).saveTextWidth(value);
				else if (dataType == PreviewEnums.DataType.HEIGHT)
					DataManager.getDataCampaign().getListSlideshow().get(slide).getListSlides().get(elementIndex).saveTextHeight(value);
				break;
			default:
				if (dataType == PreviewEnums.DataType.X)
					DataManager.getDataCampaign().getListSlideshow().get(slide).getListSlides().get(elementIndex).saveImageX(value);
				else if (dataType == PreviewEnums.DataType.Y)
					DataManager.getDataCampaign().getListSlideshow().get(slide).getListSlides().get(elementIndex).saveImageY(value);
				else if (dataType == PreviewEnums.DataType.WIDTH)
					DataManager.getDataCampaign().getListSlideshow().get(slide).getListSlides().get(elementIndex).saveImageWidth(value);
				else if (dataType == PreviewEnums.DataType.HEIGHT)
					DataManager.getDataCampaign().getListSlideshow().get(slide).getListSlides().get(elementIndex).saveImageHeight(value);
				break;
		}
	}

	public abstract ArrayList<PreviewElement> getElements();

	protected void onClose() {}

}