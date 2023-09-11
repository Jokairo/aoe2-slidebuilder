package slidebuilder.resource;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ResourceExpansion {
	private String id;
	private String text;
	private Color textColor;
	private String areaColor;
	private Image image;
	private int jsonIndex;
	
	public ResourceExpansion(String id, String text, Color textColor, String areaColor, Image image, int jsonIndex) {
		this.id = id;
		this.text = text;
		this.textColor = textColor;
		this.areaColor = areaColor;
		this.image = image;
		this.jsonIndex = jsonIndex;
	}
	
	//The text that will be displayed in the combobox menu
	public String getId() {
		return id;
	}
	
	//The text that will be displayed in the help text
	public String getText() {
		return text;
	}
	
	//The color or the text
	public Color getTextColor() {
		return textColor;
	}
	
	//The color of the area where the text is displayed
	public String getAreaColor() {
		return areaColor;
	}
	
	//The image that is displayed in the text area
	public Image getImage() {
		return image;
	}
	
	//The index that the Json file will use so that the game displays the correct help text style
		public int getJsonIndex() {
			return jsonIndex;
		}
}
