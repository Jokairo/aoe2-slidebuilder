package slidebuilder.util;

public class BackgroundUtil {
	
	private static int backgroundAlgorithm(int imageWidth, int imageHeight, int returnType) {
		int width;
		int divider = 0;
	
		//Same aspect ratio
		if(imageHeight < 540) {
			int diff = 540 - imageHeight;
			width = imageWidth + diff;
		}
		else {
			divider = (int) imageHeight/540;
			width = (int) imageWidth/divider;
		}
		
		//double d1 = 5672.0/2160.0;
		//AoE2:DE Scaling ratio
		double d1 = 2470.0 / 1042.0;
		
		//Current image ratio
		double d2 = width/540.0;
		
		//Get the difference between the ratios
		double percent = (d1 / d2);

		//Apply difference to image width
		int new_width = (int) (width*percent);
	
		if(returnType == 0)
			return new_width;
		return divider;
	}
	
	
	public static int getBackgroundWidth(int imageWidth, int imageHeight) {
		return backgroundAlgorithm(imageWidth, imageHeight, 0);
	}
	
	public static int getBackgroundDivider(int imageWidth, int imageHeight) {
		return backgroundAlgorithm(imageWidth, imageHeight, 1);
	}
}
