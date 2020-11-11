package org.bandahealth.idempiere.graphql.repository;

import org.compiere.wf.MWorkflow;

import java.util.Properties;

public class WorkflowRepository extends BaseRepository<MWorkflow, MWorkflow> {
	@Override
	protected MWorkflow createModelInstance(Properties idempiereContext) {
		return new MWorkflow(idempiereContext, 0, null);
	}

	@Override
	public MWorkflow mapInputModelToModel(MWorkflow entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
