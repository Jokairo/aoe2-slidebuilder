package slidebuilder.components;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import slidebuilder.data.DataManager;
import slidebuilder.enums.PreviewEnums;
import slidebuilder.previews.PreviewScenarios;
import slidebuilder.resource.ResourceDifficulty;
import slidebuilder.resource.ResourceManager;
import slidebuilder.util.FontMetrics;

public class ButtonPreviewLabel {
	
	private final double size_percentage;
	private final int ICON_SIZE = 18;
	
	private double label_x = 0;
	private double label_y = 0;
	
	private final ImageView rectangle;
	private final ImageView rectangle_l;
	private final ImageView rectangle_r;
	
	private final Text text;
	private final StackPane stackPane;
	private final Pane border;

	private double label_width;
	private double label_height;
	
	private final ImageView imageDifficulty;
	
	private final ButtonPreview button;
	
	private final Font font = Font.loadFont(getClass().getResource("/fonts/backgroundFont.ttf").toExternalForm(), 9);
	boolean selected = false;
	private double originalX;
	private double originalY;
	private double clickedX;
	private double clickedY;


	public ButtonPreviewLabel(ButtonPreview button, double size_percentage, int buttonIndex) {
		rectangle = new ImageView(new Image(getClass().getResource("/images/label_middle.png").toString()));
		rectangle_l = new ImageView(new Image(getClass().getResource("/images/label_left.png").toString()));
		rectangle_r = new ImageView(new Image(getClass().getResource("/images/label_right.png").toString()));
		
		imageDifficulty = new ImageView();
		
		text = new Text(" ");
		label_width = text.getBoundsInLocal().getWidth();
		label_height = text.getBoundsInLocal().getHeight();
		text.setFill(Color.WHITE);
		
		text.setFont(font);
		
		stackPane = new StackPane();
		stackPane.getChildren().addAll(rectangle, rectangle_l, rectangle_r, imageDifficulty, text);
		stackPane.setAlignment(Pos.CENTER);
		
		this.button = button;
		this.size_percentage = size_percentage;

		border = new Pane();
		border.setMouseTransparent(true); // Allow clicking on wrapper

		// Make separate controls for ButtonPreviewLabel
		// Differs from PreviewElement as this needs offset from parent button and size is calculated differently
		stackPane.setOnMousePressed(e -> {
			if (!selected) return;
			clickedX = e.getSceneX();
			clickedY = e.getSceneY();
			setOriginalCoordinates();
		});

		stackPane.setOnMouseDragged(e -> {
			if (!selected) return;

			if (e.getButton() == MouseButton.PRIMARY) {
				PreviewScenarios preview = DataManager.getPreviewScenarios();

				int mouseX = (int)getScaledMouseX(e.getSceneX(), true);
				int mouseY = (int)getScaledMouseY(e.getSceneY(), true);

				mouseX = mouseX - (int)(button.getTranslateX());
				mouseY = mouseY - (int)(button.getTranslateY());

				if (buttonIndex == DataManager.globalTabIndex) {
					preview.getButtonLabelProperties().setX(""+mouseX * 4);
					preview.getButtonLabelProperties().setY(""+mouseY * 4);
				}
				else {
					setLabelX(mouseX);
					setLabelY(mouseY);
					setLabelCoordinates();
					DataManager.getPreviewScenarios().saveElementData(PreviewEnums.Elements.BUTTON_LABEL, PreviewEnums.DataType.X, buttonIndex, mouseX * 4);
					DataManager.getPreviewScenarios().saveElementData(PreviewEnums.Elements.BUTTON_LABEL, PreviewEnums.DataType.Y, buttonIndex, mouseY * 4);
				}
			}
		});
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		if (selected) {
			border.setStyle("-fx-border-color: red");
		}
		else {
			border.setStyle(null);
		}
	}

	public boolean getSelected() {
		return selected;
	}

	private double getScaledMouseX(double mouse_x, boolean useOffset) {
		PreviewScenarios preview = DataManager.getPreviewScenarios();
		double percentageX = mouse_x / preview.getSceneWidth();

		double defaultWidth = 960.0;

		double screenScale = defaultWidth / preview.getSceneWidth();
		double scaledClickX = clickedX * screenScale;

		// Apply icon offset, icon moves the stackpane when label full size
		double iconOffset = imageDifficulty.getImage() != null
				? label_width > button.getFitWidth() ? ICON_SIZE : 0
				: 0;

		// Apply width offset, stackpane origin is centered and 0 coordinate starts from button center point
		double widthOffset = (border.getPrefWidth()) / 2 - iconOffset / 2 - button.getFitWidth() / 2;

		int offsetX = !useOffset ? 0 : (int)((originalX + widthOffset) - scaledClickX);

		return (int)(defaultWidth * percentageX) + offsetX;
	}

