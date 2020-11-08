package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MClient;
import org.compiere.util.Env;

public class ClientRepository extends BaseRepository<MClient, MClient> {

	@Override
	protected MClient createModelInstance() {
		return new MClient(Env.getCtx(), 0, null);
	}

	@Override
	public MClient mapInputModelToModel(MClient entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
