package org.bandahealth.idempiere.graphql.utils;

import org.adempiere.exceptions.DBException;
import org.compiere.model.PO;

import java.util.List;

public interface BandaQuery<T extends PO> {
	List<T> list() throws DBException;

	T first() throws DBException;

	String getSQL() throws DBException;

	BandaQuery<T> addJoinClause(String joinClause);

	BandaQuery<T> setOrderBy(String orderBy);

	int count() throws DBException;

	BandaQuery<T> setPage(int pPageSize, int pPagesToSkip);

	BandaQuery<T> setOnlyActiveRecords(boolean onlyActiveRecords);

	BandaQuery<T> setParameters(List<Object> parameters);
}
