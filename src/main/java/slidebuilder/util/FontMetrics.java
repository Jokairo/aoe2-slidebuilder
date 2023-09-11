package slidebuilder.util;

import javafx.geometry.Bounds;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

//from http://werner.yellowcouch.org/log/fontmetrics-jdk9/

/*
 * Replacement for calculating text width with font applied in JavaFX version 9+
 */

public class FontMetrics {
	
	final private Text internal;
	
	public float ascent, descent, lineHeight;
	
	public FontMetrics(Font fnt) {
		internal =new Text();
		internal.setFont(fnt);
		Bounds b= internal.getLayoutBounds();
		lineHeight= (float) b.getHeight();
		ascent= (float) -b.getMinY();
		descent=(float) b.getMaxY();
	}

 	public float computeStringWidth(String txt) {
		internal.setText(txt);
		return (float) internal.getLayoutBounds().getWidth();
 	}
}