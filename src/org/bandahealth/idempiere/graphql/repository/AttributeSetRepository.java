package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.graphql.model.input.AttributeSetInput;
import org.compiere.model.MAttributeSet;
import org.compiere.util.Env;

public class AttributeSetRepository extends BaseRepository<MAttributeSet, AttributeSetInput> {
	@Override
	public MAttributeSet getModelInstance() {
		return new MAttributeSet(Env.getCtx(), 0, null);
	}

	@Override
	public MAttributeSet save(AttributeSetInput entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
