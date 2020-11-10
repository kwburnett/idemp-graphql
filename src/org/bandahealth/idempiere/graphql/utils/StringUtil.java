package org.bandahealth.idempiere.graphql.utils;

public class StringUtil {
	/**
	 * Check if string is null or empty
	 *
	 * @param s
	 * @return
	 */
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.isEmpty();
	}

	/**
	 * Remove newlines from a string
	 *
	 * @param s The string to fix
	 * @return A string without newlines
	 */
	public static String stripNewLines(String s) {
		return s == null ? null : s.replace("\r", "").replace("\n", "");
	}
}
