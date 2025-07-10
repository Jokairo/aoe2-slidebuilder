package slidebuilder.components;

import javafx.scene.layout.Pane;
import slidebuilder.data.DataManager;
import slidebuilder.enums.PreviewEnums;
import slidebuilder.previews.PreviewInterface;

public class PreviewElement extends Pane {

    private final double size_percentage = 0.25; //Elements are displayed in 25% of the real size

    private PreviewElementChild child;
    private boolean selected = false;
    private boolean hidden = false;

    private double originalX;
    private double originalY;
    private double originalWidth;
    private double originalHeight;

    private double aspect = 1920.0/1080.0;
    private boolean keepAspect = true;

    public PreviewElement() {
        // For testing
        //this.setStyle("-fx-background-color: #ffffff");
    }

    public void setElementX(int value) {
        setTranslateX(value * size_percentage);
        child.setElementX(value * size_percentage);
    }

    public void setElementY(int value) {
        setTranslateY(value * size_percentage);
        child.setElementY(value * size_percentage);
    }

    public void setElementWidth(int value) {
        setPrefWidth(value * size_percentage);
        child.setElementWidth(value * size_percentage);
    }

    public void setElementHeight(int value) {
        setPrefHeight(value * size_percentage);
        child.setElementHeight(value * size_percentage);
    }

    public void setAspectRatio(int width, int height) {
        aspect = width / (double)height;
    }

    public void setKeepAspect(boolean keepAspect) {
        this.keepAspect = keepAspect;
    }

    public boolean getKeepAspect() {
        return this.keepAspect;
    }

    public double getAspectRatio() {
        return this.aspect;
    }

    public void setElementWidthWithAspect(int value) {
        setElementWidth(value);

        int newHeight = (int)(value / aspect);
        setElementHeight(newHeight);
        child.saveHeightAfterWidthChange(newHeight);
    }

    public void setElementHeightWithAspect(int value) {
        setElementHeight(value);

        int newWidth = (int)(value * aspect);
        setElementWidth(newWidth);
        child.saveWidthAfterHeightChange(newWidth);
    }

    public double getElementX() {
        return this.getTranslateX();
    }

    public double getElementY() {
        return this.getTranslateY();
    }

    public double getElementWidth() {
        return this.getPrefWidth();
    }

    public double getElementHeight() {
        return this.getPrefHeight();
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected)
            this.setStyle("-fx-border-color: red");
        else
            this.setStyle(null);
    }

    public void setChild(PreviewElementChild child) {
        this.child = child;
        child.setWrapperClass(this);
        child.onInit();
    }

    public void setHidden(boolean b) {
        if (b) setSelected(false);

        hidden = b;
        child.setVisible(!b);
    }

    public boolean getHidden() {
        return hidden;
    }

    public PreviewElementChild getChild() {
        return child;
    }

    protected int getMouseXRelatedToWindowSize(double mouseX, boolean useOffset, boolean getGameCoordinate) {
        int scaleFactor = getGameCoordinate ? 4 : 1;
        PreviewInterface preview;
        PreviewEnums.Elements elementType = child.getElementType();
        if (elementType == PreviewEnums.Elements.BUTTON || elementType == PreviewEnums.Elements.BUTTON_LABEL)
            preview = DataManager.getPreviewScenarios();
        else
            preview = DataManager.getPreviewSlideshow();

        double percentageX = mouseX / preview.getSceneWidth();
        double defaultWidth = 960.0;

        double screenScale = defaultWidth / preview.getSceneWidth();
        double scaledClickX = preview.getClickedX() * screenScale;

        int offsetX = !useOffset ? 0 : (int)(getOriginalX() - scaledClickX);
        return (int)(defaultWidth * percentageX * scaleFactor) + offsetX * scaleFactor;
    }

    protected int getMouseYRelatedToWindowSize(double mouseY, boolean useOffset, boolean getGameCoordinate) {
        int scaleFactor = getGameCoordinate ? 4 : 1;
        PreviewInterface preview;
        PreviewEnums.Elements elementType = child.getElementType();
        if (elementType == PreviewEnums.Elements.BUTTON || elementType == PreviewEnums.Elements.BUTTON_LABEL)
            preview = DataManager.getPreviewScenarios();
        else
            preview = DataManager.getPreviewSlideshow();

        double percentageY = mouseY / preview.getSceneHeight();
        double defaultHeight = 540.0;

        double screenScale = defaultHeight / preview.getSceneHeight();
        double scaledClickY = preview.getClickedY() * screenScale;

        int offsetY = !useOffset ? 0 : (int)(getOriginalY() - scaledClickY);
        return (int)(defaultHeight * percentageY * scaleFactor) + offsetY * scaleFactor;
    }

    public boolean isMouseInside(double mouseX, double mouseY) {
        int scaledMouseX = getMouseXRelatedToWindowSize(mouseX, false, false);
        int scaledMouseY = getMouseYRelatedToWindowSize(mouseY, false, false);

        double x = getTranslateX();
        double y = getTranslateY();
        double width = getPrefWidth();
        double height = getPrefHeight();

        return scaledMouseX <= x+width && scaledMouseX >= x && scaledMouseY <= y+height && scaledMouseY >= y;
    }

    public PreviewEnums.DragDirection isMouseInEdge(double mouseX, double mouseY) {
        int scaledMouseX = getMouseXRelatedToWindowSize(mouseX, false, false);
        int scaledMouseY = getMouseYRelatedToWindowSize(mouseY, false, false);

        double x = getTranslateX();
        double y = getTranslateY();
        double width = getPrefWidth();
        double height = getPrefHeight();
        double x2 = x + width;
        double y2 = y + height;
        double margin = 5;

        if (scaledMouseX <= x+margin && scaledMouseY <= y+margin) return PreviewEnums.DragDirection.TOP_LEFT;
        else if (scaledMouseX >= x2-margin && scaledMouseY <= y+margin) return PreviewEnums.DragDirection.TOP_RIGHT;
        else if (scaledMouseX >= x2-margin && scaledMouseY >= y2-margin) return PreviewEnums.DragDirection.BOTTOM_RIGHT;
        else if (scaledMouseX <= x+margin && scaledMouseY >= y2-margin) return PreviewEnums.DragDirection.BOTTOM_LEFT;
        else if (scaledMouseX <= x+margin) return PreviewEnums.DragDirection.LEFT;
        else if (scaledMouseY <= y+margin) return PreviewEnums.DragDirection.TOP;
        else if (scaledMouseX >= x2-margin) return PreviewEnums.DragDirection.RIGHT;
        else if (scaledMouseY >= y2-margin) return PreviewEnums.DragDirection.BOTTOM;
        return null;
    }

    public void setOriginalCoordinates() {
        originalX = getElementX();
        originalY = getElementY();
        originalWidth = getElementWidth();
        originalHeight = getElementHeight();
    }

    public double getOriginalX() {
        return originalX;
    }

    public double getOriginalY() {
        return originalY;
    }

    public double getOriginalWidth() {
        return originalWidth;
    }

    public double getOriginalHeight() {
        return originalHeight;
    }
}
