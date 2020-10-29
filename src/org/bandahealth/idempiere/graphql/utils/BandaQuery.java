package org.bandahealth.idempiere.graphql.utils;

import org.adempiere.exceptions.DBException;
import org.compiere.model.PO;

import java.util.List;

/**
 * This interface is meant to allow wrapping of the iDempiere Query and/or providing another query implementation.
 *
 * @param <T> A model extending from iDempiere's PO model
 */
public interface BandaQuery<T extends PO> {
	/**
	 * Return a list of all po that match the query criteria.
	 *
	 * @return List
	 * @throws DBException
	 */
	List<T> list() throws DBException;

	/**
	 * Return first PO that match query criteria
	 *
	 * @return first PO
	 * @throws DBException
	 */
	T first() throws DBException;

	/**
	 * red1 - returns full SQL string - for caller needs
	 *
	 * @return buildSQL(null, true)
	 */
	String getSQL() throws DBException;

	BandaQuery<T> addJoinClause(String joinClause);

	/**
	 * Set order by clause.
	 * If the string starts with "ORDER BY" then "ORDER BY" keywords will be discarded.
	 *
	 * @param orderBy SQL ORDER BY clause
	 */
	BandaQuery<T> setOrderBy(String orderBy);

	/**
	 * Count items that match query criteria
	 *
	 * @return count
	 * @throws DBException
	 */
	int count() throws DBException;

	/**
	 * Set the pagination of the query.
	 *
	 * @param pPageSize    Limit current query rows return.
	 * @param pPagesToSkip Number of pages will be skipped on query run. ZERO for first page
	 * @return current Query
	 */
	BandaQuery<T> setPage(int pPageSize, int pPagesToSkip);

	/**
	 * Select only active records (i.e. IsActive='Y')
	 *
	 * @param onlyActiveRecords
	 */
	BandaQuery<T> setOnlyActiveRecords(boolean onlyActiveRecords);

	/**
	 * Set query parameters
	 *
	 * @param parameters collection of parameters
	 */
	BandaQuery<T> setParameters(List<Object> parameters);

	/**
	 * Check if there items for query criteria
	 *
	 * @return true if exists, false otherwise
	 * @throws DBException
	 */
	boolean match() throws DBException;
}
