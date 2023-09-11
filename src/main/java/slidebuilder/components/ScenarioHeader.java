package slidebuilder.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import slidebuilder.util.FontMetrics;

public class ScenarioHeader {
	
	private Label text = new Label("");
	private StackPane header = new StackPane();
	private ImageView header_right = new ImageView(new Image(getClass().getResource("/images/header_right.png").toString()));
	private ImageView header_left = new ImageView(new Image(getClass().getResource("/images/header_left.png").toString()));
	private ImageView header_middle = new ImageView(new Image(getClass().getResource("/images/header_middle.png").toString()));
	private final Font font = Font.loadFont(getClass().getResource("/fonts/backgroundFont.ttf").toExternalForm(), 14);
	
	public ScenarioHeader() {
		header.getChildren().add(header_right);
		header.getChildren().add(header_middle);
		header.getChildren().add(header_left);
		header.getChildren().add(text);
		
		header.setTranslateX(0);
		header.setTranslateY(0);
		header.setPrefWidth(960);
		
		//Adjust header size
		double scale = 1.75;
		header_right.setFitWidth(header_right.getImage().getWidth() / scale);
		header_right.setFitHeight(header_right.getImage().getHeight() / scale);
		header_left.setFitWidth(header_left.getImage().getWidth() / scale);
		header_left.setFitHeight(header_left.getImage().getHeight() / scale);
		header_middle.setFitWidth(header_middle.getImage().getWidth() / scale);
		header_middle.setFitHeight(header_middle.getImage().getHeight() / scale);
		
		//Text
		text.setFont(font);
		text.setTextFill(Color.rgb(57, 28, 27));
		text.setAlignment(Pos.TOP_LEFT);
		
		//Header
		header.setAlignment(Pos.CENTER);
		setHeaderSize();
	}
	
	public StackPane getHeader() {
		return header;
	}
	
	public void setTitle(String t) {
		text.setText(t);
		setHeaderSize();
	}
	
	private void setHeaderSize() {
		//Automatically scales the Title bar to fit the text
		header_left.setTranslateX(- header_left.getImage().getWidth()/4);
		header_right.setTranslateX(+ header_right.getImage().getWidth()/4);
		
		FontMetrics fm = new FontMetrics(font);
		float label_width = fm.computeStringWidth(text.getText());
		int scale_width = 100;
		
		if (label_width > scale_width) {
			header_middle.setFitWidth(header_middle.getImage().getWidth() + (label_width-scale_width-30));
			header_left.setTranslateX(header_left.getTranslateX() - header_middle.getFitWidth() / 2);
			header_right.setTranslateX(header_right.getTranslateX() + header_middle.getFitWidth() / 2);
			//header.setTranslateX(400 - (header_middle.getFitWidth()/4));

			//header.setPrefWidth(label_width);
			
			System.out.println("----------------------");
			System.out.println("label width: "+label_width);
			System.out.println("header middle width: "+header_middle.getFitWidth());
			System.out.println("header_left x: "+header_left.getTranslateX());
			System.out.println("header right x: "+header_right.getTranslateX());
			System.out.println("header width: "+header.getPrefWidth());
		}
		else {
			header_middle.setFitWidth(header_middle.getImage().getWidth());
		}
	}
}
