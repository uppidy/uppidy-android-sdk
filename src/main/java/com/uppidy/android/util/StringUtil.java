package com.uppidy.android.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**

 * Provides several <code>String</code> manipulation methods 
 * 
 * Some of them have been copied from deleted org.springframework.security.util.StringSplitUtils
 * 
 * @author arudnev@uppidy.com
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
	 * Formats a number stripping it from any non digit except for a leading
	 * plus sign
	 * 
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
	 * Creates a comma separated value from the collection elements using the
	 * toString method
	 * 
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
	 * right places. A backslash will be inserted within </, allowing JSON text
	 * to be delivered in HTML. In JSON text, a string cannot contain a control
	 * character or an unescaped quote or backslash.
	 * 
	 * @param string
	 *            A String
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

	private static final String[] EMPTY_STRING_ARRAY = new String[0];

	/**
	 * Splits a <code>String</code> at the first instance of the delimiter.
	 * <p>
	 * Does not include the delimiter in the response.
	 * </p>
	 * 
	 * @param toSplit
	 *            the string to split
	 * @param delimiter
	 *            to split the string up with
	 * @return a two element array with index 0 being before the delimiter, and
	 *         index 1 being after the delimiter (neither element includes the
	 *         delimiter)
	 * @throws IllegalArgumentException
	 *             if an argument was invalid
	 */
	public static String[] split(String toSplit, String delimiter) {
		Assert.hasLength(toSplit, "Cannot split a null or empty string");
		Assert.hasLength(delimiter, "Cannot use a null or empty delimiter to split a string");

		if (delimiter.length() != 1) {
			throw new IllegalArgumentException("Delimiter can only be one character in length");
		}

		int offset = toSplit.indexOf(delimiter);

		if (offset < 0) {
			return null;
		}

		String beforeDelimiter = toSplit.substring(0, offset);
		String afterDelimiter = toSplit.substring(offset + 1);

		return new String[] { beforeDelimiter, afterDelimiter };
	}

	/**
	 * Takes an array of <code>String</code>s, and for each element removes any
	 * instances of <code>removeCharacter</code>, and splits the element based
	 * on the <code>delimiter</code>. A <code>Map</code> is then generated, with
	 * the left of the delimiter providing the key, and the right of the
	 * delimiter providing the value.
	 * <p>
	 * Will trim both the key and value before adding to the <code>Map</code>.
	 * </p>
	 * 
	 * @param array
	 *            the array to process
	 * @param delimiter
	 *            to split each element using (typically the equals symbol)
	 * @param removeCharacters
	 *            one or more characters to remove from each element prior to
	 *            attempting the split operation (typically the quotation mark
	 *            symbol) or <code>null</code> if no removal should occur
	 * @return a <code>Map</code> representing the array contents, or
	 *         <code>null</code> if the array to process was null or empty
	 */
	public static Map<String, String> splitEachArrayElementAndCreateMap(String[] array, String delimiter,
			String removeCharacters) {
		if ((array == null) || (array.length == 0)) {
			return null;
		}

		Map<String, String> map = new HashMap<String, String>();

		for (int i = 0; i < array.length; i++) {
			String postRemove;

			if (removeCharacters == null) {
				postRemove = array[i];
			} else {
				postRemove = StringUtils.replace(array[i], removeCharacters, "");
			}

			String[] splitThisArrayElement = split(postRemove, delimiter);

			if (splitThisArrayElement == null) {
				continue;
			}

			map.put(splitThisArrayElement[0].trim(), splitThisArrayElement[1].trim());
		}

		return map;
	}

	/**
	 * Splits a given string on the given separator character, skips the
	 * contents of quoted substrings when looking for separators. Introduced for
	 * use in DigestProcessingFilter (see SEC-506).
	 * <p/>
	 * This was copied and modified from commons-lang StringUtils
	 */
	public static String[] splitIgnoringQuotes(String str, char separatorChar) {
		if (str == null) {
			return null;
		}

		int len = str.length();

		if (len == 0) {
			return EMPTY_STRING_ARRAY;
		}

		List<String> list = new ArrayList<String>();
		int i = 0;
		int start = 0;
		boolean match = false;

		while (i < len) {
			if (str.charAt(i) == '"') {
				i++;
				while (i < len) {
					if (str.charAt(i) == '"') {
						i++;
						break;
					}
					i++;
				}
				match = true;
				continue;
			}
			if (str.charAt(i) == separatorChar) {
				if (match) {
					list.add(str.substring(start, i));
					match = false;
				}
				start = ++i;
				continue;
			}
			match = true;
			i++;
		}
		if (match) {
			list.add(str.substring(start, i));
		}

		return list.toArray(new String[list.size()]);
	}
}
