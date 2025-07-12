package slidebuilder.util;

import javafx.scene.control.ComboBox;
import slidebuilder.data.CustomImageComboBox;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.enums.ResourceEnum;
import slidebuilder.resource.ResourceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComboBoxInitializer {
    public static void init(ComboBox<String> combo, CreatorEnum type, String defaultValue) {
        //Automatically add user added images
        combo.setItems(CustomImageComboBox.getCustomImageNameList(type));
        combo.getSelectionModel().select(defaultValue);

        //Change to list's default value if currently selected custom resource is deleted
        combo.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean exists = CustomImageComboBox.getCustomImageNameList(type).contains(oldVal);
            if (oldVal != null && !exists) {
                combo.getSelectionModel().select(defaultValue);
            }
        });
    }

    public static void initFromResourceList(ComboBox<String> combo, ResourceEnum resource) {
        List<String> items = getResourceItems(resource);
        combo.getItems().setAll(items);
        combo.getSelectionModel().selectFirst();
    }

    public static void initFromResourceList(ComboBox<String> combo, ResourceEnum resource, String optionalItem) {
        List<String> items = getResourceItems(resource);
        combo.getItems().clear();
        combo.getItems().add(optionalItem);
        combo.getItems().addAll(items);
        combo.getSelectionModel().selectFirst();
    }

    private static List<String> getResourceItems(ResourceEnum resource) {
        List<String> items = new ArrayList<>();
        switch (resource) {
            case CAMPAIGN:
                items = ResourceManager.instance.getCampaignResourceList().stream()
                        .map(r -> r.getName()).collect(Collectors.toList());
                break;
            case DIFFICULTY:
                items = ResourceManager.instance.getDifficultyResourceList().stream()
                        .map(r -> r.getName()).collect(Collectors.toList());
                break;

            case EXPANSION:
                items = ResourceManager.instance.getExpansionResourceList().stream()
                        .map(r -> r.getId()).collect(Collectors.toList());
                break;
        };
        return items;
    }
}