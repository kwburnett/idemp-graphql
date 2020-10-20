package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MClient;
import org.compiere.util.Env;

public class ClientRepository extends BaseRepository<MClient> {

	@Override
	public MClient getModelInstance() {
		return new MClient(Env.getCtx(), 0, null);
	}
}
