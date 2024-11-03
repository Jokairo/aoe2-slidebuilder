package slidebuilder.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import slidebuilder.data.DataManager;
import slidebuilder.enums.CreatorEnum;
import slidebuilder.enums.PreviewEnums;

public class SlideImage extends PreviewElementChild {

    private final ImageView picture = new ImageView();

    public SlideImage(int elementIndex) {
        super(PreviewEnums.Elements.IMAGE, elementIndex);
        picture.setMouseTransparent(true);
    }

    public ImageView getImage() {
        return picture;
    }

    public void setImage(String name) {
        if(name == null || name.equalsIgnoreCase("None")) {
            picture.setImage(null);
            return;
        }

        Image picture_image = DataManager.getDataCampaign().getCustomImageData().getCustomImage(CreatorEnum.SLIDE_IMAGE, name).getImage();
        picture.setImage(picture_image);
    }

    @Override
    public void onInit() {
        getWrapperClass().setElementX(10);
        getWrapperClass().setElementY(50);
        getWrapperClass().setElementWidth(2000);
        getWrapperClass().setElementHeight(1048);
    }

    @Override
    public void setElementX(double x) {
        picture.setTranslateX(x);
    }

    @Override
    public void setElementY(double y) {
        picture.setTranslateY(y);
    }

    @Override
    public void setElementWidth(double width) {
        picture.setFitWidth(width);
    }

    @Override
    public void setElementHeight(double height) {
        picture.setFitHeight(height);
    }

    @Override
    public void onHover(boolean isHover) {
        // Not used
    }

    @Override
    public void onPress(boolean isPress) {
        // Not used
    }
}
