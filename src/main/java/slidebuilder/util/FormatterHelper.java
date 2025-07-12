package slidebuilder.util;

import javafx.scene.control.TextField;

public class FormatterHelper {
    public static void applyIntFormat(boolean allowNegative, TextField... fields) {
        for (TextField tf : fields) {
            tf.setTextFormatter(new TextFieldFormatter(0, allowNegative));
        }
    }

    public static void applyDecimalFormat(TextField field, int decimals) {
        field.setTextFormatter(new TextFieldFormatter(decimals, false));
    }
}