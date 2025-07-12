package slidebuilder.util;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

public class FieldBinder {
    public static void bindText(TextField field, Consumer<String> action) {
        field.textProperty().addListener((obs, oldVal, newVal) -> action.accept(newVal));
    }

    public static void bindTextArea(TextArea area, Consumer<String> action) {
        area.textProperty().addListener((obs, oldVal, newVal) -> action.accept(newVal));
    }

    public static void bindInt(TextField field, Consumer<Integer> action) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            action.accept(ParseUtil.parseInt(newVal));
        });
    }

    public static void bindCombo(ComboBox<String> combo, Consumer<String> action) {
        combo.valueProperty().addListener((obs, oldVal, newVal) -> action.accept(newVal));
    }

    public static void bindPropertyToText(ObservableValue<String> property, TextField targetField) {
        property.addListener((obs, oldVal, newVal) -> targetField.setText(newVal));
    }
}