package org.bandahealth.idempiere.graphql.utils;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;

import java.util.List;
import java.util.Set;

public class QueryUtil {

	public static <T> String getWhereClauseAndSetParametersForSet(Set<T> items, List<Object> parameters) {
		String parameterList = "?,".repeat(items.size());
		parameters.addAll(items);
		return parameterList.substring(0, parameterList.length() - 1);
	}

	/**
	 * Determine if we need to fetch the total count (triggering another DB query) for the results
	 *
	 * @param environment The GraphQL environment
	 * @return
	 */
	public static boolean isTotalCountRequested(DataFetchingEnvironment environment) {
		return environment.getSelectionSet().contains("pagingInfo");
	}
}
