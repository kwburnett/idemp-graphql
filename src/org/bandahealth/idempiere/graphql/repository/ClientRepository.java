package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MClient;

import java.util.Properties;

public class ClientRepository extends BaseRepository<MClient, MClient> {

	@Override
	protected MClient createModelInstance(Properties idempiereContext) {
		return new MClient(idempiereContext, 0, null);
	}

	@Override
	public MClient mapInputModelToModel(MClient entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	protected boolean shouldUseContextClientId() {
		return false;
	}
}
