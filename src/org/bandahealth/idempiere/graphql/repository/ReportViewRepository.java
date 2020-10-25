package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MReportView;
import org.compiere.util.Env;

public class ReportViewRepository extends BaseRepository<MReportView, MReportView> {
	@Override
	public MReportView getModelInstance() {
		return new MReportView(Env.getCtx(), 0, null);
	}

	@Override
	public MReportView save(MReportView entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
