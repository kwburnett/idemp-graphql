package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MUOM;

import java.util.Properties;

public class UomRepository extends BaseRepository<MUOM, MUOM> {
	@Override
	protected MUOM createModelInstance(Properties idempiereContext) {
		return new MUOM(idempiereContext, 0, null);
	}

	@Override
	public MUOM mapInputModelToModel(MUOM entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	protected boolean shouldUseContextClientId() {
		return false;
	}
}
