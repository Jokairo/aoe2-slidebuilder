package slidebuilder.enums;

public class PreviewEnums {
    public enum Actions {
        NONE,
        MOVING,
        SCALING,
    }

    public enum DragDirection {
        TOP,
        TOP_RIGHT,
        RIGHT,
        BOTTOM_RIGHT,
        BOTTOM,
        BOTTOM_LEFT,
        LEFT,
        TOP_LEFT
    }

    public enum Elements {
        BUTTON,
        BUTTON_LABEL,
        IMAGE,
        TEXT,
    }

    public enum DataType {
        X,
        Y,
        WIDTH,
        HEIGHT
    }
}
