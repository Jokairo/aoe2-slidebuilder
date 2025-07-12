package slidebuilder.controllers;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;
import javafx.util.StringConverter;
import slidebuilder.components.AudioMarker;
import slidebuilder.controllers.interfaces.ControllerInterface;
import slidebuilder.data.DataManager;
import slidebuilder.data.DataSlideshow;
import slidebuilder.enums.SceneEnum;
import slidebuilder.util.Popup;

public class ControllerAudioEdit extends ControllerInterface {

	private static final double SLIDER_UPDATE_DELAY_MS = 500;
	private static final int DEFAULT_MARKER_Y = 50;
	private static final double THUMB_OFFSET = 3.0;

	@FXML private AnchorPane slide_pane;
	@FXML private Button slide_button_back;
	@FXML private Label slide_title;
	@FXML private TextField audio_bar_link;
	@FXML private Slider audio_slider;
	@FXML private Label audio_text_min;
	@FXML private Label audio_text_max;
	@FXML private Button audio_button_play;
	@FXML private Button audio_button_add;
	@FXML private Button audio_button_remove;
	@FXML private Slider audio_volume;
	@FXML private Label audio_marker_text;

	private MediaPlayer mediaPlayer;
	private StringConverter<Double> timeFormatter;
	private final ArrayList<AudioMarker> markerList = new ArrayList<>();
	private boolean isDragging = false;
	private boolean isPlaying = false;
	private int maxMarkers = 4;
	private boolean isDefault = true;
	private int ownerId = 0;

	@FXML
	public void initialize() {
		setSceneBack(SceneEnum.CAMPAIGN_SLIDE);
		audio_bar_link.setEditable(false);

		setupTimeFormatter();
		setupSliders();
	}

	@FXML
	private void playAudio(ActionEvent event) {
		if (mediaPlayer != null && !mediaPlayer.getStatus().equals(Status.PLAYING)) {
			mediaPlayer.play();
			audio_button_play.setText("Pause");
			isPlaying = true;
		}
		else {
			stopAudio();
		}
	}

	@FXML
	private void addMarker(ActionEvent event) {
		if (markerList.size() >= maxMarkers) return;

		Bounds thumbBounds = audio_slider.lookup(".thumb").getBoundsInParent();
		double x = thumbBounds.getMinX() + THUMB_OFFSET;
		double minX = slide_pane.getTranslateX();

		// Get slider max value
		double temp_value = audio_slider.getValue();
		audio_slider.setValue(audio_slider.getMax());
		double maxX = audio_slider.lookup(".thumb").getBoundsInParent().getMinX() + THUMB_OFFSET;
		audio_slider.setValue(temp_value);

		AudioMarker marker = new AudioMarker(x, DEFAULT_MARKER_Y, minX, maxX, timeFormatter, audio_slider);
		marker.setOnMousePressed(e -> {
			if (e.getButton() == MouseButton.SECONDARY) {
				removeMarker(marker);
			}
		});

		slide_pane.getChildren().add(marker);
		markerList.add(marker);
		updateMarkerText();
	}

	@FXML
	private void markerValues() {
		// Stop audio anyway when marker button is pressed
		stopAudio();

		// All markers must have been used
		if (markerList.size() != maxMarkers) {
			Popup.showError("All markers must be added!");
			return;
		}

		ArrayList<AudioMarker> sortedMarkers = new ArrayList<>(markerList);
		sortedMarkers.sort(Comparator.comparingDouble(AudioMarker::getDurationValue));

		double lastValue = 0;
		int pos = 0;
		for (AudioMarker marker : sortedMarkers) {
			double duration = marker.getDurationValue() - lastValue;
			double roundedDuration = roundToTwoDecimalPlaces(duration);
			//Add value to the textfield in slide editor
			DataManager.getDataCampaign().getListSlideshow().get(ownerId).getListSlides().get(pos).setDuration(roundedDuration);
			lastValue = marker.getDurationValue();
			pos++;
		}

		//For last slide, calculate duration from slider max value
		double finalDuration = roundToTwoDecimalPlaces(audio_slider.getMax() - lastValue);
		DataManager.getDataCampaign().getListSlideshow().get(ownerId).getListSlides().get(pos).setDuration(finalDuration);

		Popup.showSuccess("Updated slide durations to sync with audio.");
	}

	@Override
	public void sceneIn() {
		int index = DataManager.globalTabIndex;
		ownerId = index;
		DataSlideshow slideshow = DataManager.getDataCampaign().getListSlideshow().get(index);

		setMaxMarkers(slideshow.getSlides() - 1);

		// Set the audio that will be edited
		String audio_path = slideshow.getAudioPath();
		setNewAudioFile(new File(audio_path));
	}

	@Override
	public void sceneOut() {
		// Reset audio editor values once you exit the editor
		stopAudio();
		resetEditor();
	}

	public void setMaxMarkers(int count) {
		// If the amount of markers has changed, reset the editor
		if(maxMarkers != count) resetEditor();
		maxMarkers = count;
		updateMarkerText();
	}

