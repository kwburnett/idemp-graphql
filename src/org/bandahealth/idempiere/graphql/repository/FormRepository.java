package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MForm;
import org.compiere.util.Env;

public class FormRepository extends BaseRepository<MForm, MForm> {
	@Override
	public MForm getModelInstance() {
		return new MForm(Env.getCtx(), 0, null);
	}

	@Override
	public MForm save(MForm entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
