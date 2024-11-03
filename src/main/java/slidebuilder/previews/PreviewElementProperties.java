package slidebuilder.previews;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PreviewElementProperties {
    private final StringProperty x = new SimpleStringProperty();
    private final StringProperty y = new SimpleStringProperty();
    private final StringProperty width = new SimpleStringProperty();
    private final StringProperty height = new SimpleStringProperty();

    public void setX(String value) {
        x.set(value);
    }

    public void setY(String value) {
        y.set(value);
    }

    public void setWidth(String value) {
        width.set(value);
    }

    public void setHeight(String value) {
        height.set(value);
    }

    public StringProperty getX() {
        return x;
    }

    public StringProperty getY() {
        return y;
    }

    public StringProperty getWidth() {
        return width;
    }

    public StringProperty getHeight() {
        return height;
    }
}
