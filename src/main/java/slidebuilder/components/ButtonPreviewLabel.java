package slidebuilder.components;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import slidebuilder.resource.ResourceDifficulty;
import slidebuilder.resource.ResourceManager;
import slidebuilder.util.FontMetrics;

public class ButtonPreviewLabel {
	
	private final double size_percentage;
	
	private double label_x = 0;
	private double label_y = 0;
	
	private ImageView rectangle;
	private ImageView rectangle_l;
	private ImageView rectangle_r;
	
	private Text text;
	private StackPane stackPane;
	private double label_width;
	private double label_height;
	
	private ImageView imageDifficulty;
	
	private ButtonPreview button;
	
	private Font font = Font.loadFont(getClass().getResource("/fonts/backgroundFont.ttf").toExternalForm(), 9);
	
	public ButtonPreviewLabel(ButtonPreview button, double size_percentage) {
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
			extra_space = 18;
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
		
		System.out.println("Rectangle length: "+rec_width);
		System.out.println("Text length: "+label_width);
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
		stackPane.setVisible(b);
	}
	
	public StackPane getLabelArea() {
		return stackPane;
	}
}
