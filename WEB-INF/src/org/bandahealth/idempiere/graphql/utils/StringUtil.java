package org.bandahealth.idempiere.graphql.utils;

public class StringUtil {

	/**
	 * Check if string is not null and empty
	 * @param name
	 * @return
	 */
	public static boolean isNotNullAndEmpty(String name) {
		return name != null && !name.isEmpty();
	}

	/**
	 * Check if string is null or empty
	 * @param s
	 * @return
	 */
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.isEmpty();
	}
}
