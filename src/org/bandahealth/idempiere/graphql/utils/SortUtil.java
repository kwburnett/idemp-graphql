package org.bandahealth.idempiere.graphql.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MUser;
import org.compiere.model.PO;
import org.compiere.util.CLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The utility to parse and read sort JSON coming from the server and work with it
 */
public class SortUtil {
	private static final String MALFORMED_SORT_STRING_ERROR = "Sort criteria doesn't meet the standard form.";
	protected static CLogger logger = CLogger.getCLogger(SortUtil.class);

	/**
	 * This takes in a sort JSON model generated and converts it into an appropriate ORDER BY clause to pass to the DB.
	 * <p>
	 * The expected JSON has the following structure (any of the following patterns can be combined in any order)
	 * [
	 * database-column
	 * -OR-
	 * [database-column]
	 * -OR-
	 * [database-column, sort-direction]
	 * ]
	 * </p>
	 *
	 * @param dbModel  The iDempiere DB model for determining field types
	 * @param sortJson The JSON string received for sorting
	 * @param <T>      An iDempiere model extending from PO
	 * @return An ORDER BY clause based off the sort criteria to use in a DB query
	 */
	public static <T extends PO> String getOrderByClauseFromSort(T dbModel, String sortJson) {
		String defaultOrderBy = checkColumnExists(dbModel, MUser.COLUMNNAME_Created) ? dbModel.get_TableName() + "." +
				MUser.COLUMNNAME_Created + " DESC NULLS LAST" : null;
		if (StringUtil.isNullOrEmpty(sortJson)) {
			return defaultOrderBy;
		}
		try {
			// Parse the JSON string
			List<Object> listOfSortCriteria = parseJsonString(sortJson);
			if (listOfSortCriteria.isEmpty()) {
				return defaultOrderBy;
			}

			String orderBy = listOfSortCriteria.stream().map(sortCriteria -> {
				// If this is null or an empty array, skip
				if (sortCriteria == null || sortCriteria instanceof List<?> && ((List<?>) sortCriteria).isEmpty()) {
					return null;
				}
				String sortColumn;
				String sortDirection;
				// If this is just a string, add and sort by ASC
				if (sortCriteria instanceof String) {
					sortColumn = (String) sortCriteria;
					sortDirection = "ASC";
				} else {
					List<String> sortCriteriaColumnAndDirection = (List<String>) sortCriteria;
					sortColumn = sortCriteriaColumnAndDirection.get(0);
					sortDirection = "ASC";
					// If only one argument was specified,
					if (sortCriteriaColumnAndDirection.size() == 2) {
						sortDirection = sortCriteriaColumnAndDirection.get(1);
					}
				}
				if (QueryUtil.doesDBStringHaveInvalidCharacters(sortColumn) ||
						QueryUtil.doesDBStringHaveInvalidCharacters(sortDirection)) {
					return null;
				}
				if (!QueryUtil.doesTableAliasExistOnColumn(sortColumn)) {
					sortColumn = dbModel.get_TableName() + "." + sortColumn;
				}
				return sortColumn + " " + sortDirection + " NULLS LAST";
			}).filter(sortCriteria -> !StringUtil.isNullOrEmpty(sortCriteria)).collect(Collectors.joining(","));
			return orderBy.isEmpty() ? defaultOrderBy : orderBy;
		} catch (Exception e) {
			throw new AdempiereException(MALFORMED_SORT_STRING_ERROR);
		}
	}

	/**
	 * Parse the filter string into an object
	 *
	 * @param filterJson The JSON string received for filtering
	 * @return The filter expressions
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	private static List<Object> parseJsonString(String filterJson) throws JsonMappingException,
			JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(filterJson, ArrayList.class);
	}

	/**
	 * Check if the column exists on the specified model
	 *
	 * @param dbModel    The iDempiere DB model for determining non-aliased field types
	 * @param columnName The column to check
	 * @param <T>        An iDempiere model extending from PO
	 * @return Whether the column exists on the specified model
	 */
	private static <T extends PO> boolean checkColumnExists(T dbModel, String columnName) {
		if (dbModel != null) {
			return dbModel.get_ColumnIndex(columnName) > -1;
		}

		return false;
	}

	/**
	 * Parse through the field names and return a list of aliases.
	 *
	 * @param sortJson
	 * @return
	 */
	public static List<String> getTablesNeedingJoins(String sortJson) {
		List<String> neededJoinTables = new ArrayList<>();
		if (StringUtil.isNullOrEmpty(sortJson)) {
			return neededJoinTables;
		}
		try {
			List<Object> listOfSortCriteria = parseJsonString(sortJson);
			if (listOfSortCriteria.isEmpty()) {
				return neededJoinTables;
			}
			// Make sure to return the distinct list without duplicates
			return listOfSortCriteria.stream().map(sortCriteria -> {
				// If this is null or an empty array, skip
				if (sortCriteria == null || sortCriteria instanceof List<?> && ((List<?>) sortCriteria).isEmpty()) {
					return null;
				}
				String sortColumn;
				// If this is just a string, add
				if (sortCriteria instanceof String) {
					sortColumn = (String) sortCriteria;
				} else {
					sortColumn = ((List<String>) sortCriteria).get(0);
				}
				if (QueryUtil.doesDBStringHaveInvalidCharacters(sortColumn) ||
						!QueryUtil.doesTableAliasExistOnColumn(sortColumn)) {
					return null;
				}
				return QueryUtil.getTableAliasFromColumn(sortColumn);
			}).filter(sortCriteria -> !StringUtil.isNullOrEmpty(sortCriteria)).distinct().collect(Collectors.toList());
		} catch (Exception e) {
			throw new AdempiereException(MALFORMED_SORT_STRING_ERROR);
		}
	}
}
