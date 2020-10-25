package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MProcessPara;
import org.compiere.util.Env;

public class ProcessParameterRepository extends BaseRepository<MProcessPara, MProcessPara> {
	@Override
	public MProcessPara getModelInstance() {
		return new MProcessPara(Env.getCtx(), 0, null);
	}

	@Override
	public MProcessPara save(MProcessPara entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
