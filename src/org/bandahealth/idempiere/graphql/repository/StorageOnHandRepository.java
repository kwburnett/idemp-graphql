package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MStorageOnHand;
import org.compiere.util.Env;

public class StorageOnHandRepository extends BaseRepository<MStorageOnHand, MStorageOnHand> {
	@Override
	public MStorageOnHand getModelInstance() {
		return new MStorageOnHand(Env.getCtx(), 0, null);
	}

	@Override
	public MStorageOnHand save(MStorageOnHand entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
