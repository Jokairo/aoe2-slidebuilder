package slidebuilder.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageColorUtil {
	
	/*
	 * Code from https://stackoverflow.com/questions/46797579/how-can-i-control-the-brightness-of-an-image
	 * by user davidbuzatto
	 */
	
	public static BufferedImage newBrightness(String image_path, float brightnessPercentage) throws IOException {

		Image source = ImageIO.read( new File(image_path));
		
        BufferedImage bi = new BufferedImage( 
                source.getWidth( null ), 
                source.getHeight( null ), 
                BufferedImage.TYPE_INT_ARGB );

        int[] pixel = { 0, 0, 0, 0 };
        float[] hsbvals = { 0, 0, 0 };

        bi.getGraphics().drawImage( source, 0, 0, null );

        // recalculare every pixel, changing the brightness
        for ( int i = 0; i < bi.getHeight(); i++ ) {
            for ( int j = 0; j < bi.getWidth(); j++ ) {

                // get the pixel data
                bi.getRaster().getPixel( j, i, pixel );

                // converts its data to hsb to change brightness
                Color.RGBtoHSB( pixel[0], pixel[1], pixel[2], hsbvals );

                // calculates the brightness component.
                float newBrightness = hsbvals[2] * brightnessPercentage;
                if ( newBrightness > 1f ) {
                    newBrightness = 1f;
                }

                // create a new color with the new brightness
                Color c = new Color( Color.HSBtoRGB( hsbvals[0], hsbvals[1], newBrightness ) );

                // set the new pixel
                bi.getRaster().setPixel( j, i, new int[]{ c.getRed(), c.getGreen(), c.getBlue(), pixel[3] } );

            }

        }

        return bi;
	}
}
