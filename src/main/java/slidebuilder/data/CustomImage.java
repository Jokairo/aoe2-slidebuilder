package slidebuilder.data;

import java.io.Serializable;

import javafx.scene.image.Image;
import slidebuilder.enums.CreatorEnum;

public class CustomImage implements Serializable {
	
	private static final long serialVersionUID = 2L;
	private String name;
	private String path;
	private int width;
	private int height;
	private transient Image image;
	private CreatorEnum imageType;
	
	public CustomImage(String name, String path) {
		this.name = name;
		this.path = path;
	}
	
	public CustomImage(String name, String path, int width, int height, CreatorEnum imageType) {
		this.name = name;
		this.path = path;
		this.width = width;
		this.height = height;
		this.imageType = imageType;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPath() {
		return path;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setImage() {
		if(imageType == CreatorEnum.SLIDE_IMAGE) {
			image = new Image("file:///"+path, width/4, height/4, false, false);
		}
		else if(imageType == CreatorEnum.ICON) {
			image = new Image("file:///"+path, width/4, height/4, false, false);
		}
		else if(imageType == CreatorEnum.SLIDE_BG) {
			image = new Image("file:///"+path, 0, 270, true, false);
		}
		else {
			image = new Image("file:///"+path, 480, 270, false, false);
		}
	}
}
