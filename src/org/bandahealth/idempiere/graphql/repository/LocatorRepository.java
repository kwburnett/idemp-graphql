package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MLocator;
import org.compiere.util.Env;

public class LocatorRepository extends BaseRepository<MLocator, MLocator> {
	@Override
	public MLocator getModelInstance() {
		return new MLocator(Env.getCtx(), 0, null);
	}

	@Override
	public MLocator save(MLocator entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