	private void resetEditor() {
		// Already default values, no point doing it again
		if (isDefault) return;

		audio_bar_link.setText(null);
		audio_bar_link.positionCaret(0);
		resetAudio();
		resetSlider();
		removeAllMarkers();
		isDefault = true;
	}
	
	private void setNewAudioFile(File f) {
		// Set the file path as textbox's text
		String path_text = f.getAbsolutePath();
		audio_bar_link.setText(path_text);
		audio_bar_link.positionCaret(path_text.length());
		
		// Create new media from the audio file
		Media sound = new Media(f.toURI().toString());
		mediaPlayer = new MediaPlayer(sound);
		
		// Set slider values to be the audio file duration
		mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
	        if (!audio_slider.isValueChanging()) {
	        	audio_slider.setValue(newTime.toSeconds());
	        	audio_text_min.setText(timeFormatter.toString(newTime.toSeconds()));
	        }
	    });

		mediaPlayer.setOnEndOfMedia(this::resetAudio);
		mediaPlayer.setVolume(audio_volume.getValue());
		
		// Update Slider Label after 0.5 seconds, if done instantly won't work
		new Timer().schedule(new TimerTask() {
			public void run() {
				Platform.runLater(() -> {
					double total = mediaPlayer.getTotalDuration().toSeconds();
					audio_slider.setMax(total);
					audio_text_max.setText(timeFormatter.toString(total));
					cancel();
				});
			}
		}, (long) SLIDER_UPDATE_DELAY_MS, 1);

		// Mark that audio settings have changed
		isDefault = false;
		removeAllMarkers();
	}

	private void stopAudio() {
		if (mediaPlayer != null)
			mediaPlayer.pause();
		audio_button_play.setText("Play");
		isPlaying = false;
	}

	private void resetAudio() {
		if (mediaPlayer != null)
			mediaPlayer.seek(Duration.seconds(0.0));
		audio_slider.setValue(0);
		stopAudio();
	}
	
	private void resetSlider() {
		double d = 0.1;
		audio_slider.setMax(d);
		audio_text_max.setText(timeFormatter.toString(d));
	}
	
	private void removeMarker(AudioMarker marker) {
		slide_pane.getChildren().remove(marker);
		markerList.remove(marker);
		updateMarkerText();
	}
	
	private void removeAllMarkers() {
		while (!markerList.isEmpty()) removeMarker(markerList.get(0));
	}
	
	private void updateMarkerText() {
		audio_marker_text.setText("Markers " + markerList.size() + "/" + maxMarkers);
	}
	
	private double roundToTwoDecimalPlaces(Double d) {
		return Double.parseDouble(String.format(Locale.US, "%.2f", d));
	}

	private void updateSliderStyle(Slider slider, double value) {
		double percent = 100.0 * (value - slider.getMin()) / (slider.getMax() - slider.getMin());
		slider.setStyle("-track-color: linear-gradient(to right, #836a59 " + percent + "%, #f0cbac " + percent + "%);");
	}

	private void setupTimeFormatter() {
		/*
		 * StringConverter code from https://stackoverflow.com/questions/37517949/how-can-i-set-the-javafx-slider-to-format-for-time
		 * by user DVarga
		 */
		timeFormatter = new StringConverter<Double>() {
			@Override
			public String toString(Double object) {
				long seconds = object.longValue();
				long minutes = TimeUnit.SECONDS.toMinutes(seconds);
				long remainingseconds = seconds - TimeUnit.MINUTES.toSeconds(minutes);
				return String.format("%02d", minutes) + ":" + String.format("%02d", remainingseconds);
			}

			@Override
			public Double fromString(String string) {
				return null;
			}
		};
	}

	private void setupSliders() {
		resetSlider();
		audio_slider.setLabelFormatter(timeFormatter);
		audio_slider.setMinorTickCount(3);
		audio_slider.setMajorTickUnit(30);
		updateSliderStyle(audio_volume, audio_volume.getValue());
		updateSliderStyle(audio_slider, audio_slider.getValue());

		audio_volume.valueProperty().addListener((obs, oldValue, newValue) -> {
			if (mediaPlayer != null) mediaPlayer.setVolume(newValue.doubleValue());
			updateSliderStyle(audio_volume, newValue.doubleValue());
		});

		audio_slider.valueProperty().addListener((obs, oldValue, newValue) -> {
			updateSliderStyle(audio_slider, newValue.doubleValue());
		});

		// Find the correct audio position when duration slider is pressed
		audio_slider.setOnMousePressed(e -> {
			if (mediaPlayer != null)
				mediaPlayer.seek(Duration.seconds(audio_slider.getValue()));
		});

		// Pause audio when duration slider is dragged
		audio_slider.setOnMouseDragged(e -> {
			if (mediaPlayer != null) {
				mediaPlayer.pause();
				isDragging = true;
			}
		});

		// Play audio from mouse position when mouse released on duration slider
		audio_slider.setOnMouseReleased(e -> {
			if (mediaPlayer != null && isDragging) {
				mediaPlayer.seek(Duration.seconds(audio_slider.getValue()));
				if (isPlaying) mediaPlayer.play();
				isDragging = false;
			}
		});

		audio_button_play.setText("Play");
	}
}
