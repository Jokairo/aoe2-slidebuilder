package slidebuilder.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

public class TextFieldFormatter extends TextFormatter<Number> {

	private static final Pattern pattern_decimal = Pattern.compile("-?(([0-9]*)|0)?(\\.[0-9]*)?");
	private static final Pattern pattern_normal = Pattern.compile("-?(([0-9]*)|0)?");
	
	public TextFieldFormatter(int min_decimals, boolean allow_negative) {
        super(getStringConverter(min_decimals), 0, getUnaryOperator(min_decimals, allow_negative));
    }
	
	private static StringConverter<Number> getStringConverter(int min_decimals) {
			
			return new StringConverter<Number>() {
			
		    @Override
		    public Number fromString(String s) {
		        if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
		            return 0.0 ;
		        } else {
		            return Double.valueOf(s);
		        }
		    }


		    @Override
		    public String toString(Number d) {
		    	String format = "##";
		    	if (min_decimals > 0) {
		    		format += ".";
		    		for(int i=0; i < min_decimals; i++) {
		    			format += "#";
		    		}
		    	}
		    	DecimalFormat df = new DecimalFormat(format, new DecimalFormatSymbols(Locale.ENGLISH));
		    	df.setMinimumFractionDigits(min_decimals);
		    	return df.format(d);
		    }
		};
	}
	
	private static UnaryOperator<TextFormatter.Change> getUnaryOperator(int decimals, boolean allow_negative) {
        return new UnaryOperator<TextFormatter.Change>() {

			@Override
			public Change apply(Change c) {
				String text = c.getControlNewText();
				
				if (!allow_negative && text.startsWith("-")) {
                    return null;
                }
				
			    if ((decimals > 0 && pattern_decimal.matcher(text).matches()) ||
			    		(decimals == 0 && pattern_normal.matcher(text).matches())) {
			        return c ;
			    } else {
			        return null ;
			    }
			}
		    
        };
    }

}
