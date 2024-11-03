package slidebuilder.components;

import javafx.scene.Node;
import slidebuilder.data.DataManager;
import slidebuilder.enums.PreviewEnums;
import slidebuilder.enums.SceneEnum;
import slidebuilder.previews.PreviewElementProperties;
import slidebuilder.previews.PreviewInterface;

import java.util.ArrayList;

public abstract class PreviewElementChild {
    private PreviewElement wrapper;
    private ArrayList<Node> children;
    private final PreviewEnums.Elements elementType;
    private final int elementIndex;

    public PreviewElementChild(PreviewEnums.Elements elementType, int elementIndex) {
        this.elementType = elementType;
        this.elementIndex = elementIndex;
    }

    public PreviewEnums.Elements getElementType() {
        return elementType;
    }

    public abstract void onInit();
    public abstract void setElementX(double x);

    public abstract void setElementY(double y);
    public abstract void setElementWidth(double width);
    public abstract void setElementHeight(double height);
    public abstract void onHover(boolean isHover);
    public abstract void onPress(boolean isPress);

    public void setWrapperClass(PreviewElement wrapper) {
        this.wrapper = wrapper;
    }

    public void addElementChildren(Node n) {
        if (children == null) {
            this.children = new ArrayList<>();
        }
        children.add(n);
    }

    public ArrayList<Node> getElementChildren() {
        return children;
    }

    public PreviewElement getWrapperClass() {
        return wrapper;
    }

    private boolean isElementControlsVisible() {
        if (elementType == PreviewEnums.Elements.BUTTON || elementType == PreviewEnums.Elements.BUTTON_LABEL) {
            return DataManager.currentScene == SceneEnum.CAMPAIGN_SCENARIOSELECT_EDIT && elementIndex == DataManager.globalTabIndex;
        }
        return DataManager.currentScene == SceneEnum.CAMPAIGN_SLIDE_EDIT;
    }

    private PreviewElementProperties getProperties() {
        if (elementType == PreviewEnums.Elements.BUTTON) return DataManager.getPreviewScenarios().getButtonProperties();
        else if (elementType == PreviewEnums.Elements.BUTTON_LABEL) return DataManager.getPreviewScenarios().getButtonLabelProperties();
        else if (elementType == PreviewEnums.Elements.TEXT) return DataManager.getPreviewSlideshow().getTextProperties();
        return DataManager.getPreviewSlideshow().getImageProperties();
    }

    private void saveData(PreviewEnums.DataType dataType, int value) {
        if (elementType == PreviewEnums.Elements.BUTTON || elementType == PreviewEnums.Elements.BUTTON_LABEL)
            DataManager.getPreviewScenarios().saveElementData(elementType, dataType, elementIndex, value);
        else DataManager.getPreviewSlideshow().saveElementData(elementType, dataType, elementIndex, value);
    }

    public void onMove(double mouse_x, double mouse_y) {
        int mouseX = getWrapperClass().getMouseXRelatedToWindowSize(mouse_x, true, true);
        int mouseY = getWrapperClass().getMouseYRelatedToWindowSize(mouse_y, true, true);

        boolean isControlsVisible = isElementControlsVisible();
        if (isControlsVisible) {
            getProperties().setX("" + mouseX);
            getProperties().setY("" + mouseY);
        }
        else {
            getWrapperClass().setElementX(mouseX);
            getWrapperClass().setElementY(mouseY);

            saveData(PreviewEnums.DataType.X, mouseX);
            saveData(PreviewEnums.DataType.Y, mouseY);
        }
    }

