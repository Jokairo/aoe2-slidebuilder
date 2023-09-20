package slidebuilder.data;

import java.io.Serializable;
import java.util.ArrayList;

public class DataSlideshow implements Serializable {

	private static final long serialVersionUID = 1L;
	private int  slides;
	private String background;
	private boolean disable;
	private String audio_path;
	private ArrayList<DataSlideshowSlide> listSlides = new ArrayList<>();
	
	public void save(int slides, String background, boolean disable, String audio_path) {
		checkForChanges(slides, background, disable, audio_path);

		this.slides = slides;
		this.background = background;
		this.disable = disable;
		this.audio_path = audio_path;
	}

	public void setDefaultBackground(String bg) {
		background = bg;
	}
	
	public int getSlides() {
		return slides;
	}
	
	public String getBackground() {
		return background;
	}
	
	public boolean getDisable() {
		return disable;
	}
	
	public String getAudioPath() {
		return audio_path;
	}
	
	public ArrayList<DataSlideshowSlide> getListSlides() {
		return listSlides;
	}

	private void checkForChanges(int slides, String background, boolean disable, String audio_path) {
		if(this.background == null) return;

		if(this.slides != slides || !this.background.equals(background) || this.disable != disable || (this.audio_path == null && audio_path != null) || (this.audio_path != null && !this.audio_path.equals(audio_path)))
			DataManager.getDataCampaign().setUnsavedChanges(true);
	}
}
