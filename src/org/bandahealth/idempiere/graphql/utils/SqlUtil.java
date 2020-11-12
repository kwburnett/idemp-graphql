package org.bandahealth.idempiere.graphql.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.adempiere.exceptions.DBException;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

/**
 * Abstract common sql functionality
 */
public class SqlUtil {
	private static CLogger log = CLogger.getCLogger(SqlUtil.class);

	public static Map<Integer, Integer> getGroupedCount(String tableName, String whereClause, List<Object> parameters,
			String groupingIdColumn, Set<Integer> idsToGroupBy) {
		return getGroupedCount(tableName, whereClause, parameters, groupingIdColumn, new ArrayList<>() {{
			addAll(idsToGroupBy);
		}});
	}

	public static Map<Integer, Integer> getGroupedCount(String tableName, String whereClause, List<Object> parameters,
			String groupingIdColumn, List<Integer> idsToGroupBy) {
		StringBuilder sql = new StringBuilder("SELECT " + groupingIdColumn + ", COUNT(*) FROM ")
				.append(tableName)
				.append(" ")
				.append(whereClause)
				.append(" GROUP BY ")
				.append(groupingIdColumn);

		Map<Integer, Integer> counts = idsToGroupBy.stream().collect(Collectors.toMap(id -> id, id -> 0));

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = DB.prepareStatement(sql.toString(), null);
			DB.setParameters(statement, parameters);

			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int idColumn = resultSet.getInt(1);
				counts.put(idColumn, resultSet.getInt(2));
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, sql.toString(), e);
			throw new DBException(e, sql.toString());
		} finally {
			DB.close(resultSet, statement);
		}

		return counts;
	}

	public static Integer getCount(String sqlFromAndWhereClause, List<Object> parameters) {
		String sql = "SELECT COUNT(*) " + sqlFromAndWhereClause;
		Integer count = null;

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = DB.prepareStatement(sql.toString(), null);
			DB.setParameters(statement, parameters);

			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}

		} catch (SQLException e) {
			log.log(Level.SEVERE, sql.toString(), e);
			throw new DBException(e, sql.toString());
		} finally {
			DB.close(resultSet, statement);
		}

		return count;
	}

	/**
	 * The will only work for PostgresQL JDBC, but gets the wrapped SQL string to handle parameters dealings.
	 *
	 * @param preparedStatement The DB prepared statement
	 * @return A string containing the DB statement with parameters filled in
	 */
	public static String getSql(PreparedStatement preparedStatement) {
		String statement = preparedStatement.toString();
		if (!statement.contains(" SELECT ")) {
			return null;
		}
		int indexOfSelect = statement.indexOf("SELECT ");
		int lastIndexOfBox = statement.lastIndexOf("]");
		return statement.substring(indexOfSelect, lastIndexOfBox);
	}
}
