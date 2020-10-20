package org.bandahealth.idempiere.graphql.respository;

import org.adempiere.exceptions.AdempiereException;
import org.bandahealth.idempiere.graphql.utils.FilterUtil;
import org.bandahealth.idempiere.graphql.utils.StringUtil;
import org.compiere.model.MUser;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepository<T extends PO> {

	public abstract T getModelInstance();

	public List<T> get(String filterJson, String sort, int page, int pageSize, String whereClause,
										 List<Object> parameters) {
		return this.get(filterJson, sort, page, pageSize, whereClause, parameters, null);
	}

	/**
	 * Get all with the inclusion of a join clause for joined cases of sorting
	 *
	 * @param filterJson
	 * @param sort
	 * @param page
	 * @param pageSize
	 * @param whereClause
	 * @param parameters
	 * @param joinClause  Use to specify a linked table so joining can occur
	 * @return
	 */
	public List<T> get(String filterJson, String sort, int page, int pageSize, String whereClause,
										 List<Object> parameters, String joinClause) {
		try {
			List<T> results = new ArrayList<>();
			if (parameters == null) {
				parameters = new ArrayList<>();
			}

			String filterWhereClause = FilterUtil.getWhereClauseFromFilter(getModelInstance(), filterJson, parameters);
			if (StringUtil.isNullOrEmpty(whereClause)) {
				whereClause = filterWhereClause;
			} else {
				whereClause += " AND " + filterWhereClause;
			}

			Query query = new Query(Env.getCtx(), getModelInstance().get_TableName(), whereClause, null)
					.setClient_ID();

			if (joinClause != null) {
				query.addJoinClause(joinClause);
			}

//			String orderBy = getOrderBy(sortColumn, sortOrder);
			String orderBy = getOrderBy(null, null);
			if (orderBy != null) {
				query = query.setOrderBy(orderBy);
			}

			if (parameters.size() > 0) {
				query = query.setParameters(parameters);
			}

			// get total count without pagination parameters
//			pagingInfo.setTotalRecordCount(query.count());

			// set pagination params
			query = query.setPage(pageSize, page);
			return query.list();
		} catch (Exception ex) {
			throw new AdempiereException(ex);
		}
	}

	protected String getOrderBy(String sortColumn, String sortOrder) {
		if (sortColumn != null && !sortColumn.isEmpty() && sortOrder != null) {
			// check if column exists
			if (checkColumnExists(sortColumn)) {
				return sortColumn + " "
						+ (sortOrder.equalsIgnoreCase("DESC") ? "DESC" : "ASC")
						+ " NULLS LAST";
			}
		} else {
			// every table has the 'created' column
			return checkColumnExists(MUser.COLUMNNAME_Created) ? MUser.COLUMNNAME_Created + " " + "DESC"
					+ " NULLS LAST" : null;
		}

		return null;
	}

	private boolean checkColumnExists(String columnName) {
		if (getModelInstance() != null) {
			return getModelInstance().get_ColumnIndex(columnName) > -1;
		}

		return false;
	}
}
