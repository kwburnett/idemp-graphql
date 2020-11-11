package org.bandahealth.idempiere.graphql.repository;

import org.bandahealth.idempiere.base.model.MChargeType_BH;
import org.bandahealth.idempiere.graphql.model.input.ChargeTypeInput;

import java.util.Properties;

public class ChargeTypeRepository extends BaseRepository<MChargeType_BH, ChargeTypeInput> {
	@Override
	protected MChargeType_BH createModelInstance(Properties idempiereContext) {
		return new MChargeType_BH(idempiereContext, 0, null);
	}

	@Override
	public MChargeType_BH mapInputModelToModel(ChargeTypeInput entity, Properties idempiereContext) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
