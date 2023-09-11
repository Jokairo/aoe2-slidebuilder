package slidebuilder.components;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

public abstract class ClickableButton extends ImageView {
	
	private boolean pressed = false;
	private boolean hover = false;
	
	public ClickableButton() {
		
		this.setOnMouseEntered(e -> {
			onButtonEnter();
			hover = true;
		});
		
		this.setOnMouseDragged(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				onButtonDrag(e.getX(), e.getY());
			}
		});
		
		this.setOnMousePressed(e -> {
			if (e.getButton() == MouseButton.PRIMARY && hover) {
				onButtonLeftPress();
				pressed = true;
			}
			
			else if (e.getButton() == MouseButton.SECONDARY && hover) {
				onButtonRightPress();
				pressed = true;
			}
		});
		
		this.setOnMouseExited(e -> {
			onButtonExit();
			pressed = false;
			hover = false;
		});
		
		this.setOnMouseReleased(e -> {
			if (e.getButton() == MouseButton.PRIMARY && hover) {
				onButtonRelease();
				pressed = false;
			}
		});
	}
	
	public void setButtonX(double value) {
		this.setTranslateX(value);
	}
	
	public void setButtonY(double value) {
		this.setTranslateY(value);
	}
	
	public double getButtonX() {
		return this.getTranslateX();
	}
	
	public double getButtonY() {
		return this.getTranslateY();
	}
	
	public double getButtonWidth() {
		return this.getFitWidth();
	}
	
	public double getButtonHeight() {
		return this.getFitHeight();
	}
	
	public boolean getPressed() {
		return pressed;
	}
	
	public boolean getHover() {
		return hover;
	}
	
	public abstract void onButtonEnter();
	public abstract void onButtonLeftPress();
	public abstract void onButtonRightPress();
	public abstract void onButtonDrag(double x, double y);
	public abstract void onButtonExit();
	public abstract void onButtonRelease();
}
