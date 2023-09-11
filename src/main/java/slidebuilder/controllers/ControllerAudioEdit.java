package slidebuilder.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
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
import slidebuilder.Main;
import slidebuilder.components.AudioMarker;
import slidebuilder.controllers.interfaces.ControllerInterface;
import slidebuilder.data.DataManager;
import slidebuilder.enums.SceneEnum;
import slidebuilder.util.Popup;

public class ControllerAudioEdit extends ControllerInterface {

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
	private StringConverter<Double> stringConverter;
	private ArrayList<AudioMarker> markerList = new ArrayList<>();
	private boolean drag = false;
	private boolean playing = false;
	private int maxMarkers = 4;
	private boolean is_default = true;
	private int owner_id = 0;
	
	//INIT
	@FXML
	public void initialize() {
		setSceneBack(SceneEnum.CAMPAIGN_SLIDE);
		
		audio_bar_link.setEditable(false);
		
		/*
		 * StringConverter code from https://stackoverflow.com/questions/37517949/how-can-i-set-the-javafx-slider-to-format-for-time
		 * by user DVarga
		 */
		 stringConverter = new StringConverter<Double>() {

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
        
        resetSlider();
        audio_slider.setLabelFormatter(stringConverter);
        audio_slider.setMinorTickCount(3);
        audio_slider.setMajorTickUnit(30);
        
        //Volume slider
        //Set default
        double per = 100.0 * (audio_volume.getValue() - audio_volume.getMin()) / (audio_volume.getMax() - audio_volume.getMin());
    	setSliderStyle(audio_volume, per);
        
    	//Set listener
        audio_volume.valueProperty().addListener((obs, oldValue, newValue) -> {
        	if(mediaPlayer != null)
        		mediaPlayer.setVolume(audio_volume.getValue());
        	double percentage = 100.0 * (newValue.doubleValue() - audio_volume.getMin()) / (audio_volume.getMax() - audio_volume.getMin());
        	setSliderStyle(audio_volume, percentage);
	    });
        
        //Audio duration slider
        //Set default
        per = 100.0 * (audio_slider.getValue() - audio_slider.getMin()) / (audio_slider.getMax() - audio_slider.getMin());
    	setSliderStyle(audio_slider, per);
        
    	//Set listener
        audio_slider.valueProperty().addListener((obs, oldValue, newValue) -> {
        	double percentage = 100.0 * (newValue.doubleValue() - audio_slider.getMin()) / (audio_slider.getMax() - audio_slider.getMin());
        	setSliderStyle(audio_slider, percentage);
	    });
        
        //Find the correct audio position when duration slider is pressed
        audio_slider.setOnMousePressed(e -> {
        	if(mediaPlayer != null)
        		mediaPlayer.seek(Duration.seconds(audio_slider.getValue()));
        });
        
        //Pause audio when duration slider is dragged
        audio_slider.setOnMouseDragged(e -> {
        	if(mediaPlayer != null) {
        		mediaPlayer.pause();
        		drag = true;
        	}
        });
        
        //Play audio from mouse position when mouse released on duration slider
        audio_slider.setOnMouseReleased(e -> {
        	if(mediaPlayer != null && drag) {
        		mediaPlayer.seek(Duration.seconds(audio_slider.getValue()));
        		if(playing) {
	        		mediaPlayer.play();
        		}
        		drag = false;
        	}
        });
        
        audio_button_play.setText("Play");
	}
	
	private void setNewAudioFile(File f) {
		//Set the file path as textbox's text
		String path_text = f.getAbsolutePath();
		audio_bar_link.setText(path_text);
		audio_bar_link.positionCaret(path_text.length());
		
		//Create new media from the audio file
		Media sound = new Media(new File(audio_bar_link.getText()).toURI().toString());
		mediaPlayer = new MediaPlayer(sound);
		
		//Set slider values to be the audio file duration
		mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
	        if (!audio_slider.isValueChanging()) {
	        	audio_slider.setValue(newTime.toSeconds());
	        	audio_text_min.setText(stringConverter.toString(newTime.toSeconds()));
	        }
	    });
		
		//Reset media
		mediaPlayer.setOnEndOfMedia(() -> {
			resetAudio();
		});
		
		//Set current volume
		mediaPlayer.setVolume(audio_volume.getValue());
		
