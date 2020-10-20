package org.bandahealth.idempiere.graphql.respository;

import org.compiere.model.MElementValue;
import org.compiere.util.Env;

public class AccountRepository extends BaseRepository<MElementValue> {
	@Override
	public MElementValue getModelInstance() {
		return new MElementValue(Env.getCtx(), 0, null);
	}
}
