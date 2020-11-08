package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MLocator;
import org.compiere.util.Env;

public class LocatorRepository extends BaseRepository<MLocator, MLocator> {
	@Override
	protected MLocator createModelInstance() {
		return new MLocator(Env.getCtx(), 0, null);
	}

	@Override
	public MLocator mapInputModelToModel(MLocator entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