		//Update Slider Label after 0.5 seconds, if done instantly won't work
		Timer timer = new java.util.Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				//Platform runLater otherwise stringConverter throws error
				Platform.runLater(new Runnable(){
					public void run() {
				    	double d = mediaPlayer.getTotalDuration().toSeconds();
				    	audio_slider.setMax(d);
				    	
				    	audio_text_max.setText(stringConverter.toString(d));
				    	cancel();
					}
				});
			}
		}, 1 * 500, 1);
			
		
		audio_slider.setLabelFormatter(stringConverter);

		
		//Delete previous markers
		for(int i=0; i < markerList.size(); i++) {
			slide_pane.getChildren().remove(markerList.get(i));
		}
		markerList.clear();
		updateMarkerText();
		
		//Mark that audio settings have changed
		is_default = false;
	}
	
	private void resetSlider() {
		Double d = 0.1;
		audio_slider.setMax(d);
		audio_text_max.setText(stringConverter.toString(d));
	}
	
	private void resetAudio() {
		audio_slider.setValue(0);
		if(mediaPlayer != null)
			mediaPlayer.seek(Duration.seconds(0.0));
		stopAudio();
	}
	
	private void stopAudio() {
		if(mediaPlayer != null)
			mediaPlayer.pause();
		audio_button_play.setText("Play");
		playing = false;
	}
	
	@FXML
	private void playAudio(ActionEvent event) {
		if(!mediaPlayer.getStatus().equals(Status.PLAYING)) {
			mediaPlayer.play();
			audio_button_play.setText("Pause");
			playing = true;
		}
		else {
			stopAudio();
		}
	}
	
	@FXML
	private void addMarker(ActionEvent event) {
		if(markerList.size() < maxMarkers) {
			Bounds bounds = audio_slider.lookup(".thumb").getBoundsInParent();
			double thumb_size = 3;
			double x = bounds.getMinX() + thumb_size;
			
			double min_x = slide_pane.getTranslateX();
			double max_x;
			
			//Get slider max value
			double temp_value = audio_slider.getValue();
			audio_slider.setValue(audio_slider.getMax());
			bounds = audio_slider.lookup(".thumb").getBoundsInParent();
			max_x = bounds.getMinX() + thumb_size;
			audio_slider.setValue(temp_value);
			
			AudioMarker marker = new AudioMarker(x, 50, min_x, max_x, stringConverter, audio_slider);
			
			marker.setOnMousePressed(e -> {
				if (e.getButton() == MouseButton.SECONDARY) {
					removeMarker(marker);
				}
	        });
			
			slide_pane.getChildren().add(marker);
			
			markerList.add(marker);
			updateMarkerText();
		}
	}
	
	private void removeMarker(AudioMarker marker) {
		slide_pane.getChildren().remove(marker);
		markerList.remove(marker);
		updateMarkerText();
	}
	
	private void removeAllMarkers() {
		while(markerList.size() > 0) {
			AudioMarker marker = markerList.get(0);
			removeMarker(marker);
		}
	}
	
	private void updateMarkerText() {
		audio_marker_text.setText("Markers "+markerList.size()+"/"+maxMarkers);
	}
	
	@FXML
	private void markerValues() {
		//Stop audio anyway when marker button is pressed
		stopAudio();
		
		//All markerss must have been used
		if(markerList.size() != maxMarkers) {
			Popup.showError("All markers must be added!");
			return;
		}
		
		ArrayList<AudioMarker> tempList = new ArrayList<>();
		for(int i=0; i < markerList.size(); i++) {
			tempList.add(markerList.get(i));
		}
		double lastValue = 0;
		int position = 0;
		while(tempList.size() > 0) {
			double smallestValue = -1;
			int index = 0;
			for(int i=0; i < tempList.size(); i++) {
				if(i == 0 || tempList.get(i).getDurationValue() < smallestValue) {
					smallestValue = tempList.get(i).getDurationValue();
					index = i;
				}
			}
			
			//Calculate duration
			Double duration = (smallestValue-lastValue);
			
			//Round to show only two digits so it can be pasted to the textfield
			Double d2 = doubleRoundDecimal(duration);
			
			//Add value to the textfield in slide editor
			DataManager.getDataCampaign().getListSlideshow().get(owner_id).getListSlides().get(position).setDuration(d2);
			
			System.out.println("Slide duration: "+d2);
			tempList.remove(index);
			lastValue = smallestValue;
			position++;
		}
		
		//For last slide, calculate duration from slider max value
		Double duration = (audio_slider.getMax()-lastValue);
		
		//Round to show only two digits so it can be pasted to the textfield
		Double d2 = doubleRoundDecimal(duration);
		
		//Add value to the textfield in slide editor
		DataManager.getDataCampaign().getListSlideshow().get(owner_id).getListSlides().get(position).setDuration(d2);
		
		System.out.println("Last slide duration: "+d2);
		
		//Finished
		Popup.showSuccess("Changes applied successfully! The slides duration has been synced with audio.");
	}
	
	private double doubleRoundDecimal(Double d) {
		return Double.parseDouble(String.format(Locale.US, "%.2f", d));
	}
	
	public void setMaxMarkers(int i) {
		//If the amount of markers has changed, reset the editor
		if(maxMarkers != i) defaultValues();
		
		maxMarkers = i;
		updateMarkerText();
	}
	
	public void defaultValues() {
		
		//Already default values, no point doing it again
		if(is_default) return;
		
		//Remove text
		audio_bar_link.setText(null);
		audio_bar_link.positionCaret(0);
		
		//Stop audio and reset slider
		resetAudio();
		resetSlider();
		
		//Remove markers
		removeAllMarkers();
		
		is_default = true;
	}
	
	private void setSliderStyle(Slider slider, double percentage) {
		slider.setStyle("-track-color: linear-gradient(to right, #836a59 " + percentage+"%, #f0cbac "+percentage+("%);"));
	}
	
	public void setOwnerId(int owner_id) {
		this.owner_id = owner_id;
	}

	@Override
	public void sceneIn() {
		int index = DataManager.globalTabIndex;
		int slides = DataManager.getDataCampaign().getListSlideshow().get(index).getSlides();
		
		//Set max audio marker numbers
		setMaxMarkers(slides - 1);
		setOwnerId(index);
		
		//Set the audio that will be edited
        String audio_path = DataManager.getDataCampaign().getListSlideshow().get(index).getAudioPath();
        setNewAudioFile(new File(audio_path));
	}

	@Override
	public void sceneOut() {
		//Reset audio editor values once you exit the editor
		stopAudio();
		defaultValues();
	}
	
}
