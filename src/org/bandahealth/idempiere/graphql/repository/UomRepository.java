package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MUOM;
import org.compiere.util.Env;

public class UomRepository extends BaseRepository<MUOM, MUOM> {
	@Override
	public MUOM getModelInstance() {
		return new MUOM(Env.getCtx(), 0, null);
	}

	@Override
	public MUOM save(MUOM entity) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	protected boolean shouldUseContextClientId() {
		return false;
	}
}