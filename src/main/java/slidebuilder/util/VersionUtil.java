package slidebuilder.util;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class VersionUtil {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = VersionUtil.class.getResourceAsStream("/version.properties")) {
            if (in != null) {
                props.load(in);
            } else {
                System.err.println("version.properties not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAuthor() {
        return props.getProperty("author", "Jokairo");
    }

    public static String getUrl() {
        return props.getProperty("url", "");
    }

    public static String getAppName() {
        return props.getProperty("appname", "Slide Builder");
    }

    public static String getYear() {
        return props.getProperty("year", "2025");
    }

    public static String getFullVersion() {
        return props.getProperty("version", "0.0.0.0");
    }

    public static String getShortVersion() {
        String full = getFullVersion();
        String[] parts = full.split("\\.");
        return parts.length >= 3 ? String.join(".", parts[0], parts[1], parts[2]) : full;
    }
}
