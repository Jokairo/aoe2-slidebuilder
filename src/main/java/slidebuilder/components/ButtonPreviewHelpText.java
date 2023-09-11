package slidebuilder.components;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import slidebuilder.resource.ResourceExpansion;
import slidebuilder.resource.ResourceManager;

public class ButtonPreviewHelpText {

	private TextFlow textArea = new TextFlow();
	private StackPane rec = new StackPane();
	private Text recText = new Text();
	private ImageView recImage = new ImageView();
	
	private final Font font = Font.loadFont(getClass().getResource("/fonts/backgroundFont.ttf").toExternalForm(), 14);
	private final Font font_help = Font.loadFont(getClass().getResource("/fonts/backgroundFont.ttf").toExternalForm(), 13);
	private final Font font_help_italic = Font.loadFont(getClass().getResource("/fonts/backgroundFontItalic.otf").toExternalForm(), 13);
	
	public ButtonPreviewHelpText() {

		textArea.setTranslateX(690);
		textArea.setTranslateY(540);
		textArea.setPrefWidth(960 - textArea.getTranslateX());
		
		textArea.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
		textArea.heightProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<?> o, Object oldVal, Object newVal) {
				textArea.setTranslateY(540 - textArea.getHeight());
				rec.setTranslateY(textArea.getTranslateY() - rec.getHeight());
			}
		});
		
		recText.setFont(font);
		recText.setTranslateX(20);
		
		rec.setTranslateX(textArea.getTranslateX());
		rec.setPrefWidth(textArea.getPrefWidth());
		rec.setPrefHeight(20);
		rec.setTranslateY(textArea.getTranslateY() - rec.getHeight());
		rec.getChildren().addAll(recImage, recText);
		rec.setAlignment(Pos.CENTER_LEFT);
	}
	
	public void showHelpText(String text, String helpChoice) {
		colorText(text);
		textArea.setVisible(true);
		setHelpStyle(helpChoice);
	}
	
	public void disableHelpText() {
		textArea.setVisible(false);
		textArea.getChildren().clear();
		rec.setVisible(false);
	}
	
	private void colorText(String text) {
		String helpText = text;
		
		String[] splits = helpText.split("(?=<)");
		Color color = Color.WHITE;
		boolean italic = false;

		for(int i=0; i < splits.length; i++) {
			String s = "";
			if(splits[i].startsWith("<default>")) {
				color = Color.WHITE;
				italic = false;
				s = splits[i].substring(9);
			}
			else if(splits[i].startsWith("<blue>")) {
				color = Color.BLUE;
				italic = false;
				s = splits[i].substring(6);
			}
			else if(splits[i].startsWith("<red>")) {
				color = Color.RED;
				italic = false;
				s = splits[i].substring(5);
			}
			else if(splits[i].startsWith("<yellow>")) {
				color = Color.YELLOW;
				italic = false;
				s = splits[i].substring(8);
			}
			else if(splits[i].startsWith("<cyan>")) {
				color = Color.CYAN;
				italic = false;
				s = splits[i].substring(6);
			}
			else if(splits[i].startsWith("<purple>")) {
				color = Color.PURPLE;
				italic = false;
				s = splits[i].substring(8);
			}
			else if(splits[i].startsWith("<gray>")) {
				color = Color.GRAY;
				italic = false;
				s = splits[i].substring(6);
			}
			else if(splits[i].startsWith("<orange>")) {
				color = Color.ORANGE;
				italic = false;
				s = splits[i].substring(8);
			}
			else if(splits[i].startsWith("<i>")) {
				italic = !italic;
				s = splits[i].substring(3);
			}
			else {
				//Find first > character if there is, dont include text before >
				s = splits[i];
				int index = s.indexOf(">");
				System.out.println("index "+index);
				if(index != -1) {
					s = s.substring(index+1);
				}
				else if (i!=0) s = "";
			}
			
			//Dont show > character
			s = s.replace(">", "");
			
			//Convert line break to line break character
			//s = s.replace("\n", "\\n");
			
			if(s.equals("")) {
				continue;
			}
			else {
				Text t = new Text(s);
				t.setFill(color);
				if(italic)
					t.setFont(font_help_italic);
				else
					t.setFont(font_help);
				textArea.getChildren().add(t);
			}
		}
	}
	
	private void setHelpStyle(String name) {
		ResourceExpansion re = ResourceManager.instance.getResourceExpansionFromName(name);
		if (re != null) {
			recText.setText(re.getText());
			recText.setFill(re.getTextColor());
			rec.setStyle("-fx-background-color: "+re.getAreaColor()+";");
			recImage.setImage(re.getImage());
			rec.setVisible(true);
		}
	}
	
	public TextFlow getTextComponent() {
		return textArea;
	}
	
	public StackPane getTextAreaComponent() {
		return rec;
	}
}
