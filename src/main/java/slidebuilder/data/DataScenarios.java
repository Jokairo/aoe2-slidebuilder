package slidebuilder.data;

import java.io.Serializable;

public class DataScenarios implements Serializable {
	private static final long serialVersionUID = 1L;
	private String button_text;
	private int button_x;
	private int button_y;
	private int button_text_x;
	private int button_text_y;
	private String image;
	private int image_width;
	private int image_height;
	private String help_text;
	private String help_style;
	private String difficulty;
	private boolean default_size;
	
	public void save(String button_text, int button_x, int button_y, int button_text_x, int button_text_y, 
			String image, int image_width, int image_height, String help_text, String help_style,
			String difficulty, boolean default_size) {
		this.button_text = button_text;
		this.button_x = button_x;
		this.button_y = button_y;
		this.button_text_x = button_text_x;
		this.button_text_y = button_text_y;
		this.image = image;
		this.image_width = image_width;
		this.image_height = image_height;
		this.help_text = help_text;
		this.help_style = help_style;
		this.difficulty = difficulty;
		this.default_size = default_size;
	}
	
	public String getButtonText() {
		return button_text;
	}
	
	public int getButtonX() {
		return button_x;
	}
	
	public int getButtonY() {
		return button_y;
	}
	
	public int getButtonTextX() {
		return button_text_x;
	}
	
	public int getButtonTextY() {
		return button_text_y;
	}
	
	public String getImage() {
		return image;
	}
	
	public String getHelpText() {
		return help_text;
	}
	
	public String getHelpStyle() {
		return help_style;
	}
	
	public int getImageWidth() {
		return image_width;
	}
	
	public int getImageHeight() {
		return image_height;
	}
	
	public String getDifficulty() {
		return difficulty;
	}
	
	public boolean getIsDefaultSize() {
		return default_size;
	}
}
