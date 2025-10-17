package slidebuilder.components;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import slidebuilder.data.CustomImage;
import slidebuilder.data.DataManager;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.util.FileUtil;

public class ComboBoxValidator {
    private final VBox container;
    private final Label title;
    private final ComboBox<String> comboBox;
    private final HBox errorContainer;

    private final CreatorEnum creatorEnum;

    public ComboBoxValidator(CreatorEnum creatorEnum) {
        this.creatorEnum = creatorEnum;

        container = new VBox();
        title = new Label();
        comboBox = new ComboBox<>();
        errorContainer = new HBox();
        Label errorLabel = new Label("File not found.");
        ImageView warningIcon = new ImageView(new Image(getClass().getResource("/images/warning.png").toString(), 20, 20, false, false));

        container.setSpacing(5);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setMaxWidth(Double.MAX_VALUE);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        VBox.setVgrow(container, Priority.ALWAYS);
        errorContainer.setSpacing(5);
        errorContainer.setAlignment(Pos.CENTER_LEFT);
        errorContainer.getChildren().addAll(warningIcon, errorLabel);
        errorContainer.setVisible(false);

        container.getChildren().addAll(title, comboBox, errorContainer);

        // Update error on selection
        comboBox.setOnAction(e -> validateSelection());
    }

    public ComboBox<String> getComboBox() {
        return comboBox;
    }

    public VBox getContainer() {
        return container;
    }

    public ObservableList<String> getItems() {
        return comboBox.getItems();
    }

    public String getValue() {
        return comboBox.getValue();
    }

    public void setTitle(String t) {
        title.setText(t);
    }

    public void setItems(ObservableList<String> items) {
        comboBox.setItems(items);
    }

    public void setValue(String value) {
        comboBox.getSelectionModel().select(value);
        validateSelection();
    }

    public void setDisable(boolean b) {
        comboBox.setDisable(b);
    }

    public boolean validateSelection() {
        String selected = comboBox.getValue();

        CustomImage ci = DataManager.getDataCampaign().getCustomImageData().getCustomImage(creatorEnum, selected);
        boolean isCustom = ci != null;
        if (!isCustom) {
            errorContainer.setVisible(false);
            return true;
        }

        boolean valid = FileUtil.fileExists(ci.getPath());

        errorContainer.setVisible(!valid);
        return valid;
    }
}
