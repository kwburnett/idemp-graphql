package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MUOM;
import org.compiere.util.Env;

public class UomRepository extends BaseRepository<MUOM, MUOM> {
	@Override
	protected MUOM createModelInstance() {
		return new MUOM(Env.getCtx(), 0, null);
	}

	@Override
	public MUOM mapInputModelToModel(MUOM entity) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	protected boolean shouldUseContextClientId() {
		return false;
	}
}