    private void calculateScale(int mouseX, int mouseY, PreviewEnums.DragDirection dragDirection) {
        int scaleFactor = 4;

        boolean isControlsVisible = isElementControlsVisible();
        if (dragDirection == PreviewEnums.DragDirection.RIGHT) {
            double wDiff = mouseX - getWrapperClass().getOriginalX();
            double newWidth = Math.max(wDiff, 1) * scaleFactor;
            int intWidth = (int)newWidth;

            if (isControlsVisible) {
                getProperties().setWidth("" + intWidth);
            }
            else {
                getWrapperClass().setElementWidth(intWidth);
                saveData(PreviewEnums.DataType.WIDTH, intWidth);
            }
        }

        else if (dragDirection == PreviewEnums.DragDirection.BOTTOM) {
            double hDiff = mouseY - getWrapperClass().getOriginalY();
            double newHeight = Math.max(hDiff, 1) * scaleFactor;
            int intHeight = (int)newHeight;

            if (isControlsVisible) {
                getProperties().setHeight("" + intHeight);
            }
            else {
                getWrapperClass().setElementHeight(intHeight);
                saveData(PreviewEnums.DataType.HEIGHT, intHeight);
            }
        }

        else if (dragDirection == PreviewEnums.DragDirection.LEFT) {
            double elWidth = getWrapperClass().getOriginalX() + getWrapperClass().getOriginalWidth();
            double wDiff = mouseX - elWidth;
            double newWidth = Math.abs(Math.min(wDiff, -1)) * scaleFactor;
            int intWidth = (int)newWidth;

            // New position
            double oX = getWrapperClass().getOriginalX() * scaleFactor;
            double oW = getWrapperClass().getOriginalWidth() * scaleFactor;
            double newX = oX + oW - newWidth;
            int intX = (int)newX;

            if (isControlsVisible) {
                getProperties().setX("" + intX);
                getProperties().setWidth("" + intWidth);
            }
            else {
                getWrapperClass().setElementX(intX);
                getWrapperClass().setElementWidth(intWidth);
                saveData(PreviewEnums.DataType.X, intX);
                saveData(PreviewEnums.DataType.WIDTH, intWidth);
            }
        }

        else if (dragDirection == PreviewEnums.DragDirection.TOP) {
            double elHeight = getWrapperClass().getOriginalY() + getWrapperClass().getOriginalHeight();
            double hDiff = mouseY - elHeight;
            double newHeight = Math.abs(Math.min(hDiff, -1)) * scaleFactor;
            int intHeight = (int)newHeight;

            // New position
            double oY = getWrapperClass().getOriginalY() * scaleFactor;
            double oH = getWrapperClass().getOriginalHeight() * scaleFactor;
            double newY = oY + oH - newHeight;
            int intY = (int)newY;

            if (isControlsVisible) {
                getProperties().setY("" + intY);
                getProperties().setHeight("" + intHeight);
            }
            else {
                getWrapperClass().setElementY(intY);
                getWrapperClass().setElementHeight(intHeight);
                saveData(PreviewEnums.DataType.Y, intY);
                saveData(PreviewEnums.DataType.HEIGHT, intHeight);
            }
        }

    }

    public void onScale(double mouse_x, double mouse_y) {
        PreviewInterface preview;
        if (elementType == PreviewEnums.Elements.BUTTON || elementType == PreviewEnums.Elements.BUTTON_LABEL)
            preview = DataManager.getPreviewScenarios();
        else
            preview = DataManager.getPreviewSlideshow();

        int mouseX = getWrapperClass().getMouseXRelatedToWindowSize(mouse_x, false, false);
        int mouseY = getWrapperClass().getMouseYRelatedToWindowSize(mouse_y, false, false);

        PreviewEnums.DragDirection dragDirection = preview.getDragDirection();

        if (dragDirection == PreviewEnums.DragDirection.RIGHT) {
            calculateScale(mouseX, mouseY, dragDirection);
        }
        else if (dragDirection == PreviewEnums.DragDirection.TOP) {
            calculateScale(mouseX, mouseY, dragDirection);
        }
        else if (dragDirection == PreviewEnums.DragDirection.LEFT) {
            calculateScale(mouseX, mouseY, dragDirection);
        }
        else if (dragDirection == PreviewEnums.DragDirection.BOTTOM) {
            calculateScale(mouseX, mouseY, dragDirection);
        }
        else if (dragDirection == PreviewEnums.DragDirection.TOP_RIGHT) {
            calculateScale(mouseX, mouseY, PreviewEnums.DragDirection.TOP);
            calculateScale(mouseX, mouseY, PreviewEnums.DragDirection.RIGHT);
        }
        else if (dragDirection == PreviewEnums.DragDirection.TOP_LEFT) {
            calculateScale(mouseX, mouseY, PreviewEnums.DragDirection.TOP);
            calculateScale(mouseX, mouseY, PreviewEnums.DragDirection.LEFT);
        }
        else if (dragDirection == PreviewEnums.DragDirection.BOTTOM_LEFT) {
            calculateScale(mouseX, mouseY, PreviewEnums.DragDirection.BOTTOM);
            calculateScale(mouseX, mouseY, PreviewEnums.DragDirection.LEFT);
        }
        else if (dragDirection == PreviewEnums.DragDirection.BOTTOM_RIGHT) {
            calculateScale(mouseX, mouseY, PreviewEnums.DragDirection.BOTTOM);
            calculateScale(mouseX, mouseY, PreviewEnums.DragDirection.RIGHT);
        }
    }

    public void setVisible(boolean b) {
        children.forEach(c -> {
            c.setVisible(b);
        });
    }
}
