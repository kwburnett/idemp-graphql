package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MLocator;

import java.util.Properties;

public class LocatorRepository extends BaseRepository<MLocator, MLocator> {
	@Override
	protected MLocator createModelInstance(Properties idempiereContext) {
		return new MLocator(idempiereContext, 0, null);
	}

	@Override
	public MLocator mapInputModelToModel(MLocator entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
