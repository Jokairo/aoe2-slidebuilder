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
        boolean keepAspect = wrapper.getKeepAspect();

        double originalX = getWrapperClass().getOriginalX();
        double originalY = getWrapperClass().getOriginalY();
        double currW = getWrapperClass().getElementWidth();
        double currH = getWrapperClass().getElementHeight();
        double originalW = getWrapperClass().getOriginalWidth();
        double originalH = getWrapperClass().getOriginalHeight();
        double aspect = getWrapperClass().getAspectRatio();

        if (dragDirection == PreviewEnums.DragDirection.RIGHT) {
            double wDiff = mouseX - originalX;
            int newWidth = (int) (Math.max(wDiff, 1) * scaleFactor);
            int newHeight = keepAspect ? (int) (newWidth / aspect) : (int) (currH * scaleFactor);
            applySizeChange(newWidth, newHeight, isControlsVisible);
        }

        else if (dragDirection == PreviewEnums.DragDirection.BOTTOM) {
            double hDiff = mouseY - originalY;
            int newHeight = (int) (Math.max(hDiff, 1) * scaleFactor);
            int newWidth = keepAspect ? (int) (newHeight * aspect) : (int) (currW * scaleFactor);

            if (keepAspect) {
                double oX = originalX * scaleFactor;
                double oW = originalW * scaleFactor;
                int newX = (int) (oX + oW - newWidth);
                applyPositionChange(newX, -1, isControlsVisible);
            }

            applySizeChange(newWidth, newHeight, isControlsVisible);
        }
        else if (dragDirection == PreviewEnums.DragDirection.LEFT) {
            double rightEdge = originalX + originalW;
            double wDiff = mouseX - rightEdge;
            int newWidth = (int) (Math.abs(Math.min(wDiff, -1)) * scaleFactor);
            int newX = (int) ((rightEdge * scaleFactor) - newWidth);

            if (keepAspect) {
                int newHeight = (int) (newWidth / aspect);
                double oY = originalY * scaleFactor;
                double oH = originalH * scaleFactor;
                int newY = (int) (oY + oH - newHeight);
                applyPositionChange(newX, newY, isControlsVisible);
                applySizeChange(newWidth, newHeight, isControlsVisible);
            }
            else {
                applyPositionChange(newX, -1, isControlsVisible);
                applySizeChange(newWidth, (int) (currH * scaleFactor), isControlsVisible);
            }
        }
        else if (dragDirection == PreviewEnums.DragDirection.TOP) {
            double bottomEdge = originalY + originalH;
            double hDiff = mouseY - bottomEdge;
            int newHeight = (int) (Math.abs(Math.min(hDiff, -1)) * scaleFactor);
            int newY = (int) ((bottomEdge * scaleFactor) - newHeight);

            if (keepAspect) {
                int newWidth = (int) (newHeight * aspect);
                applySizeChange(newWidth, newHeight, isControlsVisible);
            }
            else {
                applySizeChange((int) (currW * scaleFactor), newHeight, isControlsVisible);
            }

            applyPositionChange(-1, newY, isControlsVisible);
        }
    }

    private void applySizeChange(int width, int height, boolean isControlsVisible) {
        if (isControlsVisible) {
            getProperties().setWidth(String.valueOf(width));
            getProperties().setHeight(String.valueOf(height));
        } else {
            getWrapperClass().setElementWidth(width);
            getWrapperClass().setElementHeight(height);
            saveData(PreviewEnums.DataType.WIDTH, width);
            saveData(PreviewEnums.DataType.HEIGHT, height);
        }
    }

    private void applyPositionChange(int x, int y, boolean isControlsVisible) {
        if (x != -1) {
            if (isControlsVisible) {
                getProperties().setX(String.valueOf(x));
            }
            else {
                getWrapperClass().setElementX(x);
                saveData(PreviewEnums.DataType.X, x);
            }
        }

        if (y != -1) {
            if (isControlsVisible) {
                getProperties().setY(String.valueOf(y));
            }
            else {
                getWrapperClass().setElementY(y);
                saveData(PreviewEnums.DataType.Y, y);
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
        boolean keepAspect = wrapper.getKeepAspect();

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
            if (!keepAspect)
                calculateScale(mouseX, mouseY, PreviewEnums.DragDirection.RIGHT);
        }
        else if (dragDirection == PreviewEnums.DragDirection.TOP_LEFT) {
            if (!keepAspect)
                calculateScale(mouseX, mouseY, PreviewEnums.DragDirection.TOP);
            calculateScale(mouseX, mouseY, PreviewEnums.DragDirection.LEFT);
        }
        else if (dragDirection == PreviewEnums.DragDirection.BOTTOM_LEFT) {
            calculateScale(mouseX, mouseY, PreviewEnums.DragDirection.BOTTOM);
            if (!keepAspect)
                calculateScale(mouseX, mouseY, PreviewEnums.DragDirection.LEFT);
        }
        else if (dragDirection == PreviewEnums.DragDirection.BOTTOM_RIGHT) {
            if (!keepAspect)
                calculateScale(mouseX, mouseY, PreviewEnums.DragDirection.BOTTOM);
            calculateScale(mouseX, mouseY, PreviewEnums.DragDirection.RIGHT);
        }
    }

    public void setVisible(boolean b) {
        children.forEach(c -> {
            c.setVisible(b);
        });
    }

    public void saveWidthAfterHeightChange(int newWidth) {
        boolean isControlsVisible = isElementControlsVisible();
        if (isControlsVisible) {
            getProperties().setWidth("" + newWidth);
        }
        else {
            saveData(PreviewEnums.DataType.WIDTH, newWidth);
        }
    }

    public void saveHeightAfterWidthChange(int newHeight) {
        boolean isControlsVisible = isElementControlsVisible();
        if (isControlsVisible) {
            getProperties().setHeight("" + newHeight);
        }
        else {
            saveData(PreviewEnums.DataType.HEIGHT, newHeight);
        }
    }
}
