package slidebuilder.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageTypeUtil {
	
	public static Image createImageHover(Image image) {
		Image new_image = blendColor(image, ButtonColors.FILE_HOVER_BRIGHTNESS);
		return new_image;
	}
	
	private static ImageView setEffectPressed(ImageView imageView) {
		ColorAdjust color = new ColorAdjust();
		color.setBrightness(ButtonColors.FILE_PRESS_BRIGHTNESS);
		color.setContrast(ButtonColors.FILE_PRESS_CONTRAST);
		color.setSaturation(ButtonColors.FILE_PRESS_SATURATION);
		imageView.setEffect(color);
		return imageView;
	}
	
	public static void createImageFile(Image image, int type, String output) {
		File outputFile = new File(output);
		ImageView imageView = new ImageView(image);
		
		//Pressed effect
		if (type == 1) {
			imageView = setEffectPressed(imageView);
			
			//For pressing effect, move image downwards
			Rectangle2D viewportRect = new Rectangle2D(0, -1.5, imageView.getImage().getWidth(), imageView.getImage().getHeight());
			imageView.setViewport(viewportRect);
		}
		//Hover effect
		else if (type == 2) {
			imageView.setImage(createImageHover(image));
		}
		
		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.snapshot(parameters, null), null);
		
		try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
	
	private static Image blendColor(final Image source, double brightness) {
	    final int w = (int) source.getWidth();
	    final int h = (int) source.getHeight();
	    
	    final WritableImage output = new WritableImage(w, h);
	    final PixelWriter writer = output.getPixelWriter();
	    final PixelReader reader = source.getPixelReader();
	    
	    //Add a bit saturation
	    double saturation_green = 0.95;
	    double saturation_blue = 0.85;
	    
	    //Multiply every pixel by brightness value
	    for (int y = 0; y < h; y++) {
	        for (int x = 0; x < w; x++) {
	        	Color pixel_color = reader.getColor(x, y);
	        	double r = Math.min(1, pixel_color.getRed() * brightness);
	    	    double g = Math.min(1, pixel_color.getGreen() * brightness * saturation_green);
	    	    double b = Math.min(1, pixel_color.getBlue() * brightness * saturation_blue);
	    	    double alpha = pixel_color.getOpacity();
	    	    
	    	    Color new_color = new Color(r, g, b, alpha);
	    	    
	            writer.setColor(x, y, new_color);
	        }
	    }
	    return output;
	}
}
