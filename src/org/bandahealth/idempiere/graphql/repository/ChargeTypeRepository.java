package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MChargeType_BH;
import org.bandahealth.idempiere.graphql.model.input.ChargeTypeInput;

public class ChargeTypeRepository extends BaseRepository<MChargeType_BH, ChargeTypeInput> {
	@Override
	protected MChargeType_BH createModelInstance() {
		return new ChargeTypeInput();
	}

	@Override
	public MChargeType_BH mapInputModelToModel(ChargeTypeInput entity) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
