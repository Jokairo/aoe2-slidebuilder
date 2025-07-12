package slidebuilder.previews;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import slidebuilder.data.DataManager;
import slidebuilder.data.DataSlideshow;
import slidebuilder.data.DataSlideshowSlide;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PreviewSlideshowLive extends PreviewSlideshow {
    private Slider slider = new Slider();
    private Button playButton = new Button("Play");
    private Label timeLabel;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private boolean isDragging = false;
    private Timer playbackTimer;
    private int currentSlideIndex = 0;
    private double elapsedTime = 0;
    private DataSlideshow slideshow;
    private Runnable onCloseCallback;

    public PreviewSlideshowLive() {
        super();
        setIsEditable(false);
    }

    @Override
    public void addLiveStuffToRoot() {
        slideshow = DataManager.getDataCampaign().getListSlideshow().get(DataManager.globalTabIndex);

        playButton = new Button("Play");
        playButton.setPrefWidth(80);
        playButton.getStyleClass().add("button");

        slider = new Slider(0, 1, 0);
        slider.setPrefWidth(500);
        slider.setMinHeight(30);
        slider.getStyleClass().add("slider");

        timeLabel = new Label("00:00 / 00:00");
        timeLabel.setPrefWidth(150);
        timeLabel.getStyleClass().add("labeltext");

        HBox controls = new HBox(10, playButton, slider, timeLabel);
        controls.setAlignment(Pos.CENTER);
        controls.setTranslateY(510); // 30px from bottom
        int margin = 80;
        controls.setPrefWidth(960 - margin);
        controls.setTranslateX(margin/2);
        controls.getStyleClass().add("hbox");

        getRoot().getChildren().add(controls);

        // Events
        initSliderEvents();
        initPlayButton();

        // Refresh slider max
        double maxValue = calculateTotalSlideDuration();
        slider.setMax(maxValue);

        String audioPath = slideshow.getAudioPath();
        if (audioPath != null && !audioPath.isEmpty()) {
            Media media = new Media(new File(audioPath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }
        else {
            mediaPlayer = null;
        }

        String cssFile = getClass().getResource("/css/playback.css").toExternalForm();
        getRoot().getStylesheets().add(cssFile);

        syncVisuals();
        updateTimeLabel();
    }

    @Override
    protected void onClose() {
        slider.setValue(0);
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(0.0));
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        isPlaying = false;
        isDragging = false;
        currentSlideIndex = 0;
        elapsedTime = 0;

        if (onCloseCallback != null) {
            onCloseCallback.run();
        }
    }
    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }

    private void initPlayButton() {
        playButton.setOnAction(e -> {
            if (isPlaying) {
                stopPlayback();
            } else {
                startPlayback();
            }
        });
    }

    private void syncVisuals() {
        // Use current slider value as starting point
        elapsedTime = slider.getValue();

        if (elapsedTime >= slider.getMax()) return;

        // Determine the correct slide to start from
        currentSlideIndex = 0;
        double timeSum = 0;
        for (int i = 0; i < slideshow.getSlides(); i++) {
            timeSum += slideshow.getListSlides().get(i).getDuration();
            if (timeSum > elapsedTime) {
                currentSlideIndex = i;
                break;
            }
        }
        updateSlide(currentSlideIndex);
    }

    private void startPlayback() {
        syncVisuals();
        // Prevent duplicate timers
        if (playbackTimer != null) {
            playbackTimer.cancel();
            playbackTimer = null;
        }

        isPlaying = true;
        playButton.setText("Pause");

        if (mediaPlayer != null) {
            mediaPlayer.play();
        }

        // Playback loop
        playbackTimer = new Timer();
        playbackTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (!isPlaying) return;
                    if (isDragging) return;

                    elapsedTime += 0.1;
                    slider.setValue(elapsedTime);

                    double slideEnd = getSlideEndTime(currentSlideIndex);
                    if (elapsedTime >= slideEnd) {
                        currentSlideIndex++;
                        if (currentSlideIndex < slideshow.getSlides()) {
                            updateSlide(currentSlideIndex);
                        }
                        else {
                            resetAudio();
                        }
                    }
                });
            }
        }, 0, 100);
    }

    private void stopPlayback() {
        isPlaying = false;
        playButton.setText("Play");

        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }

        if (playbackTimer != null) {
            playbackTimer.cancel();
            playbackTimer = null;
        }
    }

    private void initSliderEvents() {
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateTimeLabel();

            if (!isPlaying || isDragging)
                syncVisuals();

            if (isPlaying && mediaPlayer != null) {
                mediaPlayer.seek(Duration.seconds(newVal.doubleValue()));
            }
        });

        // Find the correct audio position when duration slider is pressed
        slider.setOnMousePressed(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
            isDragging = true;
            syncVisuals();
        });

        // Pause audio when duration slider is dragged
        slider.setOnMouseDragged(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
            isDragging = true;
        });

        // Play audio from mouse position when mouse released on duration slider
        slider.setOnMouseReleased(e -> {
            if (isDragging) {
                if (mediaPlayer != null) {
                    mediaPlayer.seek(Duration.seconds(slider.getValue()));
                    if (isPlaying) mediaPlayer.play();
                }

                isDragging = false;
                syncVisuals();
            }
        });
    }

    private void updateTimeLabel() {
        double current = slider.getValue();
        double total = slider.getMax();

        int curMin = (int) (current / 60);
        int curSec = (int) (current % 60);
        int totalMin = (int) (total / 60);
        int totalSec = (int) (total % 60);

        timeLabel.setText(String.format("%02d:%02d / %02d:%02d", curMin, curSec, totalMin, totalSec));
    }

    private void updateSlide(int index) {
        DataSlideshowSlide slide = slideshow.getListSlides().get(index);

        setImage(slide.getImagePath());
        setImageX(slide.getImageX());
        setImageY(slide.getImageY());
        setImageWidth(slide.getImageWidth(), false, false);
        setImageHeight(slide.getTextHeight(), false, false);

        setText(slide.getText());
        setTextX(slide.getTextX());
        setTextY(slide.getTextY());
        setTextWidth(slide.getTextWidth());
        setTextHeight(slide.getTextHeight());
    }

    private double getSlideEndTime(int slideIndex) {
        double time = 0;
        for (int i = 0; i <= slideIndex; i++) {
            time += slideshow.getListSlides().get(i).getDuration();
        }
        return time;
    }

    private double calculateTotalSlideDuration() {
        if (DataManager.getDataCampaign() == null) return 0;
        ArrayList<DataSlideshowSlide> slides = slideshow.getListSlides();

        return slides.stream()
                .mapToDouble(slide -> slide.getDuration())
                .sum();
    }

    private void resetAudio() {
        if (mediaPlayer != null)
            mediaPlayer.seek(Duration.seconds(0.0));
        slider.setValue(0);
        stopPlayback();
        syncVisuals();
    }
}
