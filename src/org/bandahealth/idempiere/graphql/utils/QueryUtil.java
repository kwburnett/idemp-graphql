package org.bandahealth.idempiere.graphql.utils;

import java.util.List;
import java.util.Set;

public class QueryUtil {

	public static <T> String getWhereClauseAndSetParametersForSet(Set<T> items, List<Object> parameters) {
		String parameterList = "?,".repeat(items.size());
		parameters.addAll(items);
		return parameterList.substring(0, parameterList.length() - 1);
	}
}
