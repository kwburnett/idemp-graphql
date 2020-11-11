package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MProcessPara;

import java.util.Properties;

public class ProcessParameterRepository extends BaseRepository<MProcessPara, MProcessPara> {
	@Override
	protected MProcessPara createModelInstance(Properties idempiereContext) {
		return new MProcessPara(idempiereContext, 0, null);
	}

	@Override
	public MProcessPara mapInputModelToModel(MProcessPara entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	protected boolean shouldUseContextClientId() {
		return false;
	}
}
