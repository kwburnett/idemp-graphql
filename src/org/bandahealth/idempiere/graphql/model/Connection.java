package org.bandahealth.idempiere.graphql.model;

import java.util.List;

public class Connection<T> {

	private List<T> results;

	private PagingInfo pagingInfo;

	public Connection(List<T> results, PagingInfo pagingInfo) {
		this.results = results;
		this.pagingInfo = pagingInfo;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public PagingInfo getPagingInfo() {
		return pagingInfo;
	}

	public void setPagingInfo(PagingInfo pagingInfo) {
		this.pagingInfo = pagingInfo;
	}
}
