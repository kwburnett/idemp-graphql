package org.bandahealth.idempiere.graphql.respository;

import org.compiere.model.MAttributeSet;
import org.compiere.util.Env;

public class AttributeSetRepository extends BaseRepository<MAttributeSet> {
	@Override
	public MAttributeSet getModelInstance() {
		return new MAttributeSet(Env.getCtx(), 0, null);
	}
}
