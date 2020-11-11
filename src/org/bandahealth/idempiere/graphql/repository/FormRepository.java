package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MForm;

import java.util.Properties;

public class FormRepository extends BaseRepository<MForm, MForm> {
	@Override
	protected MForm createModelInstance(Properties idempiereContext) {
		return new MForm(idempiereContext, 0, null);
	}

	@Override
	public MForm mapInputModelToModel(MForm entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
