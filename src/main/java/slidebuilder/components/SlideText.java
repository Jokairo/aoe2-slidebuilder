package slidebuilder.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import slidebuilder.enums.PreviewEnums;

public class SlideText extends PreviewElementChild {
    private final Label text;

    public SlideText(int elementIndex) {
        super(PreviewEnums.Elements.TEXT, elementIndex);
        double MAX_FONT_SIZE = 12.3;
        Font font = Font.loadFont(getClass().getResource("/fonts/backgroundFont.ttf").toExternalForm(), MAX_FONT_SIZE);
        text = new Label("");

        text.setFont(font);
        text.setWrapText(true);

        text.setTextFill(Color.BLACK);
        text.setTextOverrun(OverrunStyle.CLIP);

        text.setAlignment(Pos.TOP_LEFT);
    }

    public Label getText() {
        return text;
    }

    public void setText(String string) {
        text.setText(string);
    }

    @Override
    public void onInit() {
        getWrapperClass().setElementX(1200);
        getWrapperClass().setElementY(700);
        getWrapperClass().setElementWidth(500);
        getWrapperClass().setElementHeight(1500);
    }

    @Override
    public void setElementX(double x) {
        text.setTranslateX(x);
    }

    @Override
    public void setElementY(double y) {
        text.setTranslateY(y);
    }

    @Override
    public void setElementWidth(double width) {
        //+ 12px
        text.setMaxWidth(width+1);
    }

    @Override
    public void setElementHeight(double height) {
        //+20 otherwise the text cuts sometimes because line spacing has been changed
        text.setMaxHeight(height);
    }

    @Override
    public void onPress(boolean b) {
        // Not used
    }

    @Override
    public void onHover(boolean b) {
        // Not used
    }
}
