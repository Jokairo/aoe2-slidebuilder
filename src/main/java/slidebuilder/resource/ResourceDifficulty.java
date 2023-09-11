package slidebuilder.resource;

import javafx.scene.image.Image;

public class ResourceDifficulty {
	private String name;
	private Image image;
	private int jsonIndex;
	
	public ResourceDifficulty(String name, Image image, int jsonIndex) {
		this.name = name;
		this.image = image;
		this.jsonIndex = jsonIndex;
	}
	
	public String getName() {
		return name;
	}
	
	public Image getImage() {
		return image;
	}
	
	public int getJsonIndex() {
		return jsonIndex;
	}
}
