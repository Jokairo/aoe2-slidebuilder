package slidebuilder.components;

import javafx.geometry.Point2D;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.util.StringConverter;

public class AudioMarker extends ClickableButton {
	
	private Image image;
	private double min_x;
	private double max_x;
	private double image_width;
	private Tooltip tooltip = new Tooltip();
	private StringConverter<Double> stringConverter;
	private Slider audio_slider;
	
	private String toolTipText = "Slide Change Marker\nRight click to remove\nTime: ";
	
	public AudioMarker(double x, double y, double min_x, double max_x, StringConverter<Double> stringConverter, Slider audio_slider) {
		image = new Image(getClass().getResource("/images/marker.png").toString());
		image_width = image.getWidth();
		setImage(image);
		this.min_x = min_x;
		this.max_x = max_x;
		setButtonX(x);
		setButtonY(y);
		this.stringConverter = stringConverter;
		this.audio_slider = audio_slider;
		
		setTooltipText();
		
		if(getButtonX() < min_x) {
        	setButtonX(min_x);
        }
        else if(getButtonX() > max_x) {
        	setButtonX(max_x);
        }
	}
	
	private double imagePositionPercentage(double mix, double max) {
		return mix/max;
	}
	
	private void setTooltipText() {
		tooltip.setText(toolTipText + stringConverter.toString(getDurationValue()));
	}
	
	public double getDurationValue() {
		return imagePositionPercentage(getButtonX(), max_x) * audio_slider.getMax();
	}

	@Override
	public void onButtonEnter() {
		Point2D p = this.localToScreen(this.getLayoutBounds().getMaxX() - 50, this.getLayoutBounds().getMaxY() - 100);
        tooltip.show(this, p.getX(), p.getY());
	}

	@Override
	public void onButtonDrag(double x, double y) {
		//Image centre will be at cursor position
        setButtonX(getButtonX() + x - image_width/2);
        
        //Clamp image to be inside slider range
        if(getButtonX() < min_x) {
        	setButtonX(min_x);
        }
        else if(getButtonX() > max_x) {
        	setButtonX(max_x);
        }
        
        setTooltipText();
	}

	@Override
	public void onButtonExit() {
		tooltip.hide();
	}

	@Override
	public void onButtonRelease() {
		//Not used
	}
	
	@Override
	public void onButtonLeftPress() {
		//Not used
	}

	@Override
	public void onButtonRightPress() {
		//Not used
	}
}
