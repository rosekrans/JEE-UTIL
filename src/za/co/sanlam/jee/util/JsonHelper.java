/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.sanlam.jee.util;

/**
 *
 * @author g9020vc
 */
public class JsonHelper {
    public static final CharSequence DOUBLE_QUOTE = "\"";
    public static final CharSequence BACK_SLASH = "\\";
    public static final CharSequence FORWARD_SLASH = "/";
    
    public static final CharSequence DOUBLE_QUOTE_ESCAPED = "\\\"";
    public static final CharSequence BACK_SLASH_ESCAPED = "\\\\";
    public static final CharSequence FORWARD_SLASH_ESCAPED = "\\/";

    public static final String escape(String notJson) {
        String json = notJson.replace(DOUBLE_QUOTE, DOUBLE_QUOTE_ESCAPED);
        json = json.replace(BACK_SLASH, BACK_SLASH_ESCAPED);
        json = json.replace(FORWARD_SLASH, FORWARD_SLASH_ESCAPED);
        return json;
    }
}
