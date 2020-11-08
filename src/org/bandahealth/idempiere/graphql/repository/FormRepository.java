package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MForm;
import org.compiere.util.Env;

public class FormRepository extends BaseRepository<MForm, MForm> {
	@Override
	protected MForm createModelInstance() {
		return new MForm(Env.getCtx(), 0, null);
	}

	@Override
	public MForm mapInputModelToModel(MForm entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
