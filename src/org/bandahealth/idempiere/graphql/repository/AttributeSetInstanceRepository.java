package org.bandahealth.idempiere.graphql.repository;

import org.compiere.model.MAttributeSetInstance;
import org.compiere.util.Env;

public class AttributeSetInstanceRepository extends BaseRepository<MAttributeSetInstance> {
	@Override
	public MAttributeSetInstance getModelInstance() {
		return new MAttributeSetInstance(Env.getCtx(), 0, null);
	}
}
