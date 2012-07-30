package com.uppidy.util;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * String utilities
 * 
 * @author martinc
 *
 */
public class StringUtil {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return sdf.format(date);
	}
	
	/**
	 * Formats a number stripping it from any non digit except for a leading plus sign
	 * @param number
	 * @return
	 */
	public static String formatPhoneNumber(String number) {
		if (number != null) {
			String strippedNumber = number.replaceAll("\\D", "");
			if (number.startsWith("+")) {
				return "+" + strippedNumber;
			} else {
				return strippedNumber;
			}
		}
		return null;
	}
	
	/**
	 * Creates a comma separated value from the collection elements
	 * using the toString method
	 * @param col
	 * @return
	 */
	public static String csv(Collection<?> col) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Object c : col) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(c.toString());
			i++;
		}
		
		return sb.toString();
	}
	
	/**
    * Produce a string in double quotes with backslash sequences in all the
    * right places. A backslash will be inserted within </, allowing JSON
    * text to be delivered in HTML. In JSON text, a string cannot contain a
    * control character or an unescaped quote or backslash.
    * @param string A String
    * @return A String correctly formatted for insertion in a JSON text.
    */
    public static String quote(String string) {
        if (string == null || string.length() == 0) {
            return "\"\"";
        }

        char b;
        char c = 0;
        int i;
        int len = string.length();
        StringBuffer sb = new StringBuffer(len + 4);
        String t;

        sb.append('"');
        for (i = 0; i < len; i += 1) {
            b = c;
            c = string.charAt(i);
            switch (c) {
            case '\\':
            case '"':
                sb.append('\\');
                sb.append(c);
                break;
            case '/':
                if (b == '<') {
                    sb.append('\\');
                }
                sb.append(c);
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\r':
                sb.append("\\r");
                break;
            default:
                if (c < ' ') {
                    t = "000" + Integer.toHexString(c);
                    sb.append("\\u" + t.substring(t.length() - 4));
                } else {
                    sb.append(c);
                }
            }
        }
        sb.append('"');
        return sb.toString();
    }
	
}
