package slidebuilder.previews;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import slidebuilder.components.PreviewElement;
import slidebuilder.components.SlideImage;
import slidebuilder.components.SlideText;
import java.util.ArrayList;

public class PreviewSlideshow extends PreviewInterface {
	private final PreviewElement pictureWrapper = new PreviewElement();
	private final SlideImage picture = new SlideImage(0);
	private final PreviewElement labelWrapper = new PreviewElement();
	private final SlideText label = new SlideText(0);
	private final ArrayList<PreviewElement> elements = new ArrayList<>();
	
	public PreviewSlideshow() {
		this.setIsSlidePreview(true);
		labelWrapper.setChild(label);
		labelWrapper.setKeepAspect(false);
		pictureWrapper.setChild(picture);

		elements.add(labelWrapper);
		elements.add(pictureWrapper);
	}
	
	public void setImage(String name) {
		picture.setImage(name);
	}
	
	public void setText(String string) {
		label.setText(string);
	}
	
	public void setTextX(int i) {
		labelWrapper.setElementX(i);
	}
	
	public void setTextY(int i) {
		labelWrapper.setElementY(i);
	}
	
	public void setTextWidth(int i) {
		labelWrapper.setElementWidth(i);
	}
	
	public void setTextHeight(int i) {
		labelWrapper.setElementHeight(i);
	}
	
	public void setImageX(int i) {
		pictureWrapper.setElementX(i);
	}
	
	public void setImageY(int i) {
		pictureWrapper.setElementY(i);
	}
	
	public void setImageWidth(int i, boolean aspect, boolean changeAspect) {
		if (aspect)
			pictureWrapper.setElementWidthWithAspect(i);
		else
			pictureWrapper.setElementWidth(i);
	}
	
	public void setImageHeight(int i, boolean aspect, boolean changeAspect) {
		if (aspect)
			pictureWrapper.setElementHeightWithAspect(i);
		else
			pictureWrapper.setElementHeight(i);
	}

	public PreviewElement getImageWrapper() {
		return pictureWrapper;
	}

	@Override
	public void addStuffToRoot() {
		VBox.setVgrow(getRoot(), Priority.ALWAYS);
		
		setBackgroundDefaultSize();
		setBackground();
		
		getRoot().getChildren().add(getBackground());

		getRoot().getChildren().add(pictureWrapper);
		getRoot().getChildren().add(picture.getImage());

		getRoot().getChildren().add(labelWrapper);
		getRoot().getChildren().add(label.getText());

	}

	@Override
	public ArrayList<PreviewElement> getElements() {
		return elements;
	}
}
