package slidebuilder.util;

public class ParseUtil {
	public static int parseInt(String s) {
		if(s == null || s.equals("") || s.equals("-"))
			return 0;
		else {
			//Cast from double to int since the value could be double
			return (int) Double.parseDouble(s);
		}
	}
	
	public static Double parseDouble(String s) {
		if(s == null || s.equals("") || s.equals("-"))
			return 0.0;
		else {
			return Double.parseDouble(s);
		}
	}
}
