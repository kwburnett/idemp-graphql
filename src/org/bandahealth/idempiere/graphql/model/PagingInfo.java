package org.bandahealth.idempiere.graphql.model;

public class PagingInfo {

	private int page;
	private int pageSize;
	private Integer totalCount;
	private boolean hasNextPage;

	/**
	 * Creates a new {@link PagingInfo} instance.
	 *
	 * @param page     The 0-based number of the page being requested.
	 * @param pageSize The number of records to include on each page.
	 */
	public PagingInfo(int page, int pageSize) {
		setPage(page);
		setPageSize(pageSize);
	}

	public static boolean isValid(PagingInfo pagingInfo) {
		return pagingInfo != null && pagingInfo.getPage() >= 0 && pagingInfo.getPageSize() > 0;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;

		checkHasMoreResults();
	}

	public Integer getTotalPages() {
		return (int) Math.ceil((double) getTotalCount() / (double) pageSize);
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	private void checkHasMoreResults() {
		setHasNextPage(getPage() < getTotalPages());
	}

	@Override
	public boolean equals(Object object) {
		if (object.getClass() == PagingInfo.class) {
			PagingInfo pagingInfo = (PagingInfo) object;
			return pagingInfo.getPage() == this.getPage() && pagingInfo.getPageSize() == this.getPageSize();
		}
		return false;
	}
}
