package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MReportView;

import java.util.Properties;

public class ReportViewRepository extends BaseRepository<MReportView, MReportView> {
	@Override
	protected MReportView createModelInstance(Properties idempiereContext) {
		return new MReportView(idempiereContext, 0, null);
	}

	@Override
	public MReportView mapInputModelToModel(MReportView entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
