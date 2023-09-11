package slidebuilder.data;

import java.io.Serializable;

public class DataSlideshowSlide implements Serializable {
	private static final long serialVersionUID = 1L;
	private String text;
	private int text_x;
	private int text_y;
	private int text_width;
	private int text_height;
	private String image_path;
	private int image_x;
	private int image_y;
	private int image_width;
	private int image_height;
	private boolean enabled;
	private double duration;
	
	public void save(String text, int text_x, int text_y, int text_width, int text_height, String image_path, int image_x, int image_y, int image_width, int image_height,
			boolean enabled, double duration) {
		this.text = text;
		this.text_x = text_x;
		this.text_y = text_y;
		this.text_width = text_width;
		this.text_height = text_height;
		this.image_path = image_path;
		this.image_x = image_x;
		this.image_y = image_y;
		this.image_width = image_width;
		this.image_height = image_height;
		this.enabled = enabled;
		this.duration = duration;
	}
	
	public String getText() {
		return text;
	}
	
	public int getTextX() {
		return text_x;
	}
	
	public int getTextY() {
		return text_y;
	}
	
	public int getTextWidth() {
		return text_width;
	}
	
	public int getTextHeight() {
		return text_height;
	}
	
	public String getImagePath() {
		return image_path;
	}
	
	public int getImageX() {
		return image_x;
	}
	
	public int getImageY() {
		return image_y;
	}
	
	public int getImageWidth() {
		return image_width;
	}
	
	public int getImageHeight() {
		return image_height;
	}
	
	public boolean getEnabled() {
		return enabled;
	}
	
	public double getDuration() {
		return duration;
	}
	
	public void setDuration(double duration) {
		this.duration = duration;
	}
}
