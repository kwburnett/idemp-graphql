package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MReference;
import org.compiere.util.Env;

public class ReferenceRepository extends BaseRepository<MReference, MReference> {
	@Override
	public MReference getModelInstance() {
		return new MReference(Env.getCtx(), 0, null);
	}

	@Override
	public MReference save(MReference entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
