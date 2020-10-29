package org.bandahealth.idempiere.graphql.dataloader.impl;

import org.bandahealth.idempiere.base.model.MChargeType_BH;
import org.bandahealth.idempiere.graphql.dataloader.DataLoaderRegisterer;
import org.bandahealth.idempiere.graphql.model.input.ChargeTypeInput;
import org.bandahealth.idempiere.graphql.repository.ChargeTypeRepository;

public class ChargeTypeDataLoader extends BaseDataLoader<MChargeType_BH, ChargeTypeInput, ChargeTypeRepository>
		implements DataLoaderRegisterer {
	public static final String CHARGE_TYPE_DATA_LOADER = "chargeTypeDataLoader";
	private final ChargeTypeRepository chargeTypeRepository;

	public ChargeTypeDataLoader() {
		chargeTypeRepository = new ChargeTypeRepository();
	}

	@Override
	protected String getDefaultDataLoaderName() {
		return CHARGE_TYPE_DATA_LOADER;
	}

	@Override
	protected ChargeTypeRepository getRepositoryInstance() {
		return chargeTypeRepository;
	}
}
