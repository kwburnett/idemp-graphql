package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.repository.ReportViewRepository;
import org.compiere.model.MReportView;

public class ReportViewDataLoader extends BaseDataLoader<MReportView, MReportView, ReportViewRepository>
		implements DataLoaderRegisterer {
	public static final String REPORT_VIEW_BY_ID_DATA_LOADER = "reportViewByIdDataLoader";
	public static final String REPORT_VIEW_BY_UUID_DATA_LOADER = "reportViewByUuidDataLoader";
	private final ReportViewRepository reportViewRepository;

	public ReportViewDataLoader() {
		reportViewRepository = new ReportViewRepository();
	}

	@Override
	protected String getByIdDataLoaderName() {
		return REPORT_VIEW_BY_ID_DATA_LOADER;
	}

	@Override
	protected String getByUuidDataLoaderName() {
		return REPORT_VIEW_BY_UUID_DATA_LOADER;
	}

	@Override
	protected ReportViewRepository getRepositoryInstance() {
		return reportViewRepository;
	}
}
