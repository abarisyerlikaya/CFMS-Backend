package tr.edu.yildiz.cfms.core.utils;

public class Utils {
    private static final int TRUNCATE_LENGTH = 61;

    public static String truncate(String text) {
        if (text == null) return "";
        if (text.length() > TRUNCATE_LENGTH) return text.substring(0, TRUNCATE_LENGTH) + "...";
        return text;
    }
}
