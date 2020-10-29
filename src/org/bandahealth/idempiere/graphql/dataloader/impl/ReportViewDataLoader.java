package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.dataloader.impl.BaseDataLoader;
import org.bandahealth.idempiere.graphql.repository.ReportViewRepository;
import org.compiere.model.MReportView;

public class ReportViewDataLoader extends BaseDataLoader<MReportView, MReportView, ReportViewRepository>
		implements DataLoaderRegisterer {

	public static final String REPORT_VIEW_DATA_LOADER = "reportViewDataLoader";
	private final ReportViewRepository reportViewRepository;

	public ReportViewDataLoader() {
		reportViewRepository = new ReportViewRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return REPORT_VIEW_DATA_LOADER;
	}

	@Override
	protected ReportViewRepository getRepositoryInstance() {
		return reportViewRepository;
	}
}
