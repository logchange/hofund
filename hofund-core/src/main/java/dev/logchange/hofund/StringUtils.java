package dev.logchange.hofund;

public class StringUtils {

    public static String emptyIfNull(String val) {
        if (val == null) {
            return "";
        } else {
            return val;
        }
    }

}
