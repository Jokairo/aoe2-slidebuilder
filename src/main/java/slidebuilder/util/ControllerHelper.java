package slidebuilder.util;

import javafx.scene.control.TextField;
import slidebuilder.components.PreviewElement;

public class ControllerHelper {

    public static String getIntroOutroTitle(int tabIndex) {
        return (tabIndex % 2 == 0 ? "Intro " : "Outro ") + (tabIndex / 2 + 1);
    }

    public static String getSlideIntroOutroTitle(int campaignIndex, int slideIndex) {
        int campaignNum = campaignIndex / 2 + 1;
        int slideNum = slideIndex + 1;
        boolean isOutro = campaignIndex % 2 != 0;
        return (isOutro ? "Outro " : "Intro ") + campaignNum + " Slide " + slideNum;
    }

    public static String getNumericTabLabel(int index) {
        return String.valueOf(index + 1);
    }

    public static String getTabLabelWithPrefix(int index, String evenPrefix, String oddPrefix) {
        String prefix = (index % 2 == 0) ? evenPrefix : oddPrefix;
        int num = index / 2 + 1;
        return num + prefix;
    }

    public static void setCurrentImageAspectRatio(PreviewElement wrapper, TextField widthField, TextField heightField) {
        if (wrapper == null) return;

        int width = ParseUtil.parseInt(widthField.getText());
        int height = ParseUtil.parseInt(heightField.getText());
        wrapper.setAspectRatio(width, height);
    }
}
