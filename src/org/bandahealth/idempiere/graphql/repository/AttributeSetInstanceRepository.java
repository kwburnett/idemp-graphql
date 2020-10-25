package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.graphql.model.input.AttributeSetInstanceInput;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.util.Env;

public class AttributeSetInstanceRepository extends BaseRepository<MAttributeSetInstance, AttributeSetInstanceInput> {
	@Override
	public MAttributeSetInstance getModelInstance() {
		return new MAttributeSetInstance(Env.getCtx(), 0, null);
	}

	@Override
	public MAttributeSetInstance save(AttributeSetInstanceInput entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
