package slidebuilder.previews;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import slidebuilder.data.DataManager;
import slidebuilder.enums.CreatorEnum;

public class PreviewSlideshow extends PreviewInterface {

	private Font font;
	private ImageView picture = new ImageView();
	private Label text = new Label("");
	private final double MAX_FONT_SIZE = 12.3;
	
	private Image picture_image;
	private String currentBg;
	
	public PreviewSlideshow() {
		font = Font.loadFont(getClass().getResource("/fonts/backgroundFont.ttf").toExternalForm(), MAX_FONT_SIZE);
		this.setIsSlidePreview(true);
	}
	
	public void setImage(String name) {
		
		if(!isOpen()) return;
		
		if(name == null || name.equalsIgnoreCase("None")) {
			picture_image = null;
			picture.setImage(null);
			return;
		}

		picture_image = DataManager.getDataCampaign().getCustomImageData().getCustomImage(CreatorEnum.SLIDE_IMAGE, name).getImage();
		picture.setImage(picture_image);
	}
	
	public void setText(String string) {
		text.setText(string);
	}
	
	public void setTextX(double d) {
		//Coordinates 25%
		text.setTranslateX(d/4);
	}
	
	public void setTextY(double d) {
		//Coordinates 25%
		text.setTranslateY(d/4);
	}
	
	public void setTextWidth(double d) {
		//25% + 12px
		text.setMaxWidth(d/4+1);
		System.out.println(""+text.getLineSpacing());
	}
	
	public void setTextHeight(double d) {
		//25% + 20px
		//+20 otherwise the text cuts sometimes because line spacing has been changed
		text.setMaxHeight(d/4+20);
	}
	
	public void setImageX(double d) {
		//Coordinates 25%
		picture.setX(d/4);
	}
	
	public void setImageY(double d) {
		//Coordinates 25%
		picture.setY(d/4);
	}
	
	public void setImageWidth(double d) {
		//Image must be scaled 25% smaller
		picture.setFitWidth(d/4);
	}
	
	public void setImageHeight(double d) {
		//Image must be scaled 25% smaller
		picture.setFitHeight(d/4);
	}

	@Override
	public void addStuffToRoot() {
		text.setFont(font);
		text.setWrapText(true);
		
		text.setTextFill(Color.BLACK);
		text.setTextOverrun(OverrunStyle.CLIP);
		
		text.setAlignment(Pos.TOP_LEFT);
		
		VBox.setVgrow(getRoot(), Priority.ALWAYS);
		
		setBackgroundDefaultSize();
		setBackground();
		
		getRoot().getChildren().add(getBackground());
		getRoot().getChildren().add(picture);
		getRoot().getChildren().add(text);

	}
}
