package org.bandahealth.idempiere.graphql.repository;

import org.compiere.util.Env;
import org.compiere.wf.MWorkflow;

public class WorkflowRepository extends BaseRepository<MWorkflow, MWorkflow> {
	@Override
	public MWorkflow getModelInstance() {
		return new MWorkflow(Env.getCtx(), 0, null);
	}

	@Override
	public MWorkflow save(MWorkflow entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