	private double getScaledMouseY(double mouse_y, boolean useOffset) {
		PreviewScenarios preview = DataManager.getPreviewScenarios();
		double percentageY = mouse_y / preview.getSceneHeight();

		double defaultHeight = 540.0;

		double screenScale = defaultHeight / preview.getSceneHeight();
		double scaledClickY = clickedY * screenScale;
		int offsetY = !useOffset ? 0 : (int)(originalY - scaledClickY);

		return (int)(defaultHeight * percentageY) + offsetY;
	}

	public boolean isMouseInside(double mouseX, double mouseY) {
		int scaledMouseX = (int)getScaledMouseX(mouseX, false);
		int scaledMouseY = (int)getScaledMouseY(mouseY, false);

		double x = border.getTranslateX();
		double y = border.getTranslateY();
		double width = border.getPrefWidth();
		double height = border.getPrefHeight();

		return scaledMouseX <= x+width && scaledMouseX >= x && scaledMouseY <= y+height && scaledMouseY >= y;
	}
	
	public void setText(String t) {
		//If there is no text, make the label have an empty space ' ' otherwise the label picture starts glitching
		if(t.equals("")) t = " ";
		
		text.setText(t);
		
		FontMetrics fm = new FontMetrics(font);
		label_width = fm.computeStringWidth(t);
		
		
		setLabelCoordinates();
	}
	
	public void setLabelCoordinates() {
		
		//Base Coordinates are TopCenter of image + Label coordinates
		
		if(button.getFitWidth() > label_width) {
			stackPane.setTranslateX(button.getTranslateX() + label_x);
			stackPane.setTranslateY(button.getTranslateY() + label_y);
			stackPane.setPrefSize(button.getFitWidth(), label_height);
		}
		else {
			stackPane.setTranslateX(button.getTranslateX() + button.getFitWidth()/2 - label_width/2 + label_x);
			stackPane.setTranslateY(button.getTranslateY() + label_y);
			stackPane.setPrefSize(label_width, label_height);
		}
		
		//If difficulty option enabled, move the text a bit to the right to make space for the sword icon
		int extra_space = 0;
		if(imageDifficulty.getImage() != null) {
			text.setTranslateX(8);
			extra_space = ICON_SIZE;
		}
		else {
			text.setTranslateX(0);
		}

		//Makes that the label rectangle is exactly same size as label text
		double rec_width = label_width + extra_space;
		rectangle.setFitWidth(rec_width);
		rectangle.setFitHeight(label_height);
		
		rectangle_l.setFitWidth(5);
		rectangle_l.setFitHeight(label_height);
		rectangle_r.setFitWidth(5);
		rectangle_r.setFitHeight(label_height);
		
		rectangle_l.setTranslateX(-rec_width/2);
		rectangle_r.setTranslateX(rec_width/2);
		
		//Move Difficulty Sword icon
		if(imageDifficulty.getImage() != null)
			imageDifficulty.setTranslateX(-rec_width/2 + imageDifficulty.getImage().getWidth() * size_percentage);

		double width = stackPane.getPrefWidth();
		if (button.getFitWidth() <= label_width) width += extra_space;

		border.setTranslateX(stackPane.getTranslateX());
		border.setTranslateY(stackPane.getTranslateY());
		border.setPrefWidth(width);
		border.setPrefHeight(stackPane.getPrefHeight());
	}
	
	public void setDifficulty(String name) {
		ResourceDifficulty re = ResourceManager.instance.getResourceDifficultyFromName(name);
		Image img = re.getImage();
		
		//Set difficulty icon size
		if (img != null) {
			imageDifficulty.setFitWidth(img.getWidth()/2);
			imageDifficulty.setFitHeight(label_height);
		}
		
		//Update icon
		imageDifficulty.setImage(img);
		
		//Update button label
		setLabelCoordinates();
	}
	
	public void setLabelX(double value) {
		label_x = value;
	}
	
	public void setLabelY(double value) {
		label_y = value;
	}
	
	public void setVisible(boolean b) {
		if (!b) setSelected(false);
		stackPane.setVisible(b);
	}
	
	public StackPane getLabelArea() {
		return stackPane;
	}

	public Pane getBorder() {
		return border;
	}

	private void setOriginalCoordinates() {
		originalX = border.getTranslateX();
		originalY = border.getTranslateY();
	}
}
